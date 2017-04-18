package ca.discoverygarden.gsearch_extensions;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Scanner;

/**
 * Utilities for interfacing with Fedora in ways that GSearch can't.
 */
public class FedoraUtils {

  protected static final Logger logger = Logger.getLogger(FedoraUtils.class);

  /**
   * Authenticator for connecting to Fedora.
   */
  static class FedoraAuthenticator extends Authenticator {

    protected String fedoraUser;
    protected String fedoraPass;

    /**
     * Constructor; sets the username and password for this authenticator.
     *
     * @param username
     *   The username to set.
     * @param password
     *   The password to set.
     */
    FedoraAuthenticator(String username, String password) {
      fedoraUser = username;
      fedoraPass = password;
    }

    /**
     * Overloaded password authenticator.
     *
     * @return PasswordAuthentication
     *   Authentication for this authenticator.
     */
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(fedoraUser, fedoraPass.toCharArray());
    }

  }

  /**
   * Gets an InputStream for a datastream.
   *
   * @param pid
   *   The PID of the object that has the datastream.
   * @param dsId
   *   The ID of the datastream to get.
   * @param fedoraBase
   *   The base URL of fedora, including protocol; e.g., 
   *   http://localhost:8080/fedora.
   * @param fedoraUser
   *   The Fedora username to connect with.
   * @param fedoraPass
   *   The Fedora password to connect with.
   *
   * @return InputStream
   *   An InputStream at the constructed dissemination point.
   */
  public static InputStream getDatastreamDisseminationInputStream(String pid, String dsId, String fedoraBase, String fedoraUser, String fedoraPass) {
    // Set the authenticator.
    FedoraAuthenticator auth = new FedoraAuthenticator(fedoraUser, fedoraPass);
    Authenticator.setDefault(auth);
    // Attempt to generate the URL from input.
    try {
      URL url = getDatastreamDisseminationURL(pid, dsId, fedoraBase);
      return url.openStream();
    }
    // On exception, log and return a stream with no content so the caller
    // doesn't get messed up.
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
   *   The base URL of fedora, including protocol; e.g.,
   *   http://localhost:8080/fedora.
   * @param fedoraUser
   *   The Fedora username to connect with.
   * @param fedoraPass
   *   The Fedora password to connect with.
   *
   * @return String
   *   The text of the given datastream.
   */
  public static String getRawDatastreamDissemination(String pid, String dsId, String fedoraBase, String fedoraUser, String fedoraPass) {
    InputStream dsStream = getDatastreamDisseminationInputStream(pid, dsId, fedoraBase, fedoraUser, fedoraPass);
    // Scan the InputStream, delimited by the start of string marker, so the
    // whole string gets scanned.
    Scanner scanner = new Scanner(dsStream).useDelimiter("\\A");
    // If no content, return an empty string.
    String dsString = scanner.hasNext() ? scanner.next() : "";
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("getRawDatastreamDissemination (pid: %s, DSID: %s): %s", pid, dsId, dsString));
    }
    return dsString;
  }

  /**
   * Turns some parameters into a datastream dissemination URL.
   *
   * @return URL
   *   A URL object using the given parameters.
   */
  protected static final URL getDatastreamDisseminationURL(String pid, String dsId, String fedoraBase) throws MalformedURLException {
    // Build the URL.
    String url = String.format("%s/objects/%s/datastreams/%s/content", fedoraBase, pid, dsId);
    if (logger.isDebugEnabled()) {
      logger.debug(String.format("Building URL for %s", url));
    }
    return new URL(url);
  }
}
