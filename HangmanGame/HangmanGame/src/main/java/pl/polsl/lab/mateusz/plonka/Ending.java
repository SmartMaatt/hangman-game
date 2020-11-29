package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;

/**
 * Displays ending screan
 * @author Mateusz Płonka
 */
public class Ending implements Initializable {
    
    @FXML
    private Text text_upperPlayerName;
    @FXML
    private Text text_playerName;
    @FXML
    private Text text_triesAndErrors;
    @FXML
    private Text text_usedCharacters;
    @FXML
    private Text text_wordToFind;
    @FXML
    private Text victory;
    @FXML
    private Button next_btn;

    //Variables
    private HangmanGame myGame;

    
    /**
     * Displays already used characters in stats section
     * @param usedCharacters container of already used characters
     */
    private void displayUsedCharacters(ArrayList<Character> usedCharacters){
        String tmp = "Used chars: ";
        for (Character c : usedCharacters) {
            tmp += String.valueOf(c) + ", ";
        }
        text_usedCharacters.setText(tmp);
    }
    
    /**
     * Sets up game object
     * @param myGame game object
     * @param victory message of victory or failure 
     */
    public void setValue(HangmanGame myGame, String victory){
        this.myGame = myGame;
        this.victory.setText(victory);
        updateGui();
    }
    
    /**
     * Updates gui using params of game and player
     */
    private void updateGui(){
        text_upperPlayerName.setText("Nice " + myGame.getMyPlayer().getName());
        text_playerName.setText("Player name: " + myGame.getMyPlayer().getName());
        text_triesAndErrors.setText("Tries: " + myGame.getMyPlayer().getTries() + " | Mistakes: " + myGame.getMyPlayer().getFails() + "/" + myGame.getMAX_ERRORS());
        displayUsedCharacters(myGame.getMyPlayer().getAlreadyTried());
        text_wordToFind.setText(myGame.getChosenWord());
    }
    
    /**
     * Reaction on clicking the button
     * @throws IOException 
     */
    @FXML
    private void next_btn() throws IOException {
        //Load Asking window
        App.setRoot("AskWindow");

        AskWindow AW = App.getLoader().getController();
        AW.setValues(myGame, "Yes", 2, "No", 3, "Do you want to play again?");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
}
