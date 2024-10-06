/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-23 20:45:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 15:19:36                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONArray;
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
public class TeamsWorkFlowService {

  static long REQUEST_LIMIT_PER_MIN = 120;
  static int REQUEST_LIMIT_MAX_SIZE = 28 * 1024;

  private static Bandwidth limit = Bandwidth
    .builder()
    .capacity(REQUEST_LIMIT_PER_MIN)
    .refillIntervally(REQUEST_LIMIT_PER_MIN / 2, Duration.ofSeconds(30))
    .build();
  private static HashMap<String, Bucket> idMap = new HashMap<>();

  /**
   * Send a message
   * Group chart message robot doesn't need token
   *
   * @param workFlowUrl
   * @param data
   */
  public static void sendMessage(String workFlowUrl, JSONObject data) {
    if (!idMap.containsKey(workFlowUrl)) {
      idMap.put(workFlowUrl, Bucket.builder().addLimit(limit).build());
    }
    if (idMap.get(workFlowUrl).tryConsume(1)) {
      HttpResponse<String> response = HttpService.request(
        workFlowUrl,
        "POST",
        data.toString()
      );

      if (response.statusCode() != 202) {
        try {
          String html = response.body();
          JSONObject json = JSONObject.parseObject(html);
          JSONObject error = json.getJSONObject("error");

          log.error(
            "Send message: [{}], error: {}",
            error.getString("code"),
            error.getString("message")
          );
        } catch (Exception e) {
          log.error("Send message", e);
        }
      } else {
        log.debug("Send message: [{}] success!", data.toString());
      }
    } else {
      log.warn(
        "WeWork request {} over limit {} per Min",
        workFlowUrl,
        REQUEST_LIMIT_PER_MIN
      );
    }
  }

  /**
   * Send simple email message
   *
   * @param workFlowUrl
   * @param title
   * @param to
   * @param message
   */
  public static void sendEmailMessage(
    String workFlowUrl,
    String title,
    String to,
    String message
  ) {
    StringBuilder sb = new StringBuilder();
    String[] paragraphs = message.split("\n\n");

    for (String paragraph : paragraphs) {
      String[] lines = paragraph.split("\n");
      for (String line : lines) {
        if (line.indexOf(":\t") > -1) {
          String[] factKV = line.split("\t");
          sb
            .append("<B>")
            .append(Utils.withRightPad(factKV[0], 5, '\u3000'))
            .append(":</B>"); // 全角空格
          if (factKV.length == 2) {
            sb.append(factKV[1]);
            sb.append("<br>");
          }
        } else {
          sb.append("<B>").append(line).append("</B>");
          sb.append("<br>");
        }
      }

      sb.append("<br>");
    }

    JSONObject data = new JSONObject();
    data.put("title", title);
    data.put("to", to);
    data.put("msg", sb);

    sendMessage(workFlowUrl, data);
  }

  /**
   * Send simple AdaptiveCard message, text
   *
   * @reference https://adaptivecards.io/explorer/AdaptiveCard.html
   * @param workFlowUrl
   * @param msg
   */
  public static void sendAdaptiveCardMessage(
    String workFlowUrl,
    String totalMessage
  ) {
    Utils
      .splitStringByByteSize(totalMessage, REQUEST_LIMIT_MAX_SIZE)
      .forEach(msg -> {
        JSONObject message = new JSONObject();
        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        JSONObject content = new JSONObject();
        JSONArray body = new JSONArray();

        String[] paragraphs = msg.split("\n\n");
        for (String pText : paragraphs) {
          JSONObject p = new JSONObject();
          boolean isLineText = pText.contains(":\t");
          String[] lines = pText.split("\n");

          if (isLineText) {
            JSONArray FactSet = new JSONArray();
            for (String line : lines) {
              JSONObject Fact = new JSONObject();
              if (line.contains(":\t")) {
                String[] factKV = line.split(":\t");
                Fact.put("title", factKV[0]);
                Fact.put("value", factKV[1]);
              } else {
                Fact.put("title", line);
              }
              FactSet.add(Fact);
            }

            p.put("type", "FactSet");
            p.put("facts", FactSet);
            p.put("separator", true);
          } else {
            p.put("type", "TextBlock");
            p.put("wrap", true);
            p.put("weight", "Bolder");
            p.put("size", "Large");
            p.put("text", pText);
          }

          body.add(p);
        }

        content.put("type", "AdaptiveCard");
        content.put("version", "1.3");
        content.put("fallbackText", "Fallback text");
        content.put(
          "$schema",
          "http://adaptivecards.io/schemas/adaptive-card.json"
        );
        content.put("body", body);
        attachment.put(
          "contentType",
          "application/vnd.microsoft.card.adaptive"
        );
        attachment.put("content", content);
        attachments.add(attachment);
        message.put("attachments", attachments);

        log.debug(message.toJSONString());
        sendMessage(workFlowUrl, message);
      });
  }
}
