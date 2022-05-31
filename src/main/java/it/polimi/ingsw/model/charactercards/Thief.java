package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.Locale;

public class Thief extends CharacterCard implements StringCard, Serializable {

    private String chosenColor;

    public Thief() {
        super(12, 3);
    }

    @Override
    public void doOnClick(String par) {
        chosenColor = par.toUpperCase(Locale.ROOT);
    }

    @Override
    public void doEffect(GameExpertMode game) throws TryAgainException {
        for (Player player : game.getPlayers()) {
            int count = 3;
            while (player.getSchool().getTable(this.chosenColor).getStudents().size() > 0 && count > 0) {
                Student student = player.getSchool().getTable(this.chosenColor).removeStudent();
                game.getBoard().getStudentsBag().add(student);
                count--;
            }
        }
    }

}