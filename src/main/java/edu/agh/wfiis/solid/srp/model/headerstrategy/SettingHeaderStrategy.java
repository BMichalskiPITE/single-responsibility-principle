package edu.agh.wfiis.solid.srp.model.headerstrategy;

import edu.agh.wfiis.solid.srp.model.HeaderStrategy;
import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.Message;
import edu.agh.wfiis.solid.srp.model.raml.Header;

import java.util.Map;

public class SettingHeaderStrategy implements HeaderStrategy {

    private String expectedHeaderKey;

    private Header expected;

    private Message requestMessage;

    public SettingHeaderStrategy(String expectedHeaderKey, Header expected, Message requestMessage) {
        this.expectedHeaderKey = expectedHeaderKey;
        this.expected = expected;
        this.requestMessage = requestMessage;
    }

    @Override
    public void act() throws InvalidHeaderException {
        setHeader(expectedHeaderKey, expected.getDefaultValue());
    }

    private void setHeader(String key, String value) {
        if (requestMessage.getInboundProperty("http.headers") != null) {
            ((Map) requestMessage.getInboundProperty("http.headers")).put(key, value);
        }
    }
}
