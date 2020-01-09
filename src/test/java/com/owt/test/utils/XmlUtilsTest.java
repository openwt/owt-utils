package com.owt.test.utils;

import com.owt.utils.XmlUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import javax.management.modelmbean.XMLParseException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.owt.test.ThrowableAssertion.assertThrown;
import static org.junit.Assert.*;

public final class XmlUtilsTest {
    private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<xml><persons><person><name>Yolo</name><surname>test</surname></person>"
            + "<person><name>Another</name><surname>test</surname></person></persons></xml>";

    @Test
    public void testParse() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);

        assertNotNull(document);
    }

    @Test
    public void testParse_invalidFilePath() {
        assertThrown(() -> XmlUtils.parse("invalid")).isInstanceOf(XMLParseException.class);
    }

    @Test
    public void testParse_invalidFile() {
        assertThrown(() -> XmlUtils.parse(new File("alsoInvalid"))).isInstanceOf(XMLParseException.class);
    }

    @Test
    public void testParseInputStream_invalidXML() {
        final InputStream is = new ByteArrayInputStream("<xml><xml>".getBytes());
        assertThrown(() -> XmlUtils.parse(is)).isInstanceOf(XMLParseException.class);

        final InputStream is2 = new ByteArrayInputStream("<xml><notClosedNode></xml>".getBytes());
        assertThrown(() -> XmlUtils.parse(is2)).isInstanceOf(XMLParseException.class);
    }

    @Test
    public void testParseInputStream_emptyXML() {
        final InputStream is = new ByteArrayInputStream("".getBytes());
        assertThrown(() -> XmlUtils.parse(is)).isInstanceOf(XMLParseException.class);
    }

    @Test
    public void testParseInputStream_nullXML() {
        final InputStream is = null;
        assertThrown(() -> XmlUtils.parse(is)).isInstanceOf(XMLParseException.class);
    }

    @Test
    public void testFindAllElement() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);

        assertNotNull(document);

        final List<Element> elements = XmlUtils.findAllElements(document, "//person");

        assertEquals(2, elements.size());

        final Element elem1 = elements.get(0);
        assertNotNull(elem1);
        assertEquals("Yolo", elem1.getChildText("name"));
        assertEquals("test", elem1.getChildText("surname"));

        final Element elem2 = elements.get(1);

        assertNotNull(elem2);
        assertEquals("Another", elem2.getChildText("name"));
        assertEquals("test", elem2.getChildText("surname"));
    }

    @Test
    public void testFindFirstElement() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);

        assertNotNull(document);

        final Element element = XmlUtils.findFirstElement(document, "//person");

        assertNotNull(element);
        assertEquals("Yolo", element.getChildText("name"));
        assertEquals("test", element.getChildText("surname"));

        // same query :)
        assertEquals(element, XmlUtils.findFirstElement(document, "//person[1]"));

    }

    @Test
    public void testFindFirstElement_NodeNotFound() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);
        assertNotNull(document);

        final Element element = XmlUtils.findFirstElement(document, "//is_not_exist");

        assertNull(element);
    }

    @Test
    public void testFindSecondElement() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);

        assertNotNull(document);

        final Element element = XmlUtils.findFirstElement(document, "//person[2]");

        assertNotNull(element);
        assertEquals("Another", element.getChildText("name"));
        assertEquals("test", element.getChildText("surname"));
    }

    @Test
    public void testFindFirstElementValue() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);

        assertNotNull(document);

        final String value = XmlUtils.findFirstElementValue(document, "//person/name");

        assertNotNull(value);
        assertEquals("Yolo", value);
    }

    @Test
    public void testFindFirstElementValue_NodeNotFound() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream(xml.getBytes());
        final Document document = XmlUtils.parse(is);
        assertNotNull(document);

        final String value = XmlUtils.findFirstElementValue(document, "//is_not_exist");

        assertNull(value);
    }

    @Test
    public void testFindFirstElementValue_EmptyNode() throws XMLParseException {
        final InputStream is = new ByteArrayInputStream("<xml><node1>test</node1><node2></node2></xml>".getBytes());
        final Document document = XmlUtils.parse(is);
        assertNotNull(document);

        final String value = XmlUtils.findFirstElementValue(document, "//node2");

        assertNotNull(value);
        assertEquals("", value);
    }

    @Test
    public void testParseATC() throws IOException, XMLParseException {
        final File article = new ClassPathResource("test.xml").getFile();
        assertTrue(article.exists());

        final Document document = XmlUtils.parse(article);

        assertNotNull(document);

        final Element element = XmlUtils.findFirstElement(document, "//allthecontent");

        assertNotNull(element);
        assertEquals("Desafiador, promotor dos EUA é demitido pelo governo Trump", XmlUtils.findFirstElementValue(element, "content/title"));

        assertEquals("58c5844dea13a7579298db50_58c5844dea13a7579298db52.txt", XmlUtils.findAttributeValue(element, "content/item/@filename"));
        assertEquals("58c5844dea13a7579298db50_58c5844dea13a7579298db52.txt", XmlUtils.findAttributeValue(element, "content/item[1]/@filename"));
        assertEquals("58c5844dea13a7579298db50_58c5844dea13a7579298db54.xhtml", XmlUtils.findAttributeValue(element, "content/item[2]/@filename"));

    }
}
