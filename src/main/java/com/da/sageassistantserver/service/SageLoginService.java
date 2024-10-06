/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-11-23 20:45:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:47:04                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.ResponseJsonHelper.MsgTyp;
import com.da.sageassistantserver.utils.SageActionHelper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SageLoginService {

  @Autowired
  LogService logService;

  /**
   * Caffeine cache
   * Key is Object {auth: "xxx", function: "xxxx", trans: "xxxxx"}
   * Value is Object {success: true, SessionId: id, xid: "AA5", rcdNO: RecNO,
   * msgTyp: "info", msg: "Login"}
   *
   */
  private static LoadingCache<JSONObject, JSONObject> sageSessionCache = Caffeine
    .newBuilder()
    .maximumSize(10000 * 3)
    .expireAfterAccess(5, TimeUnit.MINUTES)
    .removalListener(
      (RemovalListener<JSONObject, JSONObject>) (key, value, cause) -> {
        /*
         * Sage will expired created session automatically, we exit page to avoid this
         */
        String auth = key.getString("auth");
        String sessionId = value.getString("SessionId");

        log.debug(
          "Sage SessionId {} is removed, cause is {}",
          sessionId,
          cause
        );

        endSession(auth, sessionId);
      }
    )
    .build(
      new CacheLoader<JSONObject, JSONObject>() {
        @Override
        public JSONObject load(JSONObject key) {
          log.debug("load key: {}", key);

          String auth = key.getString("auth");
          String function = key.getString("function");
          String trans = key.getString("trans");

          return getSageSession(auth, function, trans);
        }
      }
    );

  /**
   * Cache auth, do login will be called with high frequency, so cache the auth
   */
  public static Cache<String, Boolean> authCache = Caffeine
    .newBuilder()
    .initialCapacity(100)
    .maximumSize(1000)
    .expireAfterAccess(5, TimeUnit.MINUTES)
    .build();

  /**
   * Internal method to get Sage Session, using getSafeSessionCache to get session
   * from cache
   */
  private static JSONObject getSageSession(
    String auth,
    String function,
    String trans
  ) {
    HttpResponse<String> response = HttpService.request(
      String.format(
        "https://192.168.10.62/trans/x3/erp/EXPLOIT/$sessions?f=%s/2//M&trackngId=%s",
        function,
        UUID.randomUUID().toString()
      ),
      "POST",
      "{\"settings\":{}}",
      auth
    );
    String html = response.body();
    JSONObject json = JSONObject.parseObject(html);

    // Sage Server performance low, header contains `location`,
    // Using GET to get the response by `location`, continue
    if (SageActionService.isPerformanceLow(json)) {
      String location = response.headers().firstValue("location").orElse("");
      json = SageActionService.getTrackingResponse(auth, location);
    }

    JSONObject rtn = new JSONObject();
    if (
      json.containsKey("srvop") &&
      json.getJSONObject("srvop").containsKey("sessionInfo")
    ) {
      String sessionId = json
        .getJSONObject("srvop")
        .getJSONObject("sessionInfo")
        .getJSONObject("node")
        .getString("sid");

      rtn = ResponseJsonHelper.rtnObj(true, MsgTyp.RESULT, "Session Success");
      rtn.put("SessionId", sessionId);

      // no trans
      if (trans == null || trans.isBlank()) {
        // get RecorderNO position
        String xid = json
          .getJSONObject("sap")
          .getJSONObject("target")
          .getJSONObject("ist")
          .getString("xid");
        // get RecorderNO value
        // ❗❗❗❗❗ Some times Sage Server will return bad content, it doesn't contains
        // recorder
        try {
          String rcdNO = json
            .getJSONObject("sap")
            .getJSONObject("wins")
            .getJSONObject("B")
            .getJSONObject("entities")
            .getJSONObject(xid)
            .getString("v");

          rtn.put("xid", xid);
          rtn.put("rcdNO", rcdNO);
          return rtn;
        } catch (Exception e) {
          return ResponseJsonHelper.rtnObj(
            false,
            MsgTyp.ERROR,
            "Session and trans Failed"
          );
        }
      }
      // with trans
      else {
        String select = SageActionHelper.selectAfterPop("B", "bA", 0, trans);
        JSONObject rtn2 = SageActionService.doSageAct(
          auth,
          sessionId,
          select,
          null
        );

        if (rtn2.getBoolean("success")) {
          rtn =
            ResponseJsonHelper.rtnObj(
              true,
              MsgTyp.RESULT,
              "Session and trans Success"
            );
          rtn.put("SessionId", sessionId);

          JSONObject json2 = JSONObject.parseObject(rtn2.getString("result"));
          // get RecorderNO position
          String xid = json2
            .getJSONObject("sap")
            .getJSONObject("target")
            .getJSONObject("ist")
            .getString("xid");

          // get RecorderNO value
          // ❗❗❗❗❗ Some times Sage Server will return bad content, it doesn't contains
          // recorder
          try {
            String defaultRcdNO = json2
              .getJSONObject("sap")
              .getJSONObject("wins")
              .getJSONObject("B")
              .getJSONObject("entities")
              .getJSONObject(xid)
              .getString("v");

            rtn.put("xid", xid);
            rtn.put("defaultRcdNO", defaultRcdNO);
            return rtn;
          } catch (Exception e) {
            return ResponseJsonHelper.rtnObj(
              false,
              MsgTyp.ERROR,
              "Session and trans Failed"
            );
          }
        } else {
          return ResponseJsonHelper.rtnObj(
            false,
            MsgTyp.ERROR,
            "Session and trans Failed"
          );
        }
      }
    }
    // without session info
    else {
      log.error("Unknown response: {} ", json.toJSONString());
      rtn = ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Unknown response");
      return rtn;
    }
  }

  public static JSONObject getSageSessionCache(
    String auth,
    String function,
    String trans
  ) {
    JSONObject key = new JSONObject();
    key.put("auth", auth);
    key.put("function", function);
    key.put("trans", trans);
    return sageSessionCache.get(key);
  }

  public static JSONObject endSession(String auth, String sessionId) {
    String html = HttpService
      .request(
        String.format(
          "https://192.168.10.62/trans/x3/erp/EXPLOIT/$sessions('%s')",
          sessionId
        ),
        "DELETE",
        null,
        auth
      )
      .body();

    JSONObject json = JSONObject
      .parseObject(html)
      .getJSONArray("$diagnoses")
      .getJSONObject(0);

    String severity = json.getString("$severity");
    switch (severity) {
      case "info":
        return ResponseJsonHelper.rtnObj(
          true,
          MsgTyp.INFO,
          json.getString("$message")
        );
      default:
        return ResponseJsonHelper.rtnObj(
          false,
          MsgTyp.ERROR,
          "End session failed"
        );
    }
  }

  /**
   * Do login, using authCache to get return login result,
   * Do really login will be called with high frequency, so cache the auth,
   * 30 minutes expire
   *
   * @param auth
   * @return
   */
  public static JSONObject doLogin(String auth) {
    // do login will be called with high frequency, so cache the auth
    if (authCache.getIfPresent(auth) != null) {
      return ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.RESULT,
        "Cache login success"
      );
    }

    try {
      String html = HttpService
        .request("https://192.168.10.62/auth/login/submit", "POST", "{}", auth)
        .body();
      JSONObject json = JSONObject
        .parseObject(html)
        .getJSONArray("$diagnoses")
        .getJSONObject(0);

      String severity = json.getString("$severity");
      switch (severity) {
        case "info":
          authCache.put(auth, true); // add or update the auth cache
          return ResponseJsonHelper.rtnObj(
            true,
            MsgTyp.RESULT,
            "Login success"
          );
        case "error":
          return ResponseJsonHelper.rtnObj(
            false,
            MsgTyp.ERROR,
            json.getString("$message")
          );
        default:
          return ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Login failed");
      }
    } catch (Exception e) {
      log.error("Login Exception: {}", e.getMessage());
      return ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Login failed");
    }
  }

  public static JSONObject doLogout(String auth) {
    authCache.invalidate(auth); // remove the auth cache

    try {
      // end all session first
      for (var entry : sageSessionCache.asMap().entrySet()) {
        JSONObject key = entry.getKey();
        JSONObject val = entry.getValue();
        String auth2 = key.getString("auth");
        String sessionId = val.getString("SessionId");

        if (auth2.equals(auth)) {
          endSession(auth, sessionId);
        }
      }

      String html = HttpService
        .request("https://192.168.10.62/logout", "POST", null, auth)
        .body();
      JSONObject json = JSONObject
        .parseObject(html)
        .getJSONArray("$diagnoses")
        .getJSONObject(0);

      String severity = json.getString("$severity");
      switch (severity) {
        case "success":
          return ResponseJsonHelper.rtnObj(
            true,
            MsgTyp.RESULT,
            "Logout success"
          );
        case "error":
          return ResponseJsonHelper.rtnObj(
            false,
            MsgTyp.ERROR,
            json.getString("$message")
          );
        default:
          return ResponseJsonHelper.rtnObj(
            false,
            MsgTyp.ERROR,
            "Logout failed"
          );
      }
    } catch (Exception e) {
      log.error("Logout Exception: {}", e.getMessage());
      return ResponseJsonHelper.rtnObj(false, MsgTyp.ERROR, "Logout failed");
    }
  }

  public static JSONObject getProfile(String auth) {
    try {
      String html = HttpService
        .request(
          "https://192.168.10.62/sdata/syracuse/collaboration/syracuse/userProfiles/$template/$workingCopies",
          "POST",
          "{\"representation\": \"userProfile.$edit\"}",
          auth
        )
        .body();

      JSONObject user = JSONObject.parseObject(html).getJSONObject("user");
      JSONObject selectedLocale = JSONObject
        .parseObject(html)
        .getJSONObject("selectedLocale");

      JSONObject profile = new JSONObject();
      profile.put("userId", user.getString("$key"));
      profile.put("loginName", user.getString("$value"));
      profile.put("firstName", user.getString("firstName"));
      profile.put("lastName", user.getString("lastName"));
      profile.put(
        "userName",
        user.getString("firstName") + " " + user.getString("lastName")
      );
      profile.put("email", user.getString("email"));
      profile.put("language", selectedLocale.getString("code"));

      JSONObject rtn = ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.RESULT,
        "success"
      );
      rtn.put("profile", profile);
      return rtn;
    } catch (Exception e) {
      log.error("getProfile Exception: {}", e.getMessage());
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "GetProfile failed"
      );
    }
  }

  public static JSONObject getFunction(String auth) {
    try {
      String html = HttpService
        .request(
          "https://192.168.10.62/sdata/syracuse/collaboration/syracuse/pages('x3.erp.EXPLOIT.home.$navigation,$page,')",
          "GET",
          null,
          auth
        )
        .body();
      // regex style: "convergenceFunction":"GESSOH"
      Pattern pattern = Pattern.compile(
        "(?:\"convergenceFunction\":\")(.*?)(?:\")"
      );
      Matcher m = pattern.matcher(html);
      List<String> functions = new ArrayList<String>();
      while (m.find()) {
        String group = m.group();
        functions.add(group.substring(23, group.length() - 1));
      }

      JSONObject rtn = ResponseJsonHelper.rtnObj(
        true,
        MsgTyp.RESULT,
        "success"
      );
      rtn.put("functions", functions);
      return rtn;
    } catch (Exception e) {
      log.error("getFunction Exception: {}", e.getMessage());
      return ResponseJsonHelper.rtnObj(
        false,
        MsgTyp.ERROR,
        "GetFunctions failed"
      );
    }
  }
}
