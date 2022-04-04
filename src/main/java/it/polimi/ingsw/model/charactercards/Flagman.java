package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;

public class Flagman extends CharacterCard {

    // influenceCalc is calculated even if MotherNature isn't on this particular island

    public Flagman() {
        super(3, 3);
    }

    @Override
    public void doEffect(GameExpertMode game) {
        int islandID = 1; // placeholder
        game.getBoard().islandConquerCheck(game.getCurrentPlayer(), game.getBoard().getMotherNaturePos());
    }
}
