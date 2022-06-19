package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.controller.GameControllerExpertMode;

import java.io.Serializable;
import java.util.*;

/**
 * This class is the same as the {@link Game} class, but it handles Expert mode matches. Therefore, it has methods for
 * Character Cards handling, and it is controlled by the {@link GameControllerExpertMode}.
 */

public class GameExpertMode extends Game implements Serializable {

    private final CharacterCard[] characters;

    /**
     * Game (Expert mode) constructor.
     *
     * @param playersNumber the number of players of the match.
     * @param constants the useful constants to set for this game.
     */

    public GameExpertMode(int playersNumber, Constants constants) {
        super(playersNumber, constants);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    /**
     * Returns an array containing all the 3 Character Cards available for this game.
     *
     * @return an array of {@link CharacterCard} subclasses (each one is different, since each card present in the game
     * is different).
     */

    public CharacterCard[] getCharacters() {
        return characters;
    }

    /**
     * Returns the Character Card with the given ID between the ones present in the current game, if possible.
     *
     * @param id an {@code int} representing the ID of the desired card.
     * @return the desired {@link CharacterCard}.
     * @throws NoSuchElementException if there's no Character Card with the given ID in the current game.
     */

    public CharacterCard getCharacterCardByID(int id) throws NoSuchElementException{
        Optional<CharacterCard> card = Arrays.stream(characters).filter(x -> x.getId() == id).findFirst();
        if(card.isPresent())
            return card.get();
        else throw new NoSuchElementException("There's no Character Card with such ID!");
    }

    /**
     * Allows the current player to play a chosen Character Card. If it involves a change in an already existent
     * method, its effect will not be visible right after use; otherwise, the card's doEffect method will be
     * instantly triggered.
     *
     * @param id an {@code int} representing the ID of the card to play.
     * @throws TryAgainException if the desired card can't be played. This may happen because of the first true
     * conditions along the following:
     * 1) the current player has already played a Character Card in the same round - actually, this exception should
     * never be thrown here ({@link GameControllerExpertMode} should prevent this case), but it has been kept for safety.
     * 2) the current player has not enough coins to activate the card's effect.
     * 3) the Character Card ID provided does not refer to any of the existing 3 cards in the current game.
     * As the exception name suggests, it is then asked the current player to provide input again.
     */

    public void playerPlaysCharacterCard(int id) throws TryAgainException {

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
                if(card.getId() == 1 || card.getId() == 3 || card.getId() == 5 || card.getId()== 7 ||
                        card.getId() == 10 || card.getId() == 11 || card.getId() == 12){
                    card.doEffect(this);
                    card.setUpCard();
                }
            }
        }

