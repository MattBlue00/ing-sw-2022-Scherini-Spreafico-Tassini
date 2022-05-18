package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;

public interface View {
    void askNickname();
    void askCreateOrJoin();
    void askGameInfo();
    void askGameNumber();
    void askWizardID();
    void askAssistantCard();
    void showGenericMessage(String message);
    void showGameStatus(Game game);
}
