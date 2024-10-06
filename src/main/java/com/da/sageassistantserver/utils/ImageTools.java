/*********************************************************************************************************************
 * @Author                : Robert Huang<56649783@qq.com>                                                            *
 * @CreatedDate           : 2024-06-26 22:07:14                                                                      *
 * @LastEditors           : Robert Huang<56649783@qq.com>                                                            *
 * @LastEditDate          : 2024-12-25 14:48:40                                                                      *
 * @CopyRight             : Dedienne Aerospace China ZhuHai                                                          *
 ********************************************************************************************************************/

package com.da.sageassistantserver.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageTools {

  public static boolean addTextFull(
    byte imageIn[],
    String formatName,
    OutputStream os,
    String wmText,
    float opacity,
    int angle
  ) {
    try {
      // using ImageReader, to get all pages
      ImageInputStream is = new MemoryCacheImageInputStream(
        new ByteArrayInputStream(imageIn)
      );
      ImageReader imageReader = ImageIO
        .getImageReadersByFormatName(formatName)
        .next();
      imageReader.setInput(is);

      int maxWidth = 0;
      int totalHeight = 0;
      // default image type, keep it could have better performance and low file size
      // //TYPE_INT_RGB
      int iTYPE = BufferedImage.TYPE_INT_RGB;

      ArrayList<BufferedImage> allPages = new ArrayList<BufferedImage>();
      for (int i = 0; i < imageReader.getNumImages(true); i++) {
        BufferedImage image = imageReader.read(i);

        allPages.add(image);
        iTYPE = image.getType(); // get image type
        totalHeight += image.getHeight();
        maxWidth = Math.max(maxWidth, image.getWidth());
      }

      BufferedImage mergedImage = new BufferedImage(
        maxWidth,
        totalHeight,
        iTYPE
      );
      Graphics2D g2d = mergedImage.createGraphics();

      int currentHeight = 0;
      for (int i = 0; i < allPages.size(); i++) {
        BufferedImage image = allPages.get(i);
        addTextFull(image, wmText, opacity, angle);
        g2d.drawImage(image, 0, currentHeight, null);
        currentHeight += image.getHeight(); // last change the height
      }
      g2d.dispose();

      // write merged image to output stream, use png format for low file size
      ImageIO.write(mergedImage, "png", os);
    } catch (Exception e) {
      log.error("", e);
      return false;
    }
    return true;
  }

  private static void addTextFull(
    BufferedImage image,
    String wmText,
    float opacity,
    int angle
  ) {
    // these 3 parameters can be changed, but it's the best practice value
    int heightGapN = 10;
    int widthGapN = 1;
    int fontSize = 10;
    int ratio = Math.floorDiv(Math.round(image.getWidth()), 500);

    Graphics2D g = image.createGraphics();
    g.setFont(new Font("Arial", Font.PLAIN, fontSize * ratio));
    if (image.getType() == BufferedImage.TYPE_BYTE_BINARY) { // this type without color
      g.setColor(Color.BLACK);
    } else {
      g.setColor(Color.LIGHT_GRAY);
      g.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity)
      );
      g.rotate(-Math.toRadians(angle), 0, image.getHeight()); // rotate with left-bottom
    }

    g.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
    );
    g.setRenderingHint(
      RenderingHints.KEY_RENDERING,
      RenderingHints.VALUE_RENDER_QUALITY
    );
    g.setRenderingHint(
      RenderingHints.KEY_ALPHA_INTERPOLATION,
      RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
    );
    g.setRenderingHint(
      RenderingHints.KEY_STROKE_CONTROL,
      RenderingHints.VALUE_STROKE_PURE
    );
    g.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );

    FontMetrics fontMetrics = g.getFontMetrics();
    Rectangle2D rect = fontMetrics.getStringBounds(wmText, g);
    int textH = (int) rect.getHeight();
    int textW = (int) rect.getWidth();

    if (image.getType() == BufferedImage.TYPE_BYTE_BINARY) { // this type without color
      g.drawString(wmText, 0, textH);
      g.drawString(wmText, 0, image.getHeight());
    } else {
      // (0,0) is top left
      for (
        int y = image.getHeight();
        y > 0;
        y = y - textH * heightGapN * ratio
      ) {
        for (
          int x = 0;
          x < image.getWidth() * 1.5;
          x = x + (textW * widthGapN * ratio * 3) / 4
        ) {
          g.drawString(wmText, x, y);
        }
      }
      // after rotate, need add more
      if (angle != 0) {
        for (
          int y = image.getHeight() + textH * heightGapN * ratio;
          y < image.getHeight() * 2;
          y = y + textH * heightGapN * ratio
        ) {
          for (
            int x = 0;
            x < image.getWidth() * 1.5;
            x = x + (textW * widthGapN * ratio * 3) / 4
          ) {
            g.drawString(wmText, x, y);
          }
        }
      }
    }

    g.dispose();
  }
}
