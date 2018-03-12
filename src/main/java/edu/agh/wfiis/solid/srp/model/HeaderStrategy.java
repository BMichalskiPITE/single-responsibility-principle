package edu.agh.wfiis.solid.srp.model;

import edu.agh.wfiis.solid.srp.model.headerstrategy.InvalidHeaderStrategy;
import edu.agh.wfiis.solid.srp.model.raml.Header;
import edu.agh.wfiis.solid.srp.model.headerstrategy.RequiredHeaderStrategy;
import edu.agh.wfiis.solid.srp.model.headerstrategy.SettingHeaderStrategy;

import java.util.HashMap;
import java.util.Map;

public interface HeaderStrategy
{
    void act() throws InvalidHeaderException;

    static HeaderStrategy resolve(String expectedHeaderKey, Header expected, Message requestMessage)
    {
        Map<String, String> incomingHeaders = getIncomingHeaders(requestMessage);
        String actual = (String) incomingHeaders.get(expectedHeaderKey);

        if ((actual == null) && (expected.isRequired())) {
            return new RequiredHeaderStrategy(expectedHeaderKey);
        }

        if ((actual == null) && (expected.getDefaultValue() != null)) {
            return new SettingHeaderStrategy(expectedHeaderKey, expected, requestMessage);
        }

        if (actual != null) {
            if (!expected.validate(actual)) {
                return new InvalidHeaderStrategy(expected, actual);
            }
        }

        return null;
    }

    static Map<String, String> getIncomingHeaders(Message message) {
        Map<String, String> incomingHeaders = new HashMap<>();
        if (message.getInboundProperty("http.headers") != null) {
            incomingHeaders = message.<Map>getInboundProperty("http.headers");
        }
        return incomingHeaders;
    }

}
