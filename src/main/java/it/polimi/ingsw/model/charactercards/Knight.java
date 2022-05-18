package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.exceptions.IslandNotFoundException;

import java.util.Optional;

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

        int islandID = game.getBoard().getMotherNaturePos();

        try {
            Island selectedIsland = game.getBoard().getIslands().getIslandFromID(islandID);
            if(selectedIsland.hasVeto()) {
                selectedIsland.setVeto(false);
                game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() + 1);
                return;
            }
            Player currentPlayer = game.getCurrentPlayer();
            Player owner = selectedIsland.getOwner();
            if(owner != null) {
                if (!owner.equals(currentPlayer)) {
                    int calcCurrent = selectedIsland.influenceCalc(currentPlayer) + 2;
                    int calcOwner = selectedIsland.influenceCalc(owner);
                    GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner,
                            game.getBoard().getIslands());
                }
            }
            else{
                int calcCurrent = selectedIsland.influenceCalc(currentPlayer) + 2;
                GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0,
                        game.getBoard().getIslands());
            }
        } catch (IslandNotFoundException e) {
            e.printStackTrace();
        }

    }
}
