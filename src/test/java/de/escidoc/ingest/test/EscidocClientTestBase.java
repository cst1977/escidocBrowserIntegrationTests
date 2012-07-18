package de.escidoc.ingest.test;

import de.escidoc.core.client.AdminHandlerClient;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.adm.LoadExamplesResult.Entry;
import de.escidoc.core.resources.common.MessagesResult;
import java.net.MalformedURLException;
import java.net.URL;

public class EscidocClientTestBase {
    
    /*
     * Username/Password logins
     */
    public static final String SYSTEM_ADMIN_USER = "sysadmin";

    public static final String SYSTEM_ADMIN_PASSWORD = "escidoc";
    
    private static URL defaultInfrastructureURL;
    
    private static final String ESCIDOC_HOST = "localhost";
    private static final String ESCIDOC_PORT = "8080";
    
    private static String exampleItemId;
    private static String exampleOrganizationalUnitId;
    private static String exampleContextId;
    private static String exampleContentModelId;
    private static String exampleContainerId;
    private static boolean loadedExamples = false;
    
    /**
     * @return
     * @throws TransportException
     * @throws EscidocException
     * @throws InternalClientException
     * @throws MalformedURLException
     */
    public static final synchronized String getStaticContextId() throws TransportException, EscidocException,
        InternalClientException {
        if (!loadedExamples)
            loadStaticResources();
        return exampleContextId;
    }
    
    /**
     * @return
     * @throws TransportException
     * @throws EscidocException
     * @throws InternalClientException
     * @throws MalformedURLException
     */
    public static final synchronized String getStaticContentModelId() throws TransportException, EscidocException,
        InternalClientException {
        if (!loadedExamples)
            loadStaticResources();
        return exampleContentModelId;
    }
    
    /**
     * @return
     * @throws TransportException
     * @throws EscidocException
     * @throws InternalClientException
     * @throws MalformedURLException
     */
    private static void loadStaticResources() throws TransportException, EscidocException, InternalClientException {

        final Authentication auth =
            new Authentication(getDefaultInfrastructureURL(), SYSTEM_ADMIN_USER, SYSTEM_ADMIN_PASSWORD);
        final AdminHandlerClient c = new AdminHandlerClient(auth.getServiceAddress());
        c.setHandle(auth.getHandle());
        final MessagesResult<Entry> result = c.loadExamples();

        for (final Entry entry : result) {
            switch (entry.getResourceType()) {
                case ITEM:
                    exampleItemId = entry.getObjid();
                    break;
                case ORGANIZATIONAL_UNIT:
                    exampleOrganizationalUnitId = entry.getObjid();
                    break;
                case CONTEXT:
                    exampleContextId = entry.getObjid();
                    break;
                case CONTENT_MODEL:
                    exampleContentModelId = entry.getObjid();
                    break;
                case CONTAINER:
                    exampleContainerId = entry.getObjid();
                    break;
            }
        }
        loadedExamples = true;
    }
    
    /**
     * @return
     * @throws InternalClientException
     */
    public static final String getDefaultInfrastructureHost() throws InternalClientException {
        return ESCIDOC_HOST;
    }
    
    /**
     * @return
     * @throws InternalClientException
     */
    public static final String getDefaultInfrastructurePort() throws InternalClientException {
        return ESCIDOC_PORT;
    }
    
    /**
     * @return
     * @throws InternalClientException
     */
    public static final synchronized URL getDefaultInfrastructureURL() throws InternalClientException {

        if (defaultInfrastructureURL == null) {
            final String port = getDefaultInfrastructurePort();
            try {
                defaultInfrastructureURL =
                    new URL("http://" + getDefaultInfrastructureHost() + (port == null ? "" : ":" + port));
            }
            catch (final MalformedURLException e) {
                throw new InternalClientException(e.getMessage(), e);
            }
        }
        return defaultInfrastructureURL;
    }
}
