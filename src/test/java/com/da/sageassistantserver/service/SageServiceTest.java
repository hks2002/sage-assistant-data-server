/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-16 17:14:44                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:53:12                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SageServiceTest {

  private String auth;

  @Test
  void testDoLogin() {
    SageLoginService.doLogin(auth);
  }

  @Test
  void testDoLogout() {
    SageLoginService.doLogout(auth);
  }

  @Test
  void testEndSession() {
    SageLoginService.endSession(auth, "c961d328-f1bb-449d-b305-5446d9423fe0");
  }

  @Test
  void testDoLoginDoLogout() {
    SageLoginService.doLogin(auth);
    SageLoginService.getSageSessionCache(auth, "GESSOH", "2~1");
    SageLoginService.doLogout(auth);
  }

  @Test
  void testGetProfile() {
    SageLoginService.getProfile(auth);
  }

  @Test
  void testGetFunction() {
    SageLoginService.getFunction(auth);
  }

  @Test
  void testGetSageSession() throws ExecutionException {
    SageLoginService.getSageSessionCache(auth, "GESSOH", "2~1");
  }

  @Test
  void testUpdateField() {
    // SAD: EA55
    SageActionService.updateSageField(
      auth,
      "SalesOrder",
      "ZCC220007",
      8,
      "EA55",
      "1"
    );
    SageLoginService.doLogout(auth);
  }

  @Test
  void testPrintUUID() throws ExecutionException {
    auth = "Basic cmh1YW5nOkRhekAyMDIyMDMwOA==";
    JSONObject rtn = SagePrintService.getPrintUUID(
      auth,
      "PurchaseOrder",
      "ZCF2400543",
      null
    );
    log.debug(rtn.toString());
  }
}
