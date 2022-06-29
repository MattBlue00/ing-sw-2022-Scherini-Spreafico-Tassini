package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenecontrollers.GameBoardSceneController;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

/**
 * This class offers a User Interface via GUI. It is an implementation of the {@link View}.
 */

public class GraphicalUserInterface extends ViewObservable implements View {

    private final SceneController sceneController;
    private GameBoardSceneController boardController;
    private String nickname;

    /**
     * GraphicalUserInterface constructor.
     *
     * @param sceneController the {@link SceneController} that will control what will be shown to the user.
     */

    public GraphicalUserInterface(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Returns the scene controller.
     *
     * @return the scene controller.
     */

    public SceneController getSceneController() {
        return sceneController;
    }

    /**
     * Sets the view user's nickname.
     *
     * @param nickname the nickname chosen by the client.
     */

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Asks the client information about what server they want to try to connect to. The GUI does not need this method,
     * so it has not been implemented.
     */

    @Override
    public void askServerData() { }

    /**
     * Asks the client which nickname they want to use.
     * If it is unique, the client joins the lobby; otherwise, they are asked to choose a different nickname.
     */

    @Override
    public void askNickname() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "nickname_scene.fxml"));
    }

    /**
     * Asks the client if they want to create or join a game.
     */

    @Override
    public void askCreateOrJoin() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "lobby_scene.fxml"));
    }

    /**
     * Asks the client about the game info: number of players and game mode.
     */

    @Override
    public void askGameInfo() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "creating_game_scene.fxml"));
    }

    /**
     * Asks the client the gameID (which must be unique).
     * If it is unique a new game is created, else they are asked to choose a different gameID.
     */

    @Override
    public void askGameNumber() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "joining_game_scene.fxml"));
    }

    /**
     * Asks the client which WizardID they want to choose (must be unique).
     * If it is unique the client is added to the game, else they are asked to choose a different Wizard.
     */

    @Override
    public void askWizardID() {
        Platform.runLater(()-> sceneController.changeRootPane(observers, "wizard_choice_scene.fxml"));
    }

    /**
     * Asks the client which Assistant Card they want to play.
     */

    @Override
    public void askAssistantCard() {
        Platform.runLater(() -> boardController.activateAssistantCardChoice());
    }

    /**
     * Asks the client where they want to move the student (on an island or on the correspondent table).
     */

    @Override
    public void askMoveStudent() {
        Platform.runLater(() -> boardController.showUpdate("You can pick students from your hall."));
        Platform.runLater(() -> boardController.activateMoveStudent());
    }

    /**
     * Asks the client how many steps they wish to move Mother Nature of.
     */

    @Override
    public void askMotherNatureSteps() {
        Platform.runLater(() -> boardController.showUpdate("Where do you want to move Mother Nature?"));
        Platform.runLater(() -> boardController.activateIslands());
    }

    /**
     * Asks the client which cloud they want to choose.
     */

    @Override
    public void askCloud() {
        Platform.runLater(() -> boardController.showUpdate("Which cloud do you want to pick students from?"));
        Platform.runLater(() -> boardController.activateCloudChoice());
    }

    /**
     * Asks the client which Character Card they want to play (Expert mode exclusive).
     */

    @Override
    public void askCharacterCard() {
        Platform.runLater(() -> boardController.showUpdate("You can also play a Character Card."));
        Platform.runLater(() -> boardController.activateCharacterCards());
    }

    /**
     * Asks the client if they want to play a CharacterCard or to move a student (Expert mode exclusive).
     */

    @Override
    public void askAction() {
        if(boardController.getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            askMoveStudent();
        }
        else {
            askMoveStudent();
            askCharacterCard();
        }
    }

    /**
     * Shows a generic message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    @Override
    public void showGenericMessage(String message) {
        Platform.runLater(()-> sceneController.showAlert(message));
    }

    /**
     * Shows the client in which round phase they are. This method is not used by the GUI, so it has not been
     * implemented.
     *
     * @param isActionPhase {@code true} if the current player is about to play the Action Phase, {@code false}
     *        otherwise.
     */

    @Override
    public void showPhaseUpdate(boolean isActionPhase) {}

    /**
     * Shows the client what happened on the {@link GameBoard}.
     *
     * @param s the update to notify.
     */

    @Override
    public void showUpdateMessage(String s) {
        Platform.runLater(() -> boardController.showUpdate(s));
    }

    /**
     * Shows to the client the existing games they may join.
     */

    @Override
    public void showExistingGames(Map<Integer, GameController> existingGames) {
        askCreateOrJoin();
        Platform.runLater(()-> sceneController.showAlert(printExistingGames(existingGames)));
    }

    /**
     * Since the GUI has an easily accessible history box, acts just like the {@code showGameStatus} method.
     *
     * @param game the game whose status needs to be shown.
     */

    @Override
    public void showGameStatusFirstActionPhase(Game game) {
        showGameStatus(game);
    }

    /**
     * Shows the current Game Board to the client.
     */

    @Override
    public void showGameStatus(Game game) {
        GameBoardSceneController boardSceneController = getBoardSceneController();
        boardSceneController.setGame(game);
        boardSceneController.setNickname(nickname);
        Platform.runLater(boardSceneController::renderGameBoard);
    }

    /**
     * Shows to the client the AssistantCards they can play. In GUI, the deck is always visible, so this method is not
     * needed and was not implemented.
     *
     * @param game the game whose status needs to be shown.
     */

    @Override
    public void showDeck(Game game) {}

    /**
     * Shows a disconnection message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    @Override
    public void showDisconnectionMessage(String message) {
        Platform.runLater(() -> sceneController.showAlert(message));
        Platform.runLater(() -> sceneController.changeRootPane(observers, "lobby_scene.fxml"));
    }

    /**
     * Returns the controller of the game board. If it is not present, creates a new one.
     *
     * @return the current {@link GameBoardSceneController}.
     */

    private GameBoardSceneController getBoardSceneController() {
        GameBoardSceneController boardSceneController;
        try {
            boardSceneController = (GameBoardSceneController) sceneController.getCurrentController();
        } catch (ClassCastException e) {
            boardSceneController = new GameBoardSceneController();
            boardSceneController.addAllObservers(observers);
            GameBoardSceneController finalBsc = boardSceneController;
            this.boardController = boardSceneController;
            Platform.runLater(() -> sceneController.changeRootPane(finalBsc, "gameBoard_scene.fxml"));
            Platform.runLater(finalBsc::startGameBoard);
        }
        return boardSceneController;
    }

    /**
     * Prints onto an alert the list of existing games, along with the clients associated to them and the information
     * about its setup status (accepting players / full).
     *
     * @param existingGames a map containing the existing game IDs as keys and the associated game controller as values.
     * @return the text to print.
     */

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

    /**
     * Closes the client's app.
     */

    @Override
    public void quit() {
        Platform.exit();
        System.exit(0);
    }

}
