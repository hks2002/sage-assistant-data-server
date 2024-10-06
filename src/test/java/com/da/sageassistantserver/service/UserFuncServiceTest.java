/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-03-11 16:26:53                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-06-25 12:51:04                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class UserFuncServiceTest {

  // Unit Test for Service，sample code
  @TestConfiguration
  static class prepare {

    @Bean
    public UserFuncService getService() {
      return new UserFuncService();
    }
  }

  @Autowired
  UserFuncService userFuncService;

  @Test
  public void setLogTemplate() {
    userFuncService.getWebDavAccess("login_name");
  }
}
