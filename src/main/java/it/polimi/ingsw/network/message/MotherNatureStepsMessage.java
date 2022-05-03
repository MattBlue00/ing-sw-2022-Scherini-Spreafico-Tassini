package it.polimi.ingsw.network.message;

public class MotherNatureStepsMessage extends Message{

    private final int steps;

    public MotherNatureStepsMessage(String nickname, int steps) {
        super(nickname, MessageType.MOTHER_NATURE_STEPS_REPLY);
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
