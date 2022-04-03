package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.CharacterCardEffect;
import it.polimi.ingsw.model.GameExpertMode;

public class Postman extends CharacterCard implements CharacterCardEffect {

    public Postman() {
        super(4, 1);
    }

    public void doEffect(GameExpertMode game){
        game.setMaxSteps(game.getMaxSteps() + 2);
    }

}
