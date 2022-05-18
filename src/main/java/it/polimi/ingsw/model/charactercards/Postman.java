package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

import java.io.Serializable;

public class Postman extends CharacterCard implements Serializable {

    /*
        CHARACTER CARD DESCRIPTION:
        The postman allows the player to move Mother Nature up to two more spaces.
    */

    public Postman() {
        super(4, 1);
    }

    // no parameters
    public void doEffect(GameExpertMode game){

        game.setMaxSteps(game.getMaxSteps() + 2);

    }

}
