package it.polimi.ingsw.view;

public interface View {

    void askPlayerData();

    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    void showErrorAndExit(String error);

}
