/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2023-03-11 16:26:53                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:52:31                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.utils.Utils;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class DocsServiceTest {

  // Unit Test for Service，sample code
  @TestConfiguration
  static class prepare {

    @Bean
    public DocsService getService() {
      return new DocsService();
    }
  }

  @Autowired
  DocsService docsService;

  @Test
  public void updateFileInfo() {
    docsService.updateDocInfo(new File("Y:\\Drawing"));
  }

  @Test
  public void moveFiles() {
    Utils.moveFiles(Paths.get("Y:\\Drawing"), Paths.get("Y:\\Drawing"), 2, 3);
  }

  @Test
  public void cleanFileInfo() {
    docsService.cleanDocInfos(new File("Y:\\Drawing"));
  }
}
