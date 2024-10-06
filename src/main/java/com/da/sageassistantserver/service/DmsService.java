/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-07-02 15:44:21                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:44:42                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.service;

import com.da.sageassistantserver.model.Docs;
import com.da.sageassistantserver.utils.Utils;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Dms server is provided for Audros, which is stored new attachments for Sage.
 * <p>
 * This class is mainly provide APIs for Dms server.
 * Core is get fileId by PN searched.
 * <ul>
 * <li>extractFileId</li>
 * <li>extractFileModifiedDate</li>
 * <li>getDocuments</li>
 * <li>getDocument</li>
 * <li>getDocumentContent</li>
 * </ul>
 */
@Slf4j
public class DmsService {

  private static LoadingCache<String, String> dmsSessionCache = Caffeine
    .newBuilder()
    .maximumSize(100)
    .expireAfterWrite(15, TimeUnit.SECONDS)
    .removalListener(
      (RemovalListener<String, String>) (Auth, value, cause) -> {
        /*
         * Dms will expired created session automatically, we exit page to avoid this
         */
        log.debug(
          "[Dms] SessionId cache {} {} is removed, cause is {}",
          Auth,
          value,
          cause
        );
        doLogout(value);
      }
    )
    .build(
      new CacheLoader<String, String>() {
        @Override
        public String load(String Auth) {
          log.debug("[Dms] session cache login with Auth: {}", Auth);
          return doLogin(Auth);
        }
      }
    );

  private static String doLogin(String Auth) {
    SageLoginService.doLogin(Auth);
    String html = HttpService
      .request(
        "http://192.168.10.64:4040/cocoon/View/LoginCAD/fr/AW_AutoLogin.html?userName=TEMP&dsn=dmsDS&Client_Type=25&computerName=AWS&LDAPControl=true",
        "GET",
        null,
        null
      )
      .body();
    return extractSessionId(html);
  }

  private static void doLogout(String sessionId) {
    HttpService
      .request(
        "http://192.168.10.64:4040/cocoon/View/LogoutXML/fr/AW_Logout7.html?userName=TEMP&dsn=dmsDS&Client_Type=25&AUSessionID=" +
        sessionId,
        "GET",
        null,
        null
      )
      .body();
  }

  private static String extractSessionId(String htmlContent) {
    String id = "";
    Pattern pattern = Pattern.compile("sSessionID = '(.*?)';");
    Matcher matcher = pattern.matcher(htmlContent);

    while (matcher.find()) {
      id = matcher.group(1);
    }

    log.debug("id: {}", id);
    return id;
  }

  private static String base64Encode(String value) {
    try {
      return Base64
        .getEncoder()
        .encodeToString(URLEncoder.encode(value, "UTF-8").getBytes());
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
    }
    return null;
  }

  private static List<String> extractLIValues(String xmlContent) {
    List<String> liValues = new ArrayList<>();
    Pattern pattern = Pattern.compile("<LI>(.*?)</LI>");
    Matcher matcher = pattern.matcher(xmlContent);

    while (matcher.find()) {
      liValues.add(matcher.group(1));
    }

    log.debug("liValues: {}", liValues);
    return liValues;
  }

  private static String extractFileId(String htmlContent) {
    Pattern pattern = Pattern.compile("var id\t\t= '(.*?)';");
    Matcher matcher = pattern.matcher(htmlContent);
    String id = null;
    while (matcher.find()) {
      id = matcher.group(1);
    }
    log.debug("fileId: {}", id);
    return id;
  }

  private static String extractModifiedDate(String htmlContent) {
    String date = "";
    Pattern pattern = Pattern.compile(
      "<td data-name-attr=\"obj_modificationdate\" nowrap=\"1\">(.*?)</td>"
    );
    Matcher matcher = pattern.matcher(htmlContent);

    while (matcher.find()) {
      date = matcher.group(1);
    }

    log.debug("modifiedDate: {}", date);
    return date;
  }

  private static List<String> getDocumentNames(String Auth, String Pn) {
    List<String> docs = new ArrayList<>();
    String sessionId = dmsSessionCache.get(Auth);
    String search = base64Encode("%" + Pn);
    String url = String.format(
      "http://192.168.10.64:4040/cocoon/View/ExecuteService/fr/AW_AuplResult3.html?" +
      "ServiceName=aws.au&ServiceSubPackage=aws&UserName=TEMP&dsn=dmsDS&Client_Type=25&ServiceParameters=GET_AUTOCOMPLETION@%s@&AUSessionID=%s",
      search,
      sessionId
    );
    String xml = HttpService.request(url, "GET", null, null).body();
    List<String> liValues = extractLIValues(xml);

    for (int i = 0; i < liValues.size(); i++) {
      // only files with extension
      if (StringUtils.getFilenameExtension(liValues.get(i)) == null) {
        continue;
      }
      docs.add(liValues.get(i));
    }

    return docs;
  }

