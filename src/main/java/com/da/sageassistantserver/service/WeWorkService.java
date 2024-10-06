/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-23 20:45:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:48:03                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.utils.Utils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeWorkService {

  static long REQUEST_LIMIT_PER_MIN = 20;
  static int REQUEST_LIMIT_MAX_SIZE = 2048;

  private static Bandwidth limit = Bandwidth
    .builder()
    .capacity(REQUEST_LIMIT_PER_MIN)
    .refillIntervally(REQUEST_LIMIT_PER_MIN, Duration.ofMinutes(1))
    .build();
  private static HashMap<String, Bucket> idMap = new HashMap<>();

  /**
   * Send a message
   *
   * @param robotId
   * @param data    JsonObject for webhook api
   */
  private static void sendMessage(String robotId, JSONObject data) {
    if (!idMap.containsKey(robotId)) {
      idMap.put(robotId, Bucket.builder().addLimit(limit).build());
    }
    if (idMap.get(robotId).tryConsume(1)) {
      HttpResponse<String> response = HttpService.request(
        "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + robotId,
        "POST",
        data.toString()
      );

      try {
        String html = response.body();
        JSONObject json = JSONObject.parseObject(html);
        if (json.getInteger("errcode").equals(0)) {
          log.debug("Send message: [{}] success!", data.toString());
        } else {
          log.error(
            "Send message: [{}], error: {}",
            data.toString(),
            json.getString("errmsg")
          );
        }
      } catch (Exception e) {
        log.error("Send message", e);
      }
    } else {
      log.warn(
        "WeWork request {} over limit {} per Min",
        robotId,
        REQUEST_LIMIT_PER_MIN
      );
    }
  }

  /**
   * Send simple message, text, or markdown
   *
   * @param robotId
   * @param messageType
   * @param message
   */
  public static void sendMessage(
    String robotId,
    String message,
    String messageType
  ) {
    Utils
      .splitStringByByteSize(message, REQUEST_LIMIT_MAX_SIZE)
      .forEach(limitLenMsg -> {
        JSONObject data = new JSONObject();
        JSONObject msg = new JSONObject();
        data.put("msgtype", messageType);

        StringBuilder sb = new StringBuilder();
        String[] paragraphs = limitLenMsg.split("\n\n");

        for (String paragraph : paragraphs) {
          String[] lines = paragraph.split("\n");
          for (String line : lines) {
            if (line.indexOf(":\t") > -1) {
              String[] factKV = line.split("\t");
              sb.append(Utils.withRightPad(factKV[0], 5, '\u3000')); // 全角空格
              if (factKV.length == 2) {
                sb.append(factKV[1]);
                sb.append('\n');
              }
            } else {
              sb.append(line);
              sb.append('\n');
            }
          }

          sb.append('\n');
        }

        msg.put("content", sb.toString());
        data.put(messageType, msg);

        sendMessage(robotId, data);
      });
  }

  /**
   * Send simple message, text
   *
   * @param robotId
   * @param message
   */
  public static void sendMessage(String robotId, String message) {
    sendMessage(robotId, message, "text");
  }
}
