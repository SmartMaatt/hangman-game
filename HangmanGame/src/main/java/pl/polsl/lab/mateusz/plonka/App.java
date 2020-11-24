package pl.polsl.lab.mateusz.plonka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import pl.polsl.lab.mateusz.plonka.model.ManageDeRound;
import pl.polsl.lab.mateusz.plonka.view.View;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("SetPlayerData"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    static void setRoot(FXMLLoader fxmlLoader) throws IOException {
        scene.setRoot(fxmlLoader.load());
    }

    static FXMLLoader getFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    
    /**
     * Main method to be executed when program starts
     *
     * @param args The command line arguments args[0] - Mode: [0] Read bank from
     * file, [1] Read bank from user input args[1] - PlayerName: Name of the
     * player in first round args[2] - MaxErrors: Maximum number of errors to be
     * made in first round args[3] - FilePath: Path of file to read words (only
     * when Mode = 0)
     */
    public static void main(String[] args) {
        
        launch();
        int gameStatus = -1;        //Error (-1), Game filed(0), Game won(1)
        boolean gameOver = false;   //When true -> end program

        //Creating new ManageDeRound
        ManageDeRound myControl;
        if (args.length == 4) {
            myControl = new ManageDeRound(args[0], args[1], args[2], args[3]);
        } else if (args.length == 3) {
            myControl = new ManageDeRound(args[0], args[1], args[2]);
        } else {
            myControl = new ManageDeRound();
        }

        View myView = myControl.getMyView();

        //Game runs until it ends, so simple ;p
        while (!gameOver) {

            //Creating new player
            myControl.newPlayer();

            //Creating new game and checking if it was succeeded
            if (myControl.newGame()) {
                myView.displayLine("Game created succesfuly!");
                gameStatus = myControl.play();

            } else if (myControl.getMode() == 0) {
                myView.displayError("Change file path and try again!");
                gameStatus = -1;
            } else {
                myView.displayError("Undefined error occurred!");
                gameStatus = -1;
            }

            //If game ran correctly
            if (gameStatus > -1) {
                myView.endGameScrean(myControl.getMyPlayer(), myControl.getMyGame().getChosenWord(), myControl.getMax_errors(), gameStatus);
            }

            gameOver = myControl.gameEndLoop();
        }
    }
}