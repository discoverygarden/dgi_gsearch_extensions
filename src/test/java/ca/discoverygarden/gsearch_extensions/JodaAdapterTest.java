package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

public class JodaAdapterTest extends TestCase {
	public void testMDY() {
		String source = "07/22/2013";
		String dest = "2013-07-22T00:00:00.000Z";
		
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testMDYHm() {
		String source = "07/22/2013 11:00";
		String dest = "2013-07-22T11:00:00.000Z";
		
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testISODate() {
		String source = "2013-07-22";
		String dest = "2013-07-22T00:00:00.000Z";
		
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testISODateTime() {
		String source = "2013-07-22T00:00Z";
		String dest = "2013-07-22T00:00:00.000Z";

		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testISODateTimeWithOffset() {
		String source = "2013-07-22T04:00+04:00";
		String dest = "2013-07-22T00:00:00.000Z";

		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testUnparsable() {
		assertEquals("", JodaAdapter.transformForSolr("2222-22-22"));
	}
}
