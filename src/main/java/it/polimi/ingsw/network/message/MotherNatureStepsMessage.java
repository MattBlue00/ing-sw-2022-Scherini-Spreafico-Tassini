package it.polimi.ingsw.network.message;

public class MotherNatureStepsMessage extends Message{

    private final int steps;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname
     * @param steps Mother Nature's steps
     */
    public MotherNatureStepsMessage(String nickname, int steps) {
        super(nickname, MessageType.MOTHER_NATURE_STEPS_REPLY);
        this.steps = steps;
    }

    /**
     * This method returns Mother Nature's steps.
     * @return {@code steps}
     */
    public int getSteps() {
        return steps;
    }
}
