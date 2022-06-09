package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;

import java.io.Serializable;
import java.util.List;

/**
 * Allows the player to move a specified student from the card to the appropriate table.
 */

public class Princess extends CharacterCard implements StringCard, Serializable {

    private final Student[] students;
    private String chosenColor;

    /**
     * Character Card constructor.
     *
     * @param studentsBag the bag to take students from.
     */

    public Princess(List<Student> studentsBag) {
        super(11, 2);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    /**
     * Sets the parameter of the {@link Healer}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(String par){
        chosenColor = par;
    }

    /**
     * Allows the {@link Princess} to do her effect.
     *
     * @param game the game (Expert mode) in which the {@link Princess} will do his effect.
     * @throws TryAgainException if a student of the specified color is not present on the card or if the player
     * somehow manages to provide a non-existent color.
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        boolean done = false;
        for (int i = 0; i < 4; i++) {
            if (students[i].getColor().toString().equals(chosenColor)) {
                game.getCurrentPlayer().getSchool().getTable(students[i].getColor().toString()).addStudent(
                        students[i], game.getCurrentPlayer()
                );
                try {
                    students[i] = game.getBoard().getStudentsBag().remove(game.getBoard().getStudentsBag().size() - 1);
                }
                catch(IndexOutOfBoundsException ignored){}
                done = true;
                break;
            }
        }
        if (!done)
            throw new StudentNotFoundException("There's no such student on the card!");
    }

    /**
     * Returns an array containing the students currently present on the card.
     *
     * @return an array containing the students currently present on the card.
     */

    public Student[] getStudents(){
        return students;
    }

    /**
     * Allows the view to properly show the students on the card.
     */

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
