package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.NoVetoTilesException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

import java.io.Serializable;

public class Healer extends CharacterCard implements IntCard, Serializable {

    /*
        CHARACTER CARD DESCRIPTION:
        The healer allows the player to put a veto tile on a chosen island: this means that if Mother Nature reaches
        an island with that tile, no conquerCheck method will be called.
    */
    int islandID;

    public Healer() {
        super(5, 2);
    }

    @Override
    public void doOnClick(int par) {
        setIslandID(par);
    }

    public void setIslandID(int islandID) {
        this.islandID = islandID;
    }

    // Param: islandID
    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        if(game.getBoard().getNumOfVetos() < 1)
            throw new NoVetoTilesException("There are no veto tiles available!");
        else {
            game.getBoard().getIslands().getIslandFromID(this.islandID).setVeto(true);
            game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() - 1);
        }
    }

}
