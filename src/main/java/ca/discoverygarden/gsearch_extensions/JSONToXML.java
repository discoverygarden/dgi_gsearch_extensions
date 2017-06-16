package ca.discoverygarden.gsearch_extensions;

import org.json.JSONObject;
import ca.discoverygarden.gsearch_extensions.xml.ElementSafeJSONXML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.StringReader;
import java.io.IOException;

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
    return ElementSafeJSONXML.toString(json, enclosing_tag);
  }

  /**
   * Convert a JSON string to an XML document.
   *
   * @param in
   *   JSON to convert.
   *
   * @return
   *   XML document.
   */
  public static Document convertJSONToDocument(String in)
  throws ParserConfigurationException, SAXException, IOException
  {
    return convertJSONToDocument(in, "json");
  }

  /**
   * Convert a JSON string to an XML document.
   *
   * @param in
   *   JSON to convert.
   * @param enclosing_tag
   *   The tag to enclose the results in.
   *
   * @return
   *   XML Document.
   */
  public static Document convertJSONToDocument(String in, String enclosing_tag) 
  throws ParserConfigurationException, SAXException, IOException
  {
    StringReader xmlStringReader = new StringReader(convertJSONToXML(in, enclosing_tag));

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new InputSource(xmlStringReader));
  }
}
