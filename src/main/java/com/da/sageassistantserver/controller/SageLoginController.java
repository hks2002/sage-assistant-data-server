/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-25 15:19:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:31:04                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.service.LogService;
import com.da.sageassistantserver.service.SageLoginService;
import com.da.sageassistantserver.utils.ResponseJsonHelper;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SageLoginController {

  @Autowired
  LogService logService;

  @PostMapping("/Data/Login")
  public JSONObject doLogin(
    @RequestHeader(value = "authorization", required = false) String Auth,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(true);
    String finalAuth = Utils.getAuth(Auth, session);

    if (finalAuth == null) {
      return ResponseJsonHelper.missingAuth();
    }
    JSONObject loginRst = SageLoginService.doLogin(finalAuth);
    return loginRst;
  }

  @PostMapping("/Data/Logout")
  public JSONObject doLogout(
    HttpServletRequest request,
    @RequestHeader(value = "authorization", required = false) String Auth
  ) {
    HttpSession session = request.getSession(false);
    String finalAuth = Utils.getAuth(Auth, session);

    if (finalAuth == null) {
      return ResponseJsonHelper.missingAuth();
    }

    JSONObject rst = SageLoginService.doLogout(finalAuth);
    if (rst.getBoolean("success")) {
      session.invalidate();

      String loginName = (String) session.getAttribute("loginName");
      String loginUser = (String) session.getAttribute("loginUser");
      logService.addLog("LOGOUT_SUCCESS", loginName, loginUser);
      session.invalidate();
    }
    return rst;
  }
}
