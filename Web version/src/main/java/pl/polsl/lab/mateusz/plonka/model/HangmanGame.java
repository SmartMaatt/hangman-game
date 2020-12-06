package pl.polsl.lab.mateusz.plonka.model;

import pl.polsl.lab.mateusz.plonka.exception.HangmanException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the game
 *
 * @author Mateusz Płonka
 */
public class HangmanGame {

    /*VARIABLES*/
    //Words to play
    private ArrayList<MyString> bank = new ArrayList<MyString>();
    //Randomizer to select one of words
    private Random RANDOM = new Random();
    // Max errors before user lose
    private int maxErrors;
    // Word to find
    private String chosenWord;
    private ArrayList<Character> wordToFind = new ArrayList<Character>();
    //Player object
    private Player myPlayer;

    /*METHODS*/
 /*CONSTRUCTORS*/
    //Default
    public HangmanGame() {
        this.maxErrors = 10;
        newPlayer("Unknown");
    }

    //To set max errors
    public HangmanGame(int maxErrors, String pName) {
        this.maxErrors = maxErrors;
        newPlayer(pName);
    }

    /**
     * Creates a new player
     *
     * @param name Name of new Player
     */
    public void newPlayer(String name) {
        myPlayer = new Player(name);
    }

    /*CHOSEN WORD MANAGMENT*/
    /**
     * Method returning randomly next word to find
     *
     * @return Randomly generated string from bank
     */
    private String WordToFind() {
        return this.bank.get(RANDOM.nextInt(this.bank.size())).getValue();
    }

    /**
     * Allocates empty array of chars with size of chosen word
     *
     * @param word String to be converted info array of chars
     */
    private void createWordToFind(String word) throws HangmanException {
        this.wordToFind = new ArrayList<Character>();

        if (word.length() > 1) {
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) != ' ') {
                    this.wordToFind.add('_');
                } else {
                    this.wordToFind.add(' ');
                }
            }
        } else {
            throw new HangmanException("Given word doesn't exist!");
        }
    }

    /**
     * Method puts randomly generated word to variable
     */
    public void randNewWord() {
        this.chosenWord = WordToFind();
        try {
            createWordToFind(this.chosenWord);
        } catch (HangmanException ex) {
            Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Compares chosen word with word to find to determinate if game ended
     *
     * @return True if chosen and toBeFound words are the same, False when not
     */
    public boolean compareWords() {

        StringBuilder builder = new StringBuilder(this.wordToFind.size());
        for (Character ch : this.wordToFind) {
            builder.append(ch);
        }
        String tmp = builder.toString();

        if (this.chosenWord.equals(tmp)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds element to the bank of words
     *
     * @param word Word given to be added to bank
     * @return True if operation is succeeded, False if it finds word with only
     * special characters
     */
    public boolean addWordToBank(String word) {
        word = word.toUpperCase();
        word = word.replaceAll("[^A-Z0-9' ']+", "");
        word.trim();
        if (!word.equals("")) {
            System.out.println(word);

            MyString tmp = new MyString(word);
            this.bank.add(tmp);
            return true;
        } else {
            return false;
        }
    }

    /*GETTERS AND SETTERS*/
    //Getter of ChosenWord
    public String getChosenWord() {
        return this.chosenWord;
    }

    //Getter of word bank
    public ArrayList<MyString> getWordBank() {
        return this.bank;
    }

    //Getter of WordToFind
    public ArrayList<Character> getWordToFind() {
        return wordToFind;
    }

    //Getter of MyPlayer
    public Player getMyPlayer() {
        return myPlayer;
    }

    //Getter of maxErrors
    public int getMaxErrors() {
        return maxErrors;
    }

    //Setter of ChosenWord
    public void setChosenWord(String word) {
        this.chosenWord = word;
    }

    //Setter of WordToFind
    public void setWordToFind(ArrayList<Character> wordToFind) {
        this.wordToFind = wordToFind;
    }

    //Setter of MyPlayer
    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    public void setMaxErrors(int maxErrors) {
        this.maxErrors = maxErrors;
    }

}
