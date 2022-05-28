package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.network.message.*;

public class GameControllerExpertMode extends GameController{

    public GameControllerExpertMode() {
        super();
    }

    @Override
    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
            case ACTION_CHOICE:
                if(((ActionChoiceMessage) message).getChoice().equals("STUDENT")) {
                    if (!getVirtualViewMap().isEmpty()) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                    }
                }
                else {
                    if (!getVirtualViewMap().isEmpty() && !getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCharacterCard();
                    }
                }
                break;
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                if(getMovesLeft() == 0){
                    throw new WrongMessageSentException("No moves left!");
                }
                else{
                    handleStudentMovement(message);
                    setMovesLeft(getMovesLeft() - 1);
                    if(!getVirtualViewMap().isEmpty() && getMovesLeft() > 0) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        if(!getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed())
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
                        else
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                    }
                    if(!getVirtualViewMap().isEmpty() && getMovesLeft() == 0) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMotherNatureSteps();
                    }
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                if(getMovesLeft() == 0 && !getMotherNatureMoved()){
                    handleMotherNature(message);
                    setMotherNatureMoved(true);
                    getGame().islandConquerCheck(getGame().getBoard().getMotherNaturePos());
                    if(!getVirtualViewMap().isEmpty()) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                else
                    throw new WrongMessageSentException("You need to move other " + getMovesLeft() +
                            " students before moving Mother Nature!");
                break;
            case CLOUD_CHOICE_REPLY:
                if(getMotherNatureMoved())
                    handleCloudChoice(message);
                else
                    throw new WrongMessageSentException("You need to move mother nature first!");
                break;
            case CHARACTER_CARD_REPLY:
                if(!getMotherNatureMoved()) {
                    handleCharacterCardChoice(message);
                    if(!getVirtualViewMap().isEmpty()) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                    }
                }
                else
                    throw new WrongMessageSentException("You are no longer able to play a character card!");
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /*
        This method lets the current player play a character card, if possible.
     */

    public void handleCharacterCardChoice(Message receivedMessage) throws
            CharacterCardAlreadyPlayedException, NotEnoughCoinsException, CharacterCardNotFoundException {

        int chosenCardID = ((CharacterCardMessage) receivedMessage).getCardID();

        if(getGame() instanceof GameExpertMode) {

            if(receivedMessage instanceof CharacterCardMessageInt) {
                ((IntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageInt) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageString) {
                ((StringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageString) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageStringInt) {
                ((StringIntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID))
                        .doOnClick(((CharacterCardMessageStringInt) receivedMessage).getPar1(), ((CharacterCardMessageStringInt) receivedMessage).getPar2());
            }
            if(receivedMessage instanceof CharacterCardMessageArrayListString){
                ((ArrayListStringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageArrayListString) receivedMessage).getPar());
            }

            ((GameExpertMode) getGame()).playerPlaysCharacterCard(chosenCardID);
        }
    }

    @Override
    public void endPlanningPhase() {
        setPlanningPhaseDone(true);
        setOrder();
        getGame().setCurrentPlayer(getGame().getPlayers().get(0));
        setCurrentPlayerIndex(0);
        setMovesLeft(getGame().getConstants().PLAYER_MOVES);
        if(!getVirtualViewMap().isEmpty()) {
            System.out.println(getGame().getCurrentPlayer().getNickname());
            broadcastAssistantCardsPlayed();
            broadcastGameBoard();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
        }
    }

    @Override
    public void nextPlayerActionPhase(){
        winCheck();
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(getGame().getConstants().PLAYER_MOVES);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        if(!getVirtualViewMap().isEmpty()) {
            System.out.println(getGame().getCurrentPlayer().getNickname());
            broadcastGameBoard();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAction();
        }
    }

    @Override
    public void nextRound(){
        try{
            getGame().refillClouds();
        }
        catch(EmptyBagException e){
            System.out.println(e.getMessage());
        }
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        setCurrentPlayerIndex(0);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlayerPlanningPhaseDone(false);
        setPlanningPhaseDone(false);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        getGame().setRoundNumber(getGame().getRoundNumber() + 1);
        for(Player player : getGame().getPlayers())
            player.resetLastAssistantCardPlayed();
        if(!getVirtualViewMap().isEmpty()) {
            System.out.println(getGame().getCurrentPlayer().getNickname());
            broadcastGameBoard();
            showDeck(getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()));
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }


}
