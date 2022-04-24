package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardTest {

    @Test
    void getWeight() {
        // CAT: weight 3, steps 2
        AssistantCard assistantCard = new AssistantCard(AssistantType.CAT);
        assertEquals(3, assistantCard.getWeight());
    }

    @Test
    void getMotherNatureSteps() {
        // CAT: weight 3, steps 2
        AssistantCard assistantCard = new AssistantCard(AssistantType.CAT);
        assertEquals(2, assistantCard.getMotherNatureSteps());
    }

    @Test
    void getName() {
        // CAT: weight 3, steps 2
        AssistantCard assistantCard = new AssistantCard(AssistantType.CAT);
        assertEquals("CAT", assistantCard.getName());
    }
}