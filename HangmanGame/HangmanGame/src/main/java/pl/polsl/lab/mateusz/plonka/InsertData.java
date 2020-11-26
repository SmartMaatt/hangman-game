package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import java.net.URL;
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
 * Class controlls insertion of new words to play 
 * @author Mateusz Płonka
 */
public class InsertData implements Initializable {
    
    @FXML
    private Text msg;
    @FXML
    private Text msgResult;
    
    @FXML
    private Button btnClose;
    private int btnClose_action = 0;
    
    @FXML
    private Button btnCnt;
    private int btnCnt_action = 1;
   
    @FXML
    private TextField TextBox;
    private String correctedString = "";
    
    /*VARIABLES*/
    int mode = 0;
    int iterator = 0;
    String msgFromInput = "";
    String playerName = "";
    int max_errors = 0;
    private HangmanGame myGame;
    
    /**
     * Corrects data to program convention
     * @param param Uncorrected word
     * @return Corrected word
     */
    private String correctWord(String param){
        String word = param;
        word = word.toUpperCase();
        word = word.replaceAll("[^A-Z0-9' ']+", "");
        word.trim();
        return word;
    }
    
    /**
     * Sets presentation of scene
     * @param valueClose value of left button
     * @param actionClose code of action of left button
     * @param valueCnt value of right button
     * @param actionCnt code of action of right button
     * @param title value of big title
     * @param myGame object of game
     */
    public void setValues(int mode, String valueClose, int actionClose, String valueCnt, int actionCnt, String msg, String msgResult, HangmanGame myGame){
        btnClose.setText(valueClose);
        btnClose_action = actionClose;
        
        btnCnt.setText(valueCnt);
        btnCnt_action = actionCnt;
        
        this.myGame = myGame;
        iterator = myGame.getWordBank().size() + 1;
        
        this.mode = mode;
        msgFromInput = msg;
        
        if (mode == 0)
            this.msg.setText(msg + iterator);
        else {
            this.msg.setText(msg);
            btnClose.setDisable(true);
        }
        
        this.msgResult.setText(msgResult);
    }
    
    /**
     * Execute action depending on given code
     * @param event code of action
     */
    private void btn_click(int event) {
        //Finish adding words and play
        if (event == 0) {
            
            if (iterator > 1) {
                //Load GameBoard
                try {
                    App.setRoot("GameBoard");
                } catch (IOException ex) {
                    msg.setText(ex.getMessage());
                }

                myGame.randNewWord();
                GameBoard GB = App.getLoader().getController();
                GB.setValue(myGame);
            }else{
                msgResult.setText("Empty bank of words!");
            }
            
        }//Add this word to bank 
        else if (event == 1) {
                        
            if(myGame.addWordToBank(correctedString)){
            
                String tmp = correctedString;
                //Cleaning and setting up view
                iterator = myGame.getWordBank().size() + 1;
                msg.setText(msgFromInput + iterator);
                TextBox.setText("");
                msgResult.setText("Value " + tmp + " was added correctly!");
            }else{
                TextBox.setText("");
                msgResult.setText("Given word was empty or consisted of\nonly special characters!");
            }
    
        }//Add new player name
        else if(event == 2){
        
            if(!TextBox.getText().equals("")){
                this.playerName = TextBox.getText();
                msg.setText("Inser new max mistakes!");
                TextBox.setText("");
                myGame.newPlayer(this.playerName);
                btnCnt_action = 3;
                this.mode = 2;
                
            }else{
                msgResult.setText("Name input is empty");
            }
        }//Add new error counter
        else if(event == 3){
            
            if(!TextBox.getText().equals("")){
                this.max_errors = Integer.parseInt(TextBox.getText());
                myGame.setMAX_ERRORS(this.max_errors);
                
                try {
                    App.setRoot("AskWindow");
                } catch (IOException ex) {
                    msgResult.setText(ex.getMessage());
                }
                
                AskWindow AW = App.getLoader().getController();
                AW.setValues(myGame, "Show", 6, "Next", 7, "Do you want to display word bank?");
                
            }else{
                msgResult.setText("Max mistakes input is empty");
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        btnClose.setOnAction(e -> {btn_click(btnClose_action);});
        btnCnt.setOnAction(e -> {btn_click(btnCnt_action);});   
        
        /**
        * Listiner of given word
        */
        TextBox.textProperty().addListener(new ChangeListener<String>() {
	@Override
	public void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) {
            
            //Detects new words
            if (mode == 0) {
                if (newValue.length() > 20) {
                    TextBox.setText(oldValue);
                }

                correctedString = correctWord(TextBox.getText());
                msgResult.setText("Result: " + correctedString);
            }
            //Detects new player name
            else if(mode == 1){
                if(newValue.length() > 10) TextBox.setText(oldValue);        
            
                String tmp = TextBox.getText();
                tmp = tmp.replaceAll("[^a-zA-Z0-9' ']+", "");
                tmp.trim();
                TextBox.setText(tmp);
                msgResult.setText("");
            }
            //Detects new error counter
            else if(mode == 2){
                if (newValue.length() > 2) {
                    newValue = oldValue;
                    TextBox.setText(oldValue);
                }

                if (!newValue.matches("[0-9]*")) {
                    TextBox.setText(newValue.replaceAll("[^0-9]", ""));
                    msgResult.setText("Value is not numeric!");
                } else {
                    msgResult.setText("");
                }
            }
	}
    }); 
    }    
    
}
