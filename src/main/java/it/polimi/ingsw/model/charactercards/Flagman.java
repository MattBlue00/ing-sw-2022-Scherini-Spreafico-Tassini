package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.exceptions.IslandNotFoundException;

import java.io.Serializable;

/**
 * Allows the player to call the islandConquerCheck method on a selected island. This method call doesn't prevent
 * the player to call it another time during the normal round flow.
 */

public class Flagman extends CharacterCard implements IntCard, Serializable {

    int islandID;

    /**
     * Character Card constructor.
     */

    public Flagman() {
        super(3, 3);
    }

    /**
     * Sets the parameter of the {@link Flagman}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(int par) {
        this.islandID = par;
    }

    /**
     * Allows the {@link Flagman} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Flagman} will do his effect.
     * @throws TryAgainException if a non-existent island ID is provided as a parameter.
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        game.getBoard().islandConquerCheck(game.getCurrentPlayer(), this.islandID);
    }


}
