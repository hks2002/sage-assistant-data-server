/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 20:39:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:31:46                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.StockHistory;
import com.da.sageassistantserver.model.StockSummary;
import com.da.sageassistantserver.service.StockService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class StockController {

  @Autowired
  private StockService stockService;

  @GetMapping("/Data/CheckPN")
  public String checkPN(
    @RequestParam(value = "PN", required = false, defaultValue = "--") String PN
  ) {
    return stockService.checkPN(PN);
  }

  @GetMapping("/Data/StockOptionPN")
  public String getStockOptionPN(
    @RequestParam(value = "PN", required = false, defaultValue = "--") String PN
  ) {
    return stockService.getStockOptionPN(PN);
  }

  @GetMapping("/Data/StockQty")
  public String getStockQty(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(value = "PN", required = false, defaultValue = "--") String PN
  ) {
    return stockService.getStockQty(Site, PN);
  }

  @GetMapping("/Data/StockSummary")
  public List<StockSummary> getStockSummary(
    @RequestParam(
      value = "site",
      required = false,
      defaultValue = "ZHU"
    ) String Site
  ) {
    return (stockService.getStockSummary(Site));
  }

  @GetMapping("/Data/StockHistory")
  public List<StockHistory> getStockHistory(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "PnOrName",
      required = false,
      defaultValue = "%%"
    ) String PnOrName,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo
  ) {
    return (
      stockService.getStockHistory(
        Site,
        PnOrName,
        DateFrom,
        DateTo + " 23:59:59.999"
      )
    );
  }
}
