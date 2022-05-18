package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Jester extends CharacterCard implements ArrayListStringCard, Serializable {

    private List<Student> studentsOnTheCard = new ArrayList<>();
    private List<String> studentsToMove;

    public Jester(List<Student> studentsBag) {
        super(7, 1);
        for(int i = 0; i < 6; i++)
            this.studentsOnTheCard.add(studentsBag.remove(studentsBag.size() - 1));
    }

    @Override
    public void doEffect(GameExpertMode game) {
        int maxNumOfChanges = studentsToMove.size();

        while (maxNumOfChanges > 0) {

            Color color1 = Color.valueOf(studentsToMove.get(maxNumOfChanges - 1));
            Color color2 = Color.valueOf(studentsToMove.get(maxNumOfChanges - 2));
            try {

                Student hallStudent =
                        game.getCurrentPlayer().getSchool().getHall().removeStudent(color2.toString());

                Student cardStudent =
                        this.studentsOnTheCard.stream().filter(x -> x.getColor().equals(color1)).findFirst().get();

                if (game.getCurrentPlayer().getSchool().getTable(color1.toString()).getNumOfStudents()
                        >= Constants.TABLE_LENGTH)
                    throw new FullTableException("The " + color1 + " table is full!");

                game.getCurrentPlayer().getSchool().getHall().addStudent(cardStudent);
                this.getStudentsOnTheCard().add(hallStudent);

            } catch (StudentNotFoundException | NonExistentColorException | FullTableException e) {
                e.printStackTrace();
            }
            maxNumOfChanges = maxNumOfChanges - 2;
        }
    }


    // the first color is for the student to pick from the hall,
    // the second color is for the student to pick from the card
    // TODO: if par.size() > 6, throw new exception
    @Override
    public void doOnClick(List<String> par) {
        this.studentsToMove = new ArrayList<>(par);
    }

    public List<Student> getStudentsOnTheCard() { return studentsOnTheCard; }

    @Override
    public void showStudentsOnTheCard(){
        System.out.print("\tStudenti sulla carta: ");
        int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;

        yellowStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
        blueStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
        greenStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
        redStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.getColor().equals(Color.RED)).count();
        pinkStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

        System.out.print(
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);
    }
}
