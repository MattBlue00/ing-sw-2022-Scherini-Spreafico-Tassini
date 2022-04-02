package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;

import java.util.ArrayList;

public class Island {

    private Player owner;
    private final int id;
    private int ownerInfluence;
    private int numOfTowers;
    private ArrayList<Student> students;


    public Island(int id) {
        this.id = id;
        this.ownerInfluence = 0;
    }

    public int getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getOwnerInfluence() {
        return ownerInfluence;
    }

    public void setOwnerInfluence(int ownerInfluence) {
        this.ownerInfluence = ownerInfluence;
    }

    public void addStudent(Student student){
        students.add(student);
    }

    // TODO: check if it is correct
    public int influenceCalc(Player currentPlayer){
        int currentPlayerInfluence = 0;
        int validStudents = 0;
        currentPlayerInfluence = currentPlayerInfluence + numOfTowers;
        for(Student student : students){
            try {
                if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                    validStudents++;
                }
            } catch (NonExistentTableException e) {
                e.printStackTrace();
            }
        }
        currentPlayerInfluence = currentPlayerInfluence + validStudents;
        return currentPlayerInfluence;
    }
}
