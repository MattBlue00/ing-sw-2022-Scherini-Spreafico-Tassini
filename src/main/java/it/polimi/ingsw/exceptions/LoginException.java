package it.polimi.ingsw.exceptions;

public class LoginException extends TryAgainException{

    public LoginException(String message) {
        super(message);
    }
}
