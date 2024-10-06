/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : Invalid Date                                                                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-26 11:21:46                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.da.sageassistantserver.dao.NoticeMapper;
import com.da.sageassistantserver.dao.TeamsWorkFlowMapper;
import com.da.sageassistantserver.dao.WeworkRobotMapper;
import com.da.sageassistantserver.model.DeadPurchaseLine;
import com.da.sageassistantserver.model.LongTimeNC;
import com.da.sageassistantserver.model.LongTimeNoQC;
import com.da.sageassistantserver.model.MixPJTInPO;
import com.da.sageassistantserver.model.MixPJTInWO;
import com.da.sageassistantserver.model.PnStatus;
import com.da.sageassistantserver.model.ProjectProfit;
import com.da.sageassistantserver.model.SuspectDuplicatedPO;
import com.da.sageassistantserver.model.SuspectDuplicatedRA;
import com.da.sageassistantserver.model.TeamsWorkFlow;
import com.da.sageassistantserver.model.TobeClosedWO;
import com.da.sageassistantserver.model.TobeDealWithOrderLine;
import com.da.sageassistantserver.model.TobeDelivery;
import com.da.sageassistantserver.model.TobeInvoice;
import com.da.sageassistantserver.model.TobePurchaseBom;
import com.da.sageassistantserver.model.TobeReceive;
import com.da.sageassistantserver.model.WeworkRobot;
import com.da.sageassistantserver.utils.Utils;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NoticeService {

  private static Set<String> Sites = new HashSet<>();

  @Autowired
  NoticeMapper noticeMapper;

  @Autowired
  WeworkRobotMapper weworkRobotMapper;

  @Autowired
  TeamsWorkFlowMapper teamsWorkFlowMapper;

  @Autowired
  RobotLastRunService robotLastRunService;

  @Autowired
  PnService pnService;

  @Autowired
  SuspectDuplicateDataService suspectDuplicateDataService;

  @Autowired
  StatusService statusService;

  @Data
  class DaysDiffRange {

    Integer diff;
    Integer start;
    Integer end;

    DaysDiffRange(Integer diff, Integer start, Integer end) {
      this.diff = diff;
      this.start = start;
      this.end = end;
    }
  }

  public void sendMessage(
    String site,
    String code,
    String title,
    StringBuilder msg
  ) {
    weworkRobotMapper
      .selectList(
        (new LambdaQueryWrapper<WeworkRobot>()).eq(
            WeworkRobot::getNotice_code,
            code
          )
          .like(WeworkRobot::getSites, site)
          .eq(WeworkRobot::getEnable, 1)
      )
      .forEach(r -> {
        WeWorkService.sendMessage(
          r.getRobot_uuid(),
          title + "[" + site + "]" + "\n\n" + msg.toString()
        );
      });

    teamsWorkFlowMapper
      .selectList(
        (new LambdaQueryWrapper<TeamsWorkFlow>()).eq(
            TeamsWorkFlow::getNotice_code,
            code
          )
          .like(TeamsWorkFlow::getSites, site)
          .eq(TeamsWorkFlow::getEnable, 1)
      )
      .forEach(f -> {
        TeamsWorkFlowService.sendEmailMessage(
          f.getFlow_url(),
          title + "[" + site + "]",
          f.getMail_to(),
          msg.toString()
        );
      });
  }

  @Scheduled(cron = "0 0/5 9-18 * * MON-FRI")
  public void getSites() {
    if (Sites.isEmpty()) {
      weworkRobotMapper
        .selectList(
          (new LambdaQueryWrapper<WeworkRobot>()).eq(WeworkRobot::getEnable, 1)
        )
        .forEach(r -> {
          String[] sitesArray = r.getSites().split("[,;\\s]+");

          for (String s : sitesArray) {
            Sites.add(s.trim());
          }
        });

      teamsWorkFlowMapper
        .selectList(
          (new LambdaQueryWrapper<TeamsWorkFlow>()).eq(
              TeamsWorkFlow::getEnable,
              1
            )
        )
        .forEach(f -> {
          String[] sitesArray = f.getSites().split("[,;\\s]+");

          for (String s : sitesArray) {
            Sites.add(s.trim());
          }
        });
    }
  }

  @Scheduled(cron = "0 0 9 * * MON-FRI")
  public void sendPNNotActive() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("INACTIVE_PN");

    Sites.forEach(site -> {
      List<PnStatus> list = pnService.findObsoletePnBySite(site);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        PnStatus pn = list.get(i);
        msg.append("项目号:\t").append(pn.getProjectNO()).append("\n");
        msg.append("ＰＮ:\t").append(pn.getPN()).append(" ");
        msg
          .append(pn.getDesc1())
          .append(" ")
          .append(pn.getDesc2())
          .append(" ")
          .append(pn.getDesc3())
          .append("\n");
        msg.append("状态:\t").append(pn.getPNStatus()).append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=NotActive-PN&site=" + site
        );
        sendMessage(
          site,
          "INACTIVE_PN",
          "[SageAssistant]⚠︎PN状态不可用" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 0/1 9-11,13-17 * * MON-FRI")
  public void sendNewSalesOrder() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    String lastRun = robotLastRunService.getLastRun("NEW_SALES_ORDER");

    Sites.forEach(site -> {
      List<TobeDelivery> list = noticeMapper.findNewSOSince(site, lastRun);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        TobeDelivery order = list.get(i);
        msg.append("项目号:\t").append(order.getProjectNO()).append("\n");
        msg.append("订单号:\t").append(order.getOrderNO()).append("\n");
        msg.append("ＰＮ:\t").append(order.getPN()).append("\n");
        msg.append("ＰＮ描述:\t").append(order.getDescription()).append("\n");
        msg.append("数量:\t").append(order.getQty()).append("\n");
        msg
          .append("金额:\t")
          .append(order.getNetPrice().setScale(2, RoundingMode.HALF_UP))
          .append(" ")
          .append(order.getCurrency())
          .append("\n");
        msg
          .append("客户:\t")
          .append(order.getCustomerCode())
          .append(" ")
          .append(order.getCustomerName())
          .append("\n");
        msg
          .append("交货日期:\t")
          .append(Utils.formatDate(order.getRequestDate()))
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=New-Order&site=" + site
        );
        sendMessage(
          site,
          "NEW_SALES_ORDER",
          "[SageAssistant]🤑💰新订单来了" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 0 10 * * MON-FRI")
  public void sendSalesOrderDealWithDelay() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("SALES_ORDER_WITHOUT_DEAL");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(7, -14, -7),
        new DaysDiffRange(14, -21, -14),
        new DaysDiffRange(21, -28, -21),
        new DaysDiffRange(28, -45, -28),
        new DaysDiffRange(45, -90, -45),
        new DaysDiffRange(90, -9999, -90)
      );

      ddrs.forEach(ddr -> {
        List<TobeDealWithOrderLine> list = noticeMapper.findTobeDealWithOrderLines(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          TobeDealWithOrderLine order = list.get(i);
          msg.append("项目号:\t").append(order.getProjectNO()).append("\n");
          msg
            .append("类型:\t")
            .append(order.getOrderType())
            .append(" ")
            .append(order.getOrderCategory())
            .append("\n");
          msg
            .append("订单日期:\t")
            .append(Utils.formatDate(order.getOrderDate()))
            .append("\n");
          msg.append("ＰＮ:\t").append(order.getPN()).append("\n");
          msg.append("ＰＮ描述:\t").append(order.getDescription()).append("\n");
          msg.append("数量:\t").append(order.getQty()).append("\n");
          msg
            .append("客户:\t")
            .append(order.getCustomerCode())
            .append(" ")
            .append(order.getCustomerName())
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=New-Order&site=" + site
          );
          sendMessage(
            site,
            "SALES_ORDER_WITHOUT_DEAL",
            "[SageAssistant]😡新订单超" +
            ddr.getDiff() +
            "天未处理" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 10 10 * * MON-FRI")
  public void sendBomDealWithDelay() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("BOM_NO_DEAL");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(14, -21, -14),
        new DaysDiffRange(21, -28, -21),
        new DaysDiffRange(28, -45, -28),
        new DaysDiffRange(45, -90, -45),
        new DaysDiffRange(90, -9999, -90)
      );

      ddrs.forEach(ddr -> {
        List<TobePurchaseBom> list = noticeMapper.findTobePurchaseBom(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          TobePurchaseBom line = list.get(i);
          msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
          msg.append("类型:\t").append(line.getOrderType()).append("\n");
          msg
            .append("工包:\t")
            .append(line.getWorkOrderNO())
            .append(" ")
            .append(Utils.formatDate(line.getCreateDate()))
            .append("\n");
          msg.append("销售成品ＰＮ:\t").append(line.getForPN()).append("\n");
          msg
            .append("需采购ＰＮ:\t")
            .append("[" + line.getBomSeq() + "] ")
            .append(line.getPN())
            .append("\n");
          msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
          msg.append("需求数量:\t").append(line.getQty()).append("\n");
          msg.append("锁定数量:\t").append(line.getAllQty()).append("\n");
          msg.append("短缺数量:\t").append(line.getShortQty()).append("\n");
          msg
            .append("客户:\t")
            .append(line.getCustomerCode())
            .append(" ")
            .append(line.getCustomerName())
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=Short-Bom&site=" + site
          );
          sendMessage(
            site,
            "BOM_NO_DEAL",
            "[SageAssistant]😡Bom项超" +
            ddr.getDiff() +
            "天未采购" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 0 14 * * MON")
  public void sendNoAckPO() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("PURCHASE_ORDER_NO_ACK_DATE");

    // for purchaser
    Sites.forEach(site -> {
      List<TobeReceive> list = noticeMapper.findNoAckDatePO(site);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size() && i < 10; i++) {
        TobeReceive line = list.get(i);
        msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
        msg
          .append("采购单号:\t")
          .append(line.getPurchaseNO())
          .append("[" + line.getLine() + "] ")
          .append("\n");
        msg.append("采购ＰＮ:\t").append(line.getPN()).append("\n");
        msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
        msg
          .append("交付日期:\t")
          .append(Utils.formatDate(line.getExpectDate()))
          .append("\n");
        msg
          .append("供应商:\t")
          .append(line.getVendorCode())
          .append(" ")
          .append(line.getVendorName())
          .append("\n");
        msg.append("采购人:\t").append(line.getCreateUser()).append("\n");
        msg
          .append("采购日期:\t")
          .append(Utils.formatDate(line.getOrderDate()))
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=NO-ACK-PO&site=" + site
        );
        StringBuilder finalMsg = new StringBuilder(
          "新建采购单填写ExpDate, AckDate; 后续跟进,交货日期变更后改写ExpDate" +
          "\n\n" +
          msg
        );
        sendMessage(
          site,
          "PURCHASE_ORDER_NO_ACK_DATE",
          "[SageAssistant]😡采购单没有供应商交付日期" +
          "[共" +
          list.size() +
          "条]",
          finalMsg
        );
      }
    });
  }

  @Scheduled(cron = "0 0 11 * * MON,WED,FRI")
  public void sendLongTimeNoReceive() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("LONG_TIME_NO_RECEIVE");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(90, -120, -90),
        new DaysDiffRange(120, -150, -120),
        new DaysDiffRange(150, -9999, -150)
      );

      ddrs.forEach(ddr -> {
        List<TobeReceive> list = noticeMapper.findLongTimeNoReceive(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          TobeReceive line = list.get(i);
          msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
          msg
            .append("采购单号:\t")
            .append(line.getPurchaseNO())
            .append(" [" + line.getLine() + "] ")
            .append("\n");
          msg.append("采购ＰＮ:\t").append(line.getPN()).append("\n");
          msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
          msg
            .append("期望交付日期:\t")
            .append(Utils.formatDate(line.getExpectDate()))
            .append("\n");
          msg
            .append("供应商:\t")
            .append(line.getVendorCode())
            .append(" ")
            .append(line.getVendorName())
            .append("\n");
          msg.append("采购人:\t").append(line.getCreateUser()).append("\n");
          msg
            .append("采购日期:\t")
            .append(Utils.formatDate(line.getOrderDate()))
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=Receive&site=" + site
          );
          sendMessage(
            site,
            "LONG_TIME_NO_RECEIVE",
            "[SageAssistant]😬采购交货严重超期(大于" +
            ddr.getDiff() +
            "天)" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 10 14 * * MON-FRI")
  public void sendLongTimeNC() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("LONG_TIME_NC");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(14, -21, -14),
        new DaysDiffRange(21, -28, -21),
        new DaysDiffRange(28, -45, -28),
        new DaysDiffRange(45, -90, -45),
        new DaysDiffRange(90, -9999, -90)
      );

      ddrs.forEach(ddr -> {
        List<LongTimeNC> list = noticeMapper.findLongTimeNC(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          LongTimeNC line = list.get(i);
          msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
          msg
            .append("采购单号:\t")
            .append(line.getPurchaseNO())
            .append(" [" + line.getLine() + "] ")
            .append("\n");
          msg.append("采购ＰＮ:\t").append(line.getPN()).append("\n");
          msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
          msg
            .append("供应商:\t")
            .append(line.getVendorCode())
            .append(" ")
            .append(line.getVendorName())
            .append("\n");
          msg.append("采购人:\t").append(line.getCreateUser()).append("\n");
          msg
            .append("采购日期:\t")
            .append(Utils.formatDate(line.getOrderDate()))
            .append("\n");
          msg
            .append("首次检验日期:\t")
            .append(Utils.formatDate(line.getFirstNCDate()))
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=LongTime-NC&site=" + site
          );
          sendMessage(
            site,
            "LONG_TIME_NC",
            "[SageAssistant]😬NC处理" +
            ddr.getDiff() +
            "天仍未出货" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 5 11 * * MON-FRI")
  public void sendLongTimeNoQC() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("LONG_TIME_NO_QC");

    // for purchaser
    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(14, -21, -14),
        new DaysDiffRange(21, -28, -21),
        new DaysDiffRange(28, -45, -28),
        new DaysDiffRange(45, -90, -45),
        new DaysDiffRange(90, -9999, -90)
      );

      ddrs.forEach(ddr -> {
        List<LongTimeNoQC> list = noticeMapper.findLongTimeNoQC(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          LongTimeNoQC line = list.get(i);
          msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
          msg
            .append("采购单号:\t")
            .append(line.getPurchaseNO())
            .append(" [" + line.getLine() + "] ")
            .append("\n");
          msg.append("采购ＰＮ:\t").append(line.getPN()).append("\n");
          msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
          msg
            .append("收货日期:\t")
            .append(Utils.formatDate(line.getReceiptDate()))
            .append("\n");
          msg
            .append("供应商:\t")
            .append(line.getVendorCode())
            .append(" ")
            .append(line.getVendorName())
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=LongTime-NOQC&site=" + site
          );
          sendMessage(
            site,
            "LONG_TIME_NO_QC",
            "😬收货" +
            ddr.getDiff() +
            "天仍未检验" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 10 11 * * MON-FRI")
  public void sendOrphanPO() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("WRONG_PROJECT_PURCHASE_ORDER");

    // for purchaser
    Sites.forEach(site -> {
      List<TobeReceive> list = noticeMapper.findWrongProjectPO(site);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        TobeReceive line = list.get(i);
        msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
        msg
          .append("采购单号:\t")
          .append(line.getPurchaseNO())
          .append(" [" + line.getLine() + "] ")
          .append("\n");
        msg.append("采购ＰＮ:\t").append(line.getPN()).append("\n");
        msg.append("ＰＮ描述:\t").append(line.getDescription()).append("\n");
        msg
          .append("采购单价:\t")
          .append(line.getNetPrice().setScale(2, RoundingMode.HALF_UP))
          .append(" ")
          .append(line.getCurrency())
          .append("\n");
        msg
          .append("期望交付日期:\t")
          .append(Utils.formatDate(line.getExpectDate()))
          .append("\n");
        msg
          .append("供应商:\t")
          .append(line.getVendorCode())
          .append(" ")
          .append(line.getVendorName())
          .append("\n");
        msg.append("采购者:\t").append(line.getCreateUser()).append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=Wrong-Project-PO&site=" + site
        );
        sendMessage(
          site,
          "WRONG_PROJECT_PURCHASE_ORDER",
          "[SageAssistant]🧯🧯采购单项目错误🧯🧯" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 10 14 * * MON-FRI")
  public void sendLongTimeNoInvoice() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("LONG_TIME_NO_INVOICE");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(45, -90, -45),
        new DaysDiffRange(90, -120, -90),
        new DaysDiffRange(120, -9999, -120)
      );

      ddrs.forEach(ddr -> {
        List<TobeInvoice> list = noticeMapper.findLongTimeNoInvoice(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          TobeInvoice line = list.get(i);
          msg.append("项目号:\t").append(line.getProjectNO()).append("\n");
          msg
            .append("采购单号:\t")
            .append(line.getPurchaseNO())
            .append(" [" + line.getPurchaseLine() + "] ")
            .append("\n");
          msg
            .append("采购ＰＮ:\t")
            .append(line.getPN())
            .append("\n")
            .append(line.getDescription())
            .append("\n");
          msg.append("采购者:\t").append(line.getPurchaser()).append("\n");
          msg
            .append("采购日期:\t")
            .append(Utils.formatDate(line.getPurchaseDate()))
            .append("\n");
          msg
            .append("供应商:\t")
            .append(line.getVendorCode())
            .append(" ")
            .append(line.getVendorName())
            .append("\n");
          msg
            .append("收货单号:\t")
            .append(line.getReceiveNO())
            .append(" [")
            .append(line.getReceiveLine())
            .append("]\n");
          msg.append("收货人:\t").append(line.getReceiptor()).append("\n");
          msg
            .append("收货日期:\t")
            .append(Utils.formatDate(line.getReceiveDate()))
            .append("\n");
          msg
            .append("价格:\t")
            .append(line.getPrice().setScale(2, RoundingMode.HALF_UP))
            .append(" ")
            .append(line.getCurrency())
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=NO-Invoice-PO&site=" + site
          );
          sendMessage(
            site,
            "LONG_TIME_NO_INVOICE",
            "[SageAssistant]😬发票严重超期" +
            ddr.getDiff() +
            "天(仅2024年后)" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 0/10 9-11,13-17 * * MON-FRI")
  public void sendDuplicatePurchaseOrder() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    robotLastRunService.getLastRun("DUPLICATE_PURCHASE_ORDER");

    Sites.forEach(site -> {
      List<SuspectDuplicatedPO> list = noticeMapper.findDuplicatedPOBySite(
        site
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        SuspectDuplicatedPO order = list.get(i);
        msg.append("项目号:\t").append(order.getProjectNO()).append("\n");
        msg.append("ＰＮ:\t").append(order.getPN()).append("\n");
        msg
          .append("第" + order.getSeq() + "次采购单:\t")
          .append(order.getPurchaseNO())
          .append("-")
          .append(order.getPurchaseLine())
          .append(" ")
          .append(Utils.formatDate(order.getPurchaseDate()))
          .append(" ")
          .append(order.getPurchaser())
          .append("\n");
        msg
          .append("第" + order.getSeq() + "次采购数量:\t")
          .append(order.getPurchaseQty())
          .append("\n");
        msg
          .append("第" + order.getSeq() + "次金额:\t")
          .append(order.getCost().setScale(2, RoundingMode.HALF_UP))
          .append(" ")
          .append(order.getCurrency())
          .append("\n");
        msg
          .append("项目总采购数量:\t")
          .append(order.getTotalPurchaseQty())
          .append("\n");
        msg
          .append("关联销售/备库数量:\t")
          .append(order.getTotalSalesQty())
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n在PurchaseLine或者ReceiveLine的Text中添加'AGAIN'可抑制通知"
        );
        msg.append("\nhttps://192.168.10.12/#/SuspectDuplicateRecords");
        sendMessage(
          site,
          "DUPLICATE_PURCHASE_ORDER",
          "[SageAssistant]😵疑似重复采购" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 15 10 * * MON-FRI")
  public void sendDuplicateReceive() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    robotLastRunService.getLastRun("DUPLICATE_RECEIVE");

    Sites.forEach(site -> {
      List<SuspectDuplicatedRA> list = noticeMapper.findDuplicatedRABySite(
        site
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        SuspectDuplicatedRA ra = list.get(i);
        msg.append("项目号:\t").append(ra.getProjectNO()).append("\n");
        msg.append("ＰＮ:\t").append(ra.getPN()).append("\n");
        msg
          .append("第" + ra.getSeq() + "次收货单:\t")
          .append(ra.getReceiptNO())
          .append("-")
          .append(ra.getReceiptLine())
          .append(" ")
          .append(Utils.formatDate(ra.getReceiptDate()))
          .append(" ")
          .append(ra.getReceiptor())
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次收货数量:\t")
          .append(ra.getReceiptQty())
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次金额:\t")
          .append(ra.getReceiptAmount().setScale(2, RoundingMode.HALF_UP))
          .append(" ")
          .append(ra.getCurrency())
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次采购单:\t")
          .append(ra.getPurchaseNO())
          .append("-")
          .append(ra.getPurchaseLine())
          .append(" ")
          .append(Utils.formatDate(ra.getPurchaseDate()))
          .append(" ")
          .append(ra.getPurchaser())
          .append("\n");
        msg
          .append("项目总收货数量:\t")
          .append(ra.getTotalReceiptQty())
          .append("\n");
        msg
          .append("项目总采购数量:\t")
          .append(ra.getTotalPurchaseQty())
          .append("\n");
        msg
          .append("项目关联销售单数量:\t")
          .append(ra.getTotalSalesQty())
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n在PurchaseLine或者ReceiveLine的Text中添加'AGAIN'可抑制通知"
        );
        msg.append("\nhttps://192.168.10.12/#/SuspectDuplicateRecords");
        sendMessage(
          site,
          "DUPLICATE_RECEIVE",
          "[SageAssistant]😵疑似重复收货" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 15 11,16 * * MON-FRI")
  public void sendDuplicateWO() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    robotLastRunService.getLastRun("DUPLICATE_WORK_ORDER");

    Sites.forEach(site -> {
      List<String> list = noticeMapper.duplicatedWO(site);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        String PJT = list.get(i);
        msg.append(PJT).append("\n\n");
      }

      if (msg.length() > 0) {
        sendMessage(
          site,
          "DUPLICATE_WORK_ORDER",
          "[SageAssistant]😵疑似重复工包" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 0/10 9-11,13-17 * * MON-FRI")
  public void sendMixProjectBetweenZHUAndYSH() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("MIX_PROJECT_BETWEEN_ZHU_YSH");

    final StringBuilder msg = new StringBuilder();

    List<MixPJTInPO> list = noticeMapper.mixPOProjectBetweenZHUAndYSH();

    for (int i = 0; i < list.size(); i++) {
      MixPJTInPO o = list.get(i);
      msg.append("项目号:\t").append(o.getProjectNO()).append("\n");
      msg.append("采购单:\t").append(o.getPO()).append("\n");
      msg.append("采购行:\t").append(o.getLine()).append("\n");
      msg.append("ＰＮ:\t").append(o.getPN()).append("\n");
      msg.append("采购人:\t").append(o.getCreateUser()).append("\n");
      msg
        .append("采购日期:\t")
        .append(Utils.formatDate(o.getCreateDate()))
        .append("\n");
      msg.append("\n");
    }

    if (msg.length() > 0) {
      sendMessage(
        "ZHU",
        "MIX_PROJECT_BETWEEN_ZHU_YSH",
        "[SageAssistant]🤯珠海和上海采购单混用项目号" +
        "[" +
        msg.length() +
        "]",
        msg
      );
    }

    List<MixPJTInWO> list2 = noticeMapper.mixWOProjectBetweenZHUAndYSH();
    final StringBuilder msg2 = new StringBuilder();

    for (int i = 0; i < list2.size(); i++) {
      MixPJTInWO o = list2.get(i);
      msg2.append("项目号:\t").append(o.getProjectNO()).append("\n");
      msg2.append("工包:\t").append(o.getWO()).append("\n");
      msg2.append("ＰＮ:\t").append(o.getPN()).append("\n");
      msg2.append("创建人:\t").append(o.getCreateUser()).append("\n");
      msg2
        .append("创建日期:\t")
        .append(Utils.formatDate(o.getCreateDate()))
        .append("\n");
      msg2.append("\n");
    }

    if (msg2.length() > 0) {
      sendMessage(
        "ZHU",
        "MIX_PROJECT_BETWEEN_ZHU_YSH",
        "[SageAssistant]🤯珠海和上海工包混用项目号" +
        "[共" +
        list2.size() +
        "条]",
        msg2
      );
    }
  }

  @Scheduled(cron = "0 0/10 9-11,13-17 * * MON-FRI")
  public void sendNewReceive() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    String lastRun = robotLastRunService.getLastRun("NEW_RECEIVE");

    Sites.forEach(site -> {
      List<SuspectDuplicatedRA> list = noticeMapper.findNewRASince(
        site,
        lastRun
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        SuspectDuplicatedRA ra = list.get(i);
        msg.append("项目号:\t").append(ra.getProjectNO()).append("\n");
        msg.append("ＰＮ:\t").append(ra.getPN()).append("\n");
        msg
          .append("第" + ra.getSeq() + "次采购单:\t")
          .append(ra.getPurchaseNO())
          .append(" [")
          .append(ra.getPurchaseLine())
          .append("] ")
          .append(Utils.formatDate(ra.getPurchaseDate()))
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次收货单:\t")
          .append(ra.getReceiptNO())
          .append(" [")
          .append(ra.getReceiptLine())
          .append("] ")
          .append(Utils.formatDate(ra.getReceiptDate()))
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次收货数量:\t")
          .append(ra.getReceiptQty())
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次金额:\t")
          .append(ra.getReceiptAmount().setScale(2, RoundingMode.HALF_UP))
          .append(" ")
          .append(ra.getCurrency())
          .append("\n");
        msg
          .append("第" + ra.getSeq() + "次采购人:\t")
          .append(ra.getPurchaser())
          .append("\n");
        msg
          .append("项目总收货数量:\t")
          .append(ra.getTotalReceiptQty())
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        sendMessage(
          site,
          "NEW_RECEIVE",
          "[SageAssistant]🤯新收货通知" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 30 9 * * MON-FRI")
  public void sendLongTimeNoDelivery() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("LONG_TIME_NO_DELIVERY");

    Sites.forEach(site -> {
      List<DaysDiffRange> ddrs = Arrays.asList(
        new DaysDiffRange(30, -45, -30),
        new DaysDiffRange(45, -60, -45),
        new DaysDiffRange(60, -90, -60),
        new DaysDiffRange(90, -120, -90),
        new DaysDiffRange(120, -150, -120),
        new DaysDiffRange(150, -9999, -150)
      );

      ddrs.forEach(ddr -> {
        List<TobeDelivery> list = noticeMapper.findLongTimeNoDelivery(
          site,
          ddr.getStart(),
          ddr.getEnd()
        );
        final StringBuilder msg = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
          TobeDelivery order = list.get(i);
          msg.append("项目号:\t").append(order.getProjectNO()).append("\n");
          msg.append("订单类型:\t").append(order.getOrderType()).append("\n");
          msg
            .append("ＰＮ:\t")
            .append(order.getPN())
            .append(" ")
            .append(order.getDescription())
            .append("\n");
          msg.append("数量:\t").append(order.getQty()).append("\n");
          msg
            .append("金额:\t")
            .append(order.getNetPrice().setScale(2, RoundingMode.HALF_UP))
            .append(" ")
            .append(order.getCurrency())
            .append("\n");
          msg
            .append("客户:\t")
            .append(order.getCustomerCode())
            .append(" ")
            .append(order.getCustomerName())
            .append("\n");
          msg
            .append("要求交货日期:\t")
            .append(Utils.formatDate(order.getRequestDate()))
            .append("\n");
          msg
            .append("计划交货日期:\t")
            .append(Utils.formatDate(order.getPlanedDate()))
            .append("\n");
          msg.append("\n");
        }

        if (msg.length() > 0) {
          msg.append(
            "\n\nhttps://sageassistant/#/Todo?page=Delivery&site=" + site
          );
          sendMessage(
            site,
            "LONG_TIME_NO_DELIVERY",
            "[SageAssistant]🧯🧯订单交付严重超期(" +
            ddr.getDiff() +
            "天)🧯🧯" +
            "[共" +
            list.size() +
            "条]",
            msg
          );
        }
      });
    });
  }

  @Scheduled(cron = "0 20 10 * * MON-FRI")
  public void sendNoBomServiceOrder() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("SERVICE_ORDER_NO_WO");

    // for customer support service
    Sites.forEach(site -> {
      List<TobeDealWithOrderLine> list = noticeMapper.findNOBomServiceOrder(
        site
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        TobeDealWithOrderLine order = list.get(i);
        msg.append("项目号:\t").append(order.getProjectNO()).append("\n");
        msg
          .append("类型:\t")
          .append(order.getOrderType())
          .append(" ")
          .append(order.getOrderCategory())
          .append("\n");
        msg
          .append("订单日期:\t")
          .append(Utils.formatDate(order.getOrderDate()))
          .append("\n");
        msg
          .append("ＰＮ:\t")
          .append(order.getPN())
          .append(" ")
          .append(order.getDescription())
          .append("\n");
        msg.append("数量:\t").append(order.getQty()).append("\n");
        msg
          .append("客户:\t")
          .append(order.getCustomerCode())
          .append(" ")
          .append(order.getCustomerName())
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        sendMessage(
          site,
          "SERVICE_ORDER_NO_WO",
          "[SageAssistant]😟售后订单建议创建工包" + "[共" + list.size() + "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 20 11 * * MON-FRI")
  public void sendTobeClosedWO() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("TO_BE_CLOSE_WO");

    Sites.forEach(site -> {
      List<TobeClosedWO> list = statusService.findTobeClosedWOBySite(site);
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        TobeClosedWO ra = list.get(i);
        msg.append("项目号:\t").append(ra.getProjectNO()).append("\n");
        msg.append("工包:\t").append(ra.getWorkOrderNO()).append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=Orphan-WO&site=" + site
        );
        sendMessage(
          site,
          "TO_BE_CLOSE_WO",
          "[SageAssistant]🧹WO关闭提醒, 订单项目已关闭, 工包未关闭" +
          "[共" +
          list.size() +
          "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 20 11,16 * * MON-FRI")
  public void sendDeadPurchaseLine() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }
    robotLastRunService.getLastRun("DEAD_PURCHASE_LINE");

    Sites.forEach(site -> {
      List<DeadPurchaseLine> list = statusService.findDeadPurchaseLineBySite(
        site
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        DeadPurchaseLine o = list.get(i);
        msg.append("项目号:\t").append(o.getProjectNO()).append("\n");
        msg.append("订单:\t").append(o.getOrderNO()).append("\n");
        msg
          .append("订单日期:\t")
          .append(Utils.formatDate(o.getOrderDate()))
          .append("\n");
        msg.append("采购单:\t").append(o.getPurchaseNO()).append("\n");
        msg.append("采购行:\t").append(o.getPurchaseLine()).append("\n");
        msg.append("ＰＮ:\t").append(o.getPurchasePN()).append("\n");
        msg.append("采购人:\t").append(o.getPurchaser()).append("\n");
        msg
          .append("采购日期:\t")
          .append(Utils.formatDate(o.getPurchaseDate()))
          .append("\n");
        msg.append("\n");
      }

      if (msg.length() > 0) {
        msg.append(
          "\n\nhttps://sageassistant/#/Todo?page=Dead-PO-Line&site=" + site
        );
        sendMessage(
          site,
          "DEAD_PURCHASE_LINE",
          "[SageAssistant]🧹采购单关闭提醒, 订单项目已关闭, 采购单未收货" +
          "[共" +
          list.size() +
          "条]",
          msg
        );
      }
    });
  }

  @Scheduled(cron = "0 8 9-11,13-17 * * MON-FRI")
  public void sendPreAnalyzeProjectProfit() {
    if (!Utils.isZhuhaiServer()) {
      return;
    }

    String lastRun = robotLastRunService.getLastRun(
      "PRE_ANALYZE_PROJECT_PROFIT"
    );

    Sites.forEach(site -> {
      if (site.equals("HKG")) {
        return;
      }

      List<ProjectProfit> list = noticeMapper.findPreAnalysesProjectProfit(
        site,
        lastRun,
        1.5f
      );
      final StringBuilder msg = new StringBuilder();

      for (int i = 0; i < list.size(); i++) {
        ProjectProfit o = list.get(i);
        msg.append("项目号:\t").append(o.getProjectNO()).append("\n");
        msg.append("订单:\t").append(o.getOrderNO()).append("\n");
        msg
          .append("ＰＮ:\t")
          .append(o.getPN())
          .append(" ")
          .append(o.getDescription())
          .append("\n");
        msg.append("数量:\t").append(o.getQty()).append("\n");
        msg
          .append("销售含税价:\t")
          .append(o.getProjectSalesPrice().setScale(2, RoundingMode.HALF_UP))
          .append(o.getSalesCurrency())
          .append("\n");
        if (!o.getSalesCurrency().equals(o.getLocalCurrency())) {
          msg
            .append("销售含税价:\t")
            .append(
              o.getProjectSalesLocalPrice().setScale(2, RoundingMode.HALF_UP)
            )
            .append(o.getLocalCurrency())
            .append("\n");
        }
        msg
          .append("采购含税价:\t")
          .append(o.getProjectLocalCost().setScale(2, RoundingMode.HALF_UP))
          .append(o.getLocalCurrency())
          .append("\n");
        msg
          .append("盈余:\t")
          .append(o.getProfit().setScale(2, RoundingMode.HALF_UP))
          .append("\n");

        msg
          .append("盈余比:\t")
          .append(
            o
              .getProjectSalesLocalPrice()
              .divide(o.getProjectLocalCost())
              .setScale(2, RoundingMode.HALF_UP)
          )
          .append("\n");
        msg.append("\n");
      }
      if (msg.length() > 0) {
        sendMessage(
          site,
          "PRE_ANALYZE_PROJECT_PROFIT",
          "[SageAssistant]🥶预分析项目盈亏(销售:成本<1.5)" +
          "[共" +
          list.size() +
          "条]",
          msg
        );
      }
    });
  }
}
