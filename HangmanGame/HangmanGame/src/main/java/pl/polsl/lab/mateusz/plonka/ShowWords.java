package pl.polsl.lab.mateusz.plonka;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Class shows pop up that dynamicly displays array of words in bank
 *
 * @author Mateusz Płonka
 */
public class ShowWords {

    static boolean ansver = true;
    
    public static boolean display(ArrayList<String> WordBank) {

        //Setting up scene and stage
        Scene scene = new Scene(new Group());
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Word bank");
        window.getIcons().add(new Image("file:icon.png"));
        window.setWidth(340);
        window.setHeight(520);

        //Creating label
        final Label label = new Label("Word bank");
        label.setFont(new Font("System", 20));

        //Creating table of "Words"
        TableView<Words> table = new TableView<Words>();
        table.setEditable(true);

        //ObservableList to help managing dynamic table
        final ObservableList<Words> data
                = FXCollections.observableArrayList();

        //Fill ObservableList with word bank
        for (int i = 0; i < WordBank.size(); i++) {
            data.add(new Words(Integer.toString(i + 1), WordBank.get(i)));
        }

        //Creating columns and setting their value and width
        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(50);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Words, String>("Id"));

        TableColumn wordCol = new TableColumn("Word");
        wordCol.setMinWidth(230);
        wordCol.setCellValueFactory(
                new PropertyValueFactory<Words, String>("WordValue"));

        //Pushing values from ObservableList to table
        table.setItems(data);
        table.getColumns().addAll(idCol, wordCol);

        //Creating button to leave window
        Button okButton = new Button("Leave");
        okButton.setOnAction(e -> {
            ansver = true;
            window.close();
        });

        //Creating VBox and cosmetic addons
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, okButton);

        //Pushing elements to stage
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        window.setScene(scene);
        window.showAndWait();
        
        return ansver;
    }

    /**
     * Inner class managing words on table
     */
    public static class Words {

        private final SimpleStringProperty Id;
        private final SimpleStringProperty WordValue;

        //Constructor
        private Words(String IdVal, String Wval) {
            this.Id = new SimpleStringProperty(IdVal);
            this.WordValue = new SimpleStringProperty(Wval);
        }

        //Setters and getters
        public String getId() {
            return Id.get();
        }

        public void setId(String Id) {
            this.Id.set(Id);
        }

        public String getWordValue() {
            return WordValue.get();
        }

        public void setWordValue(String WVal) {
            this.WordValue.set(WVal);
        }
    }
}
