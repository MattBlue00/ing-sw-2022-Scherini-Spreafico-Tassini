package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;

import java.io.Serializable;
import java.util.List;

public class Princess extends CharacterCard implements StringCard, Serializable {

    private Student[] students;
    private String chosenColor;

    public Princess(List<Student> studentsBag) {
        super(11, 2);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    @Override
    public void doOnClick(String par){
        chosenColor = par;
    }

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        boolean done = false;
        for (int i = 0; i < 4; i++) {
            if (students[i].getColor().toString().equals(chosenColor)) {
                game.getCurrentPlayer().getSchool().getTable(students[i].getColor().toString()).addStudent(
                        students[i], game.getCurrentPlayer()
                );
                students[i] = game.getBoard().getStudentsBag().remove(game.getBoard().getStudentsBag().size() - 1);
                done = true;
                break;
            }
        }
        if(!done)
            throw new StudentNotFoundException("There's no such student on the card!");
    }

    // Debug method

    public Student[] getStudents(){
        return students;
    }

    //TODO: what if parameters are incorrect? We should give players other attempts!

    @Override
    public void showStudentsOnTheCard(){
        System.out.print("\t\tStudents on the card: ");

        int yellowStudents = 0, blueStudents = 0, greenStudents = 0, redStudents = 0, pinkStudents = 0;

        for (int i = 0; i < 4; i++){
            if(students[i].getColor().toString().equals("YELLOW"))
                yellowStudents++;
            else if(students[i].getColor().toString().equals("BLUE"))
                blueStudents++;
            else if(students[i].getColor().toString().equals("GREEN"))
                greenStudents++;
            else if(students[i].getColor().toString().equals("RED"))
                redStudents++;
            else pinkStudents++;
        }

        System.out.print(
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

    }

}
