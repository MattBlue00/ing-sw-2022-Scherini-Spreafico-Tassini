package it.polimi.ingsw.model.exceptions;

public class CharacterCardAlreadyPlayedException extends TryAgainException {

    public CharacterCardAlreadyPlayedException(String message) {
        super(message);
    }
}
