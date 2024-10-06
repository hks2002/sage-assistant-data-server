/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2023-03-14 23:38:37                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:52:34                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import org.junit.jupiter.api.Test;

public class HttpServiceTest {

  @Test
  void testRequest() {
    HttpService.request("https://www.baidu.com", "GET");
    HttpService.request("https://srvsyr01/auth/login/page", "GET");
  }
}
