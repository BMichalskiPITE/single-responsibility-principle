package edu.agh.wfiis.solid.srp.model.headerstrategy;

import edu.agh.wfiis.solid.srp.model.HeaderStrategy;
import edu.agh.wfiis.solid.srp.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.model.raml.Header;

public class InvalidHeaderStrategy implements HeaderStrategy {

    private Header expected;

    private String actual;

    public InvalidHeaderStrategy(Header expected, String actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public void act() throws InvalidHeaderException {
        String msg = String.format("Invalid value '%s' for header %s.", new Object[]{actual, expected});
        throw new InvalidHeaderException(msg);
    }
}
