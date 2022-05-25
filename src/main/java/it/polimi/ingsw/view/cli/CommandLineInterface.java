package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
                    out.println("Address is not valid, please enter the value again: ");
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
                    out.println("Port is not valid, please enter the value again: ");
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
        out.println("Choose to create a new game or join an existing one [ type CREATE to create or JOIN to join ] :");
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
        out.println("To create a new game insert all the required parameters : ");
        try {
            out.println("Type the number (ID) of the game you want to create [ game number must be unique ]: ");
            int gameNumber = Integer.parseInt(readLine());
            out.println("Chose the type of game you want to play [ For Expert mode type TRUE, for normal mode type FALSE ] : ");
            String str = readLine();
            while (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false")){
                System.out.println("The given input is not correct, please retry. " +
                        "\nChose the type of game you want to play [ For Expert mode type TRUE, for normal mode type FALSE ] : ");
                str = readLine();
            }
            boolean gameMode = Boolean.parseBoolean(str.toLowerCase(Locale.ROOT));
            out.println("Enter the number of players for the game [ 2 or 3 ] : ");
            int numOfPlayers;
            do {
                numOfPlayers = Integer.parseInt(readLine());
                if(numOfPlayers != 2 && numOfPlayers != 3)
                    out.println("Please enter a valid choice. \nEnter the number of players for the game [ 2 or 3 ] : ");
            }while(numOfPlayers != 2 && numOfPlayers != 3);
            int finalNumOfPlayers = numOfPlayers;
            notifyObserver(viewObserver -> viewObserver.onUpdateGameInfo(gameNumber, gameMode,finalNumOfPlayers));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askGameNumber() {
        out.println("Type the number of the game you want to join : ");
        try {
            String gameNumber = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateGameNumber(Integer.parseInt(gameNumber)));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askWizardID() {
        out.println("Choose your wizard for this game between: [ BLUE_WIZARD | YELLOW_WIZARD | GREEN_WIZARD | PINK_WIZARD ] : ");
        out.println("(The wizard must be unique)");
        try {
            String wizard = readLine();
            while (!wizard.equalsIgnoreCase("BLUE_WIZARD") && !wizard.equalsIgnoreCase("PINK_WIZARD")
                    && !wizard.equalsIgnoreCase("YELLOW_WIZARD") && !wizard.equalsIgnoreCase("GREEN_WIZARD")){
                System.out.println("The given input is not correct, please retry. \nGameWizard (BLUE_WIZARD | PINK_WIZARD | YELLOW_WIZARD | GREEN_WIZARD): ");
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
                System.out.println("The given input is not correct, please try again. \n" +
                        "Type ISLAND if you want to move a student to an Island, TABLE if you want to move it to its Table: ");
                choice = readLine();
            }
            if(choice.equalsIgnoreCase("ISLAND")) {
                out.println("Which student do you want to move to an Island? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor().toUpperCase();
                out.println("Towards which island? Please type a valid number:");
                int islandID = Integer.parseInt(readLine());
                while (islandID < 1 || islandID >12){
                    System.out.println("The given input is not correct, please try again. \n" +
                            "Towards which island? Please type a valid number:");
                    islandID = Integer.parseInt(readLine());
                }
                int finalIslandID = islandID;
                notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(finalColor, finalIslandID));
            }
            if(choice.equalsIgnoreCase("TABLE")){
                out.println("Which student do you want to move to its Table? Please type a valid color " +
                        "[YELLOW, BLUE, GREEN, RED, PINK]:");
                String finalColor = checkColor().toUpperCase();
                notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(finalColor));
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String checkColor() throws ExecutionException {
        String color = readLine().toUpperCase();
        while (!color.equalsIgnoreCase("YELLOW") && !color.equalsIgnoreCase("BLUE")
                && !color.equalsIgnoreCase("GREEN") && !color.equalsIgnoreCase("RED")
                && !color.equalsIgnoreCase("PINK")){
            System.out.println("The given input is not correct, please try again. \n" +
                    "Please type a valid color [YELLOW, BLUE, GREEN, RED, PINK]:");
            color = readLine();
        }
        return color;
    }

    @Override
    public void askMotherNatureSteps() {
        out.println("How many steps do you wish Mother Nature has to move of? Please type a valid number:");
        try {
            int steps = Integer.parseInt(readLine());
            while (steps < 1 || steps > 5){
                System.out.println("The given input is not correct, please try again. \n" +
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
        out.println("Which character card do you want to use? Please type a valid number:");
        try {
            int characterCardID = Integer.parseInt(readLine());
            while (characterCardID < 1 || characterCardID > 12 ){
                System.out.println("The given input is not correct, please try again. \n" +
                        "Which character card do you want to use? Please type a valid number:  ");
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
                    String par = checkColor().toUpperCase();
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardString(finalCharacterCardID, par));
                }
                case 1 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par1 = checkColor().toUpperCase();
                    out.println("Please enter a valid parameter (islandID number):");
                    int par2 = Integer.parseInt(readLine());
                    while (par2 < 1 || par2 > 12){
                        out.println("Please enter a valid parameter (islandID number):");
                        par2 = Integer.parseInt(readLine());
                    }
                    int finalPar = par2;
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardStringInt(finalCharacterCardID, par1, finalPar));
                }
                case 7 -> {
                    out.println("Please enter a valid list of colors (0, 2, 4 or 6 colors), STOP to end the list.");
                    List<String> list = new ArrayList<>();
                    while (true) {
                        if (list.size() == 6)
                            break;
                        out.println("Please enter the color of the hall student (STOP to end):");
                        String par1 = checkColor().toUpperCase();
                        if (par1.equalsIgnoreCase("STOP"))
                            break;
                        list.add(par1);
                        out.println("Please enter the color of the card student:");
                        String par2 = checkColor().toUpperCase();
                        list.add(par2);
                    }
                }
                case 10 -> {
                    out.println("Please enter a valid list of colors (0, 2 or 4 colors), STOP to end the list.");
                    List<String> list = new ArrayList<>();
                    while (true) {
                        if (list.size() == 4)
                            break;
                        out.println("Please enter the color of the hall student (STOP to end):");
                        String par1 = checkColor().toUpperCase();
                        if (par1.equals("STOP"))
                            break;
                        list.add(par1);
                        out.println("Please enter the color of the table student:");
                        String par2 = checkColor().toUpperCase();
                        list.add(par2);
                    }
                }
                default -> {}
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showGenericMessage(String message) {
        out.println(message);
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
}
