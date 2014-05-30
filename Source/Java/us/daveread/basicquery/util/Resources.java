package us.daveread.basicquery.util;

import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * <p>
 * Title: Access resources for the application
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2004-2014
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author David Read
 */
public class Resources {
  /**
   * The package containing the resource bundles
   */
  private static final String RESOURCE_BUNDLE_PACKAGE = "us.daveread.basicquery.resourcebundles.BasicQueryResources";

  /**
   * No operation
   */
  private Resources() {
  }

  /**
   * Get the string associated with the supplied key using the current resource
   * bundle
   * 
   * @param key
   *          The key representing the text to return
   * 
   * @return The text for the supplied key using the current resource bundle
   */
  public static String getString(String key) {
    return ResourceBundle.getBundle(RESOURCE_BUNDLE_PACKAGE).getString(key);
  }

  /**
   * Get the first character of the supplied key
   * 
   * @param key
   *          The key
   * 
   * @return The first character of the key
   */
  public static char getChar(String key) {
    return getString(key).charAt(0);
  }

  /**
   * Get the parameterized string associated with the supplied key using the
   * current resource bundle and fill in the parameter values using the supplied
   * array of parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param args
   *          The parameter values to use to populate the parameterized string
   * 
   * @return The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
  public static String getString(String key, Object[] args) {
    return MessageFormat.format(getString(key), args);
  }

  /**
   * Convenience method to deal with string having one parameter
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg
   *          The single parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         value, using the current resource bundle
   */
  public static String getString(String key, String arg) {
    String[] args;

    args = new String[1];
    args[0] = arg;

    return getString(key, args);
  }

  /**
   * Convenience method to deal with string having two parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
  public static String getString(String key, String arg1, String arg2) {
    String[] args;

    args = new String[2];
    args[0] = arg1;
    args[1] = arg2;

    return getString(key, args);
  }

  /**
   * Convenience method to deal with string having three parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * @param arg3
   *          The third parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
  public static String getString(String key, String arg1, String arg2,
      String arg3) {
    String[] args;

    args = new String[3];
    args[0] = arg1;
    args[1] = arg2;
    args[2] = arg3;

    return getString(key, args);
  }

  /**
   * Convenience method to deal with string having four parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * @param arg3
   *          The third parameter value to use to populate the parameterized
   *          string
   * @param arg4
   *          The fourth parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
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

  /**
   * Convenience method to deal with string having five parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * @param arg3
   *          The third parameter value to use to populate the parameterized
   *          string
   * @param arg4
   *          The fourth parameter value to use to populate the parameterized
   *          string
   * @param arg5
   *          The fifth parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
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

  /**
   * Convenience method to deal with string having six parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * @param arg3
   *          The third parameter value to use to populate the parameterized
   *          string
   * @param arg4
   *          The fourth parameter value to use to populate the parameterized
   *          string
   * @param arg5
   *          The fifth parameter value to use to populate the parameterized
   *          string
   * @param arg6
   *          The sixth parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
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

  /**
   * Convenience method to deal with string having seven parameters
   * 
   * @param key
   *          The key representing the parameterized string to return
   * @param arg1
   *          The first parameter value to use to populate the parameterized
   *          string
   * @param arg2
   *          The second parameter value to use to populate the parameterized
   *          string
   * @param arg3
   *          The third parameter value to use to populate the parameterized
   *          string
   * @param arg4
   *          The fourth parameter value to use to populate the parameterized
   *          string
   * @param arg5
   *          The fifth parameter value to use to populate the parameterized
   *          string
   * @param arg6
   *          The sixth parameter value to use to populate the parameterized
   *          string
   * @param arg7
   *          The seventh parameter value to use to populate the parameterized
   *          string
   * 
   * @return
   *         The text for the supplied key, updated with the supplied parameter
   *         values, using the current resource bundle
   */
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
