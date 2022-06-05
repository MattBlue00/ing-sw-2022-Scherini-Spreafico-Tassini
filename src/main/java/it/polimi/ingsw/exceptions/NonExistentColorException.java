package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player manages to provide a table color that is not present in the game.
 */

public class NonExistentColorException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public NonExistentColorException(String message) {
        super(message);
    }
}
