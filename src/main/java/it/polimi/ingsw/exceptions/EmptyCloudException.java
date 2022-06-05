package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a players tries to empty a cloud that is already empty (in other words, if they try to
 * choose a cloud already chosen in a certain round).
 */

public class EmptyCloudException extends TryAgainException {

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public EmptyCloudException(String message) {
        super(message);
    }
}
