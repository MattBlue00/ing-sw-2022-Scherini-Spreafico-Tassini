package it.polimi.ingsw.model.exceptions;

public class NonExistentColorException extends TryAgainException{

    public NonExistentColorException(String message) {
        super(message);
    }
}
