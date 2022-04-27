package it.polimi.ingsw.model.exceptions;

public class AssistantCardAlreadyPlayedException extends TryAgainException {
    public AssistantCardAlreadyPlayedException(String message) {
        super(message);
    }
}
