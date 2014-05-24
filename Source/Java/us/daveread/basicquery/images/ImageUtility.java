package us.daveread.basicquery.images;

import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

/**
 * General image access utility methods.
 * <p>Copyright: Copyright (c) 2004, David Read</p>
 * <p>This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 * <p>This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 * <p>You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA</p>
 * <p></p>
 * @author David Read
 * @version $Id: ImageUtility.java,v 1.3 2006/05/03 03:56:32 daveread Exp $
 */
public class ImageUtility {
  private static ImageUtility objcSingleton;
  private static final Logger logger = Logger.getLogger(ImageUtility.class);

  private static int icReadSize = 1000;
  /**
   * Constructor is private for singleton.
   */
  private ImageUtility() {
  }

  /**
   * Get the instance of this singleton, private as only the static utility
   * methods can have access to the class.
   *
   * @return The single instance of the class.
   */
  private static synchronized ImageUtility instance() {
    if (objcSingleton == null) {
      objcSingleton = new ImageUtility();
    }

    return objcSingleton;
  }

  /**
   * Get the image as a stream.
   *
   * @param saImageName The file name of the image to be loaded.
   *
   * @return The InputStream opened for the image file.
   */
  public static InputStream getImageAsStream(String saImageName) {
    return instance().getClass().getResourceAsStream(saImageName);
  }

  /**
   * Get the image as a byte array.
   *
   * @param saImageName The file name of the image to be loaded.
   *
   * @return The byte array containing the image data.
   */
  public static byte[] getImageAsByteArray(String saImageName) {
    byte bylImage[], bylTemp[];
    InputStream islImage;
    int ilSize, ilRead, ilPosit;

    islImage = getImageAsStream(saImageName);
    bylImage = null;
    bylTemp = new byte[icReadSize];
    ilSize = 0;

    logger.info("Image[" + saImageName + "]");

    try {
      while ((ilRead = islImage.read(bylTemp)) >= 0) {
        ilSize += ilRead;
      }
    }
    catch (Exception excAny) {
      logger.error("Failed to load image [" + saImageName + "]", excAny);
    }

    logger.info("Image Size [" + ilSize + "]");

    if (ilSize > 0) {
      try {
        islImage = getImageAsStream(saImageName);
        bylImage = new byte[ilSize];
        ilPosit = 0;
        while ((ilRead = islImage.read(bylImage, ilPosit, ilSize - ilPosit)) >
            -1 && ilPosit < ilSize) {
          ilPosit += ilRead;
          logger.debug("Loading, Bytes[" +
              ilRead +
              "]  Total[" + ilPosit +
              "]  Expect[" + ilSize + "]");
        }
      }
      catch (Throwable excAny) {
        logger.error("Failed to load image", excAny);
      }
    }

    return bylImage;
  }

  /**
   * Get the image as a URL.
   *
   * @param saImageName The file name of the image to be loaded.
   *
   * @return The URL of the image.
   *  <strong>Under JDK1.3.1_01 on windows it appears that the underlying
   *      Class.getResource() call is not supported.</strong>
   */
  public static URL getImageAsURL(String saImageName) {
    logger.info("Image [" +
        saImageName + "]");
    logger.debug("Class [" +
        instance().getClass() + "]");
    logger.debug("URL [" +
        instance().getClass().getResource(saImageName) + "]");

    return instance().getClass().getResource(saImageName);
  }
}
