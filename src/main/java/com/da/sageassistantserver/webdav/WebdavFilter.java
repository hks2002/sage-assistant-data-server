/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-16 23:24:10                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:50:05                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.webdav;

import com.da.sageassistantserver.utils.ITextTools;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
@WebFilter(filterName = "WebdavFilter", urlPatterns = "/docs/*")
public class WebdavFilter implements Filter {

  final List<String> skipFiles = Arrays.asList(
    "DESKTOP.INI",
    "AUTORUN.INI",
    "AUTORUN.INF"
  );
  final List<String> waterMakerFileTypes = Arrays.asList(
    "PDF",
    "JPG",
    "JPEG",
    "PNG",
    "TIF",
    "TIFF",
    "BMP"
  );

  @Override
  public void doFilter(
    ServletRequest request,
    ServletResponse response,
    FilterChain chain
  ) throws IOException, ServletException {
    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpRes = (HttpServletResponse) response;

    HttpSession session = httpReq.getSession();
    String userName = (String) session.getAttribute("userName");
    String bpName = (String) session.getAttribute("bpName");

    String pathInfo = Optional.ofNullable(httpReq.getPathInfo()).orElse("/");
    String fileName = Optional
      .ofNullable(StringUtils.getFilename(pathInfo))
      .orElse("");
    String fileExt = Optional
      .ofNullable(StringUtils.getFilenameExtension(fileName))
      .orElse("")
      .toUpperCase();
    String fileNameNoExt = Optional
      .ofNullable(StringUtils.stripFilenameExtension(fileName))
      .orElse("");

    // skip some files, these files request will happen when User-Agent is
    // Microsoft-WebDAV-MiniRedir
    if (skipFiles.contains(fileName.toUpperCase())) {
      log.debug("Skip file: {} ", fileName);
      return;
    }

    if (
      httpReq.getMethod().equals("GET") && waterMakerFileTypes.contains(fileExt)
    ) {
      RequestWrapper reqWrapper = new RequestWrapper(httpReq);
      ResponseWrapper resWrapper = new ResponseWrapper(httpRes);

      StringBuilder wmText = new StringBuilder(50);
      wmText.append("DEDIENNE");
      if (userName != null) {
        wmText.append(" ").append(userName);
      }
      if (bpName != null) {
        wmText.append(" ").append(bpName);
      }
      wmText.append(" ").append(Utils.now());

      StringBuilder outFileName = new StringBuilder(100);
      outFileName.append(URLEncoder.encode(fileNameNoExt, "UTF-8"));
      outFileName.append(" ").append(wmText);
      outFileName.append(".").append(fileExt);

      //resWrapper.addHeader("Content-Disposition", "inline; filename=\"" + outFileName + "\"");

      // do something
      chain.doFilter(reqWrapper, resWrapper);
      log.debug(
        "resWrapper:{} --- len:{}",
        resWrapper.getStatus(),
        resWrapper.getBytes().length
      );
      if (
        resWrapper.getStatus() == HttpServletResponse.SC_NOT_MODIFIED &&
        resWrapper.getBytes().length == 0
      ) {
        return;
      }

      // add watermark
      byte[] data = resWrapper.getBytes();
      ByteArrayOutputStream bosForWaterMark = new ByteArrayOutputStream();
      boolean success = false;

      if (fileExt.equals("PDF")) {
        success =
          ITextTools.addTextFull(
            data,
            bosForWaterMark,
            wmText.toString(),
            0.1f,
            30
          );
      } else {
        // for browser, change to PDF
        ByteArrayOutputStream bosForNewPdf = new ByteArrayOutputStream();
        if (ITextTools.toPdf(fileExt, data, bosForNewPdf)) {
          success =
            ITextTools.addTextFull(
              bosForNewPdf.toByteArray(),
              bosForWaterMark,
              wmText.toString(),
              0.1f,
              30
            );
        } else {
          success = false;
        }
      }

      byte[] finalData = bosForWaterMark.toByteArray();

      if (success) {
        if (fileExt.equals("PDF")) {
          httpRes.addHeader(
            "Content-Disposition",
            "inline; filename=\"" + outFileName + "\""
          );
        } else {
          httpRes.addHeader(
            "Content-Disposition",
            "inline; filename=\"" + outFileName + ".PDF\""
          );
        }

        httpRes.setContentType("application/pdf"); // Must set it before write
        httpRes.setContentLength(finalData.length); // Must set it before write
        httpRes.getOutputStream().write(finalData);
      } else {
        httpRes.setContentLength(data.length); // Must set it before write
        httpRes.getOutputStream().write(data);
      }

      httpRes.getOutputStream().flush();
      httpRes.getOutputStream().close();
    } else {
      // do nothing
      chain.doFilter(request, response);
    }
  }
}
