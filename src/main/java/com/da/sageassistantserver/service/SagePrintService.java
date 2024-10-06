/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-23 20:45:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:45:29                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.SageActionHelper;
import com.da.sageassistantserver.utils.SageActionHelper.Action;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class SagePrintService {

  public static JSONObject getPrintUUID(
    String auth,
    String rcdType,
    String rcdNO,
    String opt
  ) {
    JSONObject rtn = null;
    // Step 1: goto recorder
    rtn = SageActionService.goToRecorder(auth, rcdType, rcdNO);
    if (!rtn.getBoolean("success")) {
      return rtn;
    }
    String sessionId = rtn.getString("SessionId");
    String xid = rtn.getString("xid");
    String defaultRcdNO = rtn.getString("defaultRcdNO");

    // Step 2: enter print page
    rtn =
      SageActionService.doSageAct(
        auth,
        sessionId,
        SageActionHelper.act(Action.ENTER_PRINT_PAGE),
        null
      );
    if (!rtn.getBoolean("success")) {
      return rtn;
    }

    // Step 3: if pop
    if (rcdType.equals("PurchaseOrder")) {
      rtn =
        SageActionService.doSageAct(
          auth,
          sessionId,
          SageActionHelper.selectAfterPop("C", "bA", 0, "7~1:BONCDE2!1"),
          null
        );
    }
    if (rcdType.equals("Delivery")) {
      rtn =
        SageActionService.doSageAct(
          auth,
          sessionId,
          SageActionHelper.selectAfterPop("C", "bA", 0, "7~1:BONLIV!1"),
          null
        );
    }

    // Step 4: print
    rtn =
      SageActionService.doSageAct(
        auth,
        sessionId,
        SageActionHelper.act(Action.PRINT),
        null
      );
    if (!rtn.getBoolean("success")) {
      return rtn;
    }

    // Step 5: get uuid
    JSONObject obj = JSONObject.parseObject(rtn.getString("result"));
    String uuid = obj
      .getJSONObject("sap")
      .getJSONObject("jobs")
      .getJSONObject("report")
      .getJSONArray("submitted")
      .getJSONObject(0)
      .getString("uuid");

    rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "success");
    rtn.put("printUUID", uuid);

    // Finally, we goto the default record, here could not await, make response time
    // less to user
    CompletableFuture.runAsync(() -> {
      SageActionService.doSageAct(
        auth,
        sessionId,
        SageActionHelper.setThenTab("B", xid, 0, defaultRcdNO),
        null
      );
    });
    return rtn;
  }

  public static JSONObject getReportUUID(String auth, String printUUID) {
    int statusCode = 202;
    do {
      // Sleep 0.5 seconds
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      statusCode =
        HttpService
          .request(
            "https://192.168.10.62/print/srvedi01.dedaero.lan:1890/$getState('" +
            printUUID +
            "')",
            "GET",
            null,
            auth
          )
          .statusCode();
    } while (statusCode == 202); // 202 creating, 201 created

    // location style:
    // /print/$report('36144e17-6272-442b-aa4c-4c2e667fbe67')
    String reportUUID = HttpService
      .request(
        "https://192.168.10.62/print/srvedi01.dedaero.lan:1890/$getReport('" +
        printUUID +
        "')",
        "GET",
        null,
        auth
      )
      .headers()
      .firstValue("location")
      .orElse("");

    Pattern p = Pattern.compile(
      "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}"
    );
    Matcher m = p.matcher(reportUUID);
    if (m.find()) {
      JSONObject rtn = ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.RESULT,
        "success"
      );
      rtn.put("reportUUID", m.group(0));
      return rtn;
    } else {
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "Get report uuid Error"
      );
    }
  }
}
