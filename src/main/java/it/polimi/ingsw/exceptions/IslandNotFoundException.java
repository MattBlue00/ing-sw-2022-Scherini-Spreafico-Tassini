package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player provides an island ID that is not corresponding to any of the existing ones.
 */

public class IslandNotFoundException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public IslandNotFoundException(String message) {
        super(message);
    }
}
