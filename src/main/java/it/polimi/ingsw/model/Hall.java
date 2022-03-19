package it.polimi.ingsw.model;

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

    public Optional<Student> removeStudent(String color){
        return this.students.stream().filter(student -> student.getColor().toString().equals(color)).findFirst();
    }
}
