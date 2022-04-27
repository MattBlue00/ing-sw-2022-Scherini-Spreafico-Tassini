package it.polimi.ingsw.model.exceptions;

public class InvalidNumberOfStepsException extends TryAgainException {
    public InvalidNumberOfStepsException(String message) {
        super(message);
    }
}
