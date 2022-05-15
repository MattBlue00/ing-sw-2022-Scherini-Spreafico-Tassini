package it.polimi.ingsw.network.message;

public class ErrorMessage extends Message{

    private String error;

    public ErrorMessage(String error) {
        super(null, MessageType.ERROR_MESSAGE);
        this.error = error;
    }
}
