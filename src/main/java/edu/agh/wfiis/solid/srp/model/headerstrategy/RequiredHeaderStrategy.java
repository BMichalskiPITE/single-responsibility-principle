package edu.agh.wfiis.solid.srp.model.headerstrategy;

import edu.agh.wfiis.solid.srp.model.HeaderStrategy;
import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;

public class RequiredHeaderStrategy implements HeaderStrategy {

    private String expectedHeaderKey;

    public RequiredHeaderStrategy(String expectedHeaderKey) {
        this.expectedHeaderKey = expectedHeaderKey;
    }

    @Override
    public void act() throws InvalidHeaderException {
        throw new InvalidHeaderException("Required header " + expectedHeaderKey + " not specified");
    }
}
