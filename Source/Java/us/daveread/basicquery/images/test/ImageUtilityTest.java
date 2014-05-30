package us.daveread.basicquery.images.test;

import junit.framework.TestCase;
import us.daveread.basicquery.images.ImageUtility;

/**
 * <p>Title: Test the image utility class</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006-2014</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 */
public class ImageUtilityTest extends TestCase {
  /**
   * Test image filename
   */
  private static final String IMAGE_FILE_NAME = "BasicQueryLogo32x32.gif";

  /**
   * Setup the test case instance
   */
  public ImageUtilityTest() {
  }

  /**
   * Test getting the image from a stream
   */
  public void testGetImageAsStream() {
    assertNotNull(ImageUtility.getImageAsStream(IMAGE_FILE_NAME));
    assertNull(ImageUtility.getImageAsStream("NoSuchImage"));
  }

  /**
   * Test getting the image from a byte array
   */
  public void testGetImageAsByteArray() {
    assertNotNull(ImageUtility.getImageAsByteArray(IMAGE_FILE_NAME));
    assertNull(ImageUtility.getImageAsByteArray("NoSuchImage"));
  }

  /**
   * Test getting the image from a URL
   */
  public void testGetImageAsURL() {
    assertNotNull(ImageUtility.getImageAsURL(IMAGE_FILE_NAME));
    assertNull(ImageUtility.getImageAsURL("NoSuchImage"));
  }
}
