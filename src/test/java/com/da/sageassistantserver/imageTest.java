/*****************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                    *
 * @CreatedDate           : 2024-06-20 13:43:41                              *
 * @LastEditors           : Robert Huang<56649783@qq.com>                    *
 * @LastEditDate          : 2024-06-27 18:11:22                              *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                  *
 ****************************************************************************/

package com.da.sageassistantserver;

import com.da.sageassistantserver.utils.ImageTools;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.junit.jupiter.api.Test;

public class imageTest {

  @SuppressWarnings("resource")
  @Test
  public void testImage() {
    String pngPath = "c:\\var\\test.png";
    String pngPdfPath = "c:\\test.png";

    String tifPath = "c:\\var\\test.tif";
    String tifPdfPath = "c:\\test.tif";
    try {
      ImageTools.addTextFull(
        (new FileInputStream(pngPath)).readAllBytes(),
        "png",
        new FileOutputStream(pngPdfPath),
        "test",
        0.5f,
        30
      );
      ImageTools.addTextFull(
        (new FileInputStream(tifPath)).readAllBytes(),
        "tif",
        new FileOutputStream(tifPdfPath),
        "test",
        0.5f,
        30
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
