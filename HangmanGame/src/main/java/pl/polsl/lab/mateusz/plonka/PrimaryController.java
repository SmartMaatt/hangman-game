package pl.polsl.lab.mateusz.plonka;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML
    private TextField cipa;
    
    @FXML
    private void switchToSecondary() throws IOException {
       FXMLLoader fxmlLoader = App.getFXML("secondary");
       App.setRoot(fxmlLoader);
       
       SecondaryController dupa = fxmlLoader.getController();
       dupa.SetText(cipa.getText());
    }
}
