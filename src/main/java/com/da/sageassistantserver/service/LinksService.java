/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:57:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:49:11                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.LinksMapper;
import com.da.sageassistantserver.model.LinksDeliveryLine;
import com.da.sageassistantserver.model.LinksInvoiceLine;
import com.da.sageassistantserver.model.LinksPurchaseLine;
import com.da.sageassistantserver.model.LinksReceiptLine;
import com.da.sageassistantserver.model.LinksSalesLine;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinksService {

  @Autowired
  private LinksMapper linksMapper;

  public Integer findLinksLineCnt(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode
  ) {
    if (OrderType.equals("SO")) return linksMapper.findLinksLineCntBySO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      CustomerCode
    ); else if (OrderType.equals("PO")) return linksMapper.findLinksLineCntByPO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      VendorCode
    ); else return 0;
  }

  public List<LinksSalesLine> findLinksSalesLine(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode,
    Integer Offset,
    Integer Limit
  ) {
    if (OrderType.equals("SO")) return linksMapper.findLinksSalesLineBySO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    ); else if (
      OrderType.equals("PO")
    ) return linksMapper.findLinksSalesLineByPO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    ); else return Collections.emptyList();
  }

  public List<LinksPurchaseLine> findLinksPurchaseLine(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode,
    Integer Offset,
    Integer Limit
  ) {
    if (OrderType.equals("SO")) return linksMapper.findLinksPurchaseLineBySO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    ); else if (
      OrderType.equals("PO")
    ) return linksMapper.findLinksPurchaseLineByPO(
      Site,
      ProjectNO,
      DateFrom,
      DateTo,
      CustomerCode,
      VendorCode,
      Offset,
      Limit
    ); else return Collections.emptyList();
  }

  public List<LinksReceiptLine> findLinksReceiptLine(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode,
    Integer Offset,
    Integer Limit
  ) {
    if (OrderType.equals("SO")) {
      return linksMapper.findLinksReceiptLineBySO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else if (OrderType.equals("PO")) {
      return linksMapper.findLinksReceiptLineByPO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else return Collections.emptyList();
  }

  public List<LinksDeliveryLine> findLinksDeliveryLine(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode,
    Integer Offset,
    Integer Limit
  ) {
    if (OrderType.equals("SO")) {
      return linksMapper.findLinksDeliveryLineBySO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else if (OrderType.equals("PO")) {
      return linksMapper.findLinksDeliveryLineByPO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else return Collections.emptyList();
  }

  public List<LinksInvoiceLine> findLinksInvoiceLine(
    String Site,
    String ProjectNO,
    String OrderType,
    String DateFrom,
    String DateTo,
    String CustomerCode,
    String VendorCode,
    Integer Offset,
    Integer Limit
  ) {
    if (OrderType.equals("SO")) {
      return linksMapper.findLinksInvoiceLineBySO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else if (OrderType.equals("PO")) {
      return linksMapper.findLinksInvoiceLineByPO(
        Site,
        ProjectNO,
        DateFrom,
        DateTo,
        CustomerCode,
        VendorCode,
        Offset,
        Limit
      );
    } else return Collections.emptyList();
  }
}
