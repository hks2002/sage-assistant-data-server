/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 18:04:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:19:05                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.da.sageassistantserver.service.CommonService;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CommonController {

  @Autowired
  CommonService commonService;

  @GetMapping("/Data/Sites")
  public List<String> getAllSites() {
    return (commonService.getAllSites());
  }

  @GetMapping("/Data/ClientIP")
  public String getRequestIP(HttpServletRequest request) {
    for (String header : Utils.IP_HEADERS) {
      String value = request.getHeader(header);
      if (value == null || value.isEmpty()) {
        continue;
      }
      String[] parts = value.split("\\s*,\\s*");
      return parts[0];
    }
    return request.getRemoteAddr();
  }
}
