package edu.agh.wfiis.solid.srp;


import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.Message;
import edu.agh.wfiis.solid.srp.model.raml.Constraints;
import edu.agh.wfiis.solid.srp.model.raml.Header;
import org.junit.Assert;

import java.util.HashMap;

public class HttpRestRequestTest {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String ACCEPT_HEADER_NAME = "Accept";

    private static final Constraints VALIDATION_CONTRACT = prepareValidationContract();
    public static final String DEFAULT_ACCEPT_HEADER_VALUE = "application/json;q=0.9,*/*;q=0.8";

    private Message testMessage;

    @org.junit.Before
    public void setUp() throws Exception {
        testMessage = new Message();
    }

    @org.junit.Test
    public void testWhenRequiredHeaderPresent() throws Exception {
        testMessage.setInboundProperty("http.headers", new HashMap<String, String>() {{
            put(CONTENT_TYPE_HEADER_NAME, "application/json");
        }});
        new HttpRestRequest(testMessage).validate(VALIDATION_CONTRACT);
    }

    @org.junit.Test(expected = InvalidHeaderException.class)
    public void testWhenRequiredHeaderMissing() throws Exception {
        testMessage.setInboundProperty("http.headers", new HashMap<String, String>());
        new HttpRestRequest(testMessage).validate(VALIDATION_CONTRACT);
    }

    @org.junit.Test(expected = InvalidHeaderException.class)
    public void testWhenHeaderValueIsInvalid() throws Exception {
        Header accept = new Header();
        accept.setDisplayName(ACCEPT_HEADER_NAME);
        accept.setPattern("abc");

        Constraints constraints = new Constraints();
        constraints.setHeaders(new HashMap<String, Header>() {{
            put(ACCEPT_HEADER_NAME, accept);
        }});

        testMessage.setInboundProperty("http.headers", new HashMap<String, String>() {{
            put(ACCEPT_HEADER_NAME, "123");
        }});
        new HttpRestRequest(testMessage).validate(constraints);
    }

    @org.junit.Test
    public void testIfDefaultHeaderIsSet() throws Exception {
        Header accept = new Header();
        accept.setDisplayName(ACCEPT_HEADER_NAME);
        accept.setDefaultValue(DEFAULT_ACCEPT_HEADER_VALUE);

        Constraints constraints = new Constraints();
        constraints.setHeaders(new HashMap<String, Header>() {{
            put(ACCEPT_HEADER_NAME, accept);
        }});

        testMessage.setInboundProperty("http.headers", new HashMap<String, String>());
        new HttpRestRequest(testMessage).validate(constraints);
        Assert.assertEquals(DEFAULT_ACCEPT_HEADER_VALUE,
                testMessage.getInboundProperty("http.headers").get(ACCEPT_HEADER_NAME));
    }

    private static Constraints prepareValidationContract() {
        Header contentType = new Header();
        contentType.setDisplayName(CONTENT_TYPE_HEADER_NAME);
        contentType.setRequired(true);

        Header accept = new Header();
        accept.setDisplayName(ACCEPT_HEADER_NAME);
        accept.setDefaultValue(DEFAULT_ACCEPT_HEADER_VALUE);

        Constraints constraints = new Constraints();
        constraints.setHeaders(new HashMap<String, Header>() {{
            put(CONTENT_TYPE_HEADER_NAME, contentType);
            put(ACCEPT_HEADER_NAME, accept);
        }});
        return constraints;
    }

}