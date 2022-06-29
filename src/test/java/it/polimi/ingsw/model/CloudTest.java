package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTest{

    @Test
    public void testAddStudent() {

        Student s1 = new Student(Color.PINK);
        Cloud cloud = new Cloud(3);

        cloud.addStudent(s1);

        assertEquals(s1, cloud.getStudents().get(0));
    }

    @Test
    public void testRemoveStudent() {

        Student s1 = new Student(Color.PINK);
        Cloud cloud = new Cloud(3);
        List<Student> students = null;

        cloud.addStudent(s1);

        try {
            students = cloud.removeStudents();
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assert students != null;
        assertEquals(s1, students.get(0));
    }

    @Test
    public void testRemoveStudentFromEmptyCloud() {

        Student s1 = new Student(Color.PINK);
        Student s2 = new Student(Color.PINK);
        Student s3 = new Student(Color.PINK);
        Cloud cloud = new Cloud(3);
        List<Student> students = null;

        cloud.addStudent(s1);
        cloud.addStudent(s2);
        cloud.addStudent(s3);

        try {
            students = cloud.removeStudents();
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assert students != null;
        assertEquals(s1, students.get(0));
        assertEquals(s2, students.get(1));
        assertEquals(s3, students.get(2));

        assertThrows(EmptyCloudException.class, cloud::removeStudents);
    }


}