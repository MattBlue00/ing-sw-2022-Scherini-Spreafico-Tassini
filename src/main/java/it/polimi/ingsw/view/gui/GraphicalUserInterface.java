package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.GameBoardSceneController;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

public class GraphicalUserInterface extends ViewObservable implements View {

    private final SceneController sceneController;

    public GraphicalUserInterface(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public SceneController getSceneController() {
        return sceneController;
    }

    @Override
    public void askServerData() {

    }

    @Override
    public void askNickname() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "nickname_scene.fxml"));
    }

    @Override
    public void askCreateOrJoin() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "lobby_scene.fxml"));
    }

    @Override
    public void askGameInfo() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "creating_game_scene.fxml"));
    }

    @Override
    public void askGameNumber() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "joining_game_scene.fxml"));
    }

    @Override
    public void askWizardID() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "wizard_choice_scene.fxml"));
    }

    @Override
    public void askAssistantCard() {

    }

    @Override
    public void askMoveStudent() {

    }

    @Override
    public void askMotherNatureSteps() {

    }

    @Override
    public void askCloud() {

    }

    @Override
    public void askCharacterCard() {

    }

    @Override
    public void askAction() {

    }

    @Override
    public void showGenericMessage(String message) {
        Platform.runLater(()-> sceneController.showAlert(message));
    }

    @Override
    public void showExistingGames(Map<Integer, GameController> existingGames) {
        Platform.runLater(()-> sceneController.showAlert(printExistingGames(existingGames)));
    }

    @Override
    public void showGameStatusFirstActionPhase(Game game) {
        //GameBoardSceneController boardSceneController = getBoardSceneController();
        //Platform.runLater(() -> sceneController.changeRootPane(boardSceneController, "gameBoard_scene.fxml"));
    }

    @Override
    public void showGameStatus(Game game) {
        //GameBoardSceneController boardSceneController = getBoardSceneController();
        //Platform.runLater(() -> sceneController.changeRootPane(boardSceneController, "gameBoard_scene.fxml"));
    }

    @Override
    public void showDeck(Game game) {
        GameBoardSceneController boardSceneController = getBoardSceneController();
        Platform.runLater(() -> sceneController.changeRootPane(boardSceneController, "gameBoard_scene.fxml"));
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected) {
        Platform.runLater(() -> sceneController.showAlert("Player "+nicknameDisconnected+" has disconnected"));
        Platform.runLater(() -> sceneController.changeRootPane(observers, "lobby_scene.fxml"));
    }

    private GameBoardSceneController getBoardSceneController() {
        GameBoardSceneController boardSceneController;
        try {
            boardSceneController = (GameBoardSceneController) sceneController.getCurrentController();
        } catch (ClassCastException e) {
            boardSceneController = new GameBoardSceneController();
            boardSceneController.addAllObservers(observers);
            GameBoardSceneController finalBsc = boardSceneController;
            Platform.runLater(() -> sceneController.changeRootPane(finalBsc, "gameBoard_scene.fxml"));
        }
        return boardSceneController;
    }

    public String printExistingGames(Map<Integer, GameController> existingGames){
        String str = "";
        if(existingGames.isEmpty())
            str = "No games have been created yet.";
        else {
            str = str.concat("Existing games list: \n");
            for(Map.Entry<Integer, GameController> entry : existingGames.entrySet()) {
                Integer key = entry.getKey();
                GameController value = entry.getValue();
                str = str.concat("- "+key+": ");
                List<Player> players = value.getGame().getPlayers();
                for(int i = 0; i < players.size() - 1; i++){
                   str = str.concat(players.get(i).getNickname() + ", ");
                }
                try {
                    if (players.size() == value.getGame().getPlayersNumber())
                        str = str.concat(players.get(players.size() - 1).getNickname() + " (FULL)\n");
                    else
                        str = str.concat(players.get(players.size() - 1).getNickname() + " (WAITING FOR PLAYERS TO JOIN)\n");
                }catch(IndexOutOfBoundsException e){str = str.concat("NO PLAYERS IN GAME YET\n");}
            }
        }
        return str;
    }
}
