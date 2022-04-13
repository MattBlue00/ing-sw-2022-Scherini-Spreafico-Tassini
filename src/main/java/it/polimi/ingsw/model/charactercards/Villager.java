package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.IslandNotFoundException;

import java.util.Optional;

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

        Color colorToExclude = Color.GREEN; // PLACEHOLDER

        try {
            Island selectedIsland = game.getBoard().getIslands().getIslandFromID(game.getBoard().getMotherNaturePos());
            if(selectedIsland.hasVeto()) {
                selectedIsland.setVeto(false);
                game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() + 1);
                return;
            }
            Player currentPlayer = game.getCurrentPlayer();
            Optional<Player> owner = selectedIsland.getOwner();
            if(owner.isPresent()) {
                if (!owner.get().equals(currentPlayer)) {
                    int calcCurrent = selectedIsland.influenceCalc(currentPlayer, colorToExclude);
                    int calcOwner = selectedIsland.influenceCalc(owner.get(), colorToExclude);
                    GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner,
                            game.getBoard().getIslands());
                }
            }
            else{
                int calcCurrent = selectedIsland.influenceCalc(currentPlayer, colorToExclude);
                GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0,
                        game.getBoard().getIslands());
            }
        } catch (IslandNotFoundException e) {
            e.printStackTrace();
        }

    }

}
