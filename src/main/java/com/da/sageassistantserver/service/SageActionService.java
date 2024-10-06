/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-23 20:45:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-09 19:50:36                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.SageActionHelper;
import com.microsoft.sqlserver.jdbc.StringUtils;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SageActionService {

  /**
   * contains `phase`, `Tracking`, and status is 202
   */
  public static boolean isPerformanceLow(JSONObject json) {
    // only adjust body is enough
    if (
      json.containsKey("phase") && json.getString("phase").equals("Tracking")
    ) {
      log.warn("Sage Server performance low");
      return true;
    }
    return false;
  }

  /**
   * In case of server performance low, sleep 1s, then get response by GET
   *
   * @param auth
   * @param location without schemas and hosts
   * @return
   */
  public static JSONObject getTrackingResponse(String auth, String location) {
    log.debug(location);

    // Sleep 2 seconds
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String html = HttpService
      .request("https://192.168.10.62" + location, "GET", null, auth)
      .body();

    JSONObject json = JSONObject.parseObject(html);
    return isPerformanceLow(json) ? getTrackingResponse(auth, location) : json;
  }

  public static JSONObject doSageAct(
    String auth,
    String session,
    String data,
    String preTacking
  ) {
    String uuid = preTacking != null
      ? preTacking
      : UUID.randomUUID().toString();
    HttpResponse<String> response = HttpService.request(
      String.format(
        "https://192.168.10.62/trans/x3/erp/EXPLOIT/$sessions('%s')/requestSvc?act=%s&trackngId=%s",
        session,
        JSONObject.parseObject(data).getString("act"),
        uuid
      ),
      "PUT",
      data,
      auth
    );
    String html = response.body();
    JSONObject json = JSONObject.parseObject(html);

    // Sage Server performance low, header contains `location`,
    // Using GET to get the response by `location`, continue
    if (isPerformanceLow(json)) {
      String location = response.headers().firstValue("location").orElse("");
      json = getTrackingResponse(auth, location);
    }

    JSONObject sap = Optional
      .ofNullable(json.getJSONObject("sap"))
      .orElse(new JSONObject());
    JSONObject target = sap.getJSONObject("target");

    JSONObject rtn = new JSONObject();

    if (target != null) {
      String targetType = target.getString("type");

      switch (targetType) {
        case "ist":
          // ist means success, without pop window
          rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
          rtn.put(
            "result",
            Optional.ofNullable(json).orElse(new JSONObject()).toJSONString()
          );

          break;
        case "box":
          JSONObject box = target.getJSONObject("box");
          Integer boxType = box.getInteger("type");

          switch (boxType) {
            case 1:
              // type 1 means info
              rtn =
                ResponseJsonHelper.rtnObj(
                  true,
                  MsgTyp.INFO,
                  box.getString("li")
                );
              break;
            case 2:
              // 2 means success, with pop window, need to select
              rtn =
                ResponseJsonHelper.rtnObj(
                  true,
                  MsgTyp.QUESTION,
                  Optional
                    .ofNullable(json)
                    .orElse(new JSONObject())
                    .toJSONString()
                );

              break;
            case 3:
              // 3 means warning
              rtn =
                ResponseJsonHelper.rtnObj(
                  false,
                  MsgTyp.WARN,
                  box.getString("li")
                );
              break;
            default:
              rtn =
                ResponseJsonHelper.rtnObj(
                  false,
                  MsgTyp.ERROR,
                  "Unknown response"
                );
              break;
          }
          break;
        case "portal":
          // portal means login expired or exit page
          if (
            Optional
              .ofNullable(sap)
              .orElse(new JSONObject())
              .containsKey("func") &&
            Optional
              .ofNullable(sap)
              .orElse(new JSONObject())
              .getJSONObject("func")
              .containsKey("close")
          ) {
            rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.INFO, "Page exit");
          } else {
            rtn =
              ResponseJsonHelper.rtnObj(true, MsgTyp.ERROR, "Session expired");
          }
          break;
        default:
          rtn =
            ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Unknown response");
      }
      // } else if (srvop != null && diagnosis != null) {
      // rtn = ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR,
      // diagnosis.getString("$message"));
    } else if (json.containsKey("$message")) {
      log.debug(
        Optional
          .ofNullable(json.getString("$details"))
          .orElse("Unknown response")
      );
      rtn =
        ResponseJsonHelper.rtnObj(
          false,
          MsgTyp.ERROR,
          json.getString("$message")
        );
    } else {
      rtn = ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Unknown response");
    }

    return rtn;
  }

  /**
   * After go to recorder, get session id and xid and default value of recorder
   * <p>
   * get SessionId, xid, defaultRcdNO
   *
   * @param auth
   * @param rcdType
   * @param rcdNO
   * @return
   */
  public static JSONObject goToRecorder(
    String auth,
    String rcdType,
    String rcdNO
  ) {
    String function = SageActionHelper.getFunction(rcdType);

    // trans
    String trans =
      switch (function) {
        case "GESSOH" -> SageActionHelper.getSalesOrderTransaction(rcdNO);
        case "GESSIH" -> SageActionHelper.getInvoiceTransaction(rcdNO);
        case "GESPOH" -> SageActionHelper.getPurchaseTransaction(rcdNO);
        default -> "";
      };

    JSONObject session = SageLoginService.getSageSessionCache(
      auth,
      function,
      trans
    );
    // if is not successful, return it
    if (!session.getBooleanValue("success")) {
      return session;
    }

    String sessionId = session.getString("SessionId");
    String xid = session.getString("xid"); // xid is the default focus field after go into function
    String defaultRcdNO = session.getString("defaultRcdNO");

    log.debug("go to target recorder >>>");
    JSONObject rtn = SageActionService.doSageAct(
      auth,
      sessionId,
      SageActionHelper.setThenTab("B", xid, 0, rcdNO),
      null
    );
    if (!rtn.getBooleanValue("success")) {
      if (rtn.getString("msgTyp").equals("WARN")) {
        // rewrite message, original message is ""
        rtn.put("msg", "Recorder not found");
      }
    }
    log.debug("go to target recorder <<<");

    // add sessionId and xid
    rtn.put("SessionId", sessionId);
    rtn.put("xid", xid);
    rtn.put("defaultRcdNO", defaultRcdNO);
    return rtn;
  }

  public static JSONObject updateSageField(
    String auth,
    String rcdType,
    String rcdNO,
    Integer line,
    String targetField,
    String targetFieldVal
  ) {
    // step one: go to recorder
    JSONObject rtn = goToRecorder(auth, rcdType, rcdNO);
    String sessionId = rtn.getString("SessionId");
    String xid = rtn.getString("xid");
    String defaultRcdNO = rtn.getString("defaultRcdNO");
    if (!rtn.getBooleanValue("success")) {
      return rtn;
    }

    // step two: try to edit
    log.debug("edit model >>>");
    rtn =
      SageActionService.doSageAct(
        auth,
        sessionId,
        SageActionHelper.goTo("B", targetField, line),
        null
      );
    if (!rtn.getBooleanValue("success")) {
      return rtn;
    }
    log.debug("edit model <<<");

    // step three: always no revise
    if (
      rtn.getBooleanValue("success") &&
      rtn.getString("msgTyp").equals("QUESTION")
    ) {
      log.debug("always no revise >>>");
      rtn =
        SageActionService.doSageAct(
          auth,
          sessionId,
          SageActionHelper.noRevise(),
          null
        );
      log.debug("always no revise <<<");
    }

    // step four: modify and save, based on the value type
    // If someone else opened target, edit mode will return false, step four will be
    // skipped.
    if (rtn.getBooleanValue("success")) {
      Integer iVal = -1;

      if (StringUtils.isInteger(targetFieldVal)) {
        iVal = Integer.valueOf(targetFieldVal);
      }

      log.debug("modify and save >>>");
      // ❗❗❗❗❗ Limit integer value, assume that the value is list option val
      if (iVal > 0 && iVal < 10) {
        rtn =
          SageActionService.doSageAct(
            auth,
            sessionId,
            SageActionHelper.save("B", targetField, line, iVal),
            null
          );
      } else {
        rtn =
          SageActionService.doSageAct(
            auth,
            sessionId,
            SageActionHelper.save("B", targetField, line, targetFieldVal),
            null
          );
      }
      log.debug("modify and save <<<");
    }

    // Finally, we goto the default record, here could not await, make response time
    // less to user
    CompletableFuture.runAsync(() -> {
      log.debug("goto the default record >>>");
      SageActionService.doSageAct(
        auth,
        sessionId,
        SageActionHelper.setThenTab("B", xid, 0, defaultRcdNO),
        null
      );
      log.debug("goto the default record <<<");
    });

    // return
    return rtn;
  }
}
