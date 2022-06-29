package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observers.Observer;

import java.util.Map;

/**
 * This class hides the network implementation from the controller.
 * The controller calls methods from this class as if it was a normal view.
 * Instead, a network protocol is used to communicate with the real view on the client side.
 */

public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    /**
     * Virtual view constructor.
     *
     * @param clientHandler the {@link ClientHandler} to send messages to.
     */

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Asks the client information about what server they want to try to connect to.
     */

    @Override
    public void askServerData() {}

    /**
     * Asks the client which nickname they want to use.
     * If it is unique, the client joins the lobby; otherwise, they are asked to choose a different nickname.
     */

    @Override
    public void askNickname() {
        clientHandler.sendMessage(new AskMessage(AskType.NICKNAME_NOT_UNIQUE));
    }

    /**
     * Asks the client if they want to create or join a game.
     */

    @Override
    public void askCreateOrJoin() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    /**
     * Asks the client about the game info: number of players and game mode.
     */

    @Override
    public void askGameInfo() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    /**
     * Asks the client the gameID (which must be unique).
     * If it is unique a new game is created, else they are asked to choose a different gameID.
     */

    @Override
    public void askGameNumber() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    /**
     * Asks the client which WizardID they want to choose (must be unique).
     * If it is unique the client is added to the game, else they are asked to choose a different Wizard.
     */

    @Override
    public void askWizardID() {
        clientHandler.sendMessage(new AskMessage(AskType.WIZARD_ID));
    }

    /**
     * Asks the client which Assistant Card they want to play.
     */

    @Override
    public void askAssistantCard() {
        clientHandler.sendMessage(new AskMessage(AskType.ASSISTANT_CARD));
    }

    /**
     * Asks the client where they want to move the student (on an island or on the correspondent table).
     */

    @Override
    public void askMoveStudent() {
        clientHandler.sendMessage(new AskMessage(AskType.MOVE_STUDENT));
    }

    /**
     * Asks the client how many steps they wish to move Mother Nature of.
     */

    @Override
    public void askMotherNatureSteps() {
        clientHandler.sendMessage(new AskMessage(AskType.MOVE_MOTHER_NATURE));
    }

    /**
     * Asks the client which cloud they want to choose.
     */

    @Override
    public void askCloud() {
        clientHandler.sendMessage(new AskMessage(AskType.CLOUD_CHOICE));
    }

    /**
     * Asks the client which Character Card they want to play (Expert mode exclusive).
     */

    @Override
    public void askCharacterCard() {
        clientHandler.sendMessage(new AskMessage(AskType.CHARACTER_CARD));
    }

    /**
     * Asks the client if they want to play a CharacterCard or to move a student (Expert mode exclusive).
     */

    @Override
    public void askAction() {
        clientHandler.sendMessage(new AskMessage(AskType.ACTION_CHOICE));
    }

    /**
     * Shows a generic message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    @Override
    public void showGenericMessage(String message) {
        clientHandler.sendMessage(new GenericMessage(message));
    }

    /**
     * Shows to the client the existing games they may join.
     */

    @Override
    public void showExistingGames(Map<Integer, GameController> existingGames) {
        clientHandler.sendMessage(new ShowExistingGamesMessage(existingGames));
    }

    /**
     * Shows to the client the latest Assistant Cards played, the Game Board and the players' order.
     *
     * @param game the game whose status needs to be shown.
     */

    public void showGameStatusFirstActionPhase(Game game){
        clientHandler.sendMessage(new GameStatusFirstActionPhaseMessage(game));
    }

    /**
     * Shows the current Game Board to the client.
     *
     * @param game the game whose status needs to be shown.
     */

    @Override
    public void showGameStatus(Game game) {
        clientHandler.sendMessage(new GameStatusMessage(game));
    }

    /**
     * Shows to the client the AssistantCards they can play.
     *
     * @param game the game whose status needs to be shown.
     */

    @Override
    public void showDeck(Game game) { clientHandler.sendMessage(new ShowDeckMessage(game)); }

    /**
     * Shows a disconnection message to the client.
     *
     * @param message the message that will be shown to the client.
     */

    @Override
    public void showDisconnectionMessage(String message) { clientHandler.sendMessage(new DisconnectionMessage(message)); }

    /**
     * Shows the client in which round phase they are.
     *
     * @param isActionPhase {@code true} if the current player is about to play the Action Phase, {@code false}
     *        otherwise.
     */

    public void showPhaseUpdate(boolean isActionPhase) {
        clientHandler.sendMessage(new GamePhaseMessage(isActionPhase));
    }

    /**
     * Shows the client what happened on the {@link GameBoard}.
     *
     * @param s the update to notify.
     */

    @Override
    public void showUpdateMessage(String s) {
        clientHandler.sendMessage(new UpdateMessage(s));
    }

    /**
     * Closes the client's app.
     */

    @Override
    public void quit() {
        clientHandler.sendMessageAndQuit(new EndGameMessage());
    }

    /**
     * Sets the view user's nickname. The virtual view doesn't need this method, so it was not implemented.
     *
     * @param nickname the nickname chosen by the client.
     */

    @Override
    public void setNickname(String nickname) {
    }

    /**
     * Updates the observed object with the given message.
     *
     * @param message the update message.
     */

    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }

}
