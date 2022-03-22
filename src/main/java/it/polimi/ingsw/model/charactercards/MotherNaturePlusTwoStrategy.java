package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.CharacterCardStrategy;

public class MotherNaturePlusTwoStrategy implements CharacterCardStrategy {

    @Override
    public void doEffect(Player player) {
        player.getLastCardPlayed().setMotherNatureSteps(
                player.getLastCardPlayed().getMotherNatureSteps()
                + 2
        );
    }
}
