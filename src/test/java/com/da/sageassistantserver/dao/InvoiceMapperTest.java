/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-03-12 23:01:22                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-06-03 22:34:19                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvoiceMapperTest {

  @Autowired
  private InvoiceMapper invoiceMapper;

  @Test
  void testFindInvoiceBodyByFaPiao() {
    invoiceMapper.findInvoiceBodyByFaPiao("0000000000");
  }

  @Test
  void testFindInvoiceBodyByInvoiceNO() {
    invoiceMapper.findInvoiceBodyByInvoiceNO("ZFC1901001");
  }

  @Test
  void testFindInvoiceHeaderByFaPiao() {
    invoiceMapper.findInvoiceHeaderByFaPiao("0000000000");
  }

  @Test
  void testFindInvoiceHeaderByInvoiceNO() {
    invoiceMapper.findInvoiceHeaderByInvoiceNO("ZFC1901001");
  }

  @Test
  void testFindInvoiceNOByInvoiceNO() {
    invoiceMapper.findInvoiceNOByInvoiceNO("ZFC1901001", 5);
  }
}
