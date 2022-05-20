package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.network.message.*;

public class GameControllerExpertMode extends GameController{

    public GameControllerExpertMode() {
        super();
    }

    @Override
    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
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
                if(getMotherNatureMoved()) {
                    handleCloudChoice(message);
                    setPlayerActionPhaseDone(true);
                }
                else
                    throw new WrongMessageSentException("You need to move mother nature first!");
                break;
            case CHARACTER_CARD_REPLY:
                if(!getMotherNatureMoved()) {
                    handleCharacterCardChoice(message);
                    if(!getVirtualViewMap().isEmpty()) {
                        System.out.println(getGame().getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        if(getMovesLeft() > 0)
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMoveStudent();
                        else
                            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askMotherNatureSteps();
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
    public void nextPlayerActionPhase(){
        winCheck();
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        //TODO: which action to undertake next? MoveStudent or playCharacterCard?
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
        setPlanningPhaseDone(false);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        getGame().setRoundNumber(getGame().getRoundNumber() + 1);
        if(!getVirtualViewMap().isEmpty()) {
            System.out.println(getGame().getCurrentPlayer().getNickname());
            broadcastGameBoard();
            getVirtualViewMap().get(getGame().getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }


}
