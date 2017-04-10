package ca.discoverygarden.gsearch_extensions;

import org.apache.log4j.Logger;

import dk.defxws.fedoragsearch.server.GenericOperationsImpl;

import org.fcrepo.client.FedoraClient;
import org.fcrepo.server.access.FedoraAPIA;
import org.fcrepo.server.types.gen.Datastream;
import org.fcrepo.server.types.gen.MIMETypedStream;

/**
 * Utilities for interfacing with Fedora in ways that GSearch can't.
 */
public class FedoraUtils {

  protected static final Logger logger = Logger.getLogger(FedoraUtils.class);

  /**
   * Gets the raw text of a datastream.
   */
  public static String getRawDatastreamText(String pid, String repositoryName, String dsId, String fedoraSoap, String fedoraUser, String fedoraPass, String trustStorePath, String trustStorePass) {
    dsBuffer = new StringBuffer();
    FedoraAPIA apia = GenericOperationsImpl.getAPIA(repositoryName, fedoraSoap, fedoraUser, fedoraPass, trustStorePath, trustStorePass);
    // Get the DS; getDatastreamDissemination could be null.
    MIMETypedStream dsStream = apia.getDatastreamDissemination(getRealPID(pid), dsId, null);
    if (dsStream == null) {
      logger.warn(String.format("Failed to get datastream dissemination of '%s' for '%s'", dsId, pid));
      return "";
    }
    // Get the stream.
    ds = dsStream.getStream();
    if (ds == null) {
      logger.warn(String.format("Failed to get stream from '%s' for '%s'", dsId, pid));
      return "";
    }
  }
}
