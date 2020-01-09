package com.owt.utils;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.SAXHandler;
import org.jdom2.input.sax.SAXHandlerFactory;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.management.modelmbean.XMLParseException;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * XmlUtils, utility class for xml manipulation and parsing using the api JDOM2
 *
 * @author DBO Open Web Technology
 */
public final class XmlUtils {
    private static final SAXHandlerFactory FACTORY = factory -> new SAXHandler() {
        @Override
        public void startElement(
                final String namespaceURI, final String localName, final String qName, final Attributes attributes) throws SAXException {
            super.startElement("", localName, qName, attributes);
        }

        @Override
        public void startPrefixMapping(final String prefix, final String uri) {
            // do nothing
        }
    };
    private static final XPathFactory xpath = XPathFactory.instance();

    private XmlUtils() {

    }

    /**
     * Get a {@code SAXBuilder} that ignores namespaces.
     * Any namespaces present in the xml input to this builder will be omitted from the resulting
     * {@code Document}.
     */
    public static SAXBuilder getSAXBuilder() {
        // Note: SAXBuilder is NOT thread-safe, so we instantiate a new one for every call.
        final SAXBuilder saxBuilder = new SAXBuilder();
        saxBuilder.setSAXHandlerFactory(FACTORY);
        return saxBuilder;
    }

    public static Document parse(final String filePath) throws XMLParseException {
        return parse(new File(filePath));
    }

    public static Document parse(final File file) throws XMLParseException {
        try {
            return getSAXBuilder().build(file);
        } catch (final Exception e) {
            throw new XMLParseException(e, "Unable to parse the xml file");
        }
    }

    public static Document parse(final InputStream in) throws XMLParseException {
        try {
            return getSAXBuilder().build(in);
        } catch (final Exception e) {
            throw new XMLParseException(e, "Unable to parse the xml stream");
        }
    }

    public static List<Element> findAllElements(final Object document, final String xpathExpression) {
        return xpathQuery(xpathExpression).evaluate(document);
    }

    public static Element findFirstElement(final Object document, final String xpathExpression) {
        return xpathQuery(xpathExpression).evaluateFirst(document);

    }

    private static XPathExpression<Element> xpathQuery(final String xpathExpression) {
        return xpath.compile(xpathExpression, Filters.element());
    }

    public static String findFirstElementValue(final Object document, final String xpathExpression) {
        final Element element = findFirstElement(document, xpathExpression);
        return element != null ? element.getTextTrim() : null;
    }

    public static String findAttributeValue(final Object document, final String xpathExpression) {
        final Attribute attr = (Attribute) xpath.compile(xpathExpression).evaluateFirst(document);
        return attr != null && attr.getValue() != null ? attr.getValue().trim() : null;
    }

}
