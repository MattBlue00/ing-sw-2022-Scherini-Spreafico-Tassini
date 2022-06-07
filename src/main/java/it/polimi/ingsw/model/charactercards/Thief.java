package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.Locale;

/**
 * Once given a (existing) color, the game steals from every player 3 students of that color, and puts them back into
 * the students' bag. If a player has less than three students of that color, they will lose all of them without
 * further damage.
 */

public class Thief extends CharacterCard implements StringCard, Serializable {

    private String chosenColor;

    /**
     * Character Card constructor.
     */

    public Thief() {
        super(12, 3);
    }

    /**
     * Sets the parameter of the {@link Thief}.
     *
     * @param par the parameter to set.
     */

    @Override
    public void doOnClick(String par) {
        chosenColor = par.toUpperCase(Locale.ROOT);
    }

    /**
     * Allows the {@link Thief} to do his effect.
     *
     * @param game the game (Expert mode) in which the {@link Thief} will do his effect.
     * @throws TryAgainException if the player somehow manages to provide a non-existent color.
     */

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        for (Player player : game.getPlayers()) {
            int count = 3;
            while (player.getSchool().getTable(this.chosenColor).getStudents().size() > 0 && count > 0) {
                try {
                    Student student = player.getSchool().getTable(this.chosenColor).removeStudent();
                    game.getBoard().getStudentsBag().add(student);
                }
                catch(StudentNotFoundException ignored){}
                count--;
            }
        }
    }

}