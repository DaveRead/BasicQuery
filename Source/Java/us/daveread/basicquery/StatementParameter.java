package us.daveread.basicquery;

/**
 * <p>Title: Statement parameter</p>
 * <p>Description: An individual parameter for a parameterized SQL statement</p>
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
 * @version $Id: StatementParameter.java,v 1.2 2006/04/20 03:11:21 daveread Exp $
 */

public class StatementParameter {
  public final static String ID =
      "$Id: StatementParameter.java,v 1.2 2006/04/20 03:11:21 daveread Exp $";

  public final static int IN = 0;
  public final static int OUT = 1;
  public final static int IN_OUT = 2;

  private int type;
  private int dataType;
  private String dataString;

  /**
   * Create a new StatementParameter instance.  This constructor is
   * typically used for OUT parameters, which have no initial value.
   *
   * @param aType The direction (In/Out) of the parameter
   * @param aDataType The java.sql.Types value for the parameter
   */
  public StatementParameter(int aType, int aDataType) {
    type = aType;
    dataType = aDataType;
  }

  /**
   * Create a new StatementParameter instance.  This constructor is
   * typicall used for IN parameters, which have an initial value.
   *
   * @param aType The direction (In/Out) of the parameter
   * @param aDataType The java.sql.Types value for the parameter
   * @param aDataString The data supplied for this parameter
   */
  public StatementParameter(int aType, int aDataType, String aDataString) {
    type = aType;
    dataType = aDataType;
    dataString = aDataString;
  }

  /**
   * Retrieve the direction (In/Out) of this parameter
   *
   * @return The direction of the parameter - constants in this class define the meaning
   */
  public int getType() {
    return type;
  }

  /**
   * Retrieve the java.sql.Types value for this parameter
   *
   * @return The SQL data type
   */
  public int getDataType() {
    return dataType;
  }

  /**
   * Retrieve the value associated with this parameter.  Only valid for
   * In or In-Out parameters.  Note that all parameter values are
   * treated as strings.  They are converted as necessary when the SQL is
   * executed.
   *
   * @return The value of the parameter
   */
  public String getDataString() {
    return dataString;
  }
}
