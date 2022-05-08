package it.polimi.ingsw.exceptions;

public class CharacterCardAlreadyPlayedException extends TryAgainException {

    public CharacterCardAlreadyPlayedException(String message) {
        super(message);
    }
}
