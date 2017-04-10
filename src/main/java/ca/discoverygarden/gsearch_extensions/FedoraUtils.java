package ca.discoverygarden.gsearch_extensions;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utilities for interfacing with Fedora in ways that GSearch can't.
 */
public class FedoraUtils {

  protected static final Logger logger = Logger.getLogger(FedoraUtils.class);

  /**
   * Gets an InputStream for a datastream.
   *
   * @param pid
   *   The PID of the object that has the datastream.
   * @param dsId
   *   The ID of the datastream to get.
   * @param fedoraBase
   *   The base URL of fedora, e.g., localhost:8080/fedora.
   * @param fedoraUser
   *   The Fedora username to connect with.
   * @param fedoraPass
   *   The Fedora password to connect with.
   *
   * @return InputStream
   *   An InputStream at the constructed dissemination point.
   */
  public static InputStream getDatastreamDisseminationInputStream(String pid, String dsId, String fedoraBase, String fedoraUser, String fedoraPass) {
	  URL url;
	  // Attempt to generate the URL from input.
	  try {
		  url = getDatastreamDisseminationURL(pid, dsId, fedoraBase, fedoraUser, fedoraPass);
	      return url.openStream();
	  }
	  catch (MalformedURLException e) {
		  logger.warn(String.format("Attempt to generate URL for datastream dissemination failed: %s", e.getMessage()));
	  }
	  catch (IOException e) {
		  logger.warn(String.format("Failed to open stream: %s", e.getMessage()));
	  }
	  return new ByteArrayInputStream("".getBytes());
  }

  /**
   * Gets the raw text of a datastream.
   *
   * @param pid
   *   The PID of the object that has the datastream.
   * @param dsId
   *   The ID of the datastream to get.
   * @param fedoraBase
   *   The base URL of fedora, e.g., localhost:8080/fedora.
   * @param fedoraUser
   *   The Fedora username to connect with.
   * @param fedoraPass
   *   The Fedora password to connect with.
   *
   * @return String
   *   The text of the given datastream.
   */
  public static String getRawDatastreamDissemination(String pid, String dsId, String fedoraBase, String fedoraUser, String fedoraPass) {
	  try {
	      URL url = getDatastreamDisseminationURL(pid, dsId, fedoraBase, fedoraUser, fedoraPass);
	  	  URLConnection connection = url.openConnection();
	  	  InputStream dsStream = connection.getInputStream();
	  	  String encoding = connection.getContentEncoding();
	  	  encoding = encoding == null ? "UTF=8" : encoding;
	  	  return IOUtils.toString(dsStream, encoding);
	  } catch (MalformedURLException e) {
	      logger.warn(String.format("Attempt to generate URL for datastream dissemination failed: %s", e.getMessage()));
	  } catch (IOException e) {
		  logger.warn(String.format("Failed to open connection to datastream dissemination: %s", e.getMessage()));
	  }
      return "";
  }

  /**
   * Turns some parameters into a datastream dissemination URL.
   *
   * @return URL
   *   A URL object using the given parameters.
   */
  protected static URL getDatastreamDisseminationURL(String pid, String dsId, String fedoraBase, String fedoraUser, String fedoraPass) throws MalformedURLException {
	  String formattedUrl = String.format("http://%s:%s@%s/objects/%s/datastreams/%s/content", fedoraUser, fedoraPass, fedoraBase, pid, dsId);
	  return new URL(formattedUrl);
  }
}
