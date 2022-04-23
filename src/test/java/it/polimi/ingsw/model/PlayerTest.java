package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest{

    @Test
    public void testPlayAssistantCard(){
        Player player = new Player(Wizard.YELLOW_WIZARD, "Samuele");

        Game game = new Game(2);
        game.setCurrentPlayer(player);

        player.playAssistantCard("FOX");
        assertEquals("FOX", game.getCurrentPlayer().getLastAssistantCardPlayed().get().getName());
    }
}