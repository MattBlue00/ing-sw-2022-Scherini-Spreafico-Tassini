package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends TryAgainException{

    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
