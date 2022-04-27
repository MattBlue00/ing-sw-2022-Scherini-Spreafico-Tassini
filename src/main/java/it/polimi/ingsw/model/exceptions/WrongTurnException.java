package it.polimi.ingsw.model.exceptions;

public class WrongTurnException extends TryAgainException{

    public WrongTurnException(String message) {
        super(message);
    }
}
