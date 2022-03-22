package it.polimi.ingsw.model.exceptions;

public class CardAlreadyPlayedException extends Exception{
    public CardAlreadyPlayedException(String message) {
        super(message);
    }
}
