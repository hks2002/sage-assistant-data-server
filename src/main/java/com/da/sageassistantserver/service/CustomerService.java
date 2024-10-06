/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-31 16:27:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:44:36                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.dao.CustomerMapper;
import com.da.sageassistantserver.model.CustomerDetails;
import com.da.sageassistantserver.model.CustomerName;
import com.da.sageassistantserver.model.CustomerOTD;
import com.da.sageassistantserver.model.CustomerOrder;
import com.da.sageassistantserver.model.CustomerSummaryAmount;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

  @Autowired
  private CustomerMapper customerMapper;

  public List<CustomerName> getCustomerByCodeOrName(
    String cond,
    Integer count
  ) {
    if (cond.equals("%%")) {
      List<CustomerName> list = new ArrayList<>();

      CustomerName o = new CustomerName();
      o.setCustomerCode("%%");
      o.setCustomerName("");
      list.add(o);
      return list;
    }

    List<CustomerName> listPage = customerMapper.findCustomerByCodeOrName(
      "%" + cond + "%",
      count
    );

    return listPage;
  }

  public List<CustomerDetails> getCustomerDetails(
    @Param("CustomerCode") String CustomerCode
  ) {
    return customerMapper.findCustomerDetailsByCode(CustomerCode);
  }

  public Integer getCustomerOrdersCnt(
    String Site,
    String CustomerCode,
    String DateFrom,
    String DateTo,
    String OrderStatus
  ) {
    return customerMapper.findCustomerOrdersCnt(
      Site,
      CustomerCode,
      DateFrom,
      DateTo,
      OrderStatus
    );
  }

  public List<CustomerOrder> getCustomerOrders(
    String Site,
    String CustomerCode,
    String DateFrom,
    String DateTo,
    String OrderStatus,
    Integer Offset,
    Integer Limit
  ) {
    return customerMapper.findCustomerOrders(
      Site,
      CustomerCode,
      DateFrom,
      DateTo,
      OrderStatus,
      Offset,
      Limit
    );
  }

  public List<CustomerSummaryAmount> getCustomerSummaryAmount(
    String Site,
    String CustomerCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return customerMapper.findCustomerSumAmount(
      Site,
      CustomerCode,
      DateFrom,
      DateTo,
      Interval
    );
  }

  public List<CustomerOTD> getCustomerOTD(
    String Site,
    String CustomerCode,
    String DateFrom,
    String DateTo,
    String Interval
  ) {
    return customerMapper.findCustomerOTD(
      Site,
      CustomerCode,
      DateFrom,
      DateTo,
      Interval
    );
  }
}
