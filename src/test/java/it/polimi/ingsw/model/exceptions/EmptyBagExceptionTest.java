package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmptyBagExceptionTest {

    @Test
    public void exceptionTest(){

        GameExpertMode g1 = new GameExpertMode(2);
        List<Student> list = new ArrayList<>(0);
        g1.getBoard().setStudentsBag(list);

        assertThrows(EmptyBagException.class,
                () -> g1.refillClouds());

    }

}