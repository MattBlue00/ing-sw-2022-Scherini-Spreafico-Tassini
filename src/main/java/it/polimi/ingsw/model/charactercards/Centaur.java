package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidIslandException;

public class Centaur extends CharacterCard {

    public Centaur() {
        super(6, 3);
    }

    public void doEffect(GameExpertMode game){

        game.getBoard().islandConquerCheckWithoutTowers(game.getCurrentPlayer(), game.getBoard().getMotherNaturePos());
        setCost(getCost() + 1);
        setIsActive(false);
    }
}
