package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;

import java.util.List;

public class GraphicalUserInterface extends ViewObservable implements View {

    private final SceneController sceneController;

    public GraphicalUserInterface(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public SceneController getSceneController() {
        return sceneController;
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

    }

    @Override
    public void askGameNumber() {

    }

    @Override
    public void askWizardID() {

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
        Platform.runLater(()->sceneController.showAlert(message));
    }

    @Override
    public void showExistingGames(List<Integer> existingGames) {

    }

    @Override
    public void showGameStatusFirstActionPhase(Game game) {

    }

    @Override
    public void showGameStatus(Game game) {

    }

    @Override
    public void showDeck(Game game) {

    }
}
