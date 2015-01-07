package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

public class XMLStringUtilsTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testDefaultReplacement() {
		// And arbitrary small collection of characters...
		String source = "\u0001\u0000\u000f\u000c";
		String dest = "\uFFFD\uFFFD\uFFFD\uFFFD";
		
		String transformed = XMLStringUtils.escapeForXML(source);
		
		assertEquals(dest, transformed);
	}
	
	public void testCustomReplacement() {
		// And arbitrary small collection of characters...
		String source = "\u0001\u0000\u000f\u000c";
		String dest = "asdfasdfasdfasdf";
		
		String transformed = XMLStringUtils.escapeForXML(source, "asdf");
		
		assertEquals(dest, transformed);
	}
	
	public void testHighCodepoint() {
		// A random high code point character... Its values should be maintained.
		String source = new String(Character.toChars(0x10190));
		String dest = source;
		
		String transformed = XMLStringUtils.escapeForXML(source, "");
		assertTrue(!transformed.isEmpty());
		assertNotSame(dest, transformed);
	}
}
