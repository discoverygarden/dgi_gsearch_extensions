package ca.discoverygarden.gsearch_extensions;

import org.json.JSONObject;
import org.json.XML;

public class JSONToXML {

  /**
   * Converts a JSON string to an XML string enclosed in a 'json' tag.
   *
   * @param in
   *   The JSON to convert.
   *
   * @return
   *   XML string.
   */
  public static String convertJSONToXML(String in) {
    return convertJSONToXML(in, "json");
  }

  /**
   * Convert a JSON string to an XML string.
   *
   * @param in
   *   The JSON to convert.
   * @param enclosing_tag
   *   The tag to enclose the results in.
   *
   * @return
   *   XML string. 
   */
  public static String convertJSONToXML(String in, String enclosing_tag) {
    JSONObject json = new JSONObject(in);
    return XML.toString(json, enclosing_tag);
  }
}
