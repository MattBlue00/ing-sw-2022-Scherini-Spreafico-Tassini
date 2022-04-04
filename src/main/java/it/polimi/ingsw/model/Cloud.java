package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyCloudException;

import java.util.ArrayList;
import java.util.List;


public class Cloud {

    private final int capacity;
    private boolean hasBeenChosen;
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
        List<Student> chosenStudents = new ArrayList<Student>(students);
        if(students.isEmpty()) throw new EmptyCloudException("The chosen cloud is empty");
        students.clear();
        return chosenStudents;
    }

    // For debugging

    public ArrayList<Student> getStudents() {
        return students;
    }
}
