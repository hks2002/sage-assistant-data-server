/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-16 23:24:10                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:49:57                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.webdav;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

  public RequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
  }

  @Override
  public String getHeader(String name) {
    String webdavCache = (String) super
      .getSession()
      .getAttribute("webdavCache");
    if (webdavCache.toLowerCase().equals("true")) {
      return super.getHeader(name);
    } else {
      String nameLowerCase = name.toLowerCase();
      if (
        // Browsers use [If-None-Match]+[ETag] and [if-modified-since]+[last-modified] value back to Server,
        // If-None-Match: W/1000000-1000000000000
        // if-modified-since: Mon, 01 Jan 1990 00:00:00 GMT
        // then server will return 304 or 200
        nameLowerCase.equals("if-none-match") ||
        nameLowerCase.equals("etag") ||
        nameLowerCase.equals("if-modified-since") ||
        nameLowerCase.equals("last-modified")
      ) {
        return null;
      } else {
        return super.getHeader(name);
      }
    }
  }
}
