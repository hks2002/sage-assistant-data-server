/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 19:06:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:30:54                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.CostHistory;
import com.da.sageassistantserver.model.DeliveryDuration;
import com.da.sageassistantserver.model.PnDetails;
import com.da.sageassistantserver.model.PnRootPn;
import com.da.sageassistantserver.model.QuoteHistory;
import com.da.sageassistantserver.model.SalesHistory;
import com.da.sageassistantserver.model.StockInfo;
import com.da.sageassistantserver.service.PnService;
import com.da.sageassistantserver.utils.Utils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class PnController {

  @Autowired
  private PnService pnService;

  @GetMapping("/Data/PNHelper")
  public List<PnRootPn> getPNs(
    @RequestParam(
      value = "PN",
      required = false,
      defaultValue = "%%"
    ) String pnOrPnRoot,
    @RequestParam(
      value = "Count",
      required = false,
      defaultValue = "200"
    ) Integer Count
  ) {
    return (pnService.findPnByStartWith(pnOrPnRoot, Count));
  }

  @GetMapping("/Data/MakeShortPn")
  public String makeShortPn(
    @RequestParam(value = "Pn", required = true) String PN
  ) {
    return Utils.makeShortPn(PN);
  }

  @GetMapping("/Data/PNsInFamily")
  public List<PnDetails> getPNsInFamily(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findAllPnByPnRoot(PnRoot));
  }

  @GetMapping("/Data/SalesHistory")
  public List<SalesHistory> getSalesHistory(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findSalesHistoryByPnRoot(PnRoot));
  }

  @GetMapping("/Data/QuoteHistory")
  public List<QuoteHistory> getQuoteHistory(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findQuoteHistoryByPnRoot(PnRoot));
  }

  @GetMapping("/Data/CostHistory")
  public List<CostHistory> getCostHistory(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findCostHistoryByPnRoot(PnRoot));
  }

  @GetMapping("/Data/DeliveryDuration")
  public List<DeliveryDuration> getDeliveryDuration(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findDeliveryDurationByPnRoot(PnRoot));
  }

  @GetMapping("/Data/InventoryStock")
  public List<StockInfo> getInventoryStock(
    @RequestParam(
      value = "PnRoot",
      required = false,
      defaultValue = "NULL"
    ) String PnRoot
  ) {
    return (pnService.findStockInfoByPnRoot(PnRoot));
  }
}
