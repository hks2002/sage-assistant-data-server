/**********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                             *
 * @CreatedDate           : 2023-03-10 15:42:04                                                                       *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                             *
 * @LastEditDate          : 2024-12-25 14:49:29                                                                       *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                           *
 *********************************************************************************************************************/

package com.da.sageassistantserver.utils;

import com.alibaba.fastjson2.JSONArray;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

@Slf4j
public class Utils {

  /* clang-format off */
  public static final String[] IP_HEADERS = {
    "X-Forwarded-For",
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "HTTP_VIA",
    "REMOTE_ADDR"
    // you can add more matching headers here ...
  };

  /**
   * Retrieves the authorization string from the given session if the provided
   * authorization string is null or empty.
   *
   * @param auth    the authorization string to check
   * @param session the HTTP session to retrieve the authorization string from
   * @return the authorization string from the session if it exists, otherwise
   *         null
   */
  public static String getAuth(String auth, HttpSession session) {
    if (StringUtils.hasText(auth)) {
      return auth;
    } else {
      if (session != null && session.getAttribute("authorization") != null) {
        return (String) session.getAttribute("authorization");
      } else {
        return null;
      }
    }
  }

  /**
   * Convert a list of strings to a string separated by semicolons.
   *
   * @param list
   * @return
   */
  public static String ListToString(List<String> list) {
    StringBuilder sb = new StringBuilder();
    for (String s : list) {
      sb.append(s).append(";");
    }
    return sb.toString();
  }

