/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-18 17:36:09                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:48:55                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import java.awt.FontMetrics;
import java.io.OutputStream;
import java.util.HashMap;
import javax.swing.JLabel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ITextTools {

  /**
   * pdf watermark
   *
   * @param pdfIn     origin pdf file
   * @param os        output stream for new pdf,
   *                  ByteArrayOutputStream bos = new ByteArrayOutputStream();
   *                  OutputStream os = new FileOutputStream("C:\\temp.pdf");
   * @param wmText    the added watermark
   * @param fontSize  font Size
   * @param opacity   fill opacity (0-1)
   * @param angle     angle (0-360)
   * @param positionX the position of the watermark in width, default is 0
   * @param positionY the position of the watermark in height, Negative value
   *                  means from top, default is -100
   * @return true if success
   *         false if fail
   */
  public static boolean addTextOne(
    byte[] pdfIn,
    OutputStream os,
    String wmText,
    int fontSize,
    float opacity,
    int angle,
    float positionX,
    float positionY
  ) {
    PdfReader reader = null;
    PdfStamper stamper = null;
    try {
      PdfReader.unethicalreading = true;
      reader = new PdfReader(pdfIn);
      stamper = new PdfStamper(reader, os);

      // if is Encrypted, do nothing
      if (reader.isEncrypted()) {
        return true;
      }

      // get and edit meta-data
      HashMap<String, String> info = reader.getInfo();
      // info.put("Subject", "Subject");
      // info.put("Author", "Author");
      // info.put("Keywords", "Keywords");
      // info.put("Title", "Title");
      info.put("Creator", "Sage Assistant");
      // add updated meta-data to pdf
      stamper.setMoreInfo(info);

      // Encryption
      stamper.setEncryption(
        "".getBytes(),
        "".getBytes(),
        PdfWriter.ALLOW_PRINTING,
        PdfWriter.ENCRYPTION_AES_128
      );

      PdfGState gs = new PdfGState();
      gs.setFillOpacity(opacity);
      gs.setStrokeOpacity(opacity);

      BaseFont baseFont = BaseFont.createFont();
      PdfContentByte content;

      float calculatedPositionY = (positionY < 0)
        ? reader.getPageSize(1).getHeight() + positionY
        : positionY;

      int total = reader.getNumberOfPages() + 1;
      for (int i = 1; i < total; i++) {
        content = stamper.getUnderContent(i);
        content.saveState();
        content.beginText();
        content.setFontAndSize(baseFont, fontSize);
        content.setColorFill(BaseColor.LIGHT_GRAY);
        content.setGState(gs);
        content.showTextAligned(
          Element.ALIGN_LEFT,
          wmText,
          positionX,
          calculatedPositionY,
          angle
        );
        content.endText();
      }

      return true;
    } catch (InvalidPdfException e) {
      log.error("{}", "Not a validate pdf file");
      return false;
    } catch (Exception e) {
      log.error("{}", e);
      return false;
    } finally {
      if (stamper != null) {
        try {
          stamper.close();
        } catch (Exception e) {
          log.error("{}", e);
        }
      }
      if (reader != null) {
        reader.close();
      }
    }
  }

  /**
   * pdf watermark
   *
   * @param pdfIn    origin pdf file
   * @param os       output stream for new pdf,
   *                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
   *                 OutputStream os = new FileOutputStream("C:\\temp.pdf");
   * @param wmText   the added watermark
   * @param fontSize font Size
   * @param opacity  fill opacity (0-1)
   * @return true if success
   *         false if fail
   */
  public static boolean addTextHeader(
    byte[] pdfIn,
    OutputStream os,
    String wmText,
    int fontSize,
    float opacity
  ) {
    return addTextOne(
      pdfIn,
      os,
      wmText,
      fontSize,
      opacity,
      0,
      0.0f,
      -1.0f * fontSize
    );
  }

  /**
   * pdf watermark
   *
   * @param pdfIn    origin pdf file
   * @param os       output stream for new pdf,
   *                 ByteArrayOutputStream bos = new ByteArrayOutputStream();
   *                 OutputStream os = new FileOutputStream("C:\\temp.pdf");
   * @param wmText   the added watermark
   * @param fontSize font Size
   * @param opacity  fill opacity (0-1)
   * @return true if success
   *         false if fail
   */
  public static boolean addTextFooter(
    byte[] pdfIn,
    OutputStream os,
    String wmText,
    int fontSize,
    float opacity
  ) {
    return addTextOne(pdfIn, os, wmText, fontSize, opacity, 0, 0.0f, 0.0f);
  }

  /**
   * pdf watermark
   *
   * @param pdfIn   origin pdf file
   * @param os      output stream for new pdf,
   *                ByteArrayOutputStream bos = new ByteArrayOutputStream();
   *                OutputStream os = new FileOutputStream("C:\\temp.pdf");
   * @param wmText  the added watermark
   * @param opacity fill opacity (0-1)
   * @param angle   angle (0-360)
   * @return
   */
  public static boolean addTextFull(
    byte pdfIn[],
    OutputStream os,
    String wmText,
    float opacity,
    int angle
  ) {
    PdfReader reader = null;
    PdfStamper stamper = null;
    try {
      PdfReader.unethicalreading = true;
      reader = new PdfReader(pdfIn);
      stamper = new PdfStamper(reader, os);

      // if is Encrypted, do nothing
      if (reader.isEncrypted()) {
        return true;
      }

      // get and edit meta-data
      HashMap<String, String> info = reader.getInfo();
      // info.put("Subject", "Subject");
      // info.put("Author", "Author");
      // info.put("Keywords", "Keywords");
      // info.put("Title", "Title");
      info.put("Creator", "Sage Assistant");
      // add updated meta-data to pdf
      stamper.setMoreInfo(info);

      // Encryption
      stamper.setEncryption(
        "".getBytes(),
        "".getBytes(),
        PdfWriter.ALLOW_PRINTING,
        PdfWriter.ENCRYPTION_AES_128
      );

      PdfGState gs = new PdfGState();
      gs.setFillOpacity(opacity);
      gs.setStrokeOpacity(1);

      BaseFont baseFont = BaseFont.createFont();
      Rectangle pageRect = null;

      JLabel label = new JLabel(wmText);
      FontMetrics metrics = label.getFontMetrics(label.getFont());
      int textH = metrics.getHeight();
      int textW = metrics.stringWidth(label.getText());

      PdfContentByte content;
      int total = reader.getNumberOfPages() + 1;

      // these 3 parameters can be changed, but it's the best practice value
      int heightGapN = 10;
      int widthGapN = 1;
      int fontSize = 10;

      for (int i = 1; i < total; i++) {
        pageRect = reader.getPageSizeWithRotation(i);
        int ratio = Math.floorDiv(Math.round(pageRect.getWidth()), 500);

        content = stamper.getOverContent(i); // add watermark over the content
        // content = stamper.getUnderContent(i); // add watermark under the content

        for (
          int height = 0;
          height < pageRect.getHeight();
          height = height + textH * heightGapN * ratio
        ) {
          for (
            int width = 0;
            width < pageRect.getWidth();
            width = width + (textW * widthGapN * ratio * 3) / 4
          ) {
            content.saveState();
            content.beginText();
            content.setFontAndSize(baseFont, fontSize * ratio);
            content.setColorFill(BaseColor.LIGHT_GRAY);
            content.setGState(gs);
            content.showTextAligned(
              Element.ALIGN_LEFT,
              wmText,
              width,
              height,
              angle
            );
            content.endText();
          }
        }
      }

      return true;
    } catch (InvalidPdfException e) {
      log.error("{}", "Not a validate pdf file");
      return false;
    } catch (Exception e) {
      log.error("{}", e);
      return false;
    } finally {
      if (stamper != null) {
        try {
          stamper.close();
        } catch (Exception e) {
          log.error("{}", e);
        }
      }
      if (reader != null) {
        reader.close();
      }
    }
  }

  public static boolean toPdf(String type, byte[] imageIn, OutputStream os) {
    try {
      Document document = new Document();
      document.setMargins(0, 0, 0, 0);
      PdfWriter.getInstance(document, os);
      document.open();

      if (type.equals("TIF") || type.equals("TIFF")) {
        RandomAccessFileOrArray tif = new RandomAccessFileOrArray(
          new RandomAccessSourceFactory().createSource(imageIn)
        );
        int numberOfPages = TiffImage.getNumberOfPages(tif);
        for (int i = 1; i <= numberOfPages; i++) {
          Image image = TiffImage.getTiffImage(tif, i);
          // page size limit to 14400*14400
          image.scaleToFit(14400, 14400);
          document.setPageSize(
            new Rectangle(image.getScaledWidth(), image.getScaledHeight())
          );
          document.newPage();
          document.add(image);
        }
      } else {
        Image image = Image.getInstance(imageIn);
        // page size limit to 14400*14400
        image.scaleToFit(14400, 14400);
        document.setPageSize(
          new Rectangle(image.getWidth(), image.getHeight())
        );
        document.newPage();
        document.add(image);
      }

      document.close();
      return true;
    } catch (Exception e) {
      log.error("{}", e);
      return false;
    }
  }
}
