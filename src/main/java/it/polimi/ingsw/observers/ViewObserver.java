package it.polimi.ingsw.observers;

public interface ViewObserver {

    void onUpdateServerData(String address, int port);
    void onUpdateNickname(String nickname);
    void onUpdateCreateOrJoin(String choice);
    void onUpdateGameInfo(int gameNumber, boolean mode, int numOfPlayers);
    void onUpdateGameNumber(int gameNumber);
    void onUpdateWizardID(String wizardID);
    void onUpdateAssistantCard(String assistantCard);
    void onUpdateIslandStudentMove(String color, int islandID);
    void onUpdateTableStudentMove(String color);
    void onUpdateMotherNatureSteps(int steps);
    void onUpdateCloudChoice(int cloudID);
    void onUpdateCharacterCard(int characterCardID);
    void onUpdateCharacterCardInt(int characterCardID, int par);
    void onUpdateCharacterCardString(int characterCardID, String par);
    void onUpdateCharacterCardStringInt(int characterCardID, String par1, int par2);
    void onUpdateActionChoice(String choice);

}
