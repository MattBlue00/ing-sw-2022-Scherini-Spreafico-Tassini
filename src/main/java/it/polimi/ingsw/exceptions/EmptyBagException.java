package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if the game controller runs out of students while trying to refill clouds or Character
 * Cards.
 */

public class EmptyBagException extends TryAgainException {

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public EmptyBagException(String message) { super(message);}
}
