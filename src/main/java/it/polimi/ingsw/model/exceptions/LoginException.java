package it.polimi.ingsw.model.exceptions;

public class LoginException extends TryAgainException{

    public LoginException(String message) {
        super(message);
    }
}
