package it.polimi.ingsw.model;

import java.io.Serializable;

public class AssistantCard implements Serializable {

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

    public String getName(){return assistantType.getName();}
}