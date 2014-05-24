package us.daveread.basicquery.util;

import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author David Read
 * @version $Id: Resources.java,v 1.4 2006/05/04 03:36:52 daveread Exp $
 */
public class Resources {
  private Resources() {
  }

  public static String getString(String key) {
    return ResourceBundle.getBundle(
        "us.daveread.basicquery.resourcebundles.BasicQueryResources").getString(
        key);
  }

  public static char getChar(String key) {
    return getString(key).charAt(0);
  }

  public static String getString(String key, Object[] args) {
    return MessageFormat.format(getString(key), args);
  }

  public static String getString(String key, String arg) {
    String[] args;

    args = new String[1];
    args[0] = arg;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2) {
    String[] args;

    args = new String[2];
    args[0] = arg1;
    args[1] = arg2;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2,
      String arg3) {
    String[] args;

    args = new String[3];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2,
      String arg3, String arg4) {
    String[] args;

    args = new String[4];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;
    args[3] = arg4;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2,
      String arg3, String arg4, String arg5) {
    String[] args;

    args = new String[5];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;
    args[3] = arg4;
    args[4] = arg5;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2,
      String arg3, String arg4, String arg5, String arg6) {
    String[] args;

    args = new String[6];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;
    args[3] = arg4;
    args[4] = arg5;
    args[5] = arg6;

    return getString(key, args);
  }

  public static String getString(String key, String arg1, String arg2,
      String arg3, String arg4, String arg5, String arg6, String arg7) {
    String[] args;

    args = new String[7];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;
    args[3] = arg4;
    args[4] = arg5;
    args[5] = arg6;
    args[6] = arg7;

    return getString(key, args);
  }
}
