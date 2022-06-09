package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.*;

import java.io.Serializable;

/**
 * Allows the player to call a special version of the islandConquerCheck method, which allows them to take
 * a chosen color out of the regular influence algorithm.
 */

public class Villager extends CharacterCard implements StringCard, Serializable {

    private Color colorToExclude;

    /**
     * Character Card constructor.
     */

    public Villager() {
        super(9, 3);
    }

    /**
     * Sets the parameter of the {@link Villager}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(String par) {
        this.colorToExclude = Color.valueOf(par);
    }

    /**
     * Allows the {@link Villager} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Villager} will do his effect.
     * @throws TryAgainException if a non-existent island is somehow accessed or if a non-existent color is somehow
     * provided as a parameter.
     */

    public void doEffect(GameExpertMode game) throws TryAgainException {

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
                int calcCurrent = selectedIsland.influenceCalc(currentPlayer, colorToExclude);
                int calcOwner = selectedIsland.influenceCalc(owner, colorToExclude);
                GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner,
                        game.getBoard().getIslands());
                game.getBoard().setMotherNaturePos(selectedIsland.getId());
            }
        }
        else{
            int calcCurrent = selectedIsland.influenceCalc(currentPlayer, colorToExclude);
            GameBoard.islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0,
                    game.getBoard().getIslands());
            game.getBoard().setMotherNaturePos(selectedIsland.getId());
        }

    }

}
