package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The Assistant Cards are one of the key elements of the game. At the end of each round, players play the so-called
 * "Planning Phase", in which they choose a unique-per-round Assistant Card. The Assistant Card determines the players'
 * order in the "Action Phase", as well as the maximum number of Mother Nature steps allowed for each player.
 */

public class AssistantCard implements Serializable {

    private final AssistantType assistantType;

    /**
     * Assistant Card constructor.
     *
     * @param assistantType the (enumerated) type of Assistant Card.
     */

    public AssistantCard(AssistantType assistantType){
        this.assistantType = assistantType;
    }

    /**
     * Returns the weight of the Assistant Card.
     *
     * @return an {@code int} representing the weight of the Assistant Card.
     */

    public int getWeight() {
        return assistantType.getWeight();
    }

    /**
     * Returns the maximum number of Mother Nature steps allowed by the Assistant Card.
     *
     * @return an {@code int} representing the maximum number of Mother Nature steps allowed of the Assistant Card.
     */

    public int getMotherNatureSteps() {
        return assistantType.getMotherNatureSteps();
    }

    /**
     * Returns the name of the Assistant Card.
     *
     * @return a {@code String} representing the name of the Assistant Card.
     */

    public String getName(){return assistantType.getName();}
}