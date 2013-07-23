SUMMARY
-------

discoverygarden's GSearch extensions

Currently provides a thin wrapper around the Joda date/time library, in
order to more reliably transform dates for Solr.

REQUIREMENTS
------------

For build:
- Maven 2/3

INSTALLATION
------------

Build the extensions with `mvn package`, and copy the created jar into GSearch's
lib directory (`$CATALINA_HOME/webapps/fedoragsearch/WEB-INF/lib`).

CONFIGURATION
-------------


USAGE
-------------

In Xalan, it should be possible to call the code like:
```
<xsl:variable name="date_to_parse">08/13/2013</xsl:variable>
<xsl:variable xmlns:java="http://xml.apache.org/xalan/java"
  name="date"
  select="java:ca.discoverygarden.gsearch_extensions.JodaAdapter($date_to_parse)"/>
```

For better logging, one could also call like:
```
<xsl:variable xmlns:java="http://xml.apache.org/xalan/java"
  name="date"
  select="java:ca.discoverygarden.gsearch_extensions.JodaAdapter($date_to_parse, $pid, $datastream)"/>
```
where `$pid` and `$datastream` are the identifiers of the object and datastream, respectively.

CUSTOMIZATION
-------------


TROUBLESHOOTING
---------------


F.A.Q.
------


CONTACT
-------


SPONSORS
--------
