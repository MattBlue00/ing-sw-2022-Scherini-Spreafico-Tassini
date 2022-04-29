package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;

import java.util.List;

public class Innkeeper extends CharacterCard {

    /*
        CHARACTER CARD DESCRIPTION:
        The innkeeper allows the player to call a special version of the method profCheck, which reassigns the
        professor even if the current player has the same amount of students of the same color of the current owner
        of the professor.
    */

    public Innkeeper() {
        super(2, 2);
    }

    // no parameters
    @Override
    public void doEffect(GameExpertMode game) {

        final List<Player> players = game.getPlayers();

        Color[] colors = Color.values();

        try {
            for (Color color : colors) {

                int dimBiggestTable = 0;   // number of students to beat in order to claim a professor
                int playerWithProf = -1;    // index of the player who currently has this color's professor
                int playerWhoLostProf = -1; // index of the player whose prof has been claimed

                // true if this color's professor was already claimed by a player during one of the previous rounds
                boolean professorAssigned = false;

                // for each color, stores how many students each player has
                int[] numOfStudents = new int[players.size()];

                for (int i = 0; i < players.size(); i++) {
                    numOfStudents[i] =
                            players.get(i).getSchool().getTable(color.toString()).getNumOfStudents();
                    if (players.get(i).getSchool().getTable(color.toString()).getHasProfessor()) {
                        playerWithProf = i;
                        professorAssigned = true;
                    }
                }

                // if this color's professor was already assigned, profCheck rules vary
                if (professorAssigned) {
                    dimBiggestTable = numOfStudents[playerWithProf];
                }

                for (int i = 0; i < players.size(); i++) {
                    if (numOfStudents[i] >=  dimBiggestTable && numOfStudents[i] > 0
                            && i != playerWhoLostProf) {
                        // if clause needed to store which player's table will have its "hasProfessor" flag
                        // set to false, in case a player actually claimed that prof before
                        if (professorAssigned)
                            playerWhoLostProf = playerWithProf;
                        dimBiggestTable = numOfStudents[i];
                        playerWithProf = i;
                    }
                }

                // sets the "hasProfessor" flags accordingly
                if(playerWithProf != -1)
                    players.get(playerWithProf).getSchool().getTable(color.toString()).setHasProfessor(true);

                // if the professor was already assigned and its owner did change
                if (playerWhoLostProf != -1)
                    players.get(playerWhoLostProf).getSchool().getTable(color.toString()).setHasProfessor(false);

            }
        }
        catch(NonExistentColorException e){
            e.printStackTrace();
        }

    }
}

