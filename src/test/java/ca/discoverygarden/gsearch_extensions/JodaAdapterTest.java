package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

public class JodaAdapterTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
		
		// Need to set the system time zone to GMT/UTC...
	}
	
	public void testMDY() {
		String source = "07/22/2013";
		String dest = "2013-07-22T00:00:00.000Z";
		
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testMDYHm() {
		String source = "07/22/2013 11:00";
		String dest = "2013-07-22T11:00:00.000Z";
		
		// XXX: Might not be correct... I'm kinda thinking the timezone offset might break things...
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testISODate() {
		String source = "2013-07-22";
		String dest = "2013-07-22T00:00:00.000Z";
		
		// XXX: Might not be correct... I'm kinda thinking the timezone offset might break things...
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testISODateTime() {
		String source = "2013-07-22T00:00Z";
		String dest = "2013-07-22T00:00:00.000Z";
		
		// XXX: Might not be correct... I'm kinda thinking the timezone offset might break things...
		assertEquals(dest, JodaAdapter.transformForSolr(source));
	}
	
	public void testUnparsable() {
		assertEquals("", JodaAdapter.transformForSolr("2222-22-22"));
	}
}
