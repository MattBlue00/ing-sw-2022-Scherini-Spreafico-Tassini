package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.exceptions.InvalidIslandException;

public class Healer extends CharacterCard {

    /*
        CHARACTER CARD DESCRIPTION:
        The healer allows the player to put a veto tile on a chosen island: this means that if Mother Nature reaches
        an island with that tile, no conquerCheck method will be called.
    */

    public Healer() {
        super(5, 2);
    }

    @Override
    public void doEffect(GameExpertMode game) {

        int islandID = 1; // placeholder
        try{
            game.getBoard().getIslands().getIslandFromID(islandID).setVeto(true);
            game.getBoard().setNumOfVetos(game.getBoard().getNumOfVetos() - 1);
        }catch(InvalidIslandException e) {
            e.printStackTrace();
        }

    }

    //TODO: need to check for VetoTilesRunOutException
}