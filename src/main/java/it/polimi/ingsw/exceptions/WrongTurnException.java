package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a client manages to send the server (and consequently the controller) a message of an
 * expected type, but not during the game round they are allowed to.
 */

public class WrongTurnException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public WrongTurnException(String message) {
        super(message);
    }
}
