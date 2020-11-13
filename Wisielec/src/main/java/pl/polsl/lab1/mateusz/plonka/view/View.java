package pl.polsl.lab1.mateusz.plonka.view;

import java.util.ArrayList;
import java.util.Scanner;

import pl.polsl.lab1.mateusz.plonka.model.Player;



/**
 * Class manages messeges and contact with user
 * @author Mateusz
 */
public class View {
    
    //Variable of Scanner to read data from user.
    Scanner scan;
    
    //Constructor of view, defines an Scanner object
    public View(){
        this.scan = new Scanner(System.in);
    }
    
    /**
     * Displays single line form argumnet
     * @param text Message to display
     */
    static public void displayLine(String text){
        System.out.println(text);
    }
    
    /**
     * Displays single line form argumnet and style it as error
     * @param text Error information to display
     */
    static public void displayError(String text){
        System.out.println("!!! " + text.toUpperCase() + " !!!");
    }
    
    /**
     * Displays all elements of ArrayList with strings
     * @param array ArrayList of string to display
     */
    static public void displayArrayList(ArrayList<String> array){
        for(int i = 0; i<array.size(); i++){
            System.out.println("[" + i + "]" + array.get(i));
        }
    }
    
    /**
     * Displays unfound word
     * @param wToFind Word that is to find in this round
     * @param size    Size of word that needs to be found
     */
    static public void displayUnfoundWord(char[] wToFind, int size){
        for(int i = 0; i<size; i++){
            System.out.print(wToFind[i] + " ");
        }
         System.out.print("\n");
    }
    
    /**
     * Displays console game gui
     * @param myPlayer      Object of player to get information like PlayerName and Stats
     * @param wToFind       Word that is to find in this round
     * @param wToFindSize   Size of word that needs to be found
     * @param maxErrors     Maximum number of errors that could be done in this round
     */
    static public void gameScrean(Player myPlayer, char[] wToFind, int wToFindSize, int maxErrors){
        System.out.println("\n\n\nHANGMAN_GAME\n========================\n");
        displayUnfoundWord(wToFind, wToFindSize);
        System.out.println("\nSTATS:");
        System.out.println("Player Name: " + myPlayer.getName());
        System.out.println("Tries:" + myPlayer.getTries() + " | Errors: " + myPlayer.getFails() + "/" + maxErrors);
        System.out.print("Used signs: ");
        
        ArrayList<Character> signsArray = myPlayer.getAlreadyTried();
        for(int i = 0; i<signsArray.size(); i++)
            System.out.print(signsArray.get(i) + ",");
        
        System.out.println("\nEnter a letter:");
    }
    
    /**
     * Displays fail/win screan
     * @param myPlayer      Object of player to get information like PlayerName and Stats
     * @param wToFind       Word that was to find in this round
     * @param maxErrors     Maximum number of errors that could be done in this round
     * @param gameStatus    Info whenever game was won(1) or failed(0)
     */
    static public void endGameScrean(Player myPlayer, String wToFind, int maxErrors, int gameStatus ){
    
        if(gameStatus == 0)
        {
           System.out.println("\nFAILITURE ;(");
           System.out.println("Even great minds sometimes lose, don't worry " + myPlayer.getName());
        }
        else if(gameStatus == 1)
        {
           System.out.println("\nVICTORY ;)");
           System.out.println("Great minds, like you " + myPlayer.getName() + " ,always wins!");
        }
        
        System.out.println("The word to guess was: " + wToFind);
        System.out.println("==============================");
        System.out.println("STATS:");
        System.out.println("Tries: " + myPlayer.getTries());
        System.out.println("Errors: " + myPlayer.getFails() + "/" + maxErrors);
        System.out.print("Used signs: ");

        ArrayList<Character> signsArray = myPlayer.getAlreadyTried();
        for(int i = 0; i<signsArray.size(); i++)
            System.out.print(signsArray.get(i) + ",");
        
        System.out.print("\n");
    }
    
    /**
     * Reads a single line from user
     * @return Line read form user input
     */
    public String readLine(){
        return this.scan.nextLine(); 
    }
    
    /**
     * Reads a single character from user
     * @return First, converted letter of user input
     */
    public char readChar(){
        String str = this.scan.nextLine();
        
        //Correction of special charaters
        str = str.toUpperCase();
        str = str.replaceAll("[^A-Z0-9]+", "");
        str.trim();
        
        if(!str.isEmpty())
            return str.charAt(0);
        else
            return '#';
    }
}
