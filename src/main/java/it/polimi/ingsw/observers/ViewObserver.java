package it.polimi.ingsw.observers;

public interface ViewObserver {

    void onUpdateServerData(String address, int port);
    void onUpdateNickname(String nickname);
    void onUpdateCreateOrJoin(String choice);
    void onUpdateGameInfo(int gameNumber, boolean mode, int numOfPlayers);
    void onUpdateGameNumber(int gameNumber);
    void onUpdateWizardID(String wizardID);
}
