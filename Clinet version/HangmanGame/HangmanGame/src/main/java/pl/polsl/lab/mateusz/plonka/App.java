package pl.polsl.lab.mateusz.plonka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

import pl.polsl.lab.mateusz.plonka.*;
import pl.polsl.lab.mateusz.plonka.model.HangmanGame;
import pl.polsl.lab.mateusz.plonka.model.ManageDeGame;
import pl.polsl.lab.mateusz.plonka.view.View;

/**
 * Main of JavaFX App
 */
public class App extends Application {

    private static int GameVersion = 0; //[0] GUI, [1] Console
    private static FXMLLoader fxmlLoader;
    private static Scene scene;

    /**
     * Main javaFX method to execute first scene
     *
     * @param stage program main stage
     * @throws IOException I/O data exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("AskWindow"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Epic HangmanGame");
        stage.getIcons().add(new Image("file:icon.png"));
        stage.show();
    }

    /**
     * Sets FXML loader to scene variable
     *
     * @param fxml Name of Scene to jump
     * @throws IOException I/O data exception
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Execute FXMLloader from given name
     *
     * @param fxml name of scene to jump
     * @return loaded fxml
     * @throws IOException I/O data exception
     */
    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    //Getter of FXMLloader
    public static FXMLLoader getLoader() {
        return fxmlLoader;
    }

    //Getter of Scene
    public static Scene getScene() {
        return scene;
    }

    //Setter of GameVersion - [0] Gui, [1] Console
    static void setGameVersion(int GameVersion) {
        App.GameVersion = GameVersion;
    }

    //Getter of GameVersion - [0] Gui, [1] Console
    static int getGameVersion() {
        return App.GameVersion;
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
        if (App.getGameVersion() == 1) {
            int gameStatus = -1;        //Error (-1), Game filed(0), Game won(1)
            boolean gameOver = false;   //When true -> end program

            //Creating new ManageDeGame
            ManageDeGame myControl;
            if (args.length == 4) {
                myControl = new ManageDeGame(args[0], args[1], args[2], args[3]);
            } else if (args.length == 3) {
                myControl = new ManageDeGame(args[0], args[1], args[2]);
            } else {
                myControl = new ManageDeGame();
            }

            View myView = myControl.getMyView();

            //Game runs until it ends, so simple ;p
            while (!gameOver) {

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
                    myView.endGameScrean(myControl.getMyGame().getMyPlayer(), myControl.getMyGame().getChosenWord(), myControl.getMax_errors(), gameStatus);
                }

                gameOver = myControl.gameEndLoop();
            }
        }
    }
}
