package us.daveread.basicquery;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

/**
 * <p>Title: Dynamic class loader</p>
 * <p>Description: Dynamically load classes</p>
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
 * @version $Id: DynamicClassLoader.java,v 1.3 2006/05/05 00:40:48 daveread Exp $
 */

public class DynamicClassLoader extends ClassLoader {
  public final static String ID =
      "$Id: DynamicClassLoader.java,v 1.3 2006/05/05 00:40:48 daveread Exp $";

  private static final Logger logger = Logger.getLogger(DynamicClassLoader.class);

  private Map cachedClasses;
  private URLClassLoader classLoader;

  /**
   * Create a DynamicClassLoader instance from a list of file paths
   * @param archives A list of file paths
   */
  public DynamicClassLoader(List archives) {
    cachedClasses = new HashMap();
    setupClassLoader(archives);
  }

  /**
   * Add the files to the class loader
   * @param archives The list of file paths to class libraries
   */
  public void setupClassLoader(List archives) {
    List urls;
    String fileName;

    urls = new ArrayList();
    for (int a = 0; a < archives.size(); ++a) {
      try {
        fileName = ((File)archives.get(a)).getCanonicalPath();
        if (fileName.indexOf(":") > 0) {
          fileName = fileName.substring(fileName.indexOf(":") + 1);
        }
        fileName = fileName.replace('\\', '/');

        logger.info("Add to classpath " + fileName);
        urls.add(new URL("jar:file:" + fileName + "!/"));
      }
      catch (MalformedURLException mal) {
        logger.error("Error with archive URL", mal);
      }
      catch (IOException io) {
        logger.error("Failure loading archive", io);
      }

      classLoader = new URLClassLoader((URL[])urls.toArray(new URL[0]));
    }
  }

  /**
   * Load an individual class from the set of loaded packages
   * @param className The class being sought
   * @throws ClassNotFoundException
   * @return The loaded class
   */
  public synchronized Class loadClass(String className) throws
      ClassNotFoundException {
    return loadClass(className, false);
  }

  public synchronized Class loadClass(String className, boolean resolveIt) throws
      ClassNotFoundException {
    Class loadedClass;

    logger.info("Load class [" + className + "]");

    loadedClass = (Class)cachedClasses.get(className);

    if (loadedClass == null) {
      try {
        loadedClass = super.findSystemClass(className);
      }
      catch (ClassNotFoundException exc) {
        // Ignore, try and load it below
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
