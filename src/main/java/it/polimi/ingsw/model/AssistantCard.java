package it.polimi.ingsw.model;

public class AssistantCard {

    private AssistantType assistantType;

    public AssistantCard(AssistantType assistantType){
        this.assistantType = assistantType;
    }

    public int getWeight() {
        return assistantType.getWeight();
    }

    public int getMotherNatureSteps() {
        return assistantType.getMotherNatureSteps();
    }
}