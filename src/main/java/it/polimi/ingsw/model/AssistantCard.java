package it.polimi.ingsw.model;

public class AssistantCard {

    private final int id;
    private int weight;
    private int motherNatureSteps;

    public AssistantCard(int id, int weight, int motherNatureSteps) {
        this.id = id;
        this.weight = weight;
        this.motherNatureSteps = motherNatureSteps;
    }

    // Getter and setter methods

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public int getMotherNatureSteps() {
        return motherNatureSteps;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setMotherNatureSteps(int motherNatureSteps) {
        this.motherNatureSteps = motherNatureSteps;
    }
}