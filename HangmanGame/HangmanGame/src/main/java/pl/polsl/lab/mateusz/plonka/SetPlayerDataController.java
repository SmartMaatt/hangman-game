package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pl.polsl.lab.mateusz.plonka.exception.HangmanException;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;

/**
 * Adding main game information scene
 * @author Mateusz Płonka
 */
public class SetPlayerDataController implements Initializable {

    @FXML
    private TextField text2_input;
    @FXML
    private TextField text3_input;
    @FXML
    private TextField text4_input;
    @FXML
    private Text message;
    @FXML
    private Button play_btn;
    @FXML
    private RadioButton choose_mode0;
    @FXML
    private RadioButton choose_mode1;
    
    //Variables
    private int input1 = 0;
    private String input2 = "";
    private int input3 = -1;
    private String input4 = "";
    private HangmanGame myGame;
    
    /**
     * Chceck if all nessesary values are given
     * @return true if values are given, false if not
     */
    private boolean allValues(){
        if(!input2.equals("") && input3 != -1) return true;
        else return false;
    }
    
    /*FXML methods*/
    
    /**
     * Changes gui depending on choosen mode in radio buttons
     * @throws IOException 
     */
    @FXML
    private void choose_mode() throws IOException {
    
        if (choose_mode0.isSelected()) {
            text4_input.setDisable(false);
            input1 = 0;
        } else {
            text4_input.setDisable(true);
            input1 = 1;
        }

    }

    /**
     * Reaction on play button and validation of data
     * @throws IOException
     */
    @FXML
    private void play_btn() throws IOException {

        if (allValues()) {
            //Word bank taken from file
            if (input1 == 0) {
                if (text4_input.getText().matches(".*\\b.txt")) {
                    input4 = text4_input.getText();
                    message.setText("");

                    boolean succes = false;
                    myGame = new HangmanGame(input3, input2);
                    try {
                        myGame.readBank(input4);
                        myGame.randNewWord();
                        succes = true;
                    } //Usage of static public method of View to display exception
                    catch (HangmanException e) {
                        message.setText(e.getMessage());
                        text4_input.setText("");
                    }
                    
                    if (succes) {
                        //Load GameBoard
                        try {
                            App.setRoot("GameBoard");
                        } catch (IOException ex) {
                            message.setText(ex.getMessage());
                        }
                        GameBoard GB = App.getLoader().getController();
                        GB.setValue(myGame);
                    } 

                } else {
                    text4_input.setText("");
                    message.setText("End of file path need to be .txt");
                }
            } else if (input1 == 1) {
                myGame = new HangmanGame(input3, input2);
                
                //Load InsertData to get words from user
                try {
                    App.setRoot("InsertData");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                //Sending params
                InsertData ID = App.getLoader().getController();
                ID.setValues(0, "Exit", 0, "Continue", 1, "Add word no ", "", myGame);
            }
        } else{
                  message.setText("Enter all the necessary information!");
                }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    /**
     * Listiner of player name input
     */    
    text2_input.textProperty().addListener(new ChangeListener<String>() {
	@Override
	public void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) {
		
                if(newValue.length() > 10) text2_input.setText(oldValue);        
            
		input2 = text2_input.getText();
                input2 = input2.replaceAll("[^a-zA-Z0-9' ']+", "");
                input2.trim();
                text2_input.setText(input2);
                message.setText("");
	}
    });
    
    
    /**
     * Listiner of max errors
     */
    text3_input.textProperty().addListener(new ChangeListener<String>() {
	@Override
	public void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) {
	
        if(newValue.length() > 2) {
            newValue = oldValue;
            text3_input.setText(oldValue);
        }
            
        if (!newValue.matches("[0-9]*")) {
            text3_input.setText(newValue.replaceAll("[^0-9]", ""));
            message.setText("Value is not numeric!");
        } else {
            message.setText("");
        }
        
        input3 = Integer.parseInt(text3_input.getText());
	}
    });
    }   
}