  private static String getFileInfo(String Auth, String FileName) {
    String sessionId = dmsSessionCache.get(Auth);
    String search = base64Encode(FileName);
    String url = String.format(
      "http://192.168.10.64:4040/cocoon/View/ExecuteService/fr/AW_QuickSearchView7.post?" +
      "ServiceName=aws.au&ServiceParameters=GET_OBJECTS_LIST@SEARCH@%s@@@0@9999@0@&ServiceSubPackage=aws&URL_Encoding=UTF-8&date_format=enDateHour&AUSessionID=%s",
      search,
      sessionId
    );
    return HttpService.request(url, "GET", null, null).body();
  }

  private static byte[] getDocumentBytes(String id) {
    // 581 bugs, remove session, it works again
    return HttpService.getFile(
      "http://192.168.10.64:4040/cocoon/viewDocument/ANY?FileID=" +
      id +
      "&UserName=TEMP&dsn=dmsDS&Client_Type=25"
    );
  }

  /**
   * Get document information from DMS system
   *
   * @param Auth Authentication information, used for accessing DMS services
   * @param Pn Project number or similar identifier, used to filter documents
   * @return Returns a list of document information objects
   */
  public static List<Docs> getDocuments(String Auth, String Pn) {
    // Get the list of document names
    List<String> documentNames = getDocumentNames(Auth, Pn);

    List<Docs> docs = new ArrayList<>();
    // Iterate through the list of document names to get document information
    for (int i = 0; i < documentNames.size(); i++) {
      // Get the HTML content containing the document information
      String html = getFileInfo(Auth, documentNames.get(i));
      // Extract the modification date of the document from the HTML
      String dateString = extractModifiedDate(html);
      // Extract the file ID of the document from the HTML
      String fileId = extractFileId(html);
      // Parse the modification date string into a Date object
      SimpleDateFormat dateFormat = new SimpleDateFormat(
        "MM/dd/yyyy hh:mm:ss aa",
        Locale.ENGLISH
      );
      Date date;
      try {
        date = dateFormat.parse(dateString);
      } catch (ParseException e) {
        log.error(e.getMessage());
        date = new Date();
      }
      Timestamp modifiedAt = new Timestamp(date.getTime());

      // Create a Docs object and set its properties
      Docs doc = new Docs();
      doc.setFile_name(documentNames.get(i));
      doc.setDoc_modified_at(modifiedAt);
      doc.setLocation(fileId); // save id to location
      // Add the document to the list
      docs.add(doc);
    }
    // Return the list of documents
    return docs;
  }

  /**
   * Download documents from DMS system
   *
   * @param docs List of documents to download
   * @param docSavePath Local path to save the downloaded documents
   */
  public static void downloadDmsDocs(List<Docs> docs, String docSavePath) {
    // Iterate through the list of documents to download each one
    for (Docs doc : docs) {
      // Construct the full path for saving the document
      String fileName = docSavePath + doc.getFile_name();

      // Log the start of the download process
      log.debug(
        "[DOWNLOAD] {} from Dms server to {} ...",
        doc.getFile_name(),
        docSavePath
      );

      // save document to local
      byte[] bytes = getDocumentBytes(doc.getLocation()); // using location as id
      Utils.saveFileContent(fileName, bytes);

      // Check if the file has been created
      File f = new File(fileName);
      if (f.exists()) {
        // Check if the file size is greater than 0
        if (f.length() > 0 && f.length() != 581) {
          // Log the successful download
          log.info(
            "[DOWNLOAD] {} from Dms server to {}, size {}",
            doc.getFile_name(),
            fileName,
            f.length()
          );
          // set file time
          f.setLastModified(doc.getDoc_modified_at().getTime());
        } else {
          // Log the download failure due to empty file
          log.error(
            "[DOWNLOAD] {} from Dms server to {}, size {}",
            doc.getFile_name(),
            fileName,
            f.length()
          );
          // delete
          f.delete();
        }
      } else {
        // Log the download failure due to file not existing
        log.error(
          "[DOWNLOAD] {} from Dms server to {}",
          doc.getFile_name(),
          fileName
        );
      }
    }
  }
}
