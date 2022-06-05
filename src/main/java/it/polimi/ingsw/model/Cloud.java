package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyCloudException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The cloud is one of the "physical" elements of the game. It holds some students (the quantity depends on the number
 * of players playing the game) and waits for a player to choose itself. Once chosen by a player, they take the students
 * from the cloud; until the end of the round, when the cloud gets refilled with the bag students, no other player is
 * allowed to choose the same cloud.
 */

public class Cloud implements Serializable {

    private final int capacity;
    private ArrayList<Student> students;

    /**
     * Cloud constructor.
     *
     * @param capacity the number of students the cloud holds.
     */

    public Cloud(int capacity) {
        this.capacity = capacity;
        this.students = new ArrayList<>();
    }

    /**
     * Returns the capacity of the cloud.
     *
     * @return an {@code int} representing the capacity of the cloud.
     */

    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds a student to the cloud.
     *
     * @param student the {@link Student} to add.
     */

    public void addStudent(Student student){
        students.add(student);
    }

    /**
     * Removes all the students from the cloud (if there are any) and returns a list containing them.
     *
     * @return a list containing the cloud's students.
     * @throws EmptyCloudException if the cloud has no student (it has already been chosen during the same round).
     */

    public List<Student> removeStudents() throws EmptyCloudException {
        List<Student> chosenStudents = new ArrayList<>(students);
        if(students.isEmpty()) throw new EmptyCloudException("The chosen cloud has already been emptied, please choose another one.");
        students.clear();
        return chosenStudents;
    }

    /**
     * Returns an {@link ArrayList} containing the cloud's students without removing them.
     *
     * @return an {@link ArrayList} containing the cloud's students.
     */

    public ArrayList<Student> getStudents() {
        return students;
    }
}
