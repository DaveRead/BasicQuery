package us.daveread.basicquery.util;

/**
 * The file filter pattern definitions used within the program
 * 
 * @author David Read
 * 
 */
public enum FileFilterDefinition {
  /**
   * BasicQuery Query Files
   */
  QUERY_BQ("BasicQuery Files",
      new String[] {
          ".bq"
      }, true),

  /**
   * SQL Query Files
   */
  QUERY_SQL("SQL Files",
          new String[] {
            ".sql"
          }, false),

  /**
   * Text Files
   */
  QUERY_TXT("Text Files",
      new String[] {
        ".txt"
      }, false);

  /**
   * The description of the file filter
   */
  private final String description;

  /**
   * The accepted file name suffixes for the file filter
   */
  private final String[] acceptedSuffixes;

  /**
   * Is this the preferred option within its set of patterns
   */
  private boolean isPreferredOption;

  /**
   * Create a file filter
   * 
   * @param pDescription
   *          The description of the filter
   * @param pAcceptedSuffixes
   *          The accepted file name suffixes for the filter
   * @param pIsPreferredOption
   *          Is this the preferred filter within the related group of filters
   */
  FileFilterDefinition(String pDescription, String[] pAcceptedSuffixes,
      boolean pIsPreferredOption) {
    description = formatDescription(pDescription, pAcceptedSuffixes);
    acceptedSuffixes = pAcceptedSuffixes;
    isPreferredOption = pIsPreferredOption;
  }

  /**
   * Format the description to include a list of suffixes
   * 
   * @param pDescription
   *          The description
   * @param suffixes
   *          The file name suffixes this filter accepts
   *          
   * @return The description including suffixes
   */
  private String formatDescription(String pDescription, String[] suffixes) {
    String formatted = pDescription;

    if (suffixes != null && suffixes.length > 0) {
      formatted += " (";
      for (int index = 0; index < suffixes.length; ++index) {
        if (index > 0) {
          formatted = formatted + ", ";
        }
        formatted += suffixes[index];
      }
      formatted += ")";
    }

    return formatted;
  }

  /**
   * Get the description for the file filter
   * 
   * @return The file filter description
   */
  public String description() {
    return description;
  }

  /**
   * Get the accepted file name suffixes for the filter
   * 
   * @return The file name suffixes
   */
  public String[] acceptedSuffixes() {
    return acceptedSuffixes;
  }

  /**
   * Determine whether this is the preferred (default) option for a group of
   * file filters
   * 
   * @return True if this filter is preferred
   */
  public boolean isPreferredOption() {
    return isPreferredOption;
  }
}
