package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.*;

import java.io.Serializable;

/**
 * Allows the player to call a special version of the islandConquerCheck method, which gives them an extra
 * two points of influence.
 */

public class Knight extends CharacterCard implements Serializable {

    /**
     * Character Card constructor.
     */

    public Knight() {
        super(8, 2);
    }

    /**
     * Allows the {@link Knight} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Knight} will do his effect.
     * @throws TryAgainException if the method somehow tries to get data from a non-existent island
     * (it should never be thrown, so it is safely ignorable).
     */

    public void doEffect(GameExpertMode game) throws TryAgainException {

        int islandID = game.getBoard().getMotherNaturePos();

        Island selectedIsland = game.getBoard().getIslands().getIslandFromID(islandID);
        if(selectedIsland.hasVetoTile()) {
            selectedIsland.setHasVetoTile(false);
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

    }
}
