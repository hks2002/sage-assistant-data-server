/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-25 15:19:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:32:07                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.model.LogRaw;
import com.da.sageassistantserver.service.LogService;
import com.da.sageassistantserver.service.SageLoginService;
import com.da.sageassistantserver.service.UserFuncService;
import com.da.sageassistantserver.service.UserService;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  UserFuncService userFuncService;

  @Autowired
  LogService logService;

  private JSONObject getProfileFromSage(String auth, HttpSession session) {
    JSONObject rst1 = SageLoginService.getProfile(auth);

    if (rst1.getBoolean("success")) {
      JSONObject profile = rst1.getJSONObject("profile");

      // Save user id to session
      session.setAttribute(
        "sid",
        rst1.getJSONObject("profile").getString("userId")
      );
      session.setAttribute(
        "loginUser",
        rst1.getJSONObject("profile").getString("userName")
      );
      userService.updateUserBySid(
        profile.getString("userId"),
        profile.getString("loginName"),
        profile.getString("firstName"),
        profile.getString("lastName"),
        profile.getString("email"),
        profile.getString("language")
      );
    }
    return rst1;
  }

  @PostMapping("/Data/Profile")
  public JSONObject getProfile(
    HttpServletRequest request,
    @RequestHeader(value = "authorization", required = false) String Auth
  ) {
    HttpSession session = request.getSession(false);
    String finalAuth = Utils.getAuth(Auth, session);

    if (finalAuth == null) {
      return ResponseJsonHelper.missingAuth();
    }

    JSONObject rst = userService.getProfileByAuth(finalAuth);
    if (rst.getBoolean("success")) {
      // Save user id to session
      session.setAttribute(
        "sid",
        rst.getJSONObject("profile").getString("userId")
      );
      session.setAttribute(
        "loginUser",
        rst.getJSONObject("profile").getString("userName")
      );

      getProfileFromSage(finalAuth, session);
      return rst;
    } else {
      return getProfileFromSage(finalAuth, session);
    }
  }

  private JSONObject getFunctionFromSage(String auth, HttpSession session) {
    JSONObject rst1 = SageLoginService.getFunction(auth);
    if (rst1.getBoolean("success")) {
      JSONArray arr = rst1.getJSONArray("functions");
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < arr.size(); i++) {
        String s = arr.getString(i);
        sb.append(s).append(";");
      }
      userFuncService.updateSageActionsByAuth(auth, sb.toString());
    }
    return rst1;
  }

  @PostMapping("/Data/Function")
  public JSONObject getFunction(
    HttpServletRequest request,
    @RequestHeader(value = "authorization", required = false) String Auth
  ) {
    HttpSession session = request.getSession(false);
    String finalAuth = Utils.getAuth(Auth, session);

    if (finalAuth == null) {
      return ResponseJsonHelper.missingAuth();
    }

    JSONObject rst = userFuncService.getSageActionsByAuth(finalAuth);
    if (rst.getBooleanValue("success")) {
      getFunctionFromSage(finalAuth, session);
      return rst;
    } else {
      return getFunctionFromSage(finalAuth, session);
    }
  }

  @GetMapping("/Data/UserLogs")
  public List<LogRaw> getUserLog(
    HttpServletRequest request,
    @RequestHeader(value = "authorization", required = false) String Auth,
    @RequestParam(
      value = "TCode",
      required = false,
      defaultValue = ""
    ) String TCode,
    @RequestParam(
      value = "Offset",
      required = false,
      defaultValue = "0"
    ) Integer Offset,
    @RequestParam(
      value = "Limit",
      required = false,
      defaultValue = "50"
    ) Integer Limit
  ) {
    HttpSession session = request.getSession(false);
    String finalAuth = Utils.getAuth(Auth, session);

    if (finalAuth == null) {
      return null;
    }
    String loginName = (String) session.getAttribute("loginName");
    return logService.getUserLogs(TCode, loginName, Offset, Limit);
  }
}
