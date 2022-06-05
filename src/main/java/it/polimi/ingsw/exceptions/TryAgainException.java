package it.polimi.ingsw.exceptions;

/**
 * This exception is the super-class of every custom exception (except {@link TieException}).
 * This choice was made because every subclass, if error handling is required (which is not always the case,
 * e.g. {@link EmptyBagException}), when caught, triggers some sort of "try again", allowing the user
 * to reinsert input.
 */

public class TryAgainException extends Exception{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public TryAgainException(String message) {
        super(message);
    }

}
