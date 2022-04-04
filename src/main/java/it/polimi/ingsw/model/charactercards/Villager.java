package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;

public class Villager extends CharacterCard {

    /*
        CHARACTER CARD DESCRIPTION:
        The villager allows the player to call a special version of the islandConquerCheck method, which allows the
        player to take a chosen color out of the regular influenceCalc algorithm.
    */

    public Villager() {
        super(9, 3);
    }

    public void doEffect(GameExpertMode game){

        //TODO: the view will ask the player which color to choose

        Color colorToExclude = Color.BLUE; // PLACEHOLDER

        game.getBoard().islandConquerCheck(game.getCurrentPlayer(), game.getBoard().getMotherNaturePos(), colorToExclude);

        setUpCard();

    }

}
