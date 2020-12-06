package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;

/**
 * Main board of HangmanGame
 *
 * @author Mateusz Płonka
 */
public class GameBoard implements Initializable {

    @FXML
    private TextField letter_input;
    private Character letter = '#';

    @FXML
    private Text msg;
    @FXML
    private Text text_upperPlayerName;
    @FXML
    private Text text_playerName;
    @FXML
    private Text text_triesAndErrors;
    @FXML
    private Text text_usedCharacters;
    @FXML
    private Text wordToFind;
    @FXML
    private Button try_btn;

    //Variables
    private HangmanGame myGame;

    /**
     * Displays unfound word
     *
     * @param wToFind Word that is to find in this round
     */
    private void displayUnfoundWord(ArrayList<Character> wToFind) {

        //No need to use index as above
        //Simple usage of stream
        String tmp = "";
        for (Character c : wToFind) {
            tmp += String.valueOf(c) + " ";
        }
        wordToFind.setText(tmp);
    }

    /**
     * Displays already used characters in stats section
     *
     * @param usedCharacters container of already used characters
     */
    private void displayUsedCharacters(ArrayList<Character> usedCharacters) {
        String tmp = "Used chars: ";
        for (Character c : usedCharacters) {
            tmp += String.valueOf(c) + ", ";
        }
        text_usedCharacters.setText(tmp);
    }

    /**
     * Sets up game object
     *
     * @param myGame game object
     */
    public void setValue(HangmanGame myGame) {
        this.myGame = myGame;
        updateGui();
        msg.setText("");
    }

    /**
     * Updates gui using params of game and player
     */
    private void updateGui() {
        text_upperPlayerName.setText("Hello " + myGame.getMyPlayer().getName());
        text_playerName.setText("Player name: " + myGame.getMyPlayer().getName());
        text_triesAndErrors.setText("Tries: " + myGame.getMyPlayer().getTries() + " | Mistakes: " + myGame.getMyPlayer().getFails() + "/" + myGame.getMAX_ERRORS());
        displayUnfoundWord(myGame.getWordToFind());
        displayUsedCharacters(myGame.getMyPlayer().getAlreadyTried());
        letter_input.setText("");
        letter = '#';
    }

    /**
     * Reaction on clicking button to pull new letter and check game status
     *
     * @throws IOException I/O data exception
     */
    @FXML
    private void try_btn() throws IOException {

        if (letter != '#') {
            // update word found
            // we update only if typed has not already been entered or was a special character
            if (!myGame.getMyPlayer().getAlreadyTried().contains(letter) && (letter != '#')) {
                // we check if word to find contains typed
                if (myGame.getChosenWord().contains(String.valueOf(letter))) {
                    // if so, we replace _ by the character typed
                    int index = myGame.getChosenWord().indexOf(String.valueOf(letter));

                    while (index >= 0) {
                        //Change all same signs
                        myGame.getWordToFind().set(index, letter);
                        index = myGame.getChosenWord().indexOf(letter, index + 1);
                    }
                    msg.setText("");
                } else {
                    //Increse mistakes
                    myGame.getMyPlayer().incErrors();
                    msg.setText("No such letter in the word");
                }

                // typed is now a letter entered
                myGame.getMyPlayer().addToAlreadyTried(letter);
                myGame.getMyPlayer().incTries();

                //Checks if game is won
                if (myGame.compareWords()) {
                    //Load ending window
                    App.setRoot("Ending");

                    Ending end = App.getLoader().getController();
                    end.setValue(myGame, "Victory!");
                }

                //Checks if game is failed
                if (myGame.getMyPlayer().getFails() == myGame.getMAX_ERRORS()) {
                    //Load ending window
                    App.setRoot("Ending");

                    Ending end = App.getLoader().getController();
                    end.setValue(myGame, "Failure!");
                }
            } else {
                msg.setText("Already used character!");
            }
        } else {
            msg.setText("No value!");
        }

        //Updating gui
        updateGui();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Listiner of letter_input
         */
        letter_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {

                //Maximum amound of signs is 1
                if (newValue.length() > 1) {
                    letter_input.setText(oldValue);
                }

                String input = letter_input.getText();
                input = input.replaceAll("[^a-zA-Z0-9' ']+", "");
                input = input.toUpperCase();
                input.trim();
                letter_input.setText(input);

                //Casts value into character
                if (letter_input.getText().length() == 1) {
                    letter = input.charAt(0);
                } else {
                    letter = '#';
                }
            }
        });
    }
}
