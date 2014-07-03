package us.daveread.basicquery;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Dynamic class loader
 * </p>
 * <p>
 * Description: Dynamically load classes
 * </p>
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

public class DynamicClassLoader extends ClassLoader {
  /**
   * Logger
   */
  private static final Logger LOGGER = Logger
      .getLogger(DynamicClassLoader.class);

  /**
   * Cache of loaded classes
   */
  private Map<String, Class<?>> cachedClasses;

  /**
   * URL class loader instance
   */
  private URLClassLoader classLoader;

  /**
   * Create a DynamicClassLoader instance from a list of file paths
   * 
   * @param archives
   *          A list of file paths
   */
  public DynamicClassLoader(List<File> archives) {
    cachedClasses = new HashMap<String, Class<?>>();
    setupClassLoader(archives);
  }

  /**
   * Add the files to the class loader
   * 
   * @param archives
   *          The list of file paths to class libraries
   */
  public void setupClassLoader(List<File> archives) {
    List<URL> urls;
    String fileName;

    urls = new ArrayList<URL>();
    for (int a = 0; a < archives.size(); ++a) {
      try {
        fileName = (archives.get(a)).getCanonicalPath();
        //if (fileName.indexOf(":") > 0) {
        //  fileName = fileName.substring(fileName.indexOf(":") + 1);
        //}
        fileName = fileName.replace('\\', '/');

        LOGGER.info("Add to classpath " + fileName);
        urls.add(new URL("jar:file:" + fileName + "!/"));
      } catch (MalformedURLException mal) {
        LOGGER.error("Error with archive URL", mal);
      } catch (IOException io) {
        LOGGER.error("Failure loading archive", io);
      }

      classLoader = new URLClassLoader(urls.toArray(new URL[0]));
    }
  }

  @Override
  public synchronized Class<?> loadClass(String className) throws
      ClassNotFoundException {
    return loadClass(className, false);
  }

  @Override
  public synchronized Class<?> loadClass(String className, boolean resolveIt)
      throws
      ClassNotFoundException {
    Class<?> loadedClass;

    LOGGER.info("Load class [" + className + "]");

    loadedClass = cachedClasses.get(className);

    if (loadedClass == null) {
      try {
        loadedClass = super.findSystemClass(className);
      } catch (ClassNotFoundException exc) {
        // Ignore, try and load it below
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug(
              "Class not found on system classpath, look in local files", exc);
        }
      }
    }

    if (loadedClass == null) {
      loadedClass = classLoader.loadClass(className);
    }

    if (loadedClass == null) {
      throw new ClassNotFoundException(className);
    }

    if (resolveIt) {
      resolveClass(loadedClass);
    }

    cachedClasses.put(className, loadedClass);

    return loadedClass;
  }
}
