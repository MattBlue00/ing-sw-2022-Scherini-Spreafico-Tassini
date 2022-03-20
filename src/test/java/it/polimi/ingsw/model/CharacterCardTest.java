package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.Test;

public class CharacterCardTest extends TestCase {

    @Test
    public void testGetCost() {
        CharacterCard characterCard = new CharacterCard(1);
        characterCard.setCost(2);
        assertEquals(2, characterCard.getCost());
    }

    @Test
    public void testSetCost() {
    }

    @Test
    public void testGetId() {
    }
}