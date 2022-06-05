package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to move a student to a table, but the corresponding table has no spaces
 * left.
 */

public class FullTableException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public FullTableException(String message) {
        super(message);
    }

}
