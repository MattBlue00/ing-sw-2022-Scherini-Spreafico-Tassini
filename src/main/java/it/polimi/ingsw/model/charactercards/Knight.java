package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

public class Knight extends CharacterCard {

    public Knight() {
        super(8, 2);
    }

    public void doEffect(GameExpertMode game){

        int islandID = 1;
        // non è possibile fare questa cosa poichè dovrei passare a doEffect una island
        game.getBoard().islandConquerCheckPlusTwoInfluence(game.getCurrentPlayer(), islandID );

        setCost(getCost() + 1);
        setIsActive(false);
    }
}
