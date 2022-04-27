package it.polimi.ingsw.model.exceptions;

public class StudentNotFoundException extends TryAgainException{

    public StudentNotFoundException(String message) {
        super(message);
    }
}