  public static String JSONArrayToString(JSONArray array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.size(); i++) {
      sb.append(array.getString(i)).append(";");
    }
    return sb.toString();
  }

  public static boolean isWin() {
    String os = System.getProperty("os.name");
    if (os.toLowerCase().startsWith("win")) { // windows
      return true;
    } else { // linux and mac
      return false;
    }
  }

  public static Boolean isClientFromZhuhai(String ip) {
    if (
      ip.startsWith("192.168.0.") ||
      ip.startsWith("192.168.8.") ||
      ip.startsWith("192.168.13.") ||
      ip.startsWith("192.168.253.")
    ) {
      return true;
    }
    return false;
  }

  public static Boolean isZhuhaiServer() {
    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress address = addresses.nextElement();
          if (
            !address.isLoopbackAddress() &&
            address.isSiteLocalAddress() &&
            address.getHostAddress().startsWith("192.168.0.")
          ) {
            return true;
          }
        }
      }
      return false;
    } catch (SocketException e) {
      return false;
    }
  }

  public static long dateDiff(Date start, Date end) {
    return (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
  }

  public static String formatDate(Date date) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    if (date == null) {
      return "";
    }
    return formatter.format(date);
  }

  public static String today() {
    Date date = new Date();
    return formatDate(date);
  }

  public static String now() {
    Date date = new Date();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date);
  }

  public static String[] findFiles(File dir) {
    String[] files = {};

    if (!dir.exists()) {
      log.debug("Path is not exists: " + dir.getAbsolutePath());
      return files;
    }

    if (dir.exists() && dir.isDirectory()) {
      String[] filesPaths = dir.list();
      List<String> fileNames = new ArrayList<>();

      for (int i = 0; i < filesPaths.length; i++) {
        File file = new File(filesPaths[i]);

        if (
          !file.getName().startsWith("~") &&
          !file.getName().toLowerCase().equals("thumbs.db")
        ) {
          fileNames.add(file.getName());
        }
      }
      files = fileNames.toArray(new String[fileNames.size()]);
    } else {
      log.warn("Path is not folder: " + dir.getAbsolutePath());
    }

    return files;
  }

  public static String[] findFiles(String path) {
    return findFiles(new File(path));
  }

  public static String readFileContent(String filename) {
    // Reading files in jar, use getResourceAsStream(filename), here is reading for
    // war distribution
    String path = Thread
      .currentThread()
      .getContextClassLoader()
      .getResource("")
      .getPath();
    path += filename;
    log.debug("Resource base path :" + path);

    try {
      FileInputStream inputStream = new FileInputStream(path);
      String s = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      inputStream.close();
      return s;
    } catch (FileNotFoundException e) {
      log.info("FileNotFound: " + path + filename);
      return "";
    } catch (IOException e) {
      log.error("IOException: " + "When reading " + filename);
      return "";
    }
  }

  public static void saveFileContent(String filename, byte[] bytes) {
    File file = new File(filename);
    try {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      FileCopyUtils.copy(bytes, file);
    } catch (IOException e) {
      log.error("IOException: " + e.getMessage());
    }
  }

  public static String computerMd5(File file) {
    if (file.isFile()) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = Files.readAllBytes(file.toPath());
        md.update(bytes);
        byte[] digest = md.digest();
        return new String(Hex.encode(digest));
      } catch (Exception e) {
        return "";
      }
    } else {
      return "";
    }
  }

  /**
   * Pads a string with zeros on the right side to ensure it has a specified
   * length.
   *
   * @param s      the input string
   * @param length the desired length of the output string
   * @return the input string padded with zeros on the right side to match the
   *         desired length
   */
  public static String withRightPadZero(String s, int length) {
    return withRightPad(s, length, '0');
  }

  public static String withRightPad(String s, int length, char c) {
    StringBuilder sb = new StringBuilder(s);
    while (sb.length() < length) {
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Splits a string into substrings by byte size.
   *
   * @param str      the input string to be split
   * @param maxBytes the maximum number of bytes for each substring
   * @return a list of substrings split by the specified byte size
   */
  public static List<String> splitStringByByteSize(String str, int maxBytes) {
    List<String> result = new ArrayList<>();
    int startIndex = 0;

    while (startIndex < str.length()) {
      int endIndex = startIndex + 1;
      while (
        endIndex <= str.length() &&
        str
          .substring(startIndex, endIndex)
          .getBytes(StandardCharsets.UTF_8)
          .length <=
        maxBytes
      ) {
        endIndex++;
      }

      result.add(str.substring(startIndex, endIndex - 1));
      startIndex = endIndex - 1;
    }

    return result;
  }

  /**
   * Generates a file path based on the given file name, sub-folder depth, and
   * sub-folder length.
   *
   * @param fileNameNoExt   the file name without the extension
   * @param toSubFolderDeep the depth of sub-folders to create
   * @param toSubFolderLen  the length of each sub-folder
   *
   * @Note Top level is always ne character[0-9A-Z]
   *       and remove "TDS", "OMSD", "GIM" ... from file name
   * @return the generated file path
   */
  public static String getPathByFileName(
    String fileNameNoExt,
    int toSubFolderDeep,
    int toSubFolderLen
  ) {
    // remove "TDS", "OMSD", "GIM" ... from file name, ignore case
    // keep only [A-Za-z0-9] in file name
    String cleanName = fileNameNoExt
      .replaceAll("(?i)TDS", "")
      .replaceAll("(?i)OMSD", "")
      .replaceAll("(?i)DWG", "")
      .replaceAll("(?i)REV", "")
      .replaceAll("(?i)GIM", "")
      .replaceAll("(?i)NOTICE", "")
      .replaceAll("(?i)TECHNIQUE", "")
      .replaceAll("(?i)D'UTILISATIONS", "")
      .replaceAll("(?i)D'UTILISATION", "")
      .replaceAll("(?i)D'INSTRUCTIONS", "")
      .replaceAll("(?i)D'INSTRUCTION", "")
      .replaceAll("(?i)INSTRUCTIONS", "")
      .replaceAll("(?i)INSTRUCTION", "")
      .replaceAll("(?i)INFORMATION", "")
      .replaceAll("(?i)USER", "")
      .replaceAll("(?i)GUIDE", "")
      .replaceAll("(?i)MANUAL", "")
      .replaceAll("(?i)MANUEL", "")
      .replaceAll("[^A-Za-z0-9]", "")
      .toUpperCase();

    // get left toSubFolderDeep * toSubFolderLen chars, if less than it, add 0
    String subFolders = withRightPadZero(
      cleanName,
      toSubFolderDeep * toSubFolderLen
    );
    // top level fixed to 0-9 and A-Z
    StringBuilder sb = new StringBuilder(subFolders.substring(0, 1));
    for (int i = 0; i < toSubFolderDeep; i++) {
      String subFolderName = subFolders.substring(
        i * toSubFolderLen,
        (i + 1) * toSubFolderLen
      );
      // These names are reserved for Windows
      if (
        subFolderName.equals("CON") ||
        subFolderName.equals("PRN") ||
        subFolderName.equals("AUX") ||
        subFolderName.equals("NUL")
      ) {
        subFolderName = "000";
      }
      sb.append('/').append(subFolderName);
    }

    return sb.toString();
  }

  /**
   * Move files from one directory to another, ignore hidden files
   * <p>
   *
   * @param fromPath        the source directory
   * @param toPath          the destination directory
   * @param toSubFolderDeep the depth of sub-folders to create
   * @param toSubFolderLen  the length of each sub-folder
   *
   * @Note Top level is always ne character[0-9A-Z]
   *       and remove "TDS", "OMSD", "GIM" ... from file name
   * @Note this method does not update database
   */
  public static void moveFiles(
    Path fromPath,
    Path toPath,
    int toSubFolderDeep,
    int toSubFolderLen
  ) {
    if (!fromPath.toFile().isDirectory()) {
      log.warn(
        "[MoveFiles] Source path is not a directory: {}",
        fromPath.toString()
      );
      return;
    }
    if (!toPath.toFile().isDirectory()) {
      log.warn(
        "[MoveFiles] Destination path is not a directory: {}",
        toPath.toString()
      );
      return;
    }

    File[] files = fromPath.toFile().listFiles();
    if (null == files) {
      return;
    }

    for (File file : files) {
      if (file.isFile()) { // file
        moveFile(file, toPath, toSubFolderDeep, toSubFolderLen);
      } else { // directory
        // skip META-INF and WEB-INF folder
        if (
          file.getName().equals("META-INF") || file.getName().equals("WEB-INF")
        ) {
          continue;
        }

        moveFiles(
          Paths.get(file.getAbsolutePath()),
          toPath,
          toSubFolderDeep,
          toSubFolderLen
        );

        if (file.listFiles() == null || file.listFiles().length == 0) {
          if (file.delete()) { // delete original empty folder
            log.info("[Delete] {} success", file.getAbsolutePath());
          } else {
            log.warn("[Delete] {} failed", file.getAbsolutePath());
          }
        }
      }
    }
  }

  public static void moveFile(
    File file,
    Path toPath,
    int toSubFolderDeep,
    int toSubFolderLen
  ) {
    if (!toPath.toFile().isDirectory()) {
      log.warn(
        "[MoveFiles] Destination path is not a directory: {}",
        toPath.toString()
      );
      return;
    }

    if (file.isFile() && file.exists()) { // file
      String fileName = file.getName();
      int dotIndex = fileName.lastIndexOf(".");
      String fileNameNoExt = dotIndex > 0
        ? fileName.substring(0, dotIndex)
        : fileName;

      // skip hidden files
      if (
        fileName.startsWith("~") ||
        fileName.startsWith("$") ||
        fileName.toLowerCase().equals("thumbs.db")
      ) {
        // file.delete();
        return;
      }

      try {
        Path toFolder = Paths.get(
          toPath.toString() +
          '/' +
          getPathByFileName(fileNameNoExt, toSubFolderDeep, toSubFolderLen)
        );

        // make sure toFolder exists
        Files.createDirectories(toFolder);

        Path toFile = Paths.get(toFolder.toString() + '/' + fileName);
        Files.move(
          Paths.get(file.getAbsolutePath()),
          toFile,
          StandardCopyOption.REPLACE_EXISTING
        );

        if (toFile.toFile().exists()) {
          log.info(
            "[Move] {} to {} success",
            file.getAbsolutePath(),
            toFile.toString()
          );
        } else {
          log.error(
            "[Move] {} to {} failed",
            file.getAbsolutePath(),
            toFile.toString()
          );
        }
      } catch (Exception e) {
        log.error(
          "[Move] {} failed: {}",
          file.getAbsolutePath(),
          e.getMessage()
        );
      }
    }
  }

  /**
   * Clean up files in one directory，Top level is always be character[0-9A-Z].
   * <p>
   * ❗️❗️❗️It will remove hidden files，and original files,  please do a backup of your files, before run it❗️❗️❗️
   * <p>
   *
   * @Note Top level is always be character[0-9A-Z],❗️❗️❗️ please do a backup, if you want to keep some special folder❗️❗️❗️
   * @Note this method does not update database
   */
  public static void cleanUpFiles(
    File targetFolder,
    int toSubFolderDeep,
    int toSubFolderLen
  ) {
    if (!targetFolder.isDirectory()) {
      log.warn(
        "[cleanUpFiles] Source path is not a directory: {}",
        targetFolder.getAbsolutePath()
      );
      return;
    }

    File[] files = targetFolder.listFiles();
    if (files == null) {
      return;
    }

    for (File file : files) {
      if (file.isFile()) { // file
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        String fileNameNoExt = dotIndex > 0
          ? fileName.substring(0, dotIndex)
          : fileName;

        // skip hidden files
        if (
          fileName.startsWith("~") ||
          fileName.startsWith("$") ||
          fileName.toLowerCase().equals("thumbs.db")
        ) {
          file.delete();
          continue;
        }

        File toFolder = new File(
          targetFolder.getAbsolutePath() +
          getPathByFileName(fileNameNoExt, toSubFolderDeep, toSubFolderLen)
        );
        if (!toFolder.exists()) {
          toFolder.mkdirs();
        }

        File toFile = new File(toFolder, fileName);
        if (toFile.exists()) {
          file.delete();
        } else {
          if (!file.renameTo(toFile)) {
            log.warn("[Move] {} failed", file.getAbsolutePath());
          } else {
            log.info("[Move] {} success", file.getAbsolutePath());
          }
        }
      } else { // directory
        cleanUpFiles(file, toSubFolderDeep, toSubFolderLen);
      }
    }
  }

  public static String decodeBasicAuth(String basicAuth) {
    // if str end with more than one = , remove it
    byte[] decodedBytes = Base64
      .getDecoder()
      .decode(basicAuth.replaceFirst("Basic\\s+", ""));
    return new String(decodedBytes, StandardCharsets.UTF_8);
  }

  public static String encodeBasicAuth(String username, String password) {
    byte[] encodedBytes = Base64
      .getEncoder()
      .encode((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    return "Basic " + new String(encodedBytes, StandardCharsets.UTF_8);
  }

  public static String makeShortPn(String pn) {
    // if PN start with
    // 2C,7C,9C,9R,11C,97A,98A,98D,98F,98S,98L,98V,98F,99A,99D,99F,856A,956A,HU,RRT,330A,332A,350A,365A,9446M,9426M,9429M,9465M,9471M,9448M,9486M
    // end with G01, G01_A
    // remove G01-G09,P01-G09
    // remove end _-
    // remove version number
    log.debug("[makeShortPn] " + pn);
    // change / \ * ? to -
    String newPn = pn.replaceAll("(\\\\|\\*|\\/|\\?)", "-");
    log.debug("[makeShortPn00] " + newPn);
    // remove Complete, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*complete", "");
    // remove history, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*history", "");
    // remove trolley, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*trolley", "");
    // remove full, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*full", "");
    // remove all, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*all", "");
    // remove tds, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*tds", "");
    // remove omsd, case ignore
    newPn = newPn.replaceAll("(?i)omsd(-|_)*", "");
    // remove cad, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*cad", "");
    // remove rev, case ignore
    newPn = newPn.replaceAll("(?i)rev", "");
    // remove dwg, case ignore
    newPn = newPn.replaceAll("(?i)(-|_)*dwg", "");
    // split by space and get the first words
    newPn = newPn.split(" ")[0];
    log.debug("[makeShortPn00] " + newPn);

    // remove _-|__-- at tail
    newPn = newPn.replaceAll("_+", "_");
    newPn = newPn.replaceAll("-+", "-");
    newPn = newPn.replaceAll("(.*?)(_-)$", "$1");
    newPn = newPn.replaceAll("(.*?)(_)*$", "$1");
    newPn = newPn.replaceAll("(.*?)(-)*$", "$1");

    log.debug("[makeShortPn01] " + newPn);
    // remove _DRAFT|_QU|_NQ|_NQD|_CPD_PRT anywhere
    newPn = newPn.replaceAll("(.*)(_DRAFT|_QU|_NQD|_NQ|_CPD|_PRT)(.*)", "$1$3");
    log.debug("[makeShortPn02] " + newPn);
    // remove DRAFT|QU|NQ|NQD|CPDPRT at tail
    newPn = newPn.replaceAll("(.*)(DRAFT|QU|NQD|NQ|CPD|PRT)$", "$1");
    log.debug("[makeShortPn03] " + newPn);
    // remove DRAFT|QU|NQ|NQD|CPD|PRT|AF at midst
    newPn =
      newPn.replaceAll(
        "(.*)(DRAFT|QU|NQD|NQ|CPD|PRT|AF)([_|-][A-Z|\\d]{1,3})$",
        "$1$3"
      );
    newPn =
      newPn.replaceAll(
        "(.*)(DRAFT|QU|NQD|NQ|CPD|PRT|AF)(-[\\d]{1,3})(.*)",
        "$1$4"
      );
    log.debug("[makeShortPn04] " + newPn);
    // remove P01 at tail
    newPn = newPn.replaceAll("(.*)(P\\d{2})$", "$1");
    log.debug("[makeShortPn05] " + newPn);
    // remove PO1 at midst
    newPn = newPn.replaceAll("(.*)(P\\d{2})(_[A-Z]{1,3})$", "$1$3");
    log.debug("[makeShortPn06] " + newPn);
    // remove G01 at tail
    newPn = newPn.replaceAll("(.*)(G\\d{2})$", "$1");
    log.debug("[makeShortPn07] " + newPn);
    // remove G01|G01XX at midst
    newPn = newPn.replaceAll("(.*)(G\\d{2})([A-Z]{2})?(_[A-Z]{1,3})$", "$1$4");
    log.debug("[makeShortPn08] " + newPn);

    ///////////////////////////////////////////////////////////////////////////////
    // 9[7|8|9][ADFGKSLV] + 8 bit
    // remove -00, _P-00
    newPn =
      newPn.replaceAll("^([9][7|8|9][ADFGKSLV])(\\d{8})\\d*(.*)", "$1$2$3");
    newPn =
      newPn.replaceAll(
        "^([9][7|8|9][ADFGKSLV])(\\d{8})((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$2$6"
      );
    log.debug("[makeShortPn09] " + newPn);

    // 856A|956A + 4 bit
    // remove -00, _P-00
    newPn = newPn.replaceAll("^(856A|956A)(\\d{4})\\d*(.*)", "$1$2$3");
    newPn =
      newPn.replaceAll(
        "^(856A|956A)(\\d{4})((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$2$6"
      );
    log.debug("[makeShortPn10] " + newPn);

    // 2C|7C|9C|9R|11C + 4 or 5 bit and 2 ?
    // remove -00, _P-00
    newPn =
      newPn.replaceAll(
        "^(2C|7C|9C|9R|11C\\d{4,5})(-\\d{1,2})?((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$6"
      );
    log.debug("[makeShortPn11] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(RRT\\d{6})(-\\d{1,3})?((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$2$6"
      );
    log.debug("[makeShortPn12] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(HU\\d{5})(-\\d{1,3})?((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$2$6"
      );
    log.debug("[makeShortPn13] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(330A|332A|350A|365A)(\\d{6})\\d*((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$2$6"
      );
    log.debug("[makeShortPn14] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(94\\d{2}M\\d{2})\\d*((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$5"
      );
    log.debug("[makeShortPn15] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(98AMS\\d{6})\\d*((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$5"
      );
    log.debug("[makeShortPn16] " + newPn);

    newPn =
      newPn.replaceAll(
        "^(98DNSA\\d{5})\\d*((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$5"
      );
    log.debug("[makeShortPn17] " + newPn);

    newPn =
      newPn.replaceAll(
        "^([A|B|C|F|G|J|K]\\d{5})(-\\d{1,3})?((-[0-9|A-Z]{0,2})|(_P-\\d{1,3}))?(.*)",
        "$1$6"
      );
    log.debug("[makeShortPn18] " + newPn);

    // remove version
    newPn = newPn.replaceAll("(.*)([_|-][A-Z|\\d]{1,3})$", "$1");

    if (!StringUtils.hasText(newPn)) {
      log.error("[makeShortPn] " + pn);
    }
    return newPn;
  }
}
