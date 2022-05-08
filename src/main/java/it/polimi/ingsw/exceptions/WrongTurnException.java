package it.polimi.ingsw.exceptions;

public class WrongTurnException extends TryAgainException{

    public WrongTurnException(String message) {
        super(message);
    }
}
