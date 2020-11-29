package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;

/**
 * FXML Controller class Control of pop up no 1 Asks player to choose to play in
 * GUI or console version
 *
 * @author Mateusz Płonka
 */
public class AskWindow implements Initializable {

    @FXML
    private Text msg;

    @FXML
    private Button btn0;
    private int btn0_action = 0;

    @FXML
    private Button btn1;
    private int btn1_action = 1;

    //Object of game
    HangmanGame myGame;

    /**
     * Sets presentation of scene
     *
     * @param value0 value of left button
     * @param action0 code of action of left button
     * @param value1 value of right button
     * @param action1 code of action of right button
     * @param title value of big title
     */
    public void setValues(String value0, int action0, String value1, int action1, String title) {
        btn0.setText(value0);
        btn0_action = action0;

        btn1.setText(value1);
        btn1_action = action1;

        msg.setText(title);
    }

    /**
     * Sets presentation of scene
     *
     * @param myGame object of game
     * @param value0 value of left button
     * @param action0 code of action of left button
     * @param value1 value of right button
     * @param action1 code of action of right button
     * @param title value of big title
     */
    public void setValues(HangmanGame myGame, String value0, int action0, String value1, int action1, String title) {
        btn0.setText(value0);
        btn0_action = action0;

        btn1.setText(value1);
        btn1_action = action1;

        msg.setText(title);
        this.myGame = myGame;
    }

    /**
     * Reaction on click of any button on this scene
     *
     * @param event code of event to execute
     */
    private void btn_click(int event) {

        //Play gui version
        if (event == 0) {
            App.setGameVersion(0);
            try {
                App.setRoot("SetPlayerData");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }//Play console version
        else if (event == 1) {
            App.setGameVersion(1);
            Stage stage = (Stage) App.getScene().getWindow();
            stage.close();
        } //Open next ask window
        else if (event == 2) {

            try {
                //Load Asking window
                App.setRoot("AskWindow");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            AskWindow AW = App.getLoader().getController();
            AW.setValues(myGame, "Update", 4, "New", 5, "Do you want to add new elements to bank or\nstart compleatly new game?");

        }//Close game
        else if (event == 3) {
            App.setGameVersion(0);
            Stage stage = (Stage) App.getScene().getWindow();
            stage.close();

        }//Updating bank
        else if (event == 4) {

            try {
                //Load Asking window
                App.setRoot("InsertData");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            InsertData ID = App.getLoader().getController();
            ID.setValues(1, "...", 0, "Next", 2, "Insert new player name", "", myGame);

        }//Complete new game
        else if (event == 5) {

            try {
                //Load Asking window
                App.setRoot("SetPlayerData");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }//Dynamic show of word bank
        else if (event == 6) {
            ShowWords.display(myGame.getWordBank());
        }//Add new words
        else if (event == 7) {

            try {
                //Load Asking window
                App.setRoot("InsertData");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //Sending params
            InsertData ID = App.getLoader().getController();
            ID.setValues(0, "Exit", 0, "Continue", 1, "Add word no ", "", myGame);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn0.setOnAction(e -> {
            btn_click(btn0_action);
        });
        btn1.setOnAction(e -> {
            btn_click(btn1_action);
        });
    }

}
