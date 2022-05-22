package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;

public interface View {
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
    void showGenericMessage(String message);
    void showGameStatus(Game game);
    void showDeck(Game game);
}
