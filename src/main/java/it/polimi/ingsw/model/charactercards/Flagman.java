package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.exceptions.IslandNotFoundException;

import java.io.Serializable;

public class Flagman extends CharacterCard implements IntCard, Serializable {

    /*
        CHARACTER CARD DESCRIPTION:
        The flagman allows the player to call the islandConquerCheck method on a selected island. This method call
        doesn't prevent the player to call it another time during the normal round flow.
    */

    int islandID;

    public Flagman() {
        super(3, 3);
    }

    @Override
    public void doOnClick(int par) {
        this.islandID = par;
    }

    // Param: islandID
    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        game.getBoard().islandConquerCheck(game.getCurrentPlayer(), this.islandID);
    }


}
