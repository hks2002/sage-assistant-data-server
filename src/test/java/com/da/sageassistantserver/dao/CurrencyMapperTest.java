/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-03-12 21:42:56                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-06-03 22:33:34                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CurrencyMapperTest {

  @Autowired
  private CurrencyMapper currencyMapper;

  @Test
  void testFindCurrencyRate() {
    currencyMapper.findCurrencyRate("USD", "CNY", "2020-01-01");
  }
}
