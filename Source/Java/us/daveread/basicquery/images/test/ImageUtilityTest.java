package us.daveread.basicquery.images.test;

import junit.framework.*;

import us.daveread.basicquery.images.ImageUtility;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 * @version $Id: ImageUtilityTest.java,v 1.1 2006/05/01 20:18:11 daveread Exp $
 */
public class ImageUtilityTest extends TestCase {
  private static final String image = "BasicQueryLogo32x32.gif";

  public ImageUtilityTest() {
  }

  public void testGetImageAsStream() {
    assertNotNull(ImageUtility.getImageAsStream(image));
    assertNull(ImageUtility.getImageAsStream("NoSuchImage"));
  }

  public void testGetImageAsByteArray() {
    assertNotNull(ImageUtility.getImageAsByteArray(image));
    assertNull(ImageUtility.getImageAsByteArray("NoSuchImage"));
  }

  public void testGetImageAsURL() {
    assertNotNull(ImageUtility.getImageAsURL(image));
    assertNull(ImageUtility.getImageAsURL("NoSuchImage"));
  }
}
