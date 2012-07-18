package de.escidoc.ingest.test;

import junit.framework.TestCase;
import org.junit.Test;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.rest.RestContainerHandlerClient;
import de.escidoc.core.client.rest.RestIngestHandlerClient;
import de.escidoc.core.client.rest.RestItemHandlerClient;
import de.escidoc.core.common.jibx.Marshaller;
import de.escidoc.core.common.jibx.MarshallerFactory;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.properties.ContentModelSpecific;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.om.container.ContainerProperties;
import de.escidoc.core.resources.om.item.Item;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IngestIT extends TestCase {

    private Authentication auth;

    @Before
    public void setUp() throws Exception {
        auth = new Authentication(new URL("http://localhost:8080"), 
                                  "sysadmin", "escidoc");
    }

    @After
    public void tearDown() throws Exception {
        if (auth != null) {
            auth.logout();
        }
    }
    
    @Test
    public void testIngestItem() throws Exception {
        final RestItemHandlerClient ihc = new RestItemHandlerClient(auth.getServiceAddress());
        ihc.setHandle(auth.getHandle());
        
        final Marshaller<Item> m = MarshallerFactory.getInstance().getMarshaller(Item.class);

        final Item item = new Item();
        item.getProperties().setContext(new ContextRef(EscidocClientTestBase.getStaticContextId()));
        item.getProperties().setContentModel(new ContentModelRef(EscidocClientTestBase.getStaticContentModelId()));
        
        // Content-model
        final ContentModelSpecific cms = ResourceUtility.getContentModelSpecific();
        item.getProperties().setContentModelSpecific(cms);
        
        // Metadata Record(s)
        final MetadataRecords mdRecords = new MetadataRecords();
        final MetadataRecord mdrecord = ResourceUtility.getMdRecord("escidoc");
        mdRecords.add(mdrecord);
        item.setMetadataRecords(mdRecords);
        
        // create
        final String resultXml = ihc.create(m.marshalDocument(item));
        
        final Item createdItem = m.unmarshalDocument(resultXml);

        final String objId = createdItem.getObjid();
        
        // organize Item
        final String itemXml = ihc.retrieve(objId);
        
        // ingest Item
        final RestIngestHandlerClient inhc = new RestIngestHandlerClient(auth.getServiceAddress());
        inhc.setHandle(auth.getHandle());
        
        inhc.ingest(itemXml);
    }
    
    /**
     * Test ingesting a Container.
     * 
     * @throws Exception
     *             Thrown if no or wrong exception is caught from the framework.
     */
    @Test
    public void testIngestContainer1() throws Exception {
        final RestContainerHandlerClient cc = new RestContainerHandlerClient(auth.getServiceAddress());
        cc.setHandle(auth.getHandle());

        final Marshaller<Container> m = MarshallerFactory.getInstance().getMarshaller(Container.class);

        final Container container = new Container();

        // properties
        final ContainerProperties properties = new ContainerProperties();
        properties.setContext(new ContextRef(EscidocClientTestBase.getStaticContextId()));
        properties.setContentModel(new ContentModelRef(EscidocClientTestBase.getStaticContentModelId()));

        // Content-model-specific
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.newDocument();
        final Element contentModelSpecific = doc.createElementNS(null, "cms");

        final List<Element> cmsContent = new LinkedList<Element>();
        cmsContent.add(contentModelSpecific);
        final ContentModelSpecific cms = ResourceUtility.getContentModelSpecific();

        cms.setContent(cmsContent);

        properties.setContentModelSpecific(cms);
        container.setProperties(properties);

        final MetadataRecords mdRecords = new MetadataRecords();
        final MetadataRecord mdRecord = new MetadataRecord("escidoc");

        final Document docMdRecord = builder.newDocument();
        final Element element = docMdRecord.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        mdRecords.add(mdRecord);
        container.setMetadataRecords(mdRecords);

        final String resultXml = cc.create(m.marshalDocument(container));

        final Container createdContainer = m.unmarshalDocument(resultXml);
        final String objId = createdContainer.getObjid();

        // organize Container
        final String containerXml = cc.retrieve(objId);

        // ingest Item
        final RestIngestHandlerClient rihc = new RestIngestHandlerClient(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        rihc.ingest(containerXml);
    }
    
    @Test
    public void testIngestContainer2() throws Exception {
        final RestContainerHandlerClient cc = new RestContainerHandlerClient(auth.getServiceAddress());
        cc.setHandle(auth.getHandle());

        final Marshaller<Container> m = MarshallerFactory.getInstance().getMarshaller(Container.class);

        final Container container = new Container();

        // properties
        final ContainerProperties properties = new ContainerProperties();
        properties.setContext(new ContextRef(EscidocClientTestBase.getStaticContextId()));
        properties.setContentModel(new ContentModelRef(EscidocClientTestBase.getStaticContentModelId()));

        // Content-model-specific
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.newDocument();
        final Element contentModelSpecific = doc.createElementNS(null, "cms");

        final List<Element> cmsContent = new LinkedList<Element>();
        cmsContent.add(contentModelSpecific);
        final ContentModelSpecific cms = ResourceUtility.getContentModelSpecific();

        cms.setContent(cmsContent);

        properties.setContentModelSpecific(cms);
        container.setProperties(properties);

        final MetadataRecords mdRecords = new MetadataRecords();
        final MetadataRecord mdRecord = new MetadataRecord("escidoc");

        final Document docMdRecord = builder.newDocument();
        final Element element = docMdRecord.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        mdRecords.add(mdRecord);
        container.setMetadataRecords(mdRecords);

        final String resultXml = m.marshalDocument(container);

        // ingest Item
        final RestIngestHandlerClient rihc = new RestIngestHandlerClient(auth.getServiceAddress());
        rihc.setHandle(auth.getHandle());

        rihc.ingest(resultXml);
    }
}
