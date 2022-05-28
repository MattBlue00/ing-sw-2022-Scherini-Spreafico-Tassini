package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cloud implements Serializable {

    private final int capacity;
    private ArrayList<Student> students;

    public Cloud(int capacity) {
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    // Getter method

    public int getCapacity() {
        return capacity;
    }

    // Cloud methods

    public void addStudent(Student student){
        students.add(student);
    }

    /*
        When it is called by the Gameboard it creates a new list of students copying the existing one on the cloud,
        then clears the cloud's students array.

        If the cloud is already empty we throw an EmptyCloudException
     */
    public List<Student> removeStudents() throws EmptyCloudException {
        List<Student> chosenStudents = new ArrayList<>(students);
        if(students.isEmpty()) throw new EmptyCloudException("The chosen cloud has already been emptied, please choose another one.");
        students.clear();
        return chosenStudents;
    }

    // For debugging

    public ArrayList<Student> getStudents() {
        return students;
    }
}
