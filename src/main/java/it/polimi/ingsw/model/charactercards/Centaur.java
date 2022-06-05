package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.*;

import java.io.Serializable;

/**
 * Allows the player to trigger a special version of the islandConquerCheck method, which does not count into
 * the influence the towers' effect.
 */

public class Centaur extends CharacterCard implements Serializable {

    /**
     * Character Card constructor.
     */

    public Centaur() {
        super(6, 3);
    }

    /**
     * Allows the {@link Centaur} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Centaur} will do his effect.
     * @throws TryAgainException if the method somehow tries to get data from a non-existent island
     * (it should never be thrown, so it is safely ignorable).
     */

    public void doEffect(GameExpertMode game) throws TryAgainException {

        //checks if an island can be conquered without counting the towers number

        Island selectedIsland = game.getBoard().getIslands().getIslandFromID(game.getBoard().getMotherNaturePos());
        if(selectedIsland.hasVetoTile()) {
            selectedIsland.setHasVetoTile(false);
            game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() + 1);
            return;
        }
        Player currentPlayer = game.getCurrentPlayer();
        Player owner = selectedIsland.getOwner();
        if(owner != null) {
            if (!owner.equals(currentPlayer)) {
                int calcCurrent = selectedIsland.influenceCalcWithoutTowers(currentPlayer);
                int calcOwner = selectedIsland.influenceCalcWithoutTowers(owner);
                GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner,
                        game.getBoard().getIslands());
            }
        }
        else{
            int calcCurrent = selectedIsland.influenceCalcWithoutTowers(currentPlayer);
            GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0,
                    game.getBoard().getIslands());
        }

    }
}
