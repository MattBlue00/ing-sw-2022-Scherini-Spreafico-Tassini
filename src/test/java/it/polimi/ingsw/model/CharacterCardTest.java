package it.polimi.ingsw.model;

import it.polimi.ingsw.model.charactercards.Bard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest{

    @Test
    public void testGetCost() {
        CharacterCard characterCard = new Bard();
        assertEquals(1, characterCard.getCost());
        characterCard.setCost(3);
        assertEquals(3, characterCard.getCost());
    }

    @Test
    public void testSetCost() {
        CharacterCard characterCard = new Bard();
        assertEquals(1, characterCard.getCost());
        characterCard.setCost(3);
        assertEquals(3, characterCard.getCost());
    }

    @Test
    public void testGetId() {
        CharacterCard characterCard = new Bard();
        assertEquals(10, characterCard.getId());
    }
}