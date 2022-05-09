package it.polimi.ingsw.exceptions;

public class WrongMessageSentException extends TryAgainException{

    public WrongMessageSentException(String message) {
        super(message);
    }

}
