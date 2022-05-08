package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;

import java.util.Arrays;

public class GameExpertMode extends Game {

    private final CharacterCard[] characters;

    public GameExpertMode(int playersNumber) {
        super(playersNumber);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    /*
        This method lets a player play a chosen character card. If it involves a change in an already existent
        method, its effect will not be visible right after use; otherwise, the card's doEffect method will be
        instantly triggered.
     */


    public CharacterCard getCharacterCardByID(int id){
        return Arrays.stream(characters).filter(card -> card.getId()==id).findFirst().get();
    }

    public void playerPlaysCharacterCard(int id) throws
            CharacterCardAlreadyPlayedException, NotEnoughCoinsException, CharacterCardNotFoundException {

        if (getCurrentPlayer().getCharacterCardAlreadyPlayed())
            throw new CharacterCardAlreadyPlayedException("You have already played a character card this round!");

        boolean found = false;
        for (CharacterCard card : characters) {
            if (card.getId() == id) {
                if(!(getCurrentPlayer().getCoinsWallet() >= card.getCost()))
                    throw new NotEnoughCoinsException("You need " +
                            (card.getCost() - getCurrentPlayer().getCoinsWallet())
                            + " more money to use this card!");
                card.setIsActive(true);
                found = true;
                getCurrentPlayer().setCoinsWallet(getCurrentPlayer().getCoinsWallet() - card.getCost());
                if(card.getId() == 5 || card.getId() == 10 || card.getId() == 3){
                    card.doEffect(this);
                    card.setUpCard();
                }
            }
        }

        if (!found)
            throw new CharacterCardNotFoundException("No character card with id " + id + " exists.");
        else
            getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

    }

    /*
        This overridden method controls whether the card which modifies Mother Nature's movement algorithm has
        been chosen by the current player.
     */

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {

        setMaxSteps(getCurrentPlayer().getLastAssistantCardPlayed().get().getMotherNatureSteps());
        for (CharacterCard card : characters) {
            if (card.getId() == 4 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
            }
        }
        if (steps > getMaxSteps() || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);

    }

    /*
        This overridden method controls whether the card which modifies the profCheck algorithm has been chosen
        by the current player.
     */

    @Override
    public void profCheck() throws NonExistentColorException {

        boolean done = false;
        for (CharacterCard card : characters) {
            if (card.getId() == 2 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }

        if(!done)
            Game.profCheckAlgorithm(getPlayers());

    }

    /*
        This overridden method controls whether one of the cards which modify the islandConquerCheck algorithm
        has been chosen by the current player.
     */

    @Override
    public void islandConquerCheck(int islandID) throws IslandNotFoundException {
      
        boolean done = false;
        for (CharacterCard card : characters) {
            if ((card.getId() == 6 || card.getId() == 8 || card.getId() == 9)
                    && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }
        if (!done)
            getBoard().islandConquerCheck(getCurrentPlayer(), islandID);
      
    }

    // Debug methods

    public void addCharacterCards(CharacterCard[] cards){

        System.arraycopy(cards, 0, this.characters, 0, Constants.CHARACTERS_NUM);

    }

    public void showGameBoard(){

        try {

            String nextPlayerNickname;
            if(getPlayers().size() - 1 == getPlayers().indexOf(getCurrentPlayer()))
                nextPlayerNickname = getPlayers().get(0).getNickname();
            else
                nextPlayerNickname = getPlayers().get(getPlayers().indexOf(getCurrentPlayer()) + 1).getNickname();
            System.out.printf("TURNO: %d\tGIOCATORE CORRENTE: %s\tPROSSIMO GIOCATORE: %s\n",
                    getRoundNumber(), getCurrentPlayer().getNickname(), nextPlayerNickname);
            System.out.print("MONETE DI " + getCurrentPlayer().getNickname() + ": " + getCurrentPlayer().getCoinsWallet() + "\t");
            if(getCurrentPlayer().getCharacterCardAlreadyPlayed())
                System.out.println(ANSIConstants.ANSI_BOLD + "Una carta personaggio è stata già giocata." + ANSIConstants.ANSI_RESET);
            else
                System.out.println();
            System.out.println("--------------------");

            int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;

            for (int i = 1; i <= getBoard().getIslands().getSize(); i++) {

                Island currentIsland = getBoard().getIslands().getIslandFromID(i);

                String ownerNickname;
                if (currentIsland.getOwner().isPresent())
                    ownerNickname = currentIsland.getOwner().get().getNickname();
                else
                    ownerNickname = "--";

                System.out.println("ISOLA " + i);
                System.out.println(currentIsland.getNumOfTowers() + " torri");
                System.out.println("Proprietario: " + ownerNickname);

                yellowStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
                blueStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
                greenStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
                redStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
                pinkStudents =
                        (int) currentIsland.getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

                System.out.println("Studenti: " +
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

                if(currentIsland.getId() == getBoard().getMotherNaturePos())
                    System.out.println(ANSIConstants.ANSI_BOLD + "Madre Natura è qui!" + ANSIConstants.ANSI_RESET);

                System.out.println("--------------------");

            }

            for(int i = 0; i < getPlayersNumber(); i++) {
                yellowStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.YELLOW)).count();
                blueStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.BLUE)).count();
                greenStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.GREEN)).count();
                redStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.RED)).count();
                pinkStudents =
                        (int) getBoard().getCloud(i).getStudents().stream().filter(x -> x.getColor().equals(Color.PINK)).count();

                //TODO: when the cloud number is asked, remember to save it decremented of 1!
                System.out.print("NUVOLA " + (i + 1) + ": ");
                System.out.println(
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);
            }

            System.out.println("--------------------");

            System.out.println("CARTE PERSONAGGIO:");
            for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
                System.out.print(characters[i].getClass().getName() + "\t\t" +
                        "ID: " + characters[i].getId() + "\t" + "Costo: " + characters[i].getCost());
                if(characters[i].getIsActive())
                    System.out.println("\tATTIVA");
                else
                    System.out.println();

            }

            System.out.println("--------------------");

        }
        catch(IslandNotFoundException ignored){}

    }

}
