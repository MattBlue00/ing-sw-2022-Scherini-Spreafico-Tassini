package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown in the rare case a game ends in perfect tie (specifically, two or more players share the
 * same amount of towers left and the same amount of professors owned at the same time).
 */

public class TieException extends Exception{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public TieException(String message) {
        super(message);
    }

}
