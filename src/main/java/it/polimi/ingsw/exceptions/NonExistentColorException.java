package it.polimi.ingsw.exceptions;

public class NonExistentColorException extends TryAgainException{

    public NonExistentColorException(String message) {
        super(message);
    }
}
