package us.daveread.basicquery.images;

import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * General image access utility methods.
 * <p>
 * Copyright: Copyright (c) 2004-2014, David Read
 * </p>
 * <p>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * </p>
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * </p>
 * <p>
 * </p>
 * 
 * @author David Read
 */
public class ImageUtility {
  /**
   * The instance of this class - a Singleton
   */
  private static ImageUtility instance;

  /**
   * Logger
   */
  private static final Logger LOGGER = Logger.getLogger(ImageUtility.class);

  /**
   * The block size when loading images
   */
  private static final int BLOCK_READ_SIZE = 1000;

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
    if (instance == null) {
      instance = new ImageUtility();
    }

    return instance;
  }

  /**
   * Get the image as a stream.
   * 
   * @param saImageName
   *          The file name of the image to be loaded.
   * 
   * @return The InputStream opened for the image file.
   */
  public static InputStream getImageAsStream(String saImageName) {
    return instance().getClass().getResourceAsStream(saImageName);
  }

  /**
   * Get the image as a byte array.
   * 
   * @param saImageName
   *          The file name of the image to be loaded.
   * 
   * @return The byte array containing the image data.
   */
  public static byte[] getImageAsByteArray(String saImageName) {
    byte[] bylImage;
    byte[] bylTemp;
    InputStream islImage;
    int ilSize, ilRead, ilPosit;

    islImage = getImageAsStream(saImageName);
    bylImage = null;
    bylTemp = new byte[BLOCK_READ_SIZE];
    ilSize = 0;

    LOGGER.info("Image[" + saImageName + "]");

    try {
      while ((ilRead = islImage.read(bylTemp)) >= 0) {
        ilSize += ilRead;
      }

      LOGGER.info("Image Size [" + ilSize + "]");

      if (ilSize > 0) {
        islImage = getImageAsStream(saImageName);
        bylImage = new byte[ilSize];
        ilPosit = 0;
        while ((ilRead = islImage.read(bylImage, ilPosit, ilSize - ilPosit))
            > -1 && ilPosit < ilSize) {
          ilPosit += ilRead;
          LOGGER.debug("Loading, Bytes[" + ilRead
              + "]  Total[" + ilPosit + "]  Expect[" + ilSize + "]");
        }
      }
    } catch (Throwable excAny) {
      LOGGER.error("Failed to load image: " + saImageName, excAny);
    }

    return bylImage;
  }

  /**
   * Get the image as a URL.
   * 
   * @param saImageName
   *          The file name of the image to be loaded.
   * 
   * @return The URL of the image.
   *         <strong>Under JDK1.3.1_01 on windows it appears that the underlying
   *         Class.getResource() call is not supported.</strong>
   */
  public static URL getImageAsURL(String saImageName) {
    LOGGER.info("Image [" + saImageName + "]");
    LOGGER.debug("Class [" + instance().getClass() + "]");
    LOGGER
        .debug("URL [" + instance().getClass().getResource(saImageName) + "]");

    return instance().getClass().getResource(saImageName);
  }
}
