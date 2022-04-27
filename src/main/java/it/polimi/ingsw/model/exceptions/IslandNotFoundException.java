package it.polimi.ingsw.model.exceptions;

public class IslandNotFoundException extends TryAgainException{
    public IslandNotFoundException(String message) {
        super(message);
    }
}
