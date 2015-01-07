package ca.discoverygarden.gsearch_extensions.string;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

/**
 * An XML 1.0 escaper/translator.
 * 
 * Grabbed the guts of org.apache.commons.lang3.StringEscapeUtils.ESCAPE_XML10,
 * but made the replacement string able to be specified in the constructor.
 */
public class ReplacingTranslator extends AggregateTranslator {
	public ReplacingTranslator(String replacement) { 
		super(
	            new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
	            new LookupTranslator(EntityArrays.APOS_ESCAPE()),
	            new LookupTranslator(
	                    new String[][] {
	                    		 { "\u0000", replacement },
	                             { "\u0001", replacement },
	                             { "\u0002", replacement },
	                             { "\u0003", replacement },
	                             { "\u0004", replacement },
	                             { "\u0005", replacement },
	                             { "\u0006", replacement },
	                             { "\u0007", replacement },
	                             { "\u0008", replacement },
	                             { "\u000b", replacement },
	                             { "\u000c", replacement },
	                             { "\u000e", replacement },
	                             { "\u000f", replacement },
	                             { "\u0010", replacement },
	                             { "\u0011", replacement },
	                             { "\u0012", replacement },
	                             { "\u0013", replacement },
	                             { "\u0014", replacement },
	                             { "\u0015", replacement },
	                             { "\u0016", replacement },
	                             { "\u0017", replacement },
	                             { "\u0018", replacement },
	                             { "\u0019", replacement },
	                             { "\u001a", replacement },
	                             { "\u001b", replacement },
	                             { "\u001c", replacement },
	                             { "\u001d", replacement },
	                             { "\u001e", replacement },
	                             { "\u001f", replacement },
	                             { "\ufffe", replacement },
	                             { "\uffff", replacement }
	                    }),
	            NumericEntityEscaper.between(0x7f, 0x84),
	            NumericEntityEscaper.between(0x86, 0x9f),
	            new UnicodeUnpairedSurrogateRemover()
	        );
	}
}
