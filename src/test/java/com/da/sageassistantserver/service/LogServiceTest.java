/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2023-03-11 16:26:53                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-07-22 22:41:47                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.model.LogFinal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootTest
class LogServiceTest {

  // Unit Test for Service，sample code
  @TestConfiguration
  static class prepare {

    @Bean
    public LogService getService() {
      return new LogService();
    }
  }

  @Autowired
  LogService logService;

  @Test
  @Order(1)
  public void setLogTemplate() {
    logService.addLogTemplate(
      "TCode0",
      "TGroup",
      "TDefinition",
      "TDefinition_en",
      "TDefinition_zh"
    );
    logService.addLogTemplate(
      "TCode9",
      "TGroup",
      "TDefinition {0} {1} {2} {3} {4} {5} {6} {7} {8} {9}",
      "English Definition {0} {1} {2} {3} {4} {5} {6} {7} {8} {9}",
      "中文定义 {0} {1} {2} {3} {4} {5} {6} {7} {8} {9}"
    );
  }

  @Test
  @Order(2)
  void testLogService() {
    logService.addLog("TCode0");
    logService.addLog(
      "TCode9",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
  }

  @Test
  @Order(3)
  void testLogServiceResult() {
    List<LogFinal> logs = logService.getLogs(
      "TCode0",
      "TGroup",
      "2023-01-01",
      "2099-12-31",
      "en-US"
    );
    for (LogFinal l : logs) {
      log.info(l.toString());
    }

    logs =
      logService.getLogs(
        "TCode9",
        "TGroup",
        "2023-01-01",
        "2099-12-31",
        "zh-CN"
      );
    for (LogFinal l : logs) {
      log.info(l.toString());
    }
  }

  @Test
  @Order(4)
  void testLogService2() {
    logService.addLog("LOGIN_SUCCESS");
    logService.addLog(
      "LOGIN_SUCCESS",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("LOGIN_FAILED");
    logService.addLog(
      "LOGIN_FAILED",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("LOGOUT_SUCCESS");
    logService.addLog(
      "LOGOUT_SUCCESS",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_ACCESS_SUCCESS");
    logService.addLog(
      "DOC_ACCESS_SUCCESS",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_ACCESS_FAILED");
    logService.addLog(
      "DOC_ACCESS_FAILED",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_ACCESS_INIT_SUCCESS");
    logService.addLog(
      "DOC_ACCESS_INIT_SUCCESS",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_ACCESS_INIT_FAILED");
    logService.addLog(
      "DOC_ACCESS_INIT_FAILED",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_AUTO_DOWNLOAD");
    logService.addLog(
      "DOC_AUTO_DOWNLOAD",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_INFO_CREATE");
    logService.addLog(
      "DOC_INFO_CREATE",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_INFO_UPDATE");
    logService.addLog(
      "DOC_INFO_UPDATE",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_UPLOAD_SUCCESS");
    logService.addLog(
      "DOC_UPLOAD_SUCCESS",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
    logService.addLog("DOC_UPLOAD_FAILED");
    logService.addLog(
      "DOC_UPLOAD_FAILED",
      "v0",
      "v1",
      "v2",
      "v3",
      "v4",
      "v5",
      "v6",
      "v7",
      "v8",
      "v9"
    );
  }
}
