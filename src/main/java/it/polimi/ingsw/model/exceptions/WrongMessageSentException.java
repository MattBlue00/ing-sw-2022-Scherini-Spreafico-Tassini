package it.polimi.ingsw.model.exceptions;

public class WrongMessageSentException extends TryAgainException{

    public WrongMessageSentException(String message) {
        super(message);
    }

}
