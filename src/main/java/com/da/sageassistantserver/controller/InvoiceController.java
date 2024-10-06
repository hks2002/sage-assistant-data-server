/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 21:57:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:30:38                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.InvoiceBody;
import com.da.sageassistantserver.model.InvoiceHeader;
import com.da.sageassistantserver.service.InvoiceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class InvoiceController {

  @Autowired
  private InvoiceService invoiceService;

  @GetMapping("/Data/InvoiceNO")
  public String getInvoiceNO(
    @RequestParam(
      value = "InvoiceNO",
      required = false,
      defaultValue = "NULL"
    ) String InvoiceNO,
    @RequestParam(
      value = "Count",
      required = false,
      defaultValue = "10"
    ) Integer Count
  ) {
    return invoiceService.findInvoiceNOByInvoiceNO(InvoiceNO, Count);
  }

  @GetMapping("/Data/InvoiceHeaderByInvoiceNO")
  public InvoiceHeader getInvoiceHeader(
    @RequestParam(
      value = "InvoiceNO",
      required = false,
      defaultValue = "NULL"
    ) String InvoiceNO
  ) {
    return (invoiceService.findInvoiceHeaderByInvoiceNO(InvoiceNO));
  }

  @GetMapping("/Data/InvoiceBodyByInvoiceNO")
  public List<InvoiceBody> getInvoiceBody(
    @RequestParam(
      value = "InvoiceNO",
      required = false,
      defaultValue = "NULL"
    ) String InvoiceNO
  ) {
    return (invoiceService.findInvoiceBodyByInvoiceNO(InvoiceNO));
  }

  @GetMapping("/Data/InvoiceHeaderByFaPiao")
  public InvoiceHeader getInvoiceHeaderByFaPiao(
    @RequestParam(
      value = "FaPiao",
      required = false,
      defaultValue = "NULL"
    ) String FaPiao
  ) {
    return (invoiceService.findInvoiceHeaderByFaPiao(FaPiao));
  }

  @GetMapping("/Data/InvoiceBodyByFaPiao")
  public List<InvoiceBody> getInvoiceBodyByFaPiao(
    @RequestParam(
      value = "FaPiao",
      required = false,
      defaultValue = "NULL"
    ) String FaPiao
  ) {
    return (invoiceService.findInvoiceBodyByFaPiao(FaPiao));
  }
}
