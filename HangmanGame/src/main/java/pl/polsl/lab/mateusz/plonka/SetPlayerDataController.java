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

/**
 * FXML Controller class
 * @author Mateusz
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
    private int input3 = 10;
    private String input4 = "";
    
    @FXML
    private void choose_mode() throws IOException {
    
        if(choose_mode0.isSelected()){
            text4_input.setEditable(true);
            System.out.println("dupa0");
        }
        else{
           text4_input.setEditable(false);
           System.out.println("dupa1");
        }
    
    }
    
    @FXML
    private void play_btn() throws IOException {
    
        if(choose_mode0.isSelected()){
            if (text4_input.getText().matches(".*\\b.txt")) {
                input4 = text4_input.getText();
                message.setText("");
            }else{
                text4_input.setText("");
                message.setText("End of file path need to be .txt");
            }
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
		
		input2 = text2_input.getText();
                input2 = input2.replaceAll("[^a-zA-Z0-9' ']+", "");
                input2.trim();
                text2_input.setText(input2);
	}
    });
    
    
    /**
     * Listiner of max errors
     */
    text3_input.textProperty().addListener(new ChangeListener<String>() {
	@Override
	public void changed(ObservableValue<? extends String> observable,
			String oldValue, String newValue) {
		
        if (!newValue.matches("[0-9]")) {
            text3_input.setText(newValue.replaceAll("[^0-9]", ""));
        }
        
        input3 = Integer.parseInt(text3_input.getText());
	}
    });
    }   
}
