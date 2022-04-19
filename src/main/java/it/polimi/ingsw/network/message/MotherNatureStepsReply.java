package it.polimi.ingsw.network.message;

public class MotherNatureStepsReply extends Message{

    private final int steps;

    public MotherNatureStepsReply(String nickname, int steps) {
        super(nickname, MessageType.MOTHER_NATURE_STEPS_REPLY);
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
