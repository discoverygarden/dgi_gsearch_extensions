# DGI GSearch Extensions

## Introduction

discoverygarden's GSearch extensions, providing extended functionality available to GSearch XSLTs that would otherwise be extremely difficult or impossible to recreate in XSLT 1.0.

## Requirements

* [Apache Maven](https://maven.apache.org) 2 or 3 to build.

## Installation

Build the extensions with `mvn package`, and copy the created jar into GSearch's lib directory (`$CATALINA_HOME/webapps/fedoragsearch/WEB-INF/lib`).

If providing package libraries yourself, use gsearch_extensions-0.1.2.jar; otherwise use gsearch_extensions-0.1.2-jar-with-dependencies.jar.

## Usage

### Namespacing

Extensions are available to the java namespace of your XSLT parser. In Xalan, this should be:

`xmlns:java="http://xml.apache.org/xalan/java"`

From there, extension functions can be called at that namespace.

### Functions Available

#### `ca.discoverygarden.gsearch_extensions.JodaAdapter`

##### `transformForSolr($date, $pid, $datastream)`

Attempts to parse dates to a Solr-appropriate format in the following default set and order, assuming UTC if no timezone is provided:

* `M/d/y`, e.g., `7/23/2013` would become `2013-07-23T00:00:00.000Z`
* `M/d/y H:m`, e.g., `7/23/2013 11:36` would become `2013-07-23T11:36:00.000Z`
* ISO Date Time, as per [Joda-Time ISODateTimeFormat](http://joda-time.sourceforge.net/apidocs/org/joda/time/format/ISODateTimeFormat.html#dateTimeParser%28%29). Timezone offsets will be transformed off, so `2013-07-23T02:36-03:00` would be transformed to `2013-07-23T11:36:00.000Z`.

For example:

```
<xsl:variable name="date_to_parse">08/13/2013</xsl:variable>
<xsl:variable xmlns:java="http://xml.apache.org/xalan/java"
  name="date"
  select="java:ca.discoverygarden.gsearch_extensions.JodaAdapter.transformForSolr($date_to_parse)"/>
```

More parser formats can be added with `addDateParser()`. The list of formats can be reset with `resetParsers()`.

Variable|Description
--------|-----------
`$date`|A string date formatted like one of the `JodaAdapter`'s parsers.
`$pid`|(Optional) The string PID of the object being processed, for potential logging purposes.
`$datastream`|(Optional; required if `$pid` is given) The string datastream ID of the datastream being processed, for potential logging purposes.

##### `addDateParser($position, $format)`

Adds a parsing format pattern to the list of patterns to attempt when running `transformForSolr()`, optionally at the provided position.

Variable|Description
--------|-----------
`$position`|(Optional) An integer position to place the parser format at.
`$format`|A string format to add to the parser list, e.g., `Y-m-d`.

##### `resetParsers()`

Resets the list of parsers `transformForSolr()` will attempt when converting a date.

#### `ca.discoverygarden.gsearch_extensions.XMLStringUtils`

##### `escapeForXML($input, $replacement)`

Escapes a string for inclusion in XML, for example, in cases where the contents of a plaintext datastream are being provided to Solr.

The list of characters being replaced are based off of the Apache Commons lang3 library's [escapeXML10](https://commons.apache.org/proper/commons-lang/javadocs/api-3.5/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml10-java.lang.String-) list of replaced characters.

Variable|Description
--------|-----------
`$input`|The string to sanitize.
`$replacement`|(Optional) The string to use when replacing invalid characters; otherwise, invalid characters will be replaced with Unicode U+FFFD (the Unicode replacement character).

#### `ca.discoverygarden.gsearch_extensions.FedoraUtils`

##### `getDatastreamDisseminationInputStream($pid, $dsId, $fedoraBase, $fedoraUser, $fedoraPass)`

Gets the dissemination of a datastream as an InputStream object.

Variable|Description
--------|-----------
`$pid`|The PID of the object to get a datastream from.
`$dsId`|The ID of the datastream to get the dissemination for.
`$fedoraBase`|The base URL of Fedora, including the protocol; e.g., `http://localhost:8080/fedora`.
`$fedoraUser`|The username to log into Fedora with.
`$fedoraPass`|The password for the given username.

##### `getRawDatastreamDissemination($pid, $dsId, $fedoraBase, $fedoraUser, $fedoraPass)`

Gets the dissemination of a datastream as a string. Useful in cases where GSearch refuses to return the text of a datastream, i.e., most cases.

Variable|Description
--------|-----------
`$pid`|The PID of the object to get a datastream from.
`$dsId`|The ID of the datastream to get the dissemination for.
`$fedoraBase`|The base URL of Fedora, including the protocol; e.g., `http://localhost:8080/fedora`.
`$fedoraUser`|The username to log into Fedora with.
`$fedoraPass`|The password for the given username.

#### `ca.discoverygarden.gsearch_extensions.JSONToXML`

Note that when converting JSON to XML, JSON object keys are turned into element names. Keys are formatted to fit a strict set of appropriate element naming conventions; the first character is removed if not alphabetic or an underscore, and the remaining letters are removed if not alphanumeric, an underscore, a dash, or a period. Bear in mind that while colons are valid in names, they are stripped as the XML converter has no mechanism to specify namespaces.

##### `convertJSONToXML($input, $enclosing_tag)`

Converts a JSON string to an XML string.

Variable|Description
--------|-----------
`$input`|The input JSON string.
`$enclosing_tag`|(Optional) The top-level element to wrap resultant XML in, to prevent invalid XML from being written. If not provided, defaults to an element called 'json'.

##### `convertJSONToDocument($input, $enclosing_tag)`

Converts a JSON string to an XML Document object.

The resultant document can be interpreted as a Node-Set by Xalan, for example:

```
<xsl:variable name="some_json">{"something": "has content"}</xsl:variable>
<xsl:variable
    xmlns:java="http://xml.apache.org/xalan/java"
    name="some_xml"
    select="java:ca.discoverygarden.gsearch_extensions.JSONToXML.convertJSONToDocument($some_json)"/>
<!-- This will evaluate to "has content". -->
<xsl:variable name="node" select="$some_xml//something/text()">
```

Variable|Description
--------|-----------
`$input`|The input JSON string.
`$enclosing_tag`|(Optional) The top-level element to wrap resultant XML in, to prevent invalid XML from being written. If not provided, defaults to an element called 'json'.

## Troubleshooting/Issues

Having problems or solved a problem? Contact [discoverygarden](http://support.discoverygarden.ca).

## Maintainers/Sponsors

Current maintainers:

* [discoverygarden](http://www.discoverygarden.ca)

## Development

If you would like to contribute to this module, please check out our helpful
[Documentation for Developers](https://github.com/Islandora/islandora/wiki#wiki-documentation-for-developers)
info, [Developers](http://islandora.ca/developers) section on Islandora.ca and
contact [discoverygarden](http://support.discoverygarden.ca).
