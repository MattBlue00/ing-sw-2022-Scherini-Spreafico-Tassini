package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerExpertMode;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.charactercards.Flagman;
import it.polimi.ingsw.model.charactercards.Healer;
import it.polimi.ingsw.model.charactercards.Innkeeper;
import it.polimi.ingsw.network.message.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class RoundSimulationApp
{
    public static void main(String[] args) {

        GameController gc = new GameControllerExpertMode();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        gc.prepareGame(message.getPlayerNumber());

        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.startGame();
        CharacterCard c1 = new Healer();
        CharacterCard c2 = new Innkeeper();
        CharacterCard c3 = new Flagman();
        GameExpertMode g = (GameExpertMode) gc.getGame();
        g.addCharacterCards(new CharacterCard[]{c1, c2, c3});
        p1.setCoinsWallet(5);
        p2.setCoinsWallet(5);
        g.getBoard().setMotherNaturePos(12);
        g.showGameBoard();

        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "TURTLE"));
        }
        catch(Exception ignored){}

        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "FOX"));
        }
        catch(Exception ignored){}

        List<Student> bag = new ArrayList<>(30);
        for(int i = 0; i < 10; i++)
            bag.add(new Student(Color.PINK));

        try {
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToIslandMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK", 1));

            gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 1));
            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 0));

            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToIslandMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK", 3));

            ((Healer) c1).doOnClick(5);
            gc.getMessage(new CharacterCardMessage(gc.getGame().getCurrentPlayer().getNickname(), 5));
            gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 2));
            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 1));

        }
        catch(Exception ignored){}

        gc.getGame().showGameBoard();

    }
}
