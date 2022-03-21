package it.polimi.ingsw.model.exceptions;

public class NonExistentTableException extends Exception{

    public NonExistentTableException(String message) {
        super(message);
    }
}
