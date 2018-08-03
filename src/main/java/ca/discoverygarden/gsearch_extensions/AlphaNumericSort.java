package ca.discoverygarden.gsearch_extensions;

/**
 * Prepare a string with tokens for alphanumeric sorting.
 *
 * This is a sort of workaround for the fact that we're using two older versions
 * of tools that otherwise would give us the functionality we desire, namely:
 *
 * * Solr 4.10 or less doesn't give us the ability to control the query slop, so
 *   we can't lock down ordering of tokens when sorting. The best solution here
 *   is to perform our alphanumeric sort padding at index time by providing the
 *   index with a sortable "tokenized" string.
 * * XSLT 1.0 doesn't give us a clean way of splitting and re-assembling a
 *   string; the "standard" method is to do it with recursive templates, which
 *   are barely even acceptable when you have a guaranteed recursion depth.
 */
public class AlphaNumericSort {

  /**
   * Left-pad each numeric part of a string using defaults.
   *
   * @param in
   *   The string to left-pad.
   *
   * @return
   *   The string with left-padded tokens.
   */
  public static String leftPadTokens(String in) {
    String delimiters = getDefaultDelimiters();
    return leftPadTokens(in, 5, delimiters);
  }

  /**
   * Left-pad each numeric part of a string using given delimiters.
   *
   * @param in
   *   The string to left-pad.
   * @param delimiters
   *   A regular expression representing the delimiters to split on.
   *
   * @return
   *   The string with left-padded tokens.
   */
  public static String leftPadTokens(String in, String delimiters) {
    return leftPadTokens(in, 5, delimiters);
  }

  /**
   * Left-pad each numeric part of a string using a given number of zeroes.
   *
   * @param in
   *   The string to left-pad.
   * @param zeroes
   *   The number of zeroes to left-pad the string with.
   *
   * @return
   *   The string with left-padded tokens.
   */
  public static String leftPadTokens(String in, Integer zeroes) {
    String delimiters = getDefaultDelimiters();
    return leftPadTokens(in, zeroes, delimiters);
  }

  /**
   * Left-pad each numeric part of a string with zeroes using given delimiters.
   *
   * @param in
   *   The string to left-pad.
   * @param zeroes
   *   The number of zeroes to left-pad the string with.
   * @param delimiters
   *   A regular expression representing the delimiters to split on.
   *
   * @return
   *   The string with left-padded tokens.
   */
  public static String leftPadTokens(String in, Integer zeroes, String delimiters) {
    // If the string is empty, just return it.
    if (in.isEmpty()) {
      return in;
    }
    // Build the string of zeroes to be appended to each token.
    StringBuilder zero_string = new StringBuilder();
    for (Integer i = 0; i < zeroes; i++) {
      zero_string.append("0");
    }
    String[] tokens = in.split(delimiters);
    StringBuilder padded = new StringBuilder();
    for (String token : tokens) {
      // Only left-pad tokens that start with a digit.
      if (Character.isDigit(token.charAt(0))) {
        padded.append(zero_string.toString());
      }
      padded.append(token);
    }
    return padded.toString();
  }

  /**
   * Gets the default delimiter regex for use with this string.
   *
   * This includes a single space, /, -, and _.
   */
  private static String getDefaultDelimiters() {
    return "/|-|_| ";
  }
}
