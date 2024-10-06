/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 22:53:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:29:55                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.model.Docs;
import com.da.sageassistantserver.service.DmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class DmsController {

  /*
   * @param pn could be Pn or PnRoot, PnRoot without version
   */
  @GetMapping("/Data/GetDocsInfoFromDms")
  public List<Docs> getAttachmentPath(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    String auth = request.getHeader("authorization");
    String Pn = request.getParameter("Pn");
    if (auth == null || auth.isEmpty()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      return new ArrayList<Docs>();
    }
    if (Pn == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return new ArrayList<Docs>();
    }
    return DmsService.getDocuments(auth, Pn.toUpperCase());
  }
}
