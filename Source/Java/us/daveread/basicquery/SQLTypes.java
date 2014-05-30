package us.daveread.basicquery;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>Title: SQL Types</p>
 * <p>Description: Associates string representation of SQL data types to the java.sql.Types value</p>
 * <p>Copyright: Copyright (c) 2004-2014, David Read</p>
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
 * 
 * TODO Convert to Enumeration
 */

public class SQLTypes {
  /**
   * The type name
   */
  private String typeName;
  
  /**
   * The type id
   */
  private int typeId;
  
  /**
   * The set of known types
   */
  private static SQLTypes[] knownTypes;

  /**
   * The collection of known SQL data types
   */
  static {
    List<SQLTypes> allTypes;
    allTypes = new ArrayList<SQLTypes>();
    allTypes.add(new SQLTypes("BOOLEAN", java.sql.Types.BOOLEAN));
    allTypes.add(new SQLTypes("CHAR", java.sql.Types.CHAR));
    allTypes.add(new SQLTypes("DATE", java.sql.Types.DATE));
    allTypes.add(new SQLTypes("DECIMAL", java.sql.Types.DECIMAL));
    allTypes.add(new SQLTypes("DOUBLE", java.sql.Types.DOUBLE));
    allTypes.add(new SQLTypes("FLOAT", java.sql.Types.FLOAT));
    allTypes.add(new SQLTypes("INTEGER", java.sql.Types.INTEGER));
    allTypes.add(new SQLTypes("NULL", java.sql.Types.NULL));
    allTypes.add(new SQLTypes("STRING", java.sql.Types.VARCHAR));

    knownTypes = new SQLTypes[allTypes.size()];
    knownTypes = allTypes.toArray(new SQLTypes[0]);
  };

  /**
   * Create a SQLTypes instance
   *
   * @param pTypeName The string representation of the type
   * @param pTypeId The java.sql.Types value for the type
   */
  private SQLTypes(String pTypeName, int pTypeId) {
    typeName = pTypeName;
    typeId = pTypeId;
  }

  /**
   * Get the java.sql.Types value for the supplied type string representation
   *
   * @param pTypeName The string representation of the type
   * 
   * @return The java.sql.Types value associated with the supplied string representation
   */
  public static int getSQLTypeId(String pTypeName) {
    String typeName;
    int returnTypeId;
    int loop;

    typeName = pTypeName.toUpperCase();

    // Default type if we don't match a defined string
    returnTypeId = java.sql.Types.OTHER;

    for (loop = 0;
        loop < knownTypes.length && returnTypeId == java.sql.Types.OTHER;
        ++loop) {
      if (typeName.startsWith(knownTypes[loop].typeName)) {
        returnTypeId = knownTypes[loop].typeId;
      }
    }

    return returnTypeId;
  }

  /**
   * Return a String array of the known java.sql.Types that are defined
   *
   * @return The String array of known SQL data types
   */
  public static String[] getKnownTypeNames() {
    String[] knownTypeNames;

    knownTypeNames = new String[knownTypes.length];

    for (int loop = 0; loop < knownTypes.length; ++loop) {
      knownTypeNames[loop] = knownTypes[loop].typeName;
    }

    return knownTypeNames;
  }
}
