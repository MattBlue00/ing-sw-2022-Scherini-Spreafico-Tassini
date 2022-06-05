package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to play a Character Card with an unknown ID (which means that the ID may
 * be below 1 or over 12 or the card corresponding to that ID may not be currently present in the match).
 */

public class CharacterCardNotFoundException extends TryAgainException {

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public CharacterCardNotFoundException(String message) {
        super(message);
    }
}
