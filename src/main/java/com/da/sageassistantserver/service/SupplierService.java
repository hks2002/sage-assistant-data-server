/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:47:45                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.SupplierMapper;
import com.da.sageassistantserver.model.BusinessPartnerName;
import com.da.sageassistantserver.model.SupplierDetails;
import com.da.sageassistantserver.model.SupplierNCHistory;
import com.da.sageassistantserver.model.SupplierNCHistoryTiny;
import com.da.sageassistantserver.model.SupplierNCSummary;
import com.da.sageassistantserver.model.SupplierName;
import com.da.sageassistantserver.model.SupplierOTD;
import com.da.sageassistantserver.model.SupplierOTDTop;
import com.da.sageassistantserver.model.SupplierOrder;
import com.da.sageassistantserver.model.SupplierQPY;
import com.da.sageassistantserver.model.SupplierQPYTop;
import com.da.sageassistantserver.model.SupplierSummaryAmount;
import com.da.sageassistantserver.model.SupplierSummaryAmountTop;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupplierService {

  @Autowired
  private SupplierMapper supplierMapper;

  public List<SupplierName> getSupplierByCodeOrName(
    String cond,
    Integer Count
  ) {
    List<SupplierName> listPage = supplierMapper.findSupplierByCodeOrName(
      "%" + cond + "%",
      Count
    );

    return listPage;
  }

  public String getBusinessPartnerByCode(String code) {
    List<BusinessPartnerName> listPage = supplierMapper.findBusinessPartnerByCode(
      code
    );
    if (listPage.size() == 0) {
      return "";
    } else {
      return listPage.get(0).getBPName();
    }
  }

  public List<SupplierDetails> getSupplierDetails(String SupplierCode) {
    return supplierMapper.findSupplierDetailsByCode(SupplierCode);
  }

  public List<SupplierSummaryAmount> getSupplierSummaryAmount(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return supplierMapper.findSupplierSumAmount(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Interval
    );
  }

  public List<SupplierSummaryAmountTop> getSupplierSummaryAmountTop(
    String Site,
    String SupplierType,
    String DateFrom,
    String DateTo,
    Integer Limit
  ) {
    return supplierMapper.findSupplierSumAmountTop(
      Site,
      SupplierType,
      DateFrom,
      DateTo,
      Limit
    );
  }

  public List<SupplierOTD> getSupplierOTD(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return supplierMapper.findSupplierOTD(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Interval
    );
  }

  public List<SupplierOTDTop> getSupplierOTDTop(
    String Site,
    String SupplierType,
    String DateFrom,
    String DateTo,
    Integer Limit,
    String Sort
  ) {
    return supplierMapper.findSupplierOTDTop(
      Site,
      SupplierType,
      DateFrom,
      DateTo,
      Limit,
      Sort
    );
  }

  public List<SupplierQPY> getSupplierQPY(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return supplierMapper.findSupplierQPY(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Interval
    );
  }

  public List<SupplierQPYTop> getSupplierQPYTop(
    String Site,
    String SupplierType,
    String DateFrom,
    String DateTo,
    Integer Limit,
    String Sort
  ) {
    return supplierMapper.findSupplierQPYTop(
      Site,
      SupplierType,
      DateFrom,
      DateTo,
      Limit,
      Sort
    );
  }

  public List<SupplierNCSummary> getSupplierNCSummary(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return supplierMapper.findSupplierNCSummary(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Interval
    );
  }

  public Integer getSupplierOrdersCnt(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String OrderStatus
  ) {
    return supplierMapper.findSupplierOrdersCnt(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      OrderStatus
    );
  }

  public List<SupplierOrder> getSupplierOrders(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    String OrderStatus,
    Integer Offset,
    Integer Limit
  ) {
    return supplierMapper.findSupplierOrders(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      OrderStatus,
      Offset,
      Limit
    );
  }

  public Integer getSupplierNCHistoryCnt(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo
  ) {
    return supplierMapper.findSupplierNCHistoryCnt(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo
    );
  }

  public List<SupplierNCHistory> getSupplierNCHistory(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    Integer Offset,
    Integer Limit
  ) {
    return supplierMapper.findSupplierNCHistory(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Offset,
      Limit
    );
  }

  public List<SupplierNCHistoryTiny> getSupplierNCHistoryTiny(
    String Site,
    String SupplierType,
    String SupplierCode,
    String DateFrom,
    String DateTo,
    Integer Offset,
    Integer Limit
  ) {
    List<SupplierNCHistory> listRaw = supplierMapper.findSupplierNCHistory(
      Site,
      SupplierType,
      SupplierCode,
      DateFrom,
      DateTo,
      Offset,
      Limit
    );
    List<SupplierNCHistoryTiny> list = new ArrayList<>();

    for (SupplierNCHistory raw : listRaw) {
      if (raw.getNCCAT0() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(1);
        item.setDate(raw.getNCDAT0());
        item.setCat(raw.getNCCAT0());
        item.setType(raw.getNCTYP0());
        item.setCri(raw.getNCCRIT0());
        item.setDes(raw.getNCDES0());
        item.setCreatedBy(raw.getCreatedBy0());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT1() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(2);
        item.setDate(raw.getNCDAT1());
        item.setCat(raw.getNCCAT1());
        item.setType(raw.getNCTYP1());
        item.setCri(raw.getNCCRIT1());
        item.setDes(raw.getNCDES1());
        item.setCreatedBy(raw.getCreatedBy1());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT2() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(3);
        item.setDate(raw.getNCDAT2());
        item.setCat(raw.getNCCAT2());
        item.setType(raw.getNCTYP2());
        item.setCri(raw.getNCCRIT2());
        item.setDes(raw.getNCDES2());
        item.setCreatedBy(raw.getCreatedBy2());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT3() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(4);
        item.setDate(raw.getNCDAT3());
        item.setCat(raw.getNCCAT3());
        item.setType(raw.getNCTYP3());
        item.setCri(raw.getNCCRIT3());
        item.setDes(raw.getNCDES3());
        item.setCreatedBy(raw.getCreatedBy3());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT4() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(5);
        item.setDate(raw.getNCDAT4());
        item.setCat(raw.getNCCAT4());
        item.setType(raw.getNCTYP4());
        item.setCri(raw.getNCCRIT4());
        item.setDes(raw.getNCDES4());
        item.setCreatedBy(raw.getCreatedBy4());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT5() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(6);
        item.setDate(raw.getNCDAT5());
        item.setCat(raw.getNCCAT5());
        item.setType(raw.getNCTYP5());
        item.setCri(raw.getNCCRIT5());
        item.setDes(raw.getNCDES5());
        item.setCreatedBy(raw.getCreatedBy5());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT6() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(7);
        item.setDate(raw.getNCDAT6());
        item.setCat(raw.getNCCAT6());
        item.setType(raw.getNCTYP6());
        item.setCri(raw.getNCCRIT6());
        item.setDes(raw.getNCDES6());
        item.setCreatedBy(raw.getCreatedBy6());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT7() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(8);
        item.setDate(raw.getNCDAT7());
        item.setCat(raw.getNCCAT7());
        item.setType(raw.getNCTYP7());
        item.setCri(raw.getNCCRIT7());
        item.setDes(raw.getNCDES7());
        item.setCreatedBy(raw.getCreatedBy7());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT8() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(9);
        item.setDate(raw.getNCDAT8());
        item.setCat(raw.getNCCAT8());
        item.setType(raw.getNCTYP8());
        item.setCri(raw.getNCCRIT8());
        item.setDes(raw.getNCDES8());
        item.setCreatedBy(raw.getCreatedBy8());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT9() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(10);
        item.setDate(raw.getNCDAT9());
        item.setCat(raw.getNCCAT9());
        item.setType(raw.getNCTYP9());
        item.setCri(raw.getNCCRIT9());
        item.setDes(raw.getNCDES9());
        item.setCreatedBy(raw.getCreatedBy9());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT10() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(11);
        item.setDate(raw.getNCDAT10());
        item.setCat(raw.getNCCAT10());
        item.setType(raw.getNCTYP10());
        item.setCri(raw.getNCCRIT10());
        item.setDes(raw.getNCDES10());
        item.setCreatedBy(raw.getCreatedBy10());
        list.add(item);
      } else {
        continue;
      }
      if (raw.getNCCAT11() != null) {
        SupplierNCHistoryTiny item = new SupplierNCHistoryTiny();
        item.setSite(raw.getSite());
        item.setSupplierCode(raw.getSupplierCode());
        item.setNCNo(raw.getNCNo());
        item.setProjectCode(raw.getProjectNO());
        item.setReq(12);
        item.setDate(raw.getNCDAT11());
        item.setCat(raw.getNCCAT11());
        item.setType(raw.getNCTYP11());
        item.setCri(raw.getNCCRIT11());
        item.setDes(raw.getNCDES11());
        item.setCreatedBy(raw.getCreatedBy11());
        list.add(item);
      } else {
        continue;
      }
    }

    return list;
  }
}
