package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.utils.ANSIConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Allows the player to switch from 0 to 3 students between the hall and the students on the card.
 */

public class Jester extends CharacterCard implements ArrayListStringCard, StudentsCard, Serializable {

    private final List<Student> studentsOnTheCard;
    private List<String> studentsToMove;

    /**
     * Character Card constructor.
     *
     * @param studentsBag the bag to take students from.
     */

    public Jester(List<Student> studentsBag) {
        super(7, 1);
        studentsOnTheCard = new ArrayList<>();
        for(int i = 0; i < 6; i++)
            this.studentsOnTheCard.add(studentsBag.remove(studentsBag.size() - 1));
    }

    /**
     * Allows the {@link Jester} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Jester} will do his effect.
     * @throws TryAgainException if at least one of the specified students does not exist or if the player somehow
     * manages to provide a parameter with size greater than 6 (client-side input controls should prevent this).
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {

        int maxNumOfChanges = studentsToMove.size();
        if(studentsToMove.size() > 6)
            throw new TryAgainException("You can switch a maximum of 3 students!");

        while (maxNumOfChanges > 0) {

            // the first color is for the student to pick from the hall
            Color color1 = Color.valueOf(studentsToMove.get(maxNumOfChanges - 2));
            // the second color is for the student to pick from the card
            Color color2 = Color.valueOf(studentsToMove.get(maxNumOfChanges - 1));

            Student hallStudent =
                    game.getCurrentPlayer().getSchool().getHall().removeStudent(color1.toString());

            Optional<Student> cardStudentOptional =
                    this.studentsOnTheCard.stream().filter(x -> x.color().equals(color2)).findFirst();

            if(cardStudentOptional.isEmpty()) {
                game.getCurrentPlayer().getSchool().getHall().addStudent(hallStudent);
                throw new StudentNotFoundException("There's no " + color2 + " student on the card!");
            }

            Student cardStudent = cardStudentOptional.get();
            game.getCurrentPlayer().getSchool().getHall().addStudent(cardStudent);
            this.studentsOnTheCard.remove(cardStudent);
            this.studentsOnTheCard.add(hallStudent);

            maxNumOfChanges = maxNumOfChanges - 2;
        }
    }

    /**
     * Sets the parameter of the {@link Healer}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(List<String> par) {
        this.studentsToMove = new ArrayList<>(par);
    }

    /**
     * Returns a list of the students on the card.
     *
     * @return a list of the students on the card.
     */

    public List<Student> getStudentsOnTheCard() { return studentsOnTheCard; }

    /**
     * Allows the view to properly show the students on the card.
     */

    @Override
    public void showStudentsOnTheCard(){
        System.out.print("\t\tStudents on the card: ");
        int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;

        yellowStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.color().equals(Color.YELLOW)).count();
        blueStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.color().equals(Color.BLUE)).count();
        greenStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.color().equals(Color.GREEN)).count();
        redStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.color().equals(Color.RED)).count();
        pinkStudents =
                (int) this.getStudentsOnTheCard().stream().filter(x -> x.color().equals(Color.PINK)).count();

        System.out.print(
                ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);
    }
}
