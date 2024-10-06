/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-03-11 16:26:53                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-06-25 12:51:35                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class UserServiceTest {

  // Unit Test for Service，sample code
  @TestConfiguration
  static class prepare {

    @Bean
    public UserService getService() {
      return new UserService();
    }
  }

  @Autowired
  UserService userService;

  @Test
  public void setLogTemplate() {
    userService.getUserByLoginName("rhuang");
  }
}
