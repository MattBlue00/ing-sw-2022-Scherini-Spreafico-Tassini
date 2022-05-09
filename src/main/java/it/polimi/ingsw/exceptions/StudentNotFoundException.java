package it.polimi.ingsw.exceptions;

public class StudentNotFoundException extends TryAgainException{

    public StudentNotFoundException(String message) {
        super(message);
    }
}
