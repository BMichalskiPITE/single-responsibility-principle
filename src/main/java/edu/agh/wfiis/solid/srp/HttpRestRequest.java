package edu.agh.wfiis.solid.srp;

import edu.agh.wfiis.solid.srp.model.HeaderStrategy;
import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.Message;
import edu.agh.wfiis.solid.srp.model.raml.Constraints;
import edu.agh.wfiis.solid.srp.model.raml.Header;

public class HttpRestRequest {

    protected Message requestMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(Message requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Message validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;
        processHeaders();
        return requestMessage;
    }

    private void processHeaders() throws InvalidHeaderException {
        for (String expectedKey : validationConstraints.getHeaders().keySet()) {
            resolveHeaderStrategy(expectedKey).act();
        }
    }

    private HeaderStrategy resolveHeaderStrategy(String expectedHeaderKey) {
        return HeaderStrategy.resolve(
                expectedHeaderKey,
                getExpectedHeader(expectedHeaderKey),
                requestMessage
        );
    }

    private Header getExpectedHeader(String expectedHeaderKey) {
        return (Header) validationConstraints.getHeaders().get(expectedHeaderKey);
    }
}
