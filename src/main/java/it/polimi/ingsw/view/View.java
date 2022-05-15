package it.polimi.ingsw.view;

public interface View {
    void askNickname();
    void askCreateOrJoin();
    void askGameInfo();
    void askGameNumber();
    void askWizardID();
    void showGenericMessage(String message);
}
