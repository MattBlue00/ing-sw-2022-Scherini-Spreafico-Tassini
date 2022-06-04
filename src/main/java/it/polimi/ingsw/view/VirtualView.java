package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observers.Observer;

import java.util.Map;

public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void askNickname() {
        clientHandler.sendMessage(new AskMessage(AskType.NICKNAME_NOT_UNIQUE));
    }

    @Override
    public void askCreateOrJoin() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    @Override
    public void askGameInfo() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    @Override
    public void askGameNumber() {
        clientHandler.sendMessage(new AskMessage(AskType.GAME_ID));
    }

    @Override
    public void askWizardID() {
        clientHandler.sendMessage(new AskMessage(AskType.WIZARD_ID));
    }

    @Override
    public void askAssistantCard() {
        clientHandler.sendMessage(new AskMessage(AskType.ASSISTANT_CARD));
    }

    @Override
    public void askMoveStudent() {
        clientHandler.sendMessage(new AskMessage(AskType.MOVE_STUDENT));
    }

    @Override
    public void askMotherNatureSteps() {
        clientHandler.sendMessage(new AskMessage(AskType.MOVE_MOTHER_NATURE));
    }

    @Override
    public void askCloud() {
        clientHandler.sendMessage(new AskMessage(AskType.CLOUD_CHOICE));
    }

    @Override
    public void askCharacterCard() {
        clientHandler.sendMessage(new AskMessage(AskType.CHARACTER_CARD));
    }

    @Override
    public void askAction() {
        clientHandler.sendMessage(new AskMessage(AskType.ACTION_CHOICE));
    }

    @Override
    public void showGenericMessage(String message) {
        clientHandler.sendMessage(new GenericMessage(message));
    }

    @Override
    public void showExistingGames(Map<Integer, GameController> existingGames) {
        clientHandler.sendMessage(new ShowExistingGamesMessage(existingGames));
    }

    public void showGameStatusFirstActionPhase(Game game){
        clientHandler.sendMessage(new GameStatusFirstActionPhaseMessage(game));
    }
    @Override
    public void showGameStatus(Game game) {
        clientHandler.sendMessage(new GameStatusMessage(game));
    }

    @Override
    public void showDeck(Game game) { clientHandler.sendMessage(new ShowDeckMessage(game)); }

    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }

    @Override
    public void showDisconnectionMessage(String message) { clientHandler.sendMessage(new DisconnectionMessage(message)); }

}
