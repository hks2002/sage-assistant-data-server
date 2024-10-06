/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-31 16:29:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:29:43                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.CustomerDetails;
import com.da.sageassistantserver.model.CustomerName;
import com.da.sageassistantserver.model.CustomerOTD;
import com.da.sageassistantserver.model.CustomerOrder;
import com.da.sageassistantserver.model.CustomerSummaryAmount;
import com.da.sageassistantserver.service.CustomerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CustomerController {

  @Autowired
  private CustomerService CustomerService;

  @GetMapping("/Data/CustomerHelper")
  public List<CustomerName> getCustomerName(
    @RequestParam(
      value = "customerName",
      required = false,
      defaultValue = "%%"
    ) String CustomerCodeOrName,
    @RequestParam(
      value = "count",
      required = false,
      defaultValue = "50"
    ) Integer count
  ) {
    if (CustomerCodeOrName.equals("%%")) {
      CustomerName name = new CustomerName();
      name.setCustomerName("ALL");
      name.setCustomerCode("%%");
      return (List.of(name));
    }
    return (CustomerService.getCustomerByCodeOrName(CustomerCodeOrName, count));
  }

  @GetMapping("/Data/CustomerDetails")
  public List<CustomerDetails> getCustomerDetails(
    @RequestParam(
      value = "customerCode",
      required = false,
      defaultValue = "NULL"
    ) String CustomerCode
  ) {
    return (CustomerService.getCustomerDetails(CustomerCode));
  }

  @GetMapping("/Data/CustomerSummaryAmount")
  public List<CustomerSummaryAmount> getCustomerSummaryAmount(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      CustomerService.getCustomerSummaryAmount(
        Site,
        CustomerCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/CustomerOTD")
  public List<CustomerOTD> getCustomerOTD(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(
      value = "CustomerCode",
      required = false,
      defaultValue = ""
    ) String CustomerCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "Interval", required = true) String Interval
  ) {
    return (
      CustomerService.getCustomerOTD(
        Site,
        CustomerCode,
        DateFrom,
        DateTo + " 23:59:59",
        Interval
      )
    );
  }

  @GetMapping("/Data/CustomerOrdersCnt")
  public Integer getCustomerOrdersCnt(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "CustomerCode", required = true) String CustomerCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "OrderStatus", required = true) String OrderStatus,
    @RequestParam(value = "Offset", required = true) String Offset,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      CustomerService.getCustomerOrdersCnt(
        Site,
        CustomerCode,
        DateFrom,
        DateTo + " 23:59:59",
        OrderStatus
      )
    );
  }

  @GetMapping("/Data/CustomerOrders")
  public List<CustomerOrder> getCustomerOrders(
    @RequestParam(value = "Site", required = true) String Site,
    @RequestParam(value = "CustomerCode", required = true) String CustomerCode,
    @RequestParam(value = "DateFrom", required = true) String DateFrom,
    @RequestParam(value = "DateTo", required = true) String DateTo,
    @RequestParam(value = "OrderStatus", required = true) String OrderStatus,
    @RequestParam(value = "Offset", required = true) String Offset,
    @RequestParam(value = "Limit", required = true) String Limit
  ) {
    return (
      CustomerService.getCustomerOrders(
        Site,
        CustomerCode,
        DateFrom,
        DateTo + " 23:59:59",
        OrderStatus,
        Integer.parseInt(Offset),
        Integer.parseInt(Limit)
      )
    );
  }
}
