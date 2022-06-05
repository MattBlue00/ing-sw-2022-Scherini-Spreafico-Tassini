package it.polimi.ingsw.exceptions;

/**
 * This exception is thrown if a player tries to play a Character Card in a round in which they have already played
 * one.
 */

public class CharacterCardAlreadyPlayedException extends TryAgainException {

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public CharacterCardAlreadyPlayedException(String message) {
        super(message);
    }
}
