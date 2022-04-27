package it.polimi.ingsw.model.exceptions;

public class NotEnoughCoinsException extends TryAgainException{

    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
