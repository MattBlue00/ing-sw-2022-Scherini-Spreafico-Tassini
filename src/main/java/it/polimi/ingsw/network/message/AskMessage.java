package it.polimi.ingsw.network.message;

public class ErrorMessage extends Message{
    private String errorString;
    private ErrorType errorType;
    public ErrorMessage(ErrorType errorType, String errorString) {
        super(null, MessageType.ERROR_MESSAGE);
        this.errorType = errorType;
        this.errorString = errorString;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
