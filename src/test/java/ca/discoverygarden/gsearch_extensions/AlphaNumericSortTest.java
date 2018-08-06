package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

/**
 * Tests that we can build an alphanumeric tokenized string.
 */
public class AlphaNumericSortTest extends TestCase {

  // A non-standard delimiter; delimit on the letters X and Y.
  protected static final String nonStandardDelimiter = "X|Y";
  // The string we're going to attempt to chop up multiple times. The
  // expectation is that only parts starting with numbers are padded, and the
  // last portion ending in "5PartXPartY7Part" will only be delimited when using
  // the non-standard delimiter.
  protected static final String toSort = "0Part-1Part Part_4Part/5PartXPartY7Part";

  /**
   * Tests that the default delimiter/length produce an expected string.
   */
  public void testDefaultDelimiterAndLengthPadding() {
    String padded = AlphaNumericSort.leftPadTokens(toSort);
    assertEquals(padded, "000000Part000001PartPart000004Part000005PartXPartY7Part");
  }

  /**
   * Tests that a custom count of zeroes is respected.
   */
  public void testZeroCountPadding() {
    String padded = AlphaNumericSort.leftPadTokens(toSort, 2);
    assertEquals(padded, "000Part001PartPart004Part005PartXPartY7Part");
  }

  /**
   * Tests that a custom delimiter is respected.
   */
  public void testCustomDelimiterPadding() {
    String padded = AlphaNumericSort.leftPadTokens(toSort, nonStandardDelimiter);
    assertEquals(padded, "000000Part-1Part Part_4Part/5PartPart000007Part");
  }

  /**
   * Tests that a custom delimiter and zero count is respected.
   */
  public void testCustomDelimiterAndZeroCountPadding() {
    String padded = AlphaNumericSort.leftPadTokens(toSort, 2, nonStandardDelimiter);
    assertEquals(padded, "000Part-1Part Part_4Part/5PartPart007Part");
  }

  /**
   * Tests that an empty string is ignored.
   */
  public void testEmptyString() {
    String padded = AlphaNumericSort.leftPadTokens("");
    assertEquals(padded, "");
  }

}
