/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-25 15:19:00                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:31:33                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SrvInfoController {

  @Value("${project.name}")
  private String name;

  @Value("${project.version}")
  private String version;

  @Value("${spring.boot.version}")
  private String springBootVersion;

  @Value("${project.dependencies}")
  private String dependencies;

  @GetMapping("/Data/SrvVersion")
  public String getVersion() {
    return version;
  }

  @GetMapping("/Data/SrvName")
  public String getName() {
    return name;
  }

  @GetMapping("/Data/SrvInfo")
  public JSONObject getInfo() {
    JSONObject obj = new JSONObject();
    obj.put("name", name);
    obj.put("version", version);
    obj.put("springBootVersion", springBootVersion);
    return obj;
  }

  @GetMapping("/Data/SrvProjectDependencies")
  public JSONArray getDependencies() {
    String str = "";
    str = dependencies.replace("Dependency", "");
    str = str.replaceAll("=", ":");
    str = str.replaceAll(":([^,}]+)", ":\"$1\"");
    JSONArray json = JSONArray.parseArray(str);
    return json;
  }
}
