/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2022-03-26 22:53:00                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:30:20                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.model.Attachment;
import com.da.sageassistantserver.model.Docs;
import com.da.sageassistantserver.service.DocsService;
import com.da.sageassistantserver.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@Slf4j
public class DocsController {

  @Autowired
  DocsService docsService;

  @Autowired
  UserService userService;

  /*
   * @param pn could be Pn or PnRoot, PnRoot without version
   */
  @GetMapping("/Data/GetDocsInfoFromTLS")
  public List<Attachment> getAttachmentPathFromTLS(
    @RequestParam(
      value = "Pn",
      required = false,
      defaultValue = "NULL"
    ) String pn
  ) {
    return docsService.getAttachmentPath(pn);
  }

  /*
   * @param pn could be Pn or PnRoot, PnRoot without version
   */
  @GetMapping("/Data/GetDocsInfoFromZHU")
  public JSONArray getAttachmentPathFromZHU(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    String auth = request.getHeader("authorization");
    String Pn = request.getParameter("Pn");
    List<Docs> list = docsService.getDocsByPN(Pn, auth, false);
    JSONArray arr = new JSONArray();
    for (Docs doc : list) {
      JSONObject obj = new JSONObject();
      obj.put("name", doc.getFile_name());
      obj.put("size", doc.getSize());
      obj.put("lastModified", doc.getDoc_modified_at());
      obj.put("url", "/" + doc.getLocation() + "/" + doc.getFile_name());
      arr.add(obj);
    }
    return arr;
  }

  @PostMapping("/Data/FileUpload")
  public JSONObject handleFileUpload(
    HttpServletRequest request,
    @RequestParam("file") final MultipartFile uploadFile
  ) {
    HttpSession session = request.getSession();
    String user = (String) session.getAttribute("loginName");
    String userName = (String) session.getAttribute("userName");

    return docsService.handleFileUpload(uploadFile, user, userName);
  }

  @DeleteMapping("/Data/FileDelete")
  public JSONObject handleFileDelete(
    HttpServletRequest request,
    @RequestParam(value = "Path", required = true) String path
  ) {
    HttpSession session = request.getSession();
    String user = (String) session.getAttribute("loginName");
    String userName = (String) session.getAttribute("userName");
    return docsService.handleFileDelete(path, user, userName);
  }
}
