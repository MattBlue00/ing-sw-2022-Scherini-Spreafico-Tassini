package it.polimi.ingsw.network.message;

/**
 * This {@link Message} is used to communicate how many steps the player wishes Mother Nature to move of.
 */

public class MotherNatureStepsMessage extends Message{

    private final int steps;

    /**
     * Default constructor.
     *
     * @param nickname client's nickname.
     * @param steps Mother Nature's chosen steps.
     */

    public MotherNatureStepsMessage(String nickname, int steps) {
        super(nickname, MessageType.MOTHER_NATURE_STEPS_REPLY);
        this.steps = steps;
    }

    /**
     * Returns Mother Nature's chosen steps.
     *
     * @return an {@code int} representing Mother Nature's chosen steps.
     */

    public int getSteps() {
        return steps;
    }
}
