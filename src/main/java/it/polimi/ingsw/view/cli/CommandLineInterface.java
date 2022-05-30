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
            String nickname = readLine();
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
                choice = readLine();
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
            String str = readLine().toUpperCase();
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
            out.println("Type the number of the game you want to join: ");
            int gameNumber = Integer.parseInt(readLine());
            notifyObserver(viewObserver -> viewObserver.onUpdateGameNumber(gameNumber));
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        //TODO: add NumberFormatException catch clause after the input validity check is implemented
    }

    @Override
    public void askWizardID() {
        out.println("Choose your wizard for this game between [ BLUE_WIZARD | YELLOW_WIZARD | GREEN_WIZARD | PINK_WIZARD ] : ");
        out.println("(The wizard must be unique for each player)");
        try {
            String wizard = readLine();
            while (!wizard.equalsIgnoreCase("BLUE_WIZARD") && !wizard.equalsIgnoreCase("PINK_WIZARD")
                    && !wizard.equalsIgnoreCase("YELLOW_WIZARD") && !wizard.equalsIgnoreCase("GREEN_WIZARD")){
                out.println("The given input is not correct, please retry. \nGameWizard (BLUE_WIZARD | PINK_WIZARD | YELLOW_WIZARD | GREEN_WIZARD): ");
                wizard = readLine();
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
            String assistantCard = readLine();
            while (!assistantCard.equalsIgnoreCase("CHEETAH") && !assistantCard.equalsIgnoreCase("OSTRICH")
                && !assistantCard.equalsIgnoreCase("CAT") && !assistantCard.equalsIgnoreCase("EAGLE")
                && !assistantCard.equalsIgnoreCase("FOX") && !assistantCard.equalsIgnoreCase("SNAKE")
                && !assistantCard.equalsIgnoreCase("OCTOPUS") && !assistantCard.equalsIgnoreCase("DOG")
                && !assistantCard.equalsIgnoreCase("ELEPHANT") && !assistantCard.equalsIgnoreCase("TURTLE")){
                System.out.println("The given input is not correct, please retry. \nSelect the Assistant Card you want to play: ");
                assistantCard = readLine();
            }
            String finalAssistantCard = assistantCard.toUpperCase();
            notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(finalAssistantCard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void askMoveStudent(){
        out.println("Type ISLAND if you want to move a student to an Island, TABLE if you want to move it to its Table:");
        try {
            String choice = readLine().toUpperCase();
            while (!choice.equalsIgnoreCase("ISLAND") && !choice.equalsIgnoreCase("TABLE")){
                out.println("The given input is not correct, please try again. \n" +
                        "Type ISLAND if you want to move a student to an Island, TABLE if you want to move it to its Table: ");
                choice = readLine();
            }
            if(choice.equalsIgnoreCase("ISLAND")) {
                out.println("Which student do you want to move to an Island? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor(readLine().toUpperCase());
                out.println("Towards which island? Please type a valid number:");
                int islandID = Integer.parseInt(readLine());
                while (islandID < 1 || islandID >12){
                    out.println("The given input is not correct, please try again. \n" +
                            "Towards which island? Please type a valid number:");
                    islandID = Integer.parseInt(readLine());
                }
                int finalIslandID = islandID;
                notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(finalColor, finalIslandID));
            }
            if(choice.equalsIgnoreCase("TABLE")){
                out.println("Which student do you want to move to its Table? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor(readLine().toUpperCase());
                notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(finalColor));
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askMotherNatureSteps() {
        out.println("How many steps do you wish Mother Nature has to move of? Please type a valid number:");
        try {
            int steps = Integer.parseInt(readLine());
            while (steps < 1 || steps > 5){
                out.println("The given input is not correct, please try again. \n" +
                        "How many steps do you wish Mother Nature has to move of? Please type a valid number: ");
                steps = Integer.parseInt(readLine());
            }
            int finalSteps = steps;
            notifyObserver(viewObserver -> viewObserver.onUpdateMotherNatureSteps(finalSteps));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCloud() {
        out.println("Which cloud do you want to pick students from? Please type a valid number:");
        try {
            int cloudID = Integer.parseInt(readLine()) - 1;
            notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(cloudID));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askCharacterCard() {
        out.println("Which Character Card do you want to use? Please type a valid number:");
        try {
            int characterCardID = Integer.parseInt(readLine());
            while (characterCardID < 1 || characterCardID > 12 ){
                out.println("The given input is not correct, please try again. \n" +
                        "Which Character Card do you want to use? Please type a valid number:  ");
                characterCardID = Integer.parseInt(readLine());
            }
            int finalCharacterCardID = characterCardID;
            switch (characterCardID) {
                case 2, 4, 6, 8 -> notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(finalCharacterCardID));
                case 3, 5 -> {
                    out.println("Please enter a valid parameter (number):");
                    int par = Integer.parseInt(readLine());
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardInt(finalCharacterCardID, par));
                }
                case 9, 11, 12 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par = checkColor(readLine().toUpperCase());
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardString(finalCharacterCardID, par));
                }
                case 1 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par1 = checkColor(readLine().toUpperCase());
                    int par2 = 0;
                    while (par2 < 1 || par2 > 12){
                        out.println("Please enter a valid parameter (islandID number):");
                        par2 = Integer.parseInt(readLine());
                    }
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
                        String par1 = readLine().toUpperCase();
                        if (par1.equalsIgnoreCase("STOP"))
                            break;
                        par1 = checkColor(par1);
                        par.add(par1);
                        out.println("Please enter the color of the card student:");
                        String par2 = checkColor(readLine().toUpperCase());
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
                        String par1 = readLine().toUpperCase();
                        if (par1.equals("STOP"))
                            break;
                        par1 = checkColor(par1);
                        par.add(par1);
                        out.println("Please enter the color of the table student:");
                        String par2 = checkColor(readLine().toUpperCase());
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
            String choice = readLine().toUpperCase();
            while (!choice.equalsIgnoreCase("STUDENT") && !choice.equalsIgnoreCase("CARD")){
                out.println("The given input is not correct, please try again. \n" +
                        "Type STUDENT to move a student, CARD to play a character card: ");
                choice = readLine();
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
                try {
                    if (players.size() == value.getGame().getPlayersNumber())
                        out.println(players.get(players.size() - 1).getNickname() + " (FULL)");
                    else
                        out.println(players.get(players.size() - 1).getNickname() + " (WAITING FOR PLAYERS TO JOIN)");
                }catch (IndexOutOfBoundsException e){out.println("NO PLAYERS IN GAME YET");}
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
        while (!finalColor.equalsIgnoreCase("YELLOW") && !finalColor.equalsIgnoreCase("BLUE")
                && !finalColor.equalsIgnoreCase("GREEN") && !finalColor.equalsIgnoreCase("RED")
                && !finalColor.equalsIgnoreCase("PINK")){
            out.println("The given input is not correct, please try again. \n" +
                    "Please type a valid color [YELLOW, BLUE, GREEN, RED, PINK]:");
            finalColor = readLine().toUpperCase();
        }
        return finalColor;
    }

}
