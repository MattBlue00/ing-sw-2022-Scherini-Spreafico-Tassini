package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CommandLineInterface extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;

    public CommandLineInterface() {
        this.out = System.out;
    }

    private String readLine() throws ExecutionException{
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());

        inputThread = new Thread(futureTask);
        inputThread.start();
        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    public void init(){
        out.println("   ('-.  _  .-')             ('-.         .-') _  .-') _                 .-')    \n" +
                " _(  OO)( \\( -O )           ( OO ).-.    ( OO ) )(  OO) )               ( OO ).  \n" +
                "(,------.,------.  ,-.-')   / . --. /,--./ ,--,' /     '._  ,--.   ,--.(_)---\\_) \n" +
                " |  .---'|   /`. ' |  |OO)  | \\-.  \\ |   \\ |  |\\ |'--...__)  \\  `.'  / /    _ |  \n" +
                " |  |    |  /  | | |  |  \\.-'-'  |  ||    \\|  | )'--.  .--'.-')     /  \\  :` `.  \n" +
                "(|  '--. |  |_.' | |  |(_/ \\| |_.'  ||  .     |/    |  |  (OO  \\   /    '..`''.) \n" +
                " |  .--' |  .  '.',|  |_.'  |  .-.  ||  |\\    |     |  |   |   /  /\\_  .-._)   \\ \n" +
                " |  `---.|  |\\  \\(_|  |     |  | |  ||  | \\   |     |  |   `-./  /.__) \\       / \n" +
                " `------'`--' '--' `--'     `--' `--'`--'  `--'     `--'     `--'       `-----'  ");

        out.println("Welcome to Eriantys!\n");
        askServerData();
    }

    public void askServerData(){
        String address;
        String port;

        boolean addressIsValid;
        boolean portIsValid;

        String defaultAddress = "127.0.0.1";
        int defaultPort = 12345;

        out.println("Please enter the following values to connect to the server [type enter to use default values]");
        do{
            out.println("Enter the server IP address [ default = "+defaultAddress+" ] : ");
            try {
                addressIsValid = true;
                address = readLine();
                if(address.equals("")) address = defaultAddress;
                else if(!ClientController.isValidAddress(address)){
                    addressIsValid = false;
                    out.println("The address is not valid, please enter the value again: ");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }while(!addressIsValid);

        do{
            out.println("Enter the server port [ default = "+defaultPort+" ] : ");
            try {
                portIsValid = true;
                port = readLine();
                if(port.equals("")) port = String.valueOf(defaultPort);
                else if(!ClientController.isValidPort(port)) {
                    portIsValid = false;
                    out.println("The port is not valid, please enter the value again: ");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }while(!portIsValid);

        int finalPortValue = Integer.parseInt(port);
        String finalAddress = address;

        notifyObserver(viewObserver -> viewObserver.onUpdateServerData(finalAddress, finalPortValue));
    }

    @Override
    public void askNickname() {
        out.println("Enter your nickname [must be unique] : ");
        try {
            String nickname = readLine().trim();
            notifyObserver(viewObserver -> viewObserver.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCreateOrJoin() {
        out.println("Choose whether to create a new game or to join an existing one [ type CREATE to create or JOIN to join ] :");
        String choice;
        try {
            do {
                choice = readLine().trim();
                if(!choice.equalsIgnoreCase("CREATE") && !choice.equalsIgnoreCase("JOIN"))
                    out.println("Please enter a valid choice [ type CREATE to create or JOIN to join ] : ");
            }while(!choice.equalsIgnoreCase("CREATE") && !choice.equalsIgnoreCase("JOIN"));
            String finalChoice = choice;
            notifyObserver(viewObserver -> viewObserver.onUpdateCreateOrJoin(finalChoice));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askGameInfo() {
        out.println("Please insert all the required parameters in order to create a new game: ");
        try {
            int gameNumber = 0;
            do{
                try {
                    out.println("Type the number (ID) of the game you want to create [ game number must be unique and greater than zero]: ");
                    gameNumber = Integer.parseInt(readLine());
                    if(gameNumber <= 0)
                        out.println("The ID must be greater than zero!");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while(gameNumber <= 0);
            out.println("Choose the type of game you want to play [ For Expert mode type EXPERT, for Normal mode type NORMAL ] : ");
            String str = readLine().toUpperCase().trim();
            while (!str.equalsIgnoreCase("EXPERT") && !str.equalsIgnoreCase("NORMAL")){
                    out.println("The given input is not correct, please retry. " +
                        "\nChoose the type of game you want to play [ For Expert mode type EXPERT, for normal mode type NORMAL ] : ");
                str = readLine();
            }
            boolean expertMode = str.equals("EXPERT");
            int numOfPlayers = 0;
            do {
                try {
                    out.println("Enter the desired number of players [ 2 or 3 ] : ");
                    numOfPlayers = Integer.parseInt(readLine());
                    if (numOfPlayers != 2 && numOfPlayers != 3)
                        out.println("Please enter a valid choice.");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while(numOfPlayers != 2 && numOfPlayers != 3);
            int finalNumOfPlayers = numOfPlayers;
            int finalGameNumber = gameNumber;
            notifyObserver(viewObserver -> viewObserver.onUpdateGameInfo(finalGameNumber, expertMode, finalNumOfPlayers));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askGameNumber() {
        try {
            int gameNumber = 0;
            do {
                try {
                    out.println("Type the number of the game you want to join: ");
                    gameNumber = Integer.parseInt(readLine());
                    if(gameNumber <= 0)
                        out.println("The game ID must be greater than zero!");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while(gameNumber <= 0);
            int finalGameNumber = gameNumber;
            notifyObserver(viewObserver -> viewObserver.onUpdateGameNumber(finalGameNumber));
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askWizardID() {
        out.println("Choose your wizard for this game between [ BLUE_WIZARD | YELLOW_WIZARD | GREEN_WIZARD | PINK_WIZARD ] : ");
        out.println("(The wizard must be unique for each player)");
        try {
            String wizard = readLine().toUpperCase().trim();
            while (!wizard.equals("BLUE_WIZARD") && !wizard.equals("PINK_WIZARD")
                    && !wizard.equals("YELLOW_WIZARD") && !wizard.equals("GREEN_WIZARD")){
                out.println("The given input is not correct, please retry. \nChoose between [ BLUE_WIZARD | PINK_WIZARD | YELLOW_WIZARD | GREEN_WIZARD ]: ");
                wizard = readLine().toUpperCase().trim();
            }
            String finalWizard = wizard.toUpperCase();
            notifyObserver(viewObserver -> viewObserver.onUpdateWizardID(finalWizard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askAssistantCard() {
        out.println("Select the Assistant Card you want to play: ");
        try {
            String assistantCard = readLine().toUpperCase().trim();
            notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(assistantCard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void askMoveStudent(){

        try {
            String choice;
            do{
                out.println("Type ISLAND if you want to move a student to an Island, TABLE if you want to move it to its Table:");
                choice = readLine().toUpperCase().trim();
                if(!choice.equals("ISLAND") && !choice.equals("TABLE")) {
                    out.println("The given input is not correct, please try again.");
                }
            }while (!choice.equals("ISLAND") && !choice.equals("TABLE"));

            if(choice.equals("ISLAND")) {
                out.println("Which student do you want to move to an Island? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor(readLine().toUpperCase().trim());

                int islandID = 0;
                do {
                    try {
                        out.println("Towards which island? Please type a valid number:");
                        islandID = Integer.parseInt(readLine());
                        if (islandID < 1 || islandID > 12) {
                           out.println("The given input is not correct, please try again. \n" +
                                   "Towards which island? Please type a valid number:");
                        }
                    }
                    catch(NumberFormatException e){
                        out.println("Please type a valid number.");
                    }
                }while (islandID < 1 || islandID > 12);

                int finalIslandID = islandID;
                notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(finalColor, finalIslandID));
            }

            if(choice.equals("TABLE")){
                out.println("Which student do you want to move to its Table? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor(readLine().toUpperCase().trim());
                notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(finalColor));
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void askMotherNatureSteps() {

        try {
            int steps = 0;
            do {
                try {
                    out.println("How many steps do you wish Mother Nature has to move of? Please type a valid number:");
                    steps = Integer.parseInt(readLine());
                    if (steps < 1 || steps > 7)
                        out.println("The given input is not correct, please try again.");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while (steps < 1 || steps > 7);
            int finalSteps = steps;
            notifyObserver(viewObserver -> viewObserver.onUpdateMotherNatureSteps(finalSteps));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCloud() {

        try {
            int cloudID = 0;
            do {
                try {
                    out.println("Which cloud do you want to pick students from? Please type a valid number:");
                    cloudID = Integer.parseInt(readLine()) - 1;
                    if (cloudID < 0 || cloudID > 2)
                        out.println("The given input is not correct, please try again.");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while(cloudID < 0 || cloudID > 2);
            int finalCloudID = cloudID;
            notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(finalCloudID));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCharacterCard() {

        try {
            int characterCardID = 0;
            do {
                try {
                    out.println("Which Character Card do you want to use? Please type a valid number:");
                    characterCardID = Integer.parseInt(readLine());
                    if (characterCardID < 1 || characterCardID > 12)
                        out.println("The given input is not correct, please try again.");
                }
                catch(NumberFormatException e){
                    out.println("Please type a valid number.");
                }
            }while(characterCardID < 1 || characterCardID > 12);
            int finalCharacterCardID = characterCardID;
            switch (characterCardID) {
                case 2, 4, 6, 8 -> notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(finalCharacterCardID));
                case 3, 5 -> {
                    int par = 0;
                    do {
                        try {
                            out.println("Please enter a valid parameter (number):");
                            par = Integer.parseInt(readLine());
                        } catch (NumberFormatException e) {
                            out.println("Please type a valid number.");
                        }
                    }while(par <= 0);
                    int finalPar = par;
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardInt(finalCharacterCardID, finalPar));
                }
                case 9, 11, 12 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par = checkColor(readLine().toUpperCase().trim());
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardString(finalCharacterCardID, par));
                }
                case 1 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par1 = checkColor(readLine().toUpperCase().trim());
                    int par2 = 0;
                    do{
                        try {
                            out.println("Please enter a valid parameter (islandID number):");
                            par2 = Integer.parseInt(readLine());
                        } catch (NumberFormatException e) {
                            out.println("Please type a valid number.");
                        }
                    } while (par2 < 1 || par2 > 12);
                    int finalPar = par2;
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardStringInt(finalCharacterCardID, par1, finalPar));
                }
                case 7 -> {
                    out.println("Please enter a valid list of colors (0, 2, 4 or 6 colors), STOP to end the list.");
                    ArrayList<String> par = new ArrayList<>();
                    while (true) {
                        if (par.size() == 6)
                            break;
                        out.println("Please enter the color of the hall student (STOP to end):");
                        String par1 = readLine().toUpperCase().trim();
                        if (par1.equalsIgnoreCase("STOP"))
                            break;
                        par1 = checkColor(par1);
                        par.add(par1);
                        out.println("Please enter the color of the card student:");
                        String par2 = checkColor(readLine().toUpperCase().trim());
                        par.add(par2);
                    }
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardArrayListString(finalCharacterCardID, par));
                }
                case 10 -> {
                    out.println("Please enter a valid list of colors (0, 2 or 4 colors), STOP to end the list.");
                    ArrayList<String> par = new ArrayList<>();
                    while (true) {
                        if (par.size() == 4)
                            break;
                        out.println("Please enter the color of the hall student (STOP to end):");
                        String par1 = readLine().toUpperCase().trim();
                        if (par1.equals("STOP"))
                            break;
                        par1 = checkColor(par1);
                        par.add(par1);
                        out.println("Please enter the color of the table student:");
                        String par2 = checkColor(readLine().toUpperCase().trim());
                        par.add(par2);
                    }
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardArrayListString(finalCharacterCardID, par));
                }
                default -> {} // should never enter here
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askAction() {
        out.println("What do you want to do? Please type STUDENT to move a student, CARD to play a Character Card: ");
        try {
            String choice = readLine().toUpperCase().trim();
            while (!choice.equals("STUDENT") && !choice.equals("CARD")){
                out.println("The given input is not correct, please try again. \n" +
                        "Type STUDENT to move a student, CARD to play a character card: ");
                choice = readLine().toUpperCase().trim();
            }
            String finalChoice = choice;
            notifyObserver(viewObserver -> viewObserver.onUpdateActionChoice(finalChoice));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showGenericMessage(String message) {
        out.println(message);
    }

    @Override
    public void showExistingGames(Map<Integer, GameController> existingGames) {
        if(existingGames.isEmpty())
            out.println("No games have been created yet.");
        else {
            out.println("Existing games list: ");
            for(Map.Entry<Integer, GameController> entry : existingGames.entrySet()) {
                Integer key = entry.getKey();
                GameController value = entry.getValue();
                out.print("- "+key+": ");
                List<Player> players = value.getGame().getPlayers();
                for(int i = 0; i < players.size() - 1; i++){
                    out.print(players.get(i).getNickname() + ", ");
                }
                if(players.size() == value.getGame().getPlayersNumber())
                    out.println(players.get(players.size() - 1).getNickname() + " (FULL)");
                else
                    out.println(players.get(players.size() - 1).getNickname() + " (WAITING FOR PLAYERS TO JOIN)");
            }
        }
    }

    @Override
    public void showGameStatusFirstActionPhase(Game game) {
        clearInterface();
        game.showAssistantCardsPlayed();
        game.showGameBoard();
        game.showPlayersOrder();
    }

    @Override
    public void showGameStatus(Game game) {
        clearInterface();
        game.showGameBoard();
    }

    public void showDeck(Game game) {
        clearInterface();
        game.showDeck();
    }

    public void clearInterface(){
        out.flush();
    }

    private String checkColor(String color) throws ExecutionException {
        String finalColor = color;
        while (!finalColor.equals("YELLOW") && !finalColor.equals("BLUE") && !finalColor.equals("GREEN")
                && !finalColor.equals("RED") && !finalColor.equals("PINK")){
            out.println("The given input is not correct, please try again. \n" +
                    "Please type a valid color [YELLOW, BLUE, GREEN, RED, PINK]:");
            finalColor = readLine().toUpperCase();
        }
        return finalColor;
    }

}
