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

If you want to include Joda Time manually use gsearch_extensions-0.1.0.jar otherwise use 
gsearch_extensions-0.1.0-jar-with-dependencies.jar

CONFIGURATION
-------------


USAGE
-------------

In Xalan, it should be possible to call the code like:
```
<xsl:variable name="date_to_parse">08/13/2013</xsl:variable>
<xsl:variable xmlns:java="http://xml.apache.org/xalan/java"
  name="date"
  select="java:ca.discoverygarden.gsearch_extensions.JodaAdapter.transformForSolr($date_to_parse)"/>
```

For better logging, one could also call like:
```
<xsl:variable xmlns:java="http://xml.apache.org/xalan/java"
  name="date"
  select="java:ca.discoverygarden.gsearch_extensions.JodaAdapter.transformForSolr($date_to_parse, $pid, $datastream)"/>
```
where `$pid` and `$datastream` are the identifiers of the object and datastream, respectively.

The three base parsers assume that they are given dates in UTC if no timezone is provided, and are attempted in order:
- `M/d/y`
  So `7/23/2013` should result in `2013-07-23T00:00:00.000Z`.
- `M/d/y H:m`
  So `7/23/2013 11:36` should result in `2013-07-23T11:36:00.000Z`.
- ISO Date Time, as per http://joda-time.sourceforge.net/apidocs/org/joda/time/format/ISODateTimeFormat.html#dateTimeParser%28%29
  Timezone offsets will be transformed off, so `2013-07-23T02:36-03:00` will be transformed to `2013-07-23T11:36:00.000Z`.

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
