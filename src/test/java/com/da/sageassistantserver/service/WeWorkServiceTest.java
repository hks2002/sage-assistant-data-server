/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-16 17:14:44                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:52:56                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class WeWorkServiceTest {

  @Test
  void testSendMessageRaw() throws ExecutionException {
    HttpResponse<String> response = HttpService.request(
      "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + "xxx",
      "POST",
      "{\"msgtype\": \"text\", \"text\": {\"content\": \"hello world\"}}"
    );

    try {
      String html = response.body();
      JSONObject json = JSONObject.parseObject(html);
      if (json.getInteger("errcode").equals(0)) {
        log.debug("Send message: success!");
      } else {
        log.error("Send message: error: {}", json.getString("errmsg"));
      }
    } catch (Exception e) {
      log.error("Send message", e);
    }
  }

  @Test
  void testSendMessage() throws ExecutionException {
    WeWorkService.sendMessage(
      "f7f31c1d-6321-4756-aba7-0161b76ad487",
      "Hello, world"
    );
  }
}
