package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;

import java.io.Serializable;
import java.util.List;

/**
 * Allows the player to move a specified student from the card to a specified island.
 */

public class Monk extends CharacterCard implements StringIntCard, Serializable {

    private Student[] students;
    private String chosenColor;
    private int chosenIsland;

    /**
     * Character Card constructor.
     *
     * @param studentsBag the bag to take students from.
     */

    public Monk(List<Student> studentsBag) {
        super(1, 1);
        this.students = new Student[4];
        for(int i = 0; i < students.length; i++)
            students[i] = studentsBag.remove(studentsBag.size() - 1);
    }

    /**
     * Sets the parameter of the {@link Healer}.
     *
     * @param par1 the {@code String} parameter to set.
     * @param par2 the {@code int} parameter to set.
     */

    @Override
    public void doOnClick(String par1, int par2){
        chosenColor = par1;
        chosenIsland = par2;
    }

    /**
     * Allows the {@link Monk} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Monk} will do his effect.
     * @throws TryAgainException if a student of the specified color does not currently exist on the card or if the
     * island ID provided does not correspond to any of the existing islands.
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        boolean done = false;
        for (int i = 0; i < 4; i++) {
            if (students[i].getColor().toString().equals(chosenColor)) {
                game.getBoard().getIslands().getIslandFromID(chosenIsland).addStudent(students[i]);
                try {
                    students[i] = game.getBoard().getStudentsBag().remove(game.getBoard().getStudentsBag().size() - 1);
                }
                catch(IndexOutOfBoundsException ignored){}
                done = true;
                break;
            }
        }
        if(!done)
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
