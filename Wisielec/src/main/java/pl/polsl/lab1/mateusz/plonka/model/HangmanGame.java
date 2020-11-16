package pl.polsl.lab1.mateusz.plonka.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class of the game
 * @author Mateusz Płonka
 */
public class HangmanGame {
    
    /*VARIABLES*/
    //Words to play
    private ArrayList<String> bank = new ArrayList<String>();
    //Randomizer to select one of words
    private Random RANDOM = new Random();
    // Max errors before user lose
    private int MAX_ERRORS;
    // Word to find
    private String chosenWord;
    private ArrayList<Character> wordToFind = new ArrayList<Character>();
     
 
     /*METHODS*/
    
     /*CONSTRUCTORS*/
     //Default
     public HangmanGame(){
         this.MAX_ERRORS = 10;
     }
     
     //To set max errors
     public HangmanGame(int max_errors){
         this.MAX_ERRORS = max_errors;
     }
     
     /*CHOSEN WORD MANAGMENT*/
     /**
      * Method returning randomly next word to find
      * @return Randomly generated string from bank
      */
     private String WordToFind() {
        return this.bank.get(RANDOM.nextInt(this.bank.size()));
    }
     
    /**
     * Allocates empty array of chars with size of chosen word
     * @param word String to be converted info array of chars
     */
    private void createWordToFind(String word){
        this.wordToFind = new ArrayList<Character>();
        
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) != ' ')
                this.wordToFind.add('_');
            else
                this.wordToFind.add(' ');
        }
    }
     
    
    /**
     * Method puts randomly generated word to variable
     */
    public void randNewWord(){
        this.chosenWord = WordToFind();
        createWordToFind(this.chosenWord);
    }

    
    /**
     * Reads text file for bank of words to play
     * @param adress Pash of file to read bank of words
     * @return  True if operation is succeeded
     * @throws HangmanException 
     */
    public boolean readBank(String adress) throws HangmanException{
        
        boolean operation = false;
     
            try {
                //Creates file variable and scanner
                File myObj = new File(adress);
                Scanner myReader = new Scanner(myObj);
                String data = "";
                
                //Reads all words and put them to ArrayList
                while (myReader.hasNextLine()) 
                {
                  data = myReader.nextLine().toUpperCase();
                  data = data.replaceAll("[^A-Z0-9' ']+", " ");
                  data.trim();
                  System.out.println(data);
                  this.bank.add(data);
                }
                myReader.close();
                
                if(!this.bank.isEmpty())
                    operation = true;
                else
                    throw new HangmanException("Given file is empty!");
                
                } 
            catch (FileNotFoundException e) {
                //Throwing own exception to handle errors associeted with file reading
                throw new HangmanException("File " + adress + " does not exist!");
                }
       
        
        return operation;
    }
    
    
    /**
    * Compares chosen word with word to find to determinate if game ended
    * @return True if chosen and toBeFound words are the same, False when not
    */
    public boolean compareWords(){
        
        StringBuilder builder = new StringBuilder(this.wordToFind.size());
        for(Character ch: this.wordToFind)
        {
            builder.append(ch);
        }
        String tmp = builder.toString();
        
        if(this.chosenWord.equals(tmp)) return true;
        else return false;
    }
    
    
    /**
     * Adds element to the bank of words
     * @param word Word given to be added to bank
     * @return True if operation is succeeded, False if it finds word with only special characters
     */
    public boolean addWordToBank(String word){
        word = word.toUpperCase();
        word = word.replaceAll("[^A-Z0-9' ']+", "");
        word.trim();
        if(!word.equals("")){
            System.out.println(word);
            this.bank.add(word);
            return true;
        }else{
            System.out.println("Word denied because of special characters");
            return false;
        }
    }
    
    
    /*GETTERS AND SETTERS*/
    //Getter of ChosenWord
    public String getChosenWord() {
        return this.chosenWord;
    }
    
    //Getter of word bank
    public ArrayList<String> getWordBank(){
        return this.bank;
    }
    
    //Getter of WordToFind
    public ArrayList<Character> getWordToFind() {
        return wordToFind;
    }
    
    //Setter of ChosenWord
    public void setChosenWord(String word){
        this.chosenWord = word;
    }

    public void setWordToFind(ArrayList<Character> wordToFind) {
        this.wordToFind = wordToFind;
    }
    
    
}