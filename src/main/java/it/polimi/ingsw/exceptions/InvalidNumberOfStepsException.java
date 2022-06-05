package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to make Mother Nature move of a number of steps they are not allowed to.
 */

public class InvalidNumberOfStepsException extends TryAgainException {

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public InvalidNumberOfStepsException(String message) {
        super(message);
    }
}
