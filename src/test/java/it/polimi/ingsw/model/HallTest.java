package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Optional;

public class HallTest extends TestCase {

    @Test
    public void testAddStudent() {

        Hall hall = new Hall(6);
        Student student = new Student(Color.PINK);

        hall.addStudent(student);

        assertEquals(student, hall.getStudents().get(0));
    }

    @Test
    public void testRemoveStudent() {

        Hall hall = new Hall(6);
        Student s1 = new Student(Color.PINK);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.PINK);

        hall.addStudent(s1);
        hall.addStudent(s2);
        hall.addStudent(s1);

        Student s4 = hall.removeStudent("PINK").get();

        //making sure that the removed student is the first one of its color
        assertEquals(s1, s4);
        assertNotSame(s3, s4);
    }
}