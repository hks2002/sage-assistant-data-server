/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-07-04 09:39:40                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:50:10                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver;

import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.model.User;
import com.da.sageassistantserver.model.UserFunc;
import com.da.sageassistantserver.service.LogService;
import com.da.sageassistantserver.service.SageLoginService;
import com.da.sageassistantserver.service.SupplierService;
import com.da.sageassistantserver.service.UserFuncService;
import com.da.sageassistantserver.service.UserService;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

  @Autowired
  UserService userService;

  @Autowired
  UserFuncService userFuncService;

  @Autowired
  SupplierService supplierService;

  @Autowired
  LogService logService;

  final List<String> modifiableMethods = Arrays.asList(
    "PUT",
    "POST",
    "PROPPATCH",
    "MKCOL",
    "MOVE"
  );

  @Override
  protected boolean shouldNotFilter(
    @SuppressWarnings("null") HttpServletRequest request
  ) throws ServletException {
    return (
        request.getRequestURI().startsWith("/Data/Login") ||
        request.getRequestURI().startsWith("/Data/Logout") ||
        request.getRequestURI().startsWith("/Data/Analyses") ||
        request.getRequestURI().startsWith("/Data/ReportFile")
      )
      ? true
      : false;
  }

  @Override
  protected void doFilterInternal(
    @SuppressWarnings("null") HttpServletRequest req,
    @SuppressWarnings("null") HttpServletResponse res,
    @SuppressWarnings("null") FilterChain filterChain
  ) throws ServletException, IOException {
    Boolean doTrace = false;
    if (doTrace) {
      String path = getRelativePath(req);
      log.info("{} {}", req.getMethod(), path);

      Enumeration<String> headers = req.getHeaderNames();
      while (headers.hasMoreElements()) {
        String header = headers.nextElement();
        log.info("Header {}: {}", header, req.getHeader(header));
      }
      Enumeration<String> attributes = req.getAttributeNames();
      while (attributes.hasMoreElements()) {
        String attribute = attributes.nextElement();
        log.info("Attribute {}: {}", attribute, req.getAttribute(attribute));
      }
      Enumeration<String> parameters = req.getParameterNames();
      while (parameters.hasMoreElements()) {
        String parameter = parameters.nextElement();
        log.info("Parameter {}: {}", parameter, req.getParameter(parameter));
      }
    }

    if (auth(req, res)) {
      setBusinessPartner(req, res);
      filterChain.doFilter(req, res);
    }
  }

  /**
   * Check if the request is authorized.
   *
   * @note ❗❗❗ Window doesn't allow connect to no ssl webdav by default.. ❗❗❗
   * @return
   */
  public boolean auth(HttpServletRequest req, HttpServletResponse res) {
    String authorization = req.getHeader("Authorization");
    if (authorization == null || authorization.length() == 0) {
      return unauthorized(req, res);
    }

    // auth by Sage User
    HttpSession session = req.getSession();
    JSONObject loginResult = SageLoginService.doLogin(authorization);
    User user = userService.getUserByAuth(authorization);

    // login failed
    if (!loginResult.getBoolean("success")) {
      if (user == null) { // no user
        String[] loginInfo = Utils.decodeBasicAuth(authorization).split(":");
        if (loginInfo.length > 1) {
          session.setAttribute("loginName", loginInfo[0]);
        } else {
          session.setAttribute("loginName", "NO_USER");
        }
        session.setAttribute("userName", "");
      } else { // have user
        session.setAttribute("loginName", user.getLogin_name());
        session.setAttribute(
          "userName",
          user.getFirst_name() + " " + user.getLast_name()
        );
      }

      return unauthorized(req, res);
    }
    // login success
    else {
      if (user == null) { // first login, save the user info
        JSONObject profileResult = SageLoginService.getProfile(authorization);
        if (profileResult.getBoolean("success")) {
          JSONObject profile = profileResult.getJSONObject("profile");
          session.setAttribute("loginName", profile.getString("loginName"));
          session.setAttribute("userName", profile.getString("userName"));

          userService.createUser(
            profile.getString("userId"),
            profile.getString("loginName"),
            profile.getString("firstName"),
            profile.getString("lastName"),
            profile.getString("email"),
            profile.getString("language")
          );
        }
      } else {
        session.setAttribute("loginName", user.getLogin_name());
        session.setAttribute(
          "userName",
          user.getFirst_name() + " " + user.getLast_name()
        );
      }

      // log success
      if (loginResult.getString("msg").equals("Cache login success")) {
        // ignore cache login log, message is "Cache login success"
      }
      if (loginResult.getString("msg").equals("Login success")) {
        authorized(req, res);
      }

      if (req.getRequestURI().startsWith("/docs")) {
        return authDocAccessByPath(req, res);
      }
      return true;
    }
  }

  private void setBusinessPartner(
    HttpServletRequest req,
    HttpServletResponse res
  ) {
    String bpCode = req.getParameter("bpCode");
    if (bpCode != null) {
      req.getSession().setAttribute("bpCode", bpCode);
      req
        .getSession()
        .setAttribute(
          "bpName",
          supplierService.getBusinessPartnerByCode(bpCode)
        );
    }
  }

  private boolean authDocAccessByPath(
    HttpServletRequest req,
    HttpServletResponse res
  ) {
    String loginName = (String) req.getSession().getAttribute("loginName");

    List<UserFunc> userFuncs = userFuncService.getWebDavAccess(loginName);
    if (userFuncs.size() == 0) {
      boolean result = userFuncService.initWebDavAccessByLoginName(loginName);

      if (result) {
        logService.addLog("DOC_ACCESS_INIT_SUCCESS", loginName);
        userFuncs = userFuncService.getWebDavAccess(loginName);
      } else {
        logService.addLog("DOC_ACCESS_INIT_FAILED", loginName);
        return forbidden(req, res);
      }
    }

    // try again
    if (userFuncs.size() == 0) {
      return forbidden(req, res);
    } else {
      boolean allowWrite = false;
      boolean allowRead = false;
      for (UserFunc userFunc : userFuncs) {
        if (
          userFunc.getFunc_system().equals("WEBDAV") &&
          userFunc.getFunc_code().equals("WRITE") &&
          userFunc.isEnable()
        ) {
          allowWrite = true;
        } else if (
          userFunc.getFunc_system().equals("WEBDAV") &&
          userFunc.getFunc_code().equals("READ") &&
          userFunc.isEnable()
        ) {
          allowRead = true;
        }
      }

      if (allowWrite) {
        return allow(req, res);
      }
      if (allowRead) {
        String method = req.getMethod();
        return modifiableMethods.contains(method)
          ? forbidden(req, res)
          : allow(req, res);
      }
      // final return forbidden
      return forbidden(req, res);
    }
  }

  private boolean allow(HttpServletRequest req, HttpServletResponse res) {
    logDocAccessResult(req, true);
    return true;
  }

  private boolean forbidden(HttpServletRequest req, HttpServletResponse res) {
    logDocAccessResult(req, false);

    res.setStatus(403);
    return false;
  }

  private boolean authorized(HttpServletRequest req, HttpServletResponse res) {
    logAuthResult(req, true);
    return true;
  }

  private boolean unauthorized(
    HttpServletRequest req,
    HttpServletResponse res
  ) {
    logAuthResult(req, false);

    res.setStatus(401);
    res.setHeader("WWW-Authenticate", "Basic realm=\"No Auth\"");
    return false;
  }

  private void logDocAccessResult(HttpServletRequest req, boolean success) {
    HttpSession session = req.getSession();
    String user = (String) session.getAttribute("loginName");
    String userName = (String) session.getAttribute("userName");
    String path = getRelativePath(req);

    String logMsg = success ? "DOC_ACCESS_SUCCESS" : "DOC_ACCESS_FAILED";
    logService.addLog(logMsg, user, userName, path, getTrueRemoteIp(req));
  }

  private void logAuthResult(HttpServletRequest req, boolean success) {
    HttpSession session = req.getSession();
    String user = (String) session.getAttribute("loginName");
    String userName = (String) session.getAttribute("userName");

    String logMsg = success ? "LOGIN_SUCCESS" : "LOGIN_FAILED";
    logService.addLog(logMsg, user, userName, null, getTrueRemoteIp(req));
  }

  private String getTrueRemoteIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.length() == 0) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0) {
      ip = request.getHeader("X-Real-IP");
    }
    if (ip == null || ip.length() == 0) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * This function is copied from WebDavServlet.getRelativePath()
   *
   * @param request
   * @return
   */
  protected String getRelativePath(HttpServletRequest request) {
    String pathInfo;

    if (request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI) != null) {
      // For includes, get the info from the attributes
      pathInfo =
        (String) request.getAttribute(RequestDispatcher.INCLUDE_PATH_INFO);
    } else {
      pathInfo = request.getPathInfo();
    }

    return pathInfo == null ? "/" : pathInfo;
  }
}
