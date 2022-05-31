package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;

import java.io.Serializable;
import java.util.List;

public class Monk extends CharacterCard implements StringIntCard, Serializable {

    private Student[] students;
    private String chosenColor;
    private int chosenIsland;

    public Monk(List<Student> studentsBag) {
        super(1, 1);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    @Override
    public void doOnClick(String par1, int par2){
        chosenColor = par1;
        chosenIsland = par2;
    }

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        boolean done = false;
        for (int i = 0; i < 4; i++) {
            if (students[i].getColor().toString().equals(chosenColor)) {
                game.getBoard().getIslands().getIslandFromID(chosenIsland).addStudent(students[i]);
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
