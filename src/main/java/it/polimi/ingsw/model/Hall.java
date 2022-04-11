package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hall {

    private final int capacity;
    private ArrayList<Student> students;

    public Hall(int capacity) {
        this.capacity = capacity;
        students = new ArrayList<>();
    }

    public void addStudent(Student addedStudent){
        students.add(addedStudent);
    }

    public List<Student> getStudents(){
        return students;
    }

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
