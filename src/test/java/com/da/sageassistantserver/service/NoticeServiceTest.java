/******************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                     *
 * @CreatedDate           : 2023-03-16 17:14:44                               *
 * @LastEditors           : Robert Huang<56649783@qq.com>                     *
 * @LastEditDate          : 2024-08-07 15:55:53                               *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                   *
 *****************************************************************************/

package com.da.sageassistantserver.service;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
public class NoticeServiceTest {

  @TestConfiguration
  static class prepare {

    @Bean
    public NoticeService getService() {
      return new NoticeService();
    }
  }

  @Autowired
  NoticeService noticeService;

  @Test
  void testSendMessage() throws ExecutionException {
    noticeService.sendPNNotActive();
  }

  @Test
  void testSendMessage2() throws ExecutionException {
    noticeService.sendNewReceive();
  }
}
