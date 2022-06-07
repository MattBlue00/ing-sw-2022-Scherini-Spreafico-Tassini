package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Hall is a third of the {@link School}. It holds the students that may be moved to the School's dining room or
 * to one of the game's islands.
 */

public class Hall implements Serializable {

    private final int capacity;
    private ArrayList<Student> students;

    /**
     * Hall constructor.
     *
     * @param capacity the maximum number of students that can be in the {@link Hall} at the same time.
     */

    public Hall(int capacity) {
        this.capacity = capacity;
        students = new ArrayList<>();
    }

    /**
     * Adds the given student to the {@link Hall}.
     *
     * @param addedStudent the student to add.
     */

    public void addStudent(Student addedStudent){
        students.add(addedStudent);
    }

    /**
     * Returns a list of all the students currently present in the {@link Hall}.
     *
     * @return a list of all the students currently present in the {@link Hall}.
     */

    public List<Student> getStudents(){
        return students;
    }

    /**
     * Returns a student of the given color, removing it from the ones in the {@link Hall}, if such student is present.
     *
     * @param color the color of the student to remove.
     * @return the student of the desired color.
     * @throws StudentNotFoundException if a student of the given color does not currently exist in the {@link Hall}.
     */

    public Student removeStudent(String color) throws StudentNotFoundException {

        Optional<Student> studentToRemove =
                this.students.stream().filter(student -> student.getColor().toString().equals(color)).findFirst();

        if(studentToRemove.isPresent()) {
            this.students.remove(studentToRemove.get());
            return studentToRemove.get();
        }
        else
            throw new StudentNotFoundException("There's no " + color + " student in the hall!");

    }
}
