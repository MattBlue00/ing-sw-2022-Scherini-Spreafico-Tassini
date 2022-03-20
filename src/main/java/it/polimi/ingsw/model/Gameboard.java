package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gameboard {

    // gameboard variables
    private int motherNaturePos; // represent the current position of motherNature in the ilsand array
    private final Cloud clouds[];
    private Island islands[];
    private List<Student> studentsBag;


    public Gameboard(int playerNum){
        this.clouds = new Cloud[playerNum];
        this.studentsBag = new ArrayList<>();
        this.islands = new Island[12];


        // just to fill the bag with something
        for(int i=0; i<15; i++){
            studentsBag.add(new Student(Color.RED));
        }

        for(int i = 0; i<playerNum; i++) {
            Cloud cloud = new Cloud(3); // not scalable, need to find a better solution then if/else
            clouds[i] = cloud;
            for(int j=0; j<cloud.getCapacity(); j++)
                cloud.addStudent(studentsBag.remove(studentsBag.size()-1));
            }
        }


    // methods

    /*
        When called by the Game this method removes a student from the bag
        then add the extracted student to the students array of the cloud, one cloud at a time,
        it throws an EmptyBagException if the bag is already empty
     */
    public void refillClouds() throws EmptyBagException{
        for(Cloud c : clouds){
            for(int i=0; i< c.getCapacity(); i++) {
                if(studentsBag.size()==0)
                    throw new EmptyBagException("The bag is now empty");
                Student tempStudent = studentsBag.remove(studentsBag.size()-1);
                c.addStudent(tempStudent);
            }
        }
    }


    /*
        When called by the Game this method call the remove from the single cloud,
        gets a List of students as a response and then move all the students, one at a time,
        to the current player's Hall
     */
    public void takeStudentsFromCloud(int cloudID, final Player curr) throws EmptyCloudException {
        clouds[cloudID].removeStudents().forEach(student -> student.moveToHall(curr));
    }

    // For debugging

    public Cloud getCloud(int i) {
        return clouds[i];
    }

    public List<Student> getStudentsBag() {
        return studentsBag;
    }
}
