package ca.discoverygarden.gsearch_extensions;

import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

public class FedoraUtilsTest extends TestCase {

  /**
   * Tests building a URL.
   */
  public void testURLBuild() throws MalformedURLException {
    String pid = "test:pid";
    String dsId = "TEST_DSID";
    String fedoraBase = "http://localhost:8080/fedora";
    URL url = FedoraUtils.getDatastreamDisseminationURL(pid, dsId, fedoraBase);

    assertEquals(url.getPort(), 8080);
    assertEquals(url.getPath(), "/fedora/objects/test:pid/datastreams/TEST_DSID/content");
  }

}
