/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2023-03-11 16:26:53                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:52:23                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class CommonServiceTest {

  // Unit Test for Service，sample code
  @TestConfiguration
  static class prepare {

    @Bean
    public CommonService getService() {
      return new CommonService();
    }
  }

  @Autowired
  CommonService commonService;

  @Test
  void testAllSites() {
    commonService.getAllSites();
  }
}
