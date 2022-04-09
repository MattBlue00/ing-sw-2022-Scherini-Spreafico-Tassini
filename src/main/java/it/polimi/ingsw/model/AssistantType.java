package it.polimi.ingsw.model;

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

    AssistantType(String name, int weight, int motherNatureSteps) {
        this.name = name;
        this.weight = weight;
        this.motherNatureSteps = motherNatureSteps;
    }

    public int getWeight() {
        return weight;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public String getName() {
        return name;
    }
}
