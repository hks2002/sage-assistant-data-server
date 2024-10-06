/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-16 23:24:10                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:50:01                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.webdav;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponseWrapper extends HttpServletResponseWrapper {

  private ByteArrayOutputStream bos = null;
  private ServletOutputStream sos = null;

  public ResponseWrapper(HttpServletResponse response) throws IOException {
    super(response);
    bos = new ByteArrayOutputStream();
    sos = getOutputStream();
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (sos == null) {
      sos =
        new ServletOutputStream() {
          @Override
          public void write(int b) throws IOException {
            bos.write(b);
          }

          @Override
          public boolean isReady() {
            return false;
          }

          @Override
          public void setWriteListener(WriteListener arg0) {}
        };
    }
    return sos;
  }

  public byte[] getBytes() throws IOException {
    return bos.toByteArray();
  }
}
