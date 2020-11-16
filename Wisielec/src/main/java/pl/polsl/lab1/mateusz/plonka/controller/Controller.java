package pl.polsl.lab1.mateusz.plonka.controller;

import java.util.Scanner;
import pl.polsl.lab1.mateusz.plonka.model.*;
import pl.polsl.lab1.mateusz.plonka.view.*;

/**
 * Class controlls logic of game
 * @author Mateusz Płonka
 * @version v1.1
 */
public class Controller {

    /*VARIABLES*/
    int mode;                   //[0] words from bank & [1] words from stream
    int max_errors;             //max errors to make
    String adress;              //adress of file to read
    String playerName;          //player name
    View myView = new View();   //View to display messages and draw Gui
    HangmanGame myGame;         //Object of game
    Player myPlayer;            //Object of player

    /*Getters*/
    public int getMax_errors() {
        return max_errors;
    }

    public HangmanGame getMyGame() {
        return myGame;
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public View getMyView() {
        return myView;
    }

    public int getMode() {
        return mode;
    }
    

    /*Setters*/
    public void setMode(int mode) {
        if (mode > -1 && mode < 3) {
            this.mode = mode;
        } else {
            View.displayError("Given incorect mode number");
        }
    }

    //Non-parameter constructor
    public Controller() {
        int mode = 0;
        int max_errors = 0;
        String adress = "";
        String playerName = "";
        
        View.displayLine("Application was given bad arguments!");
        this.askForArgs();
    }

    /**
     * Main constructor of Contoller class
     * @param mode - [0] Read bank from file, [1] Read bank from user input 
     * @param playerName - Name of the player in first round 
     * @param restArg - Maximum number of errors to be made in first round 
     * & Path of file to read words (only when Mode = 0)
     */
    public Controller(String mode, String playerName, String... restArg) {

        //If true then bad args and need to ask for them
        boolean needToAsk = false;

        //Check argument with mode
        if (mode.equals("0")) {
            this.mode = 0;
        } else if (mode.equals("1")) {
            this.mode = 1;
        } else {
            needToAsk = true;
        }

        //Check argument with players name
        this.playerName = playerName;

        //Check argument with no of errors
        int i = 0;

        //Check if it is numeric
        try {
            i = Integer.parseInt(restArg[0]);
        } catch (NumberFormatException nfe) {
            View.displayError("Given argument isn't numeric!");
            needToAsk = true;
        }

        //Check if is higher then 0
        if (i > 0) {
            this.max_errors = i;
        } else {
            needToAsk = true;
        }

        //Check argument with file adress
        if ((mode.equals("0")) && (restArg.length == 2)) {
            if (restArg[1].matches(".*\\b.txt")) {
                this.adress = restArg[1];
            } else {
                needToAsk = true;
            }
        }

        //Ask for args if are bad
        if (needToAsk) {
            this.askForArgs();
        }
    }

    /**
     * Method asks for main params if they were not given or the game was
     * restarted
     */
    public void askForArgs() {

        //READS MODE
        String tmp = "";
        View.displayLine("Choose Mode:");
        View.displayLine("[0] Reads bank of words from file");
        View.displayLine("[1] Asks user to write words in console");

        while (true) {
            tmp = myView.readLine();
            if (tmp.equals("0")) {
                this.mode = 0;
                break;
            } else if (tmp.equals("1")) {
                this.mode = 1;
                break;
            } else {
                View.displayLine("\nChoose Mode:");
                View.displayLine("[0] Reads bank of words from file");
                View.displayLine("[1] Asks user to write words in console");
            }
        }

        //READS PLAYERS NAME
        //Check if isn't empty
        while (true) {
            View.displayLine("\nType player name:");
            this.playerName = myView.readLine();
            if (!this.playerName.equals("")) {
                break;
            }
        }

        //READS MAX ERRORS
        while (true) {
            View.displayLine("\nType number of max errors:");
            tmp = myView.readLine();

            //Check if typed is not empty
            if (tmp != null) {
                int i = 0;

                //Check if it is numeric
                try {
                    i = Integer.parseInt(tmp);
                } catch (NumberFormatException nfe) {
                    View.displayError("Given argument isn't numeric!");
                }

                //Check if is higher then 0
                if (i > 0) {
                    this.max_errors = i;
                    break;
                }
            }
        }

        //READS FILE ADRESS
        if (mode == 0) {
            while (true) {
                View.displayLine("\nType adress of file with words:");
                tmp = myView.readLine();

                if (tmp.matches(".*\\b.txt")) {
                    this.adress = tmp;
                    break;
                } else {
                    View.displayLine("Given file adress does not contain .txt!");
                }
            }
        }
    }

    /**
     * Adds new game settings and additional words to the bank
     */
    public void upgrateGame() {

        //READS PLAYERS NAME
        //Check if isn't empty
        while (true) {
            View.displayLine("\nType player name:");
            this.playerName = myView.readLine();
            if (!this.playerName.equals("")) {
                break;
            }
        }

        String tmp = "";
        //READS MAX ERRORS
        while (true) {
            View.displayLine("\nType number of max errors:");
            tmp = myView.readLine();

            //Check if typed is not empty
            if (tmp != null) {
                int i = 0;

                //Check if it is numeric
                try {
                    i = Integer.parseInt(tmp);
                } catch (NumberFormatException nfe) {
                    View.displayError("Given argument isn't numeric!");
                }

                //Check if is higher then 0
                if (i > 0) {
                    this.max_errors = i;
                    break;
                }
            }
        }

        //ADDING NEW WORDS
        int counter = myGame.getWordBank().size();
        View.displayLine("Add new words to bank.");
        View.displayLine("Type \"exit\" to finish.");

        while (true) {
            View.displayLine("Do you want to see existing bank? (Y/N)");
            tmp = myView.readLine().toUpperCase();
            if (tmp.equals("Y")) {
                View.displayArrayList(myGame.getWordBank());
                break;
            } else if (tmp.equals("N")) {
                break;
            }
        }

        //It will ask for new word until user types "exit"
        while (true) {
            View.displayLine("\nType word no [" + counter + "]");
            tmp = myView.readLine();

            if (!tmp.equals("exit")) {
                if (myGame.addWordToBank(tmp)) {
                    counter++;
                }
            } else {
                break;
            }
        }

    }

    //Creates a new player
    public void newPlayer() {
        myPlayer = new Player(this.playerName);
        View.displayLine("Created player: " + myPlayer.getName() + "\n");
    }

    /**
     * Generates new game depending on mode
     *
     * @return True when operation is succeeded, false when HangmanException is
     * cought
     */
    public boolean newGame() {

        boolean succes = false;

        //Words from file
        if (this.mode == 0) {
            myGame = new HangmanGame(max_errors);
            try {
                myGame.readBank(this.adress);
                myGame.randNewWord();
                succes = true;
            } //Usage of static public method of View to display exception
            catch (HangmanException e) {
                View.displayError(e.getMessage());
            }
        } //Words from stream
        else if (this.mode == 1) {
            myGame = new HangmanGame(max_errors);
            String tmp = "";
            int counter = 1;
            View.displayLine("Add words to bank");
            View.displayLine("Type \"exit\" to finish");

            //It will ask for new word until user types "exit"
            while (true) {
                View.displayLine("\nAdd word no [" + counter + "]");
                tmp = myView.readLine();

                if (!tmp.equals("exit")) {
                    if (myGame.addWordToBank(tmp)) {
                        counter++;
                    }
                } else {
                    break;
                }
            }
            myGame.randNewWord();
            succes = true;
        } else if (this.mode == 2) {
            myGame.randNewWord();
            succes = true;
        }

        return succes;
    }

    /**
     * Main method of game what carries gui and game algorythm managment
     *
     * @return [0] When failiture, [1] When victory
     */
    public int play() {

        // we play while nbErrors is lower than max errors or user has found the word
        while (myPlayer.getFails() < this.max_errors) {

            //Displays console game gui
            View.gameScrean(myPlayer, myGame.getWordToFind(), this.max_errors);

            // get next input from user
            char typed = myView.readChar();

            // update word found
            // we update only if typed has not already been entered or was a special character
            if ((!myPlayer.getAlreadyTried().contains(typed)) && (typed != '#')) {
                // we check if word to find contains typed
                if (myGame.getChosenWord().contains(String.valueOf(typed))) {
                    // if so, we replace _ by the character typed
                    int index = myGame.getChosenWord().indexOf(String.valueOf(typed));

                    while (index >= 0) {
                        myGame.getWordToFind().set(index, typed);
                        index = myGame.getChosenWord().indexOf(typed, index + 1);
                    }
                } else {
                    myPlayer.incErrors();
                }

                // typed is now a letter entered
                myPlayer.addToAlreadyTried(typed);
                myPlayer.incTries();
            }

            //Checks if game is finished
            if (myGame.compareWords()) {
                return 1;
            }
        }
        return 0;
    }

    
    public boolean gameEndLoop(){
    String tmp = "Hello";       //Temporary string to store information
    //Ending loop to choose what to do
        while (true) {
            View.displayLine("Do you want to play again? (Y/N)");
            tmp = myView.readLine();
            tmp = tmp.toUpperCase();

            //Loading new game
            if (tmp.equals("Y")) {

                //Adding new elements to existing bank
                while (true) {
                    View.displayLine("Do you want to add new elements to bank or start compleatly new game? (update/new)");
                    tmp = myView.readLine();

                    //Update existing bank of words
                    if (tmp.equals("update")) {
                        //Do not check while creating new game 
                        this.setMode(2);
                        this.upgrateGame();
                        break;
                    } //Type new settings and bank
                    else if (tmp.equals("new")) {
                        this.askForArgs();
                        break;
                    }
                }
                 return false;
            } //End of game
            else if (tmp.equals("N")) {
                View.displayLine("See yea " + this.myPlayer.getName() + "!");
                return true;
            }
        }
    }
    /**
     * Main method to be executed when program starts
     *
     * @param args The command line arguments 
     * args[0] - Mode: [0] Read bank from file, [1] Read bank from user input 
     * args[1] - PlayerName: Name of the player in first round 
     * args[2] - MaxErrors: Maximum number of errors to be made in first round 
     * args[3] - FilePath: Path of file to read words (only when Mode = 0)
     */
    public static void main(String[] args) {

        int gameStatus = -1;        //Error (-1), Game filed(0), Game won(1)
        boolean gameOver = false;   //When true -> end program

        //Creating new Controller
        Controller myControl;
        if(args.length == 4)
            myControl = new Controller(args[0], args[1], args[2], args[3]);
        else if(args.length == 3)
            myControl = new Controller(args[0], args[1], args[2]);
        else
            myControl = new Controller();
        
        View myView = myControl.getMyView();
        
        //Game runs until it ends, so simple ;p
        while (!gameOver) {

            //Creating new player
            myControl.newPlayer();

            //Creating new game and checking if it was succeeded
            if (myControl.newGame()) {
                View.displayLine("Game created succesfuly!");
                gameStatus = myControl.play();
                
            } else if (myControl.getMode() == 0) {
                View.displayError("Change file path and try again!");
                gameStatus = -1;
            } else {
                View.displayError("Undefined error occurred!");
                gameStatus = -1;
            }

            //If game ran correctly
            if (gameStatus > -1) {
                View.endGameScrean(myControl.getMyPlayer(), myControl.getMyGame().getChosenWord(), myControl.getMax_errors(), gameStatus);
            }
            
            gameOver = myControl.gameEndLoop();
        }
    }
}
