package pl.polsl.lab.mateusz.plonka.exception;

/**
 * Own exception that handles errors associated with HangmanGame
 *
 * @author Mateusz
 */
public class HangmanException extends Exception {

    public HangmanException() {
    }

    ;
    public HangmanException(String msg) {
        super(msg);
    }
}
