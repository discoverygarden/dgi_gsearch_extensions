package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

public class FedoraUtilsTest extends TestCase {

	/**
	 * Set that thing up.
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	/**
	 * Tests building a URL.
	 */
	public void testURLBuild() throws MalformedURLException {
		String pid = "test:pid";
		String dsId = "TEST_DSID";
		String fedoraBase = "http://localhost:8080/fedora";
		String fedoraUser = "test_user";
		String fedoraPass = "test_pass";
		URL url = FedoraUtils.getDatastreamDisseminationURL(pid, dsId, fedoraBase, fedoraUser, fedoraPass);
		
		assertEquals(url.getPort(), 8080);
		assertEquals(url.getPath(), "/fedora/objects/test:pid/datastreams/TEST_DSID/content");
	}

}
