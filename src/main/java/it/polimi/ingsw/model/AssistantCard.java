package it.polimi.ingsw.model;

public class AssistantCard {

    private final int id;
    private final int weight;
    private final int motherNatureSteps;

    public AssistantCard(int id, int weight, int motherNatureSteps) {
        this.id = id;
        this.weight = weight;
        this.motherNatureSteps = motherNatureSteps;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }
}