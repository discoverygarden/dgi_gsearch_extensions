package ca.discoverygarden.gsearch_extensions.xml;

import org.json.JSONException;

import org.json.XML;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A JSON to XML utility class.
 *
 * Facilites the conversion and normalization of XML to JSON and vice-versa.
 */
public class ElementSafeJSONXML extends XML {

  /**
   * Regex to match invalid first characters in an element name.
   *
   * Not the full set, but will do for now. Colons are being removed as we have
   * no mechanism for specifying namespace attributes.
   *
   * @see https://www.w3.org/TR/REC-xml/#NT-NameStartChar
   */
  protected static final String invalidFirstCharacterRegex = "^[^a-zA-Z_]+";

  /**
   * Regex to match invalid characters after the first in an element name.
   *
   * Not the full set, but will do for now. Colons are being removed as we have
   * no mechanism for specifying namespace attributes.
   *
   * @see https://www.w3.org/TR/REC-xml/#NT-NameChar
   */
  protected static final String invalidCharacterRegex = "[^a-zA-Z0-9\\-._]";

  /**
   * Converts a JSONObject to an XML string.
   *
   * The existing org.json.XML.toString() function does nothing to handle keys
   * which, while valid in JSON, are not valid as XML elements; it just naively
   * jams the key between triangle braces.
   *
   * Leeeeeet's fix it up.
   *
   * @param Object jsonObject
   *   A JSONObject.
   * @param String tagName
   *   An (optional) enclosing tag to place the converted JSON in.
   *
   * @throws JSONException
   *   If there is an error parsing the string.
   *
   * @return String
   *   The JSON as XML.
   */
  public static String toString(final Object object, final String tagName) throws JSONException {
    StringBuilder sb = new StringBuilder();
    JSONArray ja;
    JSONObject jo;
    String string;

    if (object instanceof JSONObject) {

      // Prepend with tagName.
      if (tagName != null) {
        sb.append('<');
        sb.append(tagName);
        sb.append('>');
      }

      // Loop through the keys.
      jo = (JSONObject) object;
      for (final String key : jo.keySet()) {
        // Sanitize the key using restricted version of the XML spec.
        final String sanitizedKey = key.replaceFirst(invalidFirstCharacterRegex).replaceAll(invalidCharacterRegex);

        // Get the value; convert if not JSONObject.
        Object value = jo.get(key);
        if (value == null) {
          value = "";
        } else if (value.getClass().isArray()) {
          value = new JSONArray(value);
        }

        // Emit content in body.
        if ("content".equals(key)) {
          if (value instanceof JSONArray) {
            ja = (JSONArray) value;
            int i = 0;
            for (Object val : ja) {
              if (i > 0) {
                sb.append('\n');
              }
              sb.append(escape(val.toString()));
              i++;
            }
          } else {
            sb.append(escape(value.toString()));
          }

        // Emit an array of similar keys.
        } else if (value instanceof JSONArray) {
          ja = (JSONArray) value;
          for (Object val : ja) {
            if (val instanceof JSONArray) {
              sb.append('<');
              sb.append(sanitizedKey);
              sb.append('>');
              sb.append(toString(val));
              sb.append("</");
              sb.append(sanitizedKey);
              sb.append('>');
            } else {
              sb.append(toString(val, sanitizedKey));
            }
          }
        // Give us a />'d version of the tag if the value is empty.
        } else if ("".equals(value)) {
          sb.append('<');
          sb.append(sanitizedKey);
          sb.append("/>");

        // Emit a new tag using the sanitized key.
        } else {
          sb.append(toString(value, sanitizedKey));
        }
      }

      // Close the tag if we must.
      if (tagName != null) {
        sb.append("</");
        sb.append(tagName);
        sb.append('>');
      }

      // Return the XML string we've built.
      return sb.toString();
    }

    // If this is a JSONArray, create an array of elements.
    if (object != null && (object instanceof JSONArray || object.getClass().isArray())) {
      if (object.getClass().isArray()) {
        ja = new JSONArray(object);
      } else {
        ja = (JSONArray) object;
      }
      for (Object val : ja) {
        // XML does not have good support for arrays. If an array
        // appears in a place where XML is lacking, synthesize an
        // <array> element.
        sb.append(toString(val, tagName == null ? "array" : tagName));
      }
      // Return the XML string we've built.
      return sb.toString();
    }

    // If this is just a string, we've hit the bottom of the iterator and can
    // just write an element.
    string = (object == null) ? "null" : escape(object.toString());
    return (tagName == null) ? "\"" + string + "\""
      : (string.length() == 0) ? "<" + tagName + "/>" : "<" + tagName
      + ">" + string + "</" + tagName + ">";
  }
}
