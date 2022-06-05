package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;

import java.util.Map;

public interface View {
    void askServerData();
    void askNickname();
    void askCreateOrJoin();
    void askGameInfo();
    void askGameNumber();
    void askWizardID();
    void askAssistantCard();
    void askMoveStudent();
    void askMotherNatureSteps();
    void askCloud();
    void askCharacterCard();
    void askAction();
    void showGenericMessage(String message);
    void showExistingGames(Map<Integer, GameController> existingGames);
    void showGameStatusFirstActionPhase(Game game);
    void showGameStatus(Game game);
    void showDeck(Game game);
    void showDisconnectionMessage(String message);
}
