package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

public class Knight extends CharacterCard {

    /*
        CHARACTER CARD DESCRIPTION:
        The knight allows the player to call a special version of the islandConquerCheck method, which gives them
        an extra two points of influence.
    */

    public Knight() {
        super(8, 2);
    }

    public void doEffect(GameExpertMode game){

        // TODO: parsing input from the view
        int islandID = 1;

        game.getBoard().islandConquerCheckPlusTwoInfluence(game.getCurrentPlayer(), islandID);

    }
}
