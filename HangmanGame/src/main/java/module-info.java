module pl.polsl.lab.mateusz.plonka {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens pl.polsl.lab.mateusz.plonka to javafx.fxml;
    exports pl.polsl.lab.mateusz.plonka;
    exports pl.polsl.lab.mateusz.plonka.model;
}
