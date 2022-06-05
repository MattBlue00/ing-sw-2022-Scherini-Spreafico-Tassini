package it.polimi.ingsw.model;

/**
 * This enumeration specifies all the existent Assistant Card types. Each one has a unique name and a unique weight,
 * with the maximum number of Mother Nature steps allowed increasing from 1 to 5. Starting from 1, it increases by one
 * every two units of weight.
 */

public enum AssistantType {

    CHEETAH("CHEETAH",1, 1),
    OSTRICH("OSTRICH",2,1),
    CAT("CAT",3,2),
    EAGLE("EAGLE",4,2),
    FOX("FOX",5,3),
    SNAKE("SNAKE",6,3),
    OCTOPUS("OCTOPUS",7,4),
    DOG("DOG",8,4),
    ELEPHANT("ELEPHANT",9,5),
    TURTLE("TURTLE",10,5);

    private final String name;
    private final int weight;
    private final int motherNatureSteps;

    /**
     * Assistant Type constructor.
     *
     * @param name the card name.
     * @param weight the card weight.
     * @param motherNatureSteps the maximum number of Mother Nature steps allowed by the card.
     */

    AssistantType(String name, int weight, int motherNatureSteps) {
        this.name = name;
        this.weight = weight;
        this.motherNatureSteps = motherNatureSteps;
    }

    /**
     * Returns the weight of the Assistant Card.
     *
     * @return an {@code int} representing the weight of the Assistant Card.
     */

    public int getWeight() {
        return weight;
    }

    /**
     * Returns the maximum number of Mother Nature steps allowed by the Assistant Card.
     *
     * @return an {@code int} representing the maximum number of Mother Nature steps allowed of the Assistant Card.
     */

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    /**
     * Returns the name of the Assistant Card.
     *
     * @return a {@code String} representing the name of the Assistant Card.
     */

    public String getName() {
        return name;
    }
}
