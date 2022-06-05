package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if the message received from a client is not of one of the types expected by the
 * controller (or by the server).
 */

public class WrongMessageSentException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public WrongMessageSentException(String message) {
        super(message);
    }

}
