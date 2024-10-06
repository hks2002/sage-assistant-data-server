/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 21:35:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:32:36                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

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
import com.da.sageassistantserver.service.SupplierService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class SupplierController {

  @Autowired
  private SupplierService SupplierService;

  @GetMapping("/Data/BPHelper")
  public String getBPName(
    @RequestParam(
      value = "bpCode",
      required = false,
      defaultValue = "XXXX"
    ) String bpCode
  ) {
    return SupplierService.getBusinessPartnerByCode(bpCode);
  }

  @GetMapping("/Data/SupplierHelper")
  public List<SupplierName> getSupplierName(
    @RequestParam(
      value = "supplierName",
      required = false,
      defaultValue = "%%"
    ) String SupplierCodeOrName,
    @RequestParam(
      value = "count",
      required = false,
      defaultValue = "50"
    ) Integer count
  ) {
    if (SupplierCodeOrName.equals("%%")) {
      SupplierName name = new SupplierName();
      name.setSupplierName("ALL");
      name.setSupplierCode("%%");
      return (List.of(name));
    }
    return (SupplierService.getSupplierByCodeOrName(SupplierCodeOrName, count));
  }

  @GetMapping("/Data/SupplierDetails")
  public List<SupplierDetails> getSupplierDetails(
    @RequestParam(
      value = "supplierCode",
      required = false,
      defaultValue = "NULL"
    ) String SupplierCode
  ) {
    return (SupplierService.getSupplierDetails(SupplierCode));
  }

  @GetMapping("/Data/SupplierSummaryAmount")
  public List<SupplierSummaryAmount> getSupplierSummaryAmount(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      SupplierService.getSupplierSummaryAmount(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/SupplierSummaryAmountTop")
  public List<SupplierSummaryAmountTop> getSupplierSummaryAmountTop(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      SupplierService.getSupplierSummaryAmountTop(
        Site,
        SupplierType,
        DateFrom,
        DateTo + " 23:59:59",
        Integer.parseInt(Limit)
      )
    );
  }

  @GetMapping("/Data/SupplierOTD")
  public List<SupplierOTD> getSupplierOTD(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      SupplierService.getSupplierOTD(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/SupplierOTDTop")
  public List<SupplierOTDTop> getSupplierOTDTop(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Limit", required = true) String Limit,
    @RequestParam(value = "Sort", required = true) String Sort
  ) {
    return (
      SupplierService.getSupplierOTDTop(
        Site,
        SupplierType,
        DateFrom,
        DateTo + " 23:59:59",
        Integer.parseInt(Limit),
        Sort
      )
    );
  }

  @GetMapping("/Data/SupplierQPY")
  public List<SupplierQPY> getSupplierQPY(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      SupplierService.getSupplierQPY(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/SupplierQPYTop")
  public List<SupplierQPYTop> getSupplierQPYTop(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Limit", required = true) String Limit,
    @RequestParam(value = "Sort", required = true) String Sort
  ) {
    return (
      SupplierService.getSupplierQPYTop(
        Site,
        SupplierType,
        DateFrom,
        DateTo + " 23:59:59",
        Integer.parseInt(Limit),
        Sort
      )
    );
  }

  @GetMapping("/Data/SupplierNCSummary")
  public List<SupplierNCSummary> getSupplierNCSummary(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      SupplierService.getSupplierNCSummary(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/SupplierOrdersCnt")
  public Integer getSupplierOrdersCnt(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "OrderStatus", required = true) String OrderStatus
  ) {
    return (
      SupplierService.getSupplierOrdersCnt(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        OrderStatus
      )
    );
  }

  @GetMapping("/Data/SupplierOrders")
  public List<SupplierOrder> getSupplierOrders(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "OrderStatus", required = true) String OrderStatus,
    @RequestParam(value = "Offset", required = true) String Offset,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      SupplierService.getSupplierOrders(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        OrderStatus,
        Integer.parseInt(Offset),
        Integer.parseInt(Limit)
      )
    );
  }

  @GetMapping("/Data/SupplierNCHistoryCnt")
  public Integer getSupplierNCHistoryCnt(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo
  ) {
    return (
      SupplierService.getSupplierNCHistoryCnt(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59"
      )
    );
  }

  @GetMapping("/Data/SupplierNCHistory")
  public List<SupplierNCHistory> getSupplierNCHistory(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Offset", required = true) String Offset,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      SupplierService.getSupplierNCHistory(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Integer.parseInt(Offset),
        Integer.parseInt(Limit)
      )
    );
  }

  @GetMapping("/Data/SupplierNCHistoryTiny")
  public List<SupplierNCHistoryTiny> getSupplierNCHistoryTiny(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "SupplierType", required = true) String SupplierType,
    @RequestParam(value = "SupplierCode", required = true) String SupplierCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Offset", required = true) String Offset,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      SupplierService.getSupplierNCHistoryTiny(
        Site,
        SupplierType,
        SupplierCode,
        DateFrom,
        DateTo + " 23:59:59",
        Integer.parseInt(Offset),
        Integer.parseInt(Limit)
      )
    );
  }
}
