package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HallTest {

    @Test
    public void testAddStudent() {

        Hall hall = new Hall();
        Student s1 = new Student(Color.PINK);

        hall.addStudent(s1);

        assertEquals(s1, hall.getStudents().get(0));
        assertEquals(1, hall.getStudents().size());

    }

    @Test
    public void testRemoveStudent() {

        Hall hall = new Hall();
        Student s1 = new Student(Color.PINK);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.PINK);

        hall.addStudent(s1);
        hall.addStudent(s2);
        hall.addStudent(s3);

        try{
            Student s4 = hall.removeStudent("PINK");
            //making sure that the removed student is the first one of its color
            assertEquals(s1, s4);
            assertNotSame(s3, s4);
        }
        catch(StudentNotFoundException e){
            throw new RuntimeException();
        }

    }
}