import pl.polsl.lab.mateusz.plonka.exception.HangmanException;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.polsl.lab.mateusz.plonka.model.*;

/**
 * @author Mateusz Płonka
 */
public class testHangmanGame {
    
    private HangmanGame myGame;

    @BeforeEach
    public void setUp() {
        myGame = new HangmanGame();
    }
    
    /**
     * Checks the situation of reading an corupted or empty file
     * @param candidate Wrong file paths to check
     */
    @ParameterizedTest
    @ValueSource(strings = {"test", "test.tx", "empty.txt"}) // incorrect extension and empty file
    void testReadBankFail(String candidate) {
        //GIVEN
        boolean test = true;
        String msg = "";
        //WHEN
        try {
            myGame.readBank(candidate);
        } catch (HangmanException e) {
            msg = e.getMessage();
            test = false;
        }
        //THEN
        assertFalse(test, "Error occured " + msg);
    }
    
    /**
     * Checks the situation of reading correct file
     * @param candidate Correct file paths to check
     */
    @ParameterizedTest
    @ValueSource(strings = {"test.txt"}) // correct file
    void testReadBankSucces(String candidate) {
        //GIVEN
        boolean test = true;
        //WHEN
        try {
            myGame.readBank(candidate);
        } catch (HangmanException e) {
            test = false;
        }
        //THEN
        assertTrue(test, "File loaded successfuly!");
    }
    
    /**
     * Checks the situation of comparing words that are not the same
     * @param wordToFind - part of word already revealed by player
     * @param chosenWord - word to find in current round
     */
    @ParameterizedTest
    @CsvSource({"TE_T,TEST", "____,TEST", "JA VA,JAVA"}) // incorrect match
    void testCompareWordsFail(String wordToFind, String chosenWord) {
        //GIVEN
        ArrayList<Character> charsArr = new ArrayList<Character>();
        for (char c : wordToFind.toCharArray()) {
            charsArr.add(c);
        }
        myGame.setWordToFind(charsArr);
        myGame.setChosenWord(chosenWord);
        
        //WHEN
        boolean test = myGame.compareWords();
        
        //THEN
        assertFalse(test, "Words are not equal!");
    }
    
    /**
     * Checks the situation of comparing words that are the same
     * @param wordToFind - part of word already revealed by player
     * @param chosenWord - word to find in current round
     */
    @ParameterizedTest
    @CsvSource({"TEST,TEST", "JAVA,JAVA", "POLSL,POLSL"}) // correct match
    void testCompareWordsSucces(String wordToFind, String chosenWord) {
        //GIVEN
        ArrayList<Character> charsArr = new ArrayList<Character>();
        for (char c : wordToFind.toCharArray()) {
            charsArr.add(c);
        }
        myGame.setWordToFind(charsArr);
        myGame.setChosenWord(chosenWord);
        
        //WHEN
        boolean test = myGame.compareWords();
        
        //THEN
        assertTrue(test, "Words are equal!");
    }
    
    /**
     * Checks situation of adding new word with only special characters
     * @param word - word to be added
     */
    @ParameterizedTest
    @ValueSource(strings = {"&*&@", "!!:"}) // incorrect words
    void testAddWordToBankFail(String word) {
        //GIVEN
        //WHEN
        boolean test = myGame.addWordToBank(word);
        //THEN
        assertFalse(test, "Word consisted of only special characters!");
    }
    
    /**
     * Checks situation of adding new, correct word
     * @param word - word to be added
     */
    @ParameterizedTest
    @ValueSource(strings = {"java", "JAVA", "JA VA", "Route98"}) // correct words
    void testAddWordToBankSucces(String word) {
        //GIVEN
        //WHEN
        boolean test = myGame.addWordToBank(word);
        //THEN
        assertTrue(test, "Given word was able to convert into game pass!");
    }  
}
