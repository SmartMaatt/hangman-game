package pl.polsl.lab.mateusz.plonka.model;

import java.util.ArrayList;

/**
 * Class of current player containing user's name, number of tries and already used characters
 * @author Mateusz Płonka
 */
public class Player {
    
    private int tries = 0;                          //Number of tries in game
    private int fails = 0;                          //Number of fails
    private String name;                            //Player's name
    private ArrayList<Character> alreadyTried =  new ArrayList<Character>();  //Letters that has already tried
    
    //Default constructor
    public Player(){
        tries = 0;
        name = "Undefined";
    }
    
    //More... effective constructor ;)
    public Player(String name){
        this.tries = tries;
        this.name = name;
    }
    
    /*GETTERS*/
    public int getTries() {
        return tries;
    }
    
    public int getFails() {
        return fails;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Character> getAlreadyTried() {
        return alreadyTried;
    }
    
    
    /*SETTERS*/
    //Incrementing number of tries
    public void incTries(){
        ++this.tries;
    }
    
    //Incrementing number of errors
    public void incErrors(){
        ++this.fails;
    }

    //Sets name of player
    public void setName(String name) {
        this.name = name;
    }

    //Adds new char to existing dynamic array
    public void addToAlreadyTried(char letter) {
        this.alreadyTried.add(letter);
    }
}
