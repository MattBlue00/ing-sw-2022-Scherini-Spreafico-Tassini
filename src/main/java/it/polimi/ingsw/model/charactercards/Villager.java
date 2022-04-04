package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;

public class Villager extends CharacterCard {

    public Villager(/* debug */ Color colorToExclude) {
        super(9, 3);
    }

    public void doEffect(GameExpertMode game){

        //TODO: the view will ask the player which color to choose

        Color colorToExclude = Color.BLUE; // PLACEHOLDER

        game.getBoard().islandConquerCheck(game.getCurrentPlayer(), game.getBoard().getMotherNaturePos(), colorToExclude);

        setUpCard();

    }

}
