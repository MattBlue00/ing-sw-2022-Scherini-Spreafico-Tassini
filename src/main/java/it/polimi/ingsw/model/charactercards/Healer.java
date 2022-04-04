package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.exceptions.InvalidIslandException;

public class Healer extends CharacterCard {

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
        setCost(getCost() + 1);
        setIsActive(false);
    }

    //TODO: need to check for vetoTilesRunOutException
}
