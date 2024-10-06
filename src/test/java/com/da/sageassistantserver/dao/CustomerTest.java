/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-12 21:46:07                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-11-05 20:32:18                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerTest {

  @Autowired
  private CustomerMapper customerMapper;

  @Test
  void testFindCustomerByCodeOrName() {
    customerMapper.findCustomerByCodeOrName("00870", 5);
  }

  @Test
  void testFindCustomerDetailsByCode() {
    customerMapper.findCustomerDetailsByCode("00870");
  }

  @Test
  void testFindCustomerSumAmount() {
    customerMapper.findCustomerSumAmount(
      "ZHU",
      "00870",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
    customerMapper.findCustomerSumAmount(
      "ZHU",
      "00870",
      "2024-01-01",
      "2024-12-31",
      "Year"
    );
    customerMapper.findCustomerSumAmount(
      "ZHU",
      "",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
    customerMapper.findCustomerSumAmount(
      "ALL",
      "",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
  }

  @Test
  void testFindCustomerOTD() {
    customerMapper.findCustomerOTD(
      "ZHU",
      "00870",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
    customerMapper.findCustomerOTD(
      "ZHU",
      "00870",
      "2024-01-01",
      "2024-12-31",
      "Year"
    );
    customerMapper.findCustomerOTD(
      "ZHU",
      "",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
    customerMapper.findCustomerOTD(
      "ALL",
      "",
      "2024-01-01",
      "2024-12-31",
      "Month"
    );
  }
}
