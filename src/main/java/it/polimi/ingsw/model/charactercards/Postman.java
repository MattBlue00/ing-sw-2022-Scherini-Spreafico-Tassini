package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

import java.io.Serializable;

/**
 * Allows the player to move Mother Nature of up to two more spaces than of the ones specified by the Assistant
 * Card played.
 */

public class Postman extends CharacterCard implements Serializable {

    /**
     * Character Card constructor.
     */

    public Postman() {
        super(4, 1);
    }

    /**
     * Allows the {@link Postman} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Postman} will do his effect.
     */

    public void doEffect(GameExpertMode game){
        game.setMaxSteps(game.getMaxSteps() + 2);
    }

}
