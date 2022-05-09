package it.polimi.ingsw.exceptions;

public class IslandNotFoundException extends TryAgainException{
    public IslandNotFoundException(String message) {
        super(message);
    }
}
