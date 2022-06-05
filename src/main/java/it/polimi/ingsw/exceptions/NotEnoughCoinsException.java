package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to play a Character Card, but they don't afford it.
 */

public class NotEnoughCoinsException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
