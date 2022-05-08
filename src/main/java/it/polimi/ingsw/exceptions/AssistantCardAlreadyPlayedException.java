package it.polimi.ingsw.exceptions;

public class AssistantCardAlreadyPlayedException extends TryAgainException {
    public AssistantCardAlreadyPlayedException(String message) {
        super(message);
    }
}
