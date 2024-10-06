/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2024-07-01 00:04:54                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:49:51                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.webdav;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.da.sageassistantserver.utils.Utils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResource;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.webresources.DirResourceSet;

//
// disable it in here

/**
 * Using spring boot, see <em>ServletConfig<em> to enable webdav
 * <p>
 * If using not in spring boot:
 * <p>
 *
 * @WebServlet(name = "MyWebdav", urlPatterns = { "/docs/*" }, initParams
 *                  = {
 * @WebInitParam(name = "listings", value = "true"),
 * @WebInitParam(name = "maxDepth", value = "4"),
 * @WebInitParam(name = "readonly", value = "false"),
 * @WebInitParam(name = "debug", value = "0")
 *                    })
 *
 *                    <p>
 */
@Slf4j
@WebServlet(
  name = "MyWebdav",
  urlPatterns = { "/docs/*" },
  initParams = {
    @WebInitParam(name = "listings", value = "true"),
    @WebInitParam(name = "maxDepth", value = "1"),
    @WebInitParam(name = "readonly", value = "true"),
    @WebInitParam(name = "debug", value = "0")
  },
  loadOnStartup = 1
)
public class MyWebDav extends WebdavServlet {

  private String appName = "";
  private String appVersion = "";
  private Boolean webdavCache = true;

  @Override
  public void init(ServletConfig config) throws ServletException {
    log.info("init");
    super.init(config);

    try {
      Properties prop = new Properties();
      FileInputStream iso = new FileInputStream(
        getServletContext().getClassLoader().getResource("").getPath() +
        "application.properties"
      );
      prop.load(iso);

      appName =
        Optional.ofNullable(prop.getProperty("project.name")).orElse("");
      appVersion =
        Optional.ofNullable(prop.getProperty("project.version")).orElse("");
      webdavCache =
        Optional
          .ofNullable(prop.getProperty("webdav.cache"))
          .orElse("true")
          .equals("true");

      String attachmentPath = Utils.isWin()
        ? prop.getProperty("attachment.path.windows")
        : prop.getProperty("attachment.path.linux");

      if (attachmentPath != "") {
        File additionPath = new File(attachmentPath);
        DirResourceSet dirResourceSet = new DirResourceSet(
          super.resources,
          "/",
          additionPath.getAbsolutePath(),
          "/"
        );
        super.resources.addPostResources(dirResourceSet);
      }
    } catch (IOException e) {
      log.error("[Webdav] {}", e);
    }
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
    throws IOException, ServletException {
    if (!webdavCache) {
      req.getSession().setAttribute("webdavCache", "false");
      resp.addHeader("Cache-Control", "no-store");
    } else {
      req.getSession().setAttribute("webdavCache", "true");
    }
    super.service(req, resp);
  }

  @Override
  protected InputStream renderHtml(
    HttpServletRequest request,
    String contextPath,
    WebResource resource,
    String encoding
  ) throws IOException {
    HttpSession session = request.getSession();
    String userName = Optional
      .ofNullable((String) session.getAttribute("userName"))
      .orElse("");
    String bpName = Optional
      .ofNullable((String) session.getAttribute("bpName"))
      .orElse("");

    // Prepare a writer to a buffered area
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    OutputStreamWriter osWriter = new OutputStreamWriter(
      stream,
      StandardCharsets.UTF_8
    );
    PrintWriter writer = new PrintWriter(osWriter);

    String htmlTemplate = Utils.readFileContent("webDav.html");
    String topContextPath = "/docs";
    String resourcesPath = resource.getWebappPath();
    WebResource[] files = resources.listResources(resourcesPath);

    String html = htmlTemplate.replace("DATA", appName);
    html = html.replace("%APP_NAME%", appName);
    html = html.replace("%APP_VERSION%", appVersion);
    html = html.replace("%USER_NAME%", userName);
    html = html.replace("%BP_NAMES%", bpName);
    html = html.replace("%HOME_URL%", topContextPath);
    html = html.replace("%CURRENT_DIRECTORY%", resourcesPath);

    JSONArray allFiles = new JSONArray();
    for (WebResource childResource : files) {
      String filename = childResource.getName();
      if (
        filename.equalsIgnoreCase("WEB-INF") ||
        filename.equalsIgnoreCase("META-INF")
      ) {
        continue;
      }
      if (!childResource.exists()) {
        continue;
      }

      JSONObject obj = new JSONObject();
      obj.put("name", filename);
      obj.put(
        "url",
        resourcesPath + filename + (childResource.isDirectory() ? "/" : "")
      );
      obj.put("isDirectory", childResource.isDirectory());
      obj.put("size", childResource.getContentLength());
      obj.put("lastModified", childResource.getLastModifiedHttp());

      allFiles.add(obj);
    }
    html = html.replace("%DOCS%", allFiles.toString().replaceAll("'", ""));

    // Return an input stream to the underlying bytes
    writer.write(html);
    writer.flush();
    return new ByteArrayInputStream(stream.toByteArray());
  }
}
