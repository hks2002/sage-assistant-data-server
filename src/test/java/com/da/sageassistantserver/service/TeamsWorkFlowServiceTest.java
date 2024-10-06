/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-16 17:14:44                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 15:23:02                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TeamsWorkFlowServiceTest {

  @Test
  void testSendMessage() throws ExecutionException {
    TeamsWorkFlowService.sendEmailMessage(
      "xxx",
      "新的title",
      "XXXX",
      "新的消息:\t好消息\n\nPN:\t好消息\nITEM1:\t<H1>好消息</H1>\n"
    );
  }
}
