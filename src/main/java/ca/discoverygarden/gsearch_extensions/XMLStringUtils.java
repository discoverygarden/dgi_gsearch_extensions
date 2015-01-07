package ca.discoverygarden.gsearch_extensions;

import ca.discoverygarden.gsearch_extensions.string.ReplacingTranslator;

public class XMLStringUtils {
	/**
	 * Escape a string for inclusion in XML.
	 * 
	 * Invalid characters will be replaced with U+FFFD ("replacement
	 * character") to indicate a replacement has taken place.
	 * 
	 * @param in
	 *   The string to escape.
	 *   
	 * @return
	 *   The escaped string.
	 */
	public static String escapeForXML(String in) {
		return escapeForXML(in, "\uFFFD");
	}
	
	/**
	 * Escape a string for inclusion in XML.
	 * 
	 * @param in
	 *   The string to escape.
	 * @param replacement
	 *   The string with which to replace invalid characters.
	 *   
	 * @return
	 *   The escaped string.
	 */
	public static String escapeForXML(String in, String replacement) {
		ReplacingTranslator translator = new ReplacingTranslator(replacement);
		return translator.translate(in);
	}
}
