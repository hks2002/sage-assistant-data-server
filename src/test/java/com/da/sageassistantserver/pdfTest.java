/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-20 13:43:41                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:54:07                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver;

import com.da.sageassistantserver.utils.ITextTools;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;

public class pdfTest {

  @Test
  public void testLimitAccess() {
    try {
      OutputStream file = new FileOutputStream(
        new File("c:\\LimitedAccess.pdf")
      );
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, file);

      writer.setEncryption(
        "".getBytes(),
        "".getBytes(),
        PdfWriter.ALLOW_PRINTING, // Only printing allowed; Try to copy text !!
        PdfWriter.ENCRYPTION_AES_128
      );

      document.open();
      document.add(new Paragraph("Limited Access File !!"));
      document.close();
      writer.close();
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDocumentProperties() {
    try {
      OutputStream file = new FileOutputStream(
        new File("c:\\DocumentProperties.pdf")
      );
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, file);

      document.open();
      document.add(new Paragraph("Document Properties !!"));
      document.addAuthor("DEDIENNE AEROSPACE CHINA ZHUHAI");
      document.addCreationDate();
      document.addCreator("Sage Assistant Server");
      document.addTitle("Title");
      document.addSubject("Subject");

      document.close();
      writer.close();
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("resource")
  @Test
  public void testImageToPdf() {
    String pngPath = "c:\\var\\test.png";
    String pngPdfPath = "c:\\test.pdf";

    String tifPath = "c:\\var\\test.tif";
    String tifPdfPath = "c:\\test2.pdf";
    try {
      ITextTools.toPdf(
        "PNG",
        (new FileInputStream(pngPath)).readAllBytes(),
        new FileOutputStream(pngPdfPath)
      );
      ITextTools.toPdf(
        "TIF",
        (new FileInputStream(tifPath)).readAllBytes(),
        new FileOutputStream(tifPdfPath)
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
