package de.escidoc.ingest.test;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.properties.ContentModelSpecific;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ResourceUtility {
    
    /**
     * Get md record with provided name.
     * 
     * @param name
     *            Name of md-record
     * @return md-record
     * @throws ParserConfigurationException
     *             Thrown if instance of DocumentBuiler failed to create.
     */
    public static MetadataRecord getMdRecord(final String name) throws ParserConfigurationException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.newDocument();

        final MetadataRecord mdRecord = new MetadataRecord(name);

        final Element element = doc.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        return mdRecord;
    }
    
    /**
     * Prepare data for content model specific.
     * 
     * @return ContentModelSpecific with some content
     * @throws ParserConfigurationException
     */
    public static ContentModelSpecific getContentModelSpecific() throws ParserConfigurationException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.newDocument();

        final Element contentModelSpecific = doc.createElementNS(null, "cms");
        final Element element1 = doc.createElement("some-other-stuff");
        element1.setTextContent("some content - " + System.nanoTime());

        final List<Element> cmsContent = new LinkedList<Element>();
        cmsContent.add(contentModelSpecific);
        cmsContent.add(element1);

        final ContentModelSpecific cms = new ContentModelSpecific();
        cms.setContent(cmsContent);

        return cms;
    }
}
