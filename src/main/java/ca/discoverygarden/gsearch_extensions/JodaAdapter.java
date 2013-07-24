package ca.discoverygarden.gsearch_extensions;

import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.format.*;

import org.apache.log4j.Logger;

public class JodaAdapter {
	final protected static Logger logger = Logger.getLogger(JodaAdapter.class);
	
	final protected static Vector<DateTimeFormatter> parsers = new Vector<DateTimeFormatter>();
	final protected static DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
	static {
		resetParsers();
	}
	
	/**
	 * Resets the list of parsers used to the base three.
	 */
	public static void resetParsers() {
		parsers.clear();
		
		// Setup base parsers.
		parsers.add(DateTimeFormat.forPattern("M/d/y").withZoneUTC());
		parsers.add(DateTimeFormat.forPattern("M/d/y H:m").withZoneUTC());
		parsers.add(ISODateTimeFormat.dateTimeParser().withZoneUTC());
	}
	
	/**
	 * Transform a date for use with Solr.
	 * 
	 * @param dateString
	 *   A string containing the date to format.
	 * @param pid
	 *   A string containing the PID related to this date.
	 * @param datastream
	 *   A string containing the datastream ID related to this date.
	 *   
	 * @return
	 *   A string containing the date as used by Solr, or an empty string if
	 *   parsing failed.
	 */
	public static String transformForSolr(String dateString, String pid, String datastream) {
		for (DateTimeFormatter i: parsers) {
			try {
				DateTime parsed = i.parseDateTime(dateString);
				return isoFormatter.print(parsed);
			}
			catch (IllegalArgumentException e) {
				// Eat the exception, we'll try the next.
			}
		}
		
		// Dump a warning message to log.
		logger.warn("Failed to transform \"" + dateString + "\" to something Solr understands. PID: \"" + pid + "\" DSID: \"" + datastream + "\"");
		return "";
	}
	
	/**
	 * Simpler call: Uses "null" for pid and datastream parameters.
	 * 
	 * @param dateString
	 *   The date string to transform for use in Solr.
	 *   
	 * @return
	 *   A string containing the date as used by Solr, or an empty string if
	 *   parsing failed.
	 */
	public static String transformForSolr(String dateString) {
		return JodaAdapter.transformForSolr(dateString, null, null);
	}
	
	/**
	 * Adds another parser to the end of the list of parser to try.
	 * 
	 * @param format
	 *   A format to attempt to parse, as accepted by
	 *   DateTimeFormat.forPattern().
	 */
	public static void addDateParser(String format) {
		parsers.add(DateTimeFormat.forPattern(format));
	}
	
	/**
	 * Adds another parser at the given offset.
	 * 
	 * @param position
	 *   The at which to attempt to put this parser. Other parsers will be
	 *   pushed back in the ordering.
	 * @param format
	 *   A format to attempt to parse, as accepted by
	 *   DateTimeFormat.forPattern().
	 */
	public static void addDateParser(int position, String format) {
		parsers.add(position, DateTimeFormat.forPattern(format));
	}
}
