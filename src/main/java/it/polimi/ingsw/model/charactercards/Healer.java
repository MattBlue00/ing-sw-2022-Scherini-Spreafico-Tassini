package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.NoVetoTilesException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

import java.io.Serializable;

/**
 * Allows the player to put a veto tile on a chosen island: this means that if Mother Nature reaches an island
 * with such a tile, no conquering check method will be called.
 */

public class Healer extends CharacterCard implements IntCard, Serializable {

    private int islandID;

    /**
     * Character Card constructor.
     */

    public Healer() {
        super(5, 2);
    }

    /**
     * Sets the parameter of the {@link Healer}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(int par) {
        this.islandID = par;
    }

    /**
     * Allows the {@link Healer} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Healer} will do his effect.
     * @throws TryAgainException if a non-existent island ID is provided as a parameter or if there are no more veto
     * tiles available.
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        if(game.getBoard().getNumOfVetos() <= 0)
            throw new NoVetoTilesException("There are no veto tiles available!");
        else {
            game.getBoard().getIslands().getIslandFromID(this.islandID).setHasVetoTile(true);
            game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() - 1);
        }
    }

}
