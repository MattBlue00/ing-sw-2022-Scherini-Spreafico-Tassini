package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to move a student that is not where stated.
 */

public class StudentNotFoundException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public StudentNotFoundException(String message) {
        super(message);
    }
}
