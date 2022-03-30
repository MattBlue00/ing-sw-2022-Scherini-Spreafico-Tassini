package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.model.exceptions.NonExistentCharacterCardException;

public class GameExpertMode extends Game{

    private final CharacterCard[] characters;

    public GameExpertMode(int playersNumber) {
        super(playersNumber);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    // TODO: at the end of player's turn, set the card's status to false
    public void playerPlaysCharacterCard(int id){

        try {
            getCurrentPlayer().playCharacterCard();
            boolean found = false;
            for(CharacterCard card : characters){
                if(card.getId() == id) {
                    card.setIsActive(true);
                    found = true;
                }
            }
            if(found == false)
                throw new NonExistentCharacterCardException("No character card with id " + id + " exists.");
            else
                getCurrentPlayer().setCharacterCardAlreadyPlayed(true);
        }
        catch(CharacterCardAlreadyPlayedException | NonExistentCharacterCardException e){
            e.printStackTrace();
        }

    }

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {
        int max_steps = getCurrentPlayer().getLastCardPlayed().getMotherNatureSteps();
        for(CharacterCard card : characters){
            if(card.getId() == 1 && card.getIsActive())       // TODO: check the correct id for this card
                max_steps += 2;
        }
        if(steps > max_steps || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);
    }

}
