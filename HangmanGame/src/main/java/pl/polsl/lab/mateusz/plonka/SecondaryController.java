package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SecondaryController {

    @FXML
    private Label chuj;
    
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    @FXML
    public void SetText(String dupa){
        chuj.setText(dupa);
    }
}