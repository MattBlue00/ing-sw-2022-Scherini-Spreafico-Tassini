package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            boolean gameMode = Boolean.parseBoolean(readLine());
            out.println("Enter the number of players for the game [ 2 or 3 ] : ");
            int numOfPlayers = Integer.parseInt(readLine());
            notifyObserver(viewObserver -> viewObserver.onUpdateGameInfo(gameNumber, gameMode, numOfPlayers));
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
            notifyObserver(viewObserver -> viewObserver.onUpdateWizardID(wizard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askAssistantCard() {
        out.println("Select the Assistant Card you want to play: ");
        try {
            String assistantCard = readLine();
            notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(assistantCard));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void askMoveStudent(){
        out.println("Type ISLAND if you want to move a student to an Island, TABLE if you want to move it to its Table:");
        try {
            String choice = readLine().toUpperCase();
            if(choice.equals("ISLAND")) {
                out.println("Which student do you want to move to an Island? Please type a valid color:");
                String color = readLine().toUpperCase();
                out.println("Towards which island? Please type a valid number:");
                int islandID = Integer.parseInt(readLine());
                notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(color, islandID));
            }
            if(choice.equals("TABLE")){
                out.println("Which student do you want to move to its Table? Please type a valid color:");
                String color = readLine().toUpperCase();
                notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(color));
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
            notifyObserver(viewObserver -> viewObserver.onUpdateMotherNatureSteps(steps));
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
            switch (characterCardID) {
                case 2, 4, 6, 8 -> notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(characterCardID));
                case 3, 5 -> {
                    out.println("Please enter a valid parameter (number):");
                    int par = Integer.parseInt(readLine());
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardInt(characterCardID, par));
                }
                case 9, 11, 12 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par = readLine().toUpperCase();
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardString(characterCardID, par));
                }
                case 1 -> {
                    out.println("Please enter a valid parameter (color):");
                    String par1 = readLine().toUpperCase();
                    out.println("Please enter a valid parameter (number):");
                    int par2 = Integer.parseInt(readLine());
                    notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardStringInt(characterCardID, par1, par2));
                }
                case 7 -> {
                    out.println("Please enter a valid list of colors (0, 2, 4 or 6 colors), STOP to end the list.");
                    List<String> list = new ArrayList<>();
                    while (true) {
                        if (list.size() == 6)
                            break;
                        out.println("Please enter the color of the hall student (STOP to end):");
                        String par1 = readLine().toUpperCase();
                        if (par1.equals("STOP"))
                            break;
                        list.add(par1);
                        out.println("Please enter the color of the card student:");
                        String par2 = readLine().toUpperCase();
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
                        String par1 = readLine().toUpperCase();
                        if (par1.equals("STOP"))
                            break;
                        list.add(par1);
                        out.println("Please enter the color of the table student:");
                        String par2 = readLine().toUpperCase();
                        list.add(par2);
                    }
                }
                default -> {
                }
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

    public void clearInterface(){
        out.flush();
    }
}
