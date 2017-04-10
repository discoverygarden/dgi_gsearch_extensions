package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Tests that we can get XML from JSON.
 */
public class JSONToXMLTest extends TestCase {

  // Our test JSON we use twice.
  protected static final String testJson = "{\"thing_1\":{\"subthing_1.1\":\"value_1.1\",\"subthing_1.2\":\"value_1.2\"},\"thing_2\":\"value_2\"}";

  /**
   * Set up the thingy.
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Tests conversion with the default root element.
   */
  public void testDefaultRootElement() {
    // Should be XML enclosed in a 'json' tag.
    String dest = "<json><thing_1><subthing_1.2>value_1.2</subthing_1.2><subthing_1.1>value_1.1</subthing_1.1></thing_1><thing_2>value_2</thing_2></json>";
    String transformed = JSONToXML.convertJSONToXML(testJson);

    assertEquals(dest, transformed);
  }

  /**
   * Tests conversion with a custom root element.
   */
  public void testCustomRootElement() {
    // Should be XML enclosed in a 'custom' tag.
    String dest = "<custom><thing_1><subthing_1.2>value_1.2</subthing_1.2><subthing_1.1>value_1.1</subthing_1.1></thing_1><thing_2>value_2</thing_2></custom>";
    String transformed = JSONToXML.convertJSONToXML(testJson, "custom");

    assertEquals(dest, transformed);
  }
  
  /**
   * Tests conversion to a Document object.
   */
  public void testDocumentConversion() throws ParserConfigurationException, SAXException, IOException {
	Document transformed = JSONToXML.convertJSONToDocument(testJson);
	NodeList thingList = transformed.getElementsByTagName("thing_1");
	
	assertEquals(thingList.getLength(), 1);
  }
}
