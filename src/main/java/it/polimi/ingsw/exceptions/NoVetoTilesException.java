package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.charactercards.Healer;

/**
 * This exception is thrown if a player tries to use the {@link Healer} Character Card, but the game board has no
 * more veto tiles available.
 */

public class NoVetoTilesException extends TryAgainException{

    /**
     * Exception constructor.
     *
     * @param message the exception message.
     */

    public NoVetoTilesException(String message) {
        super(message);
    }

}
