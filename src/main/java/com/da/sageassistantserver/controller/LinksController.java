/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 22:30:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:14:12                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.LinksDeliveryLine;
import com.da.sageassistantserver.model.LinksInvoiceLine;
import com.da.sageassistantserver.model.LinksPurchaseLine;
import com.da.sageassistantserver.model.LinksReceiptLine;
import com.da.sageassistantserver.model.LinksSalesLine;
import com.da.sageassistantserver.service.LinksService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LinksController {

  @Autowired
  private LinksService linksService;

  @GetMapping("/Data/LinksLineCnt")
  public String getTobeTracking(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO
  ) {
    return String.valueOf(
      linksService.findLinksLineCnt(
        Site,
        ProjectNO,
        OrderType,
        DateFrom,
        DateTo + " 23:59:59.999",
        CustomerCode,
        VendorCode
      )
    );
  }

  @GetMapping("/Data/LinksSalesLine")
  public List<LinksSalesLine> findLinksSalesLine(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "10"
    ) Integer Limit
  ) {
    return (
      linksService.findLinksSalesLine(
        Site,
        ProjectNO,
        OrderType,
        DateFrom,
        DateTo + " 23:59:59.999",
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      )
    );
  }

  @GetMapping("/Data/LinksPurchaseLine")
  public List<LinksPurchaseLine> findLinksPurchaseLine(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "10"
    ) Integer Limit
  ) {
    return linksService.findLinksPurchaseLine(
      Site,
      ProjectNO,
      OrderType,
      DateFrom,
      DateTo + " 23:59:59.999",
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    );
  }

  @GetMapping("/Data/LinksReceiptLine")
  public List<LinksReceiptLine> findLinksReceiptLine(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "10"
    ) Integer Limit
  ) {
    return linksService.findLinksReceiptLine(
      Site,
      ProjectNO,
      OrderType,
      DateFrom,
      DateTo + " 23:59:59.999",
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    );
  }

  @GetMapping("/Data/LinksDeliveryLine")
  public List<LinksDeliveryLine> findLinksDeliveryLine(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "10"
    ) Integer Limit
  ) {
    return linksService.findLinksDeliveryLine(
      Site,
      ProjectNO,
      OrderType,
      DateFrom,
      DateTo + " 23:59:59.999",
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    );
  }

  @GetMapping("/Data/LinksInvoiceLine")
  public List<LinksInvoiceLine> findLinksInvoiceLine(
    @RequestParam(
      value = "Site",
      required = false,
      defaultValue = "ZHU"
    ) String Site,
    @RequestParam(
      value = "OrderType",
      required = false,
      defaultValue = ""
    ) String OrderType,
    @RequestParam(
      value = "DateFrom",
      required = false,
      defaultValue = "2000-01-01"
    ) String DateFrom,
    @RequestParam(
      value = "DateTo",
      required = false,
      defaultValue = "1999-12-31"
    ) String DateTo,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(
      value = "VendorCode",
      required = false,
      defaultValue = ""
    ) String VendorCode,
    @RequestParam(
      value = "ProjectNO",
      required = false,
      defaultValue = ""
    ) String ProjectNO,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "10"
    ) Integer Limit
  ) {
    return linksService.findLinksInvoiceLine(
      Site,
      ProjectNO,
      OrderType,
      DateFrom,
      DateTo + " 23:59:59.999",
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    );
  }
}
