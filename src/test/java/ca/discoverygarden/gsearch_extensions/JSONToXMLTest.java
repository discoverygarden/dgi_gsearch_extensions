package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Tests that we can get XML from JSON.
 */
public class JSONToXMLTest extends TestCase {

  // Our test JSON we use twice.
  protected static final String testJson = "{\"thing_1\":{\"subthing_1.1\":\"value_1.1\",\"subthing_1.2\":\"value_1.2\"},\"thing_2\":\"value_2\"}";
  // Some test JSON with weird keys.
  protected static final String testWeirdKeyedJson = "{\"thing 1\":{\"sub/thing_1.1\":\"sub/value_1.1\",\"sub(thing)_1.2\":\"sub(value)_1.2\"},\"2thing_2\":\"2value_2\",\"34-_thing_3\":\"34-_value_3\"}";

  /**
   * Gets a document for an XML string.
   */
  protected Document getDocument(String xml) throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(xml.getBytes()));
  }

  /**
   * Asserts that XPath expressions return expected values.
   *
   * @param Document doc
   *   A document to test.
   * @param HashMap queries
   *   A HashMap pairing XPath query strings to test with their expected values.
   */
  protected void assertXPathValues(Document doc, HashMap<String, String> queries) throws XPathExpressionException {
    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();

    for (Map.Entry<String, String> query : queries.entrySet()) {
      XPathExpression expression = xpath.compile(query.getKey());
      assertEquals(expression.evaluate(doc), query.getValue());
    }
  }

  /**
   * Tests conversion with the default root element.
   */
  public void testDefaultRootElement() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    // Should be XML enclosed in a 'json' tag.
    Document transformed = getDocument(JSONToXML.convertJSONToXML(testJson));

    // Test some xpaths.
    HashMap<String, String> queries = new HashMap<String, String>();
    queries.put("/json/thing_1/subthing_1.1", "value_1.1");
    queries.put("/json/thing_1/subthing_1.2", "value_1.2");
    queries.put("/json/thing_2", "value_2");
    assertXPathValues(transformed, queries);
  }

  /**
   * Tests conversion with a custom root element.
   */
  public void testCustomRootElement() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    // Should be XML enclosed in a 'custom' tag.
    Document transformed = getDocument(JSONToXML.convertJSONToXML(testJson, "custom"));

    // Test some xpaths.
    HashMap<String, String> queries = new HashMap<String, String>();
    queries.put("/custom/thing_1/subthing_1.1", "value_1.1");
    queries.put("/custom/thing_1/subthing_1.2", "value_1.2");
    queries.put("/custom/thing_2", "value_2");
    assertXPathValues(transformed, queries);
  }

  /**
   * Tests conversion to a Document object.
   */
  public void testDocumentConversion() throws ParserConfigurationException, SAXException, IOException {
    Document transformed = JSONToXML.convertJSONToDocument(testJson);
    NodeList thingList = transformed.getElementsByTagName("thing_1");

    // We should have a thing_1 node in our loaded document.
    assertEquals(thingList.getLength(), 1);
  }

  /**
   * Tests conversion of JSON with keys containing invalid characters.
   *
   * Testing the removal of the following things from element names:
   *  - Non-alphabet characters, and non-underscores from the first character.
   *  - Non-alphanumeric characters, spaces, slashes, and parentheses from the
   *    remaining characters.
   */
  public void testWeirdKeyedJson() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    // Should be ... well-formed XML.
    Document transformed = getDocument(JSONToXML.convertJSONToXML(testWeirdKeyedJson));

    // Test some xpaths.
    HashMap<String, String> queries = new HashMap<String, String>();
    queries.put("/json/thing1/subthing_1.1", "sub/value_1.1");
    queries.put("/json/thing1/subthing_1.2", "sub(value)_1.2");
    queries.put("/json/thing_2", "2value_2");
    queries.put("/json/_thing_3", "34-_value_3");
    assertXPathValues(transformed, queries);
  }
}