        if (!found)
            throw new CharacterCardNotFoundException("There's no Character Card with id " + id + " in this match...");
        else
            getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

    }

    /**
     * Checks if the card which modifies Mother Nature's maximum number of steps has been activated by the current
     * player. In case it has, calls the card's doEffect method; in case it hasn't, this method is identical to the
     * overridden one.
     *
     * @param steps an {@code int} representing the number of steps the player wishes Mother Nature to move of.
     */

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {

        setMaxSteps(getCurrentPlayer().getLatestAssistantCardPlayed().getMotherNatureSteps());
        for (CharacterCard card : characters) {
            if (card.getId() == 4 && card.getIsActive()) {
                try {
                    card.doEffect(this);
                } catch (TryAgainException e) {
                    throw new RuntimeException(e);
                }
                card.setUpCard();
            }
        }
        if (steps > getMaxSteps() || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);

    }

    /**
     * Checks if the card which modifies the profCheck algorithm has been activated by the current player. In case it
     * has, calls the card's doEffect method; in case it hasn't, this method is identical to the overridden one.
     *
     * @throws NonExistentColorException if a table of a non-existent color is somehow accessed (it should never be
     * thrown, so it is safely ignorable).
     */

    @Override
    public void profCheck() throws NonExistentColorException {

        boolean done = false;
        for (CharacterCard card : characters) {
            if (card.getId() == 2 && card.getIsActive()) {
                try {
                    card.doEffect(this);
                } catch (TryAgainException e) {
                    throw new RuntimeException(e);
                }
                card.setUpCard();
                done = true;
            }
        }

        if(!done)
            Game.profCheckAlgorithm(getPlayers());

    }

    /**
     * Checks if one of the cards which modify the islandConquerCheck algorithm has been activated by the current
     * player. In case one of them have, calls the card's doEffect method; in case none of them have, this method is
     * identical to the overridden one.
     */

    @Override
    public void islandConquerCheck(int islandID) throws IslandNotFoundException {
      
        boolean done = false;
        for (CharacterCard card : characters) {
            if ((card.getId() == 6 || card.getId() == 8 || card.getId() == 9)
                    && card.getIsActive()) {
                try {
                    card.doEffect(this);
                } catch (TryAgainException e) {
                    throw new RuntimeException(e);
                }
                card.setUpCard();
                done = true;
            }
        }
        if (!done)
            getBoard().islandConquerCheck(getCurrentPlayer(), islandID);
      
    }

    /**
     * Adds the given set of Character Cards to the game.
     *
     * @param cards the array containing the Character Cards to add to the game.
     */

    public void addCharacterCards(CharacterCard[] cards){
        System.arraycopy(cards, 0, this.characters, 0, Constants.CHARACTERS_NUM);
    }

    /**
     * Fills the players' halls with the right amount of students and shuffles the players' order at the beginning of
     * the match, in order to start the game properly (just as the overridden method does). Plus, generates a random
     * set of 3 different Character Cards and adds them to the game.
     */

    public void startGame(){
        for(int i = 0; i < getConstants().MAX_HALL_STUDENTS; i++) {
            for (Player player : getPlayers()) {
                player.getSchool().getHall().addStudent(getBoard().getStudentsBag().remove
                        (getBoard().getStudentsBag().size() - 1));
            }
        }

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        Random random = new Random();
        List<Integer> cardsAlreadyPicked = new ArrayList<>(3);
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
            int num;
            do{
                num = random.nextInt(12) + 1;
            }while(cardsAlreadyPicked.contains(num));
            cardsAlreadyPicked.add(num);
            switch (num) {
                case 1 -> cards[i] = new Monk(getBoard().getStudentsBag());
                case 2 -> cards[i] = new Innkeeper();
                case 3 -> cards[i] = new Flagman();
                case 4 -> cards[i] = new Postman();
                case 5 -> cards[i] = new Healer();
                case 6 -> cards[i] = new Centaur();
                case 7 -> cards[i] = new Jester(getBoard().getStudentsBag());
                case 8 -> cards[i] = new Knight();
                case 9 -> cards[i] = new Villager();
                case 10 -> cards[i] = new Bard();
                case 11 -> cards[i] = new Princess(getBoard().getStudentsBag());
                case 12 -> cards[i] = new Thief();
                default -> {
                } // should never enter here
            }
        }
        addCharacterCards(cards);

        Collections.shuffle(getPlayers());
        setCurrentPlayer(getPlayers().get(0));

        getTowersColor().put(getPlayers().get(0), TowerColor.BLACK);
        getTowersColor().put(getPlayers().get(1), TowerColor.WHITE);
        if(getPlayers().size() == 3)
            getTowersColor().put(getPlayers().get(2), TowerColor.GREY);

    }

    /**
     * Allows the view to properly show the game status (Expert mode).
     */

    public void showGameBoard(){
        try {
            String nextPlayerNickname;
            if(getPlayers().size() - 1 == getPlayers().indexOf(getCurrentPlayer()))
                nextPlayerNickname = getPlayers().get(0).getNickname();
            else
                nextPlayerNickname = getPlayers().get(getPlayers().indexOf(getCurrentPlayer()) + 1).getNickname();
            System.out.printf("ROUND: %d\tCURRENT PLAYER: %s\tNEXT PLAYER: %s\n",
                    getRoundNumber(), getCurrentPlayer().getNickname(), nextPlayerNickname);

            getPlayers().forEach(x -> System.out.printf("Coins wallet of %s: %d     ", x.getNickname(), x.getCoinsWallet()));

            if(getCurrentPlayer().getCharacterCardAlreadyPlayed())
                System.out.println(ANSIConstants.ANSI_BOLD + "A character card has already been played!" + ANSIConstants.ANSI_RESET);
            else
                System.out.println();
            System.out.println("--------------------");

            int yellowStudents, blueStudents, greenStudents, redStudents, pinkStudents;

            for (int i = 1; i <= getBoard().getIslands().getSize(); i++) {

                Island currentIsland = getBoard().getIslands().getIslandFromID(i);

                String ownerNickname;
                if (currentIsland.getOwner() != null)
                    ownerNickname = currentIsland.getOwner().getNickname();
                else
                    ownerNickname = "--";

                System.out.println("ISLAND " + i);
                System.out.println(currentIsland.getNumOfTowers() + " towers");
                System.out.println("Owner: " + ownerNickname);

                yellowStudents = currentIsland.getNumOfStudentsOfColor("YELLOW");
                blueStudents = currentIsland.getNumOfStudentsOfColor("BLUE");
                greenStudents = currentIsland.getNumOfStudentsOfColor("GREEN");
                redStudents = currentIsland.getNumOfStudentsOfColor("RED");
                pinkStudents = currentIsland.getNumOfStudentsOfColor("PINK");

                System.out.println("Students: " +
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                        ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);

                if(currentIsland.getId() == getBoard().getMotherNaturePos())
                    System.out.println(ANSIConstants.ANSI_BOLD + "Mother Nature is here!" + ANSIConstants.ANSI_RESET);
                if(currentIsland.hasVetoTile())
                    System.out.println(ANSIConstants.ANSI_BOLD + "There's a veto tile here!" + ANSIConstants.ANSI_RESET);

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
                System.out.print("CLOUD " + (i + 1) + ": ");
                System.out.println(
                        ANSIConstants.ANSI_YELLOW + yellowStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_BLUE + blueStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_GREEN + greenStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_RED + redStudents + ANSIConstants.ANSI_RESET + " " +
                                ANSIConstants.ANSI_PINK + pinkStudents + ANSIConstants.ANSI_RESET);
            }

            System.out.println("--------------------");

            System.out.println("CHARACTER CARDS:");
            for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
                System.out.print(characters[i].getClass().getSimpleName() + "\n" +
                        "ID: " + characters[i].getId() + "\t" + "Cost: " + characters[i].getCost());
                if(characters[i] instanceof StudentsCard)
                    ((StudentsCard) characters[i]).showStudentsOnTheCard();
                if(characters[i] instanceof Healer)
                    System.out.println("\t\tVeto tiles available: " + getBoard().getNumOfVetos());
                if(characters[i].getIsActive())
                    System.out.println("\t\tACTIVE NOW");
                else
                    System.out.println();

            }

            System.out.println("--------------------");
            // Show all schools
            getPlayers().forEach(Player::showSchool);
        }
        catch(IslandNotFoundException ignored){}

    }

}
