package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.InvalidIslandException;

public class Centaur extends CharacterCard {

    /*
        CHARACTER CARD DESCRIPTION:
        The centaur allows the player to trigger a special version of the islandConquerCheck method, which
        does not count into the influence the towers' effect.
    */

    public Centaur() {
        super(6, 3);
    }

    public void doEffect(GameExpertMode game){

        game.getBoard().islandConquerCheckWithoutTowers(game.getCurrentPlayer(), game.getBoard().getMotherNaturePos());

    }
}
