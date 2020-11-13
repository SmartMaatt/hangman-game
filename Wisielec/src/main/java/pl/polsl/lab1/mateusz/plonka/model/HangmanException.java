package pl.polsl.lab1.mateusz.plonka.model;

/**
 * Own exception that handles errors associated with HangmanGame
 * @author Mateusz
 */
public class HangmanException extends Exception {
    public HangmanException(){};
    public HangmanException(String msg){super(msg);}
}
