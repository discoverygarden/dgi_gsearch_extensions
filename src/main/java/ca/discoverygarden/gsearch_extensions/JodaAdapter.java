package ca.discoverygarden.gsearch_extensions;

import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.format.*;

public class JodaAdapter {

	
	final static Vector<DateTimeFormatter> parsers = new Vector<DateTimeFormatter>();
	final static DateTimeFormatter isoFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
	static {
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
			catch (Exception e) {
				// TODO: Log something with a very low level... FINEST?
			}
		}
		
		// TODO: Dump some warning message to log.
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
}
