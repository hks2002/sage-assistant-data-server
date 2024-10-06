/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2022-03-26 17:57:07                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:46:11                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class HttpService {

  /**
   * Caffeine cache
   */
  private static Cache<String, String> cache = Caffeine
    .newBuilder()
    .expireAfterAccess(5, TimeUnit.MINUTES)
    .maximumSize(10000)
    .build();

  private static HttpClient client = null;

  private static SSLContext getSSLContext() {
    try {
      TrustManager[] trustAllCertificates = new TrustManager[] {
        new X509TrustManager() {
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          @Override
          public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {}

          @Override
          public void checkServerTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {}
        }
      };

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCertificates, new SecureRandom());
      return sc;
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  public static HttpResponse<String> request(String url, String method) {
    return request(url, method, null, null);
  }

  public static HttpResponse<String> request(
    String url,
    String method,
    String data
  ) {
    return request(url, method, data, null);
  }

  public static HttpResponse<String> request(
    String url,
    String method,
    String data,
    String auth
  ) {
    return request(url, method, data, auth, false);
  }

  public static HttpResponse<String> request(
    String url,
    String method,
    String data,
    String auth,
    boolean isAsync
  ) {
    try {
      // Disable host name verification Globally
      Properties props = System.getProperties();
      props.setProperty(
        "jdk.internal.httpclient.disableHostnameVerification",
        Boolean.TRUE.toString()
      );

      if (client == null) {
        client = HttpClient.newBuilder().sslContext(getSSLContext()).build();
      }

      Builder reqBuilder = HttpRequest
        .newBuilder()
        .uri(URI.create(url))
        .setHeader("Content-Type", "application/json")
        .setHeader("Accept", "application/json");

      if (StringUtils.hasText(auth)) {
        reqBuilder.header("authorization", auth);
      }
      // Cookie
      if (StringUtils.hasText(auth)) {
        if (cache.getIfPresent(auth) != null) {
          reqBuilder.header("Cookie", cache.getIfPresent(auth));
        }
      }
      log.debug("data:{}", data);

      switch (method) {
        case "GET":
          reqBuilder.GET();
          break;
        case "POST":
          if (data != null && !data.isBlank()) {
            reqBuilder.POST(BodyPublishers.ofString(data));
          }
          break;
        case "PUT":
          if (data != null && !data.isBlank()) {
            reqBuilder.PUT(BodyPublishers.ofString(data));
          }
          break;
        case "DELETE":
          reqBuilder.DELETE();
          break;
        default:
          reqBuilder.GET();
      }

      HttpRequest request = reqBuilder.build();
      HttpResponse<String> response = null;
      if (isAsync) {
        response = client.sendAsync(request, BodyHandlers.ofString()).get();
      } else {
        response = client.send(request, BodyHandlers.ofString());
      }

      // Cookie
      List<String> cookieResponse = response.headers().allValues("Set-Cookie");
      List<String> cookieCache = new ArrayList<String>();
      for (String cookie : cookieResponse) {
        cookieCache.add(cookie.split(";")[0]);
      }
      if (StringUtils.hasText(auth)) {
        String cookieStr = String.join(";", cookieCache);
        cache.put(auth, cookieStr);
        log.debug("cookie:{}", cookieStr);

        // save last cookie, for request need login
        cache.put("LastCookie", cookieStr);
      }

      log.debug("{}", response.statusCode());
      log.debug(response.body());

      return response;
    } catch (Exception e) {
      e.getStackTrace();
      log.error(e.getLocalizedMessage());
      return null;
    }
  }

  /**
   * need "LastCookie" for any login
   */
  public static byte[] getFile(String url) {
    try {
      // Disable host name verification Globally
      Properties props = System.getProperties();
      props.setProperty(
        "jdk.internal.httpclient.disableHostnameVerification",
        Boolean.TRUE.toString()
      );

      if (client == null) {
        client = HttpClient.newBuilder().sslContext(getSSLContext()).build();
      }

      Builder reqBuilder = HttpRequest.newBuilder().uri(URI.create(url));

      // Cookie
      if (cache.getIfPresent("LastCookie") != null) {
        reqBuilder.header("Cookie", cache.getIfPresent("LastCookie"));
      }

      reqBuilder.GET();

      HttpRequest request = reqBuilder.build();
      HttpResponse<byte[]> response = null;

      response = client.send(request, BodyHandlers.ofByteArray());

      return response.body();
    } catch (Exception e) {
      e.getStackTrace();
      log.error(e.getLocalizedMessage());
      return null;
    }
  }

  public static HttpResponse<String> proxy(
    String method,
    String url,
    Map<String, String> headerMap,
    String data
  ) {
    try {
      // Disable host name verification Globally
      Properties props = System.getProperties();
      props.setProperty(
        "jdk.internal.httpclient.disableHostnameVerification",
        Boolean.TRUE.toString()
      );

      if (client == null) {
        client = HttpClient.newBuilder().sslContext(getSSLContext()).build();
      }

      Builder reqBuilder = HttpRequest.newBuilder().uri(URI.create(url));

      // add header
      headerMap.forEach((name, value) -> {
        switch (name) {
          case "host":
          case "connection":
          case "content-length":
            break;
          default:
            reqBuilder.setHeader(name, value);
        }
      });

      switch (method) {
        case "GET":
          reqBuilder.GET();
          break;
        case "POST":
          if (data != null && !data.isBlank()) {
            reqBuilder.POST(BodyPublishers.ofString(data));
          }
          break;
        case "PUT":
          if (data != null && !data.isBlank()) {
            reqBuilder.PUT(BodyPublishers.ofString(data));
          }
          break;
        case "DELETE":
          reqBuilder.DELETE();
          break;
        default:
          reqBuilder.GET();
      }

      HttpRequest request = reqBuilder.build();
      HttpResponse<String> response = client.send(
        request,
        BodyHandlers.ofString()
      );
      log.debug(response.body());

      return response;
    } catch (Exception e) {
      e.getStackTrace();
      log.error(e.getLocalizedMessage());
      return null;
    }
  }
}
