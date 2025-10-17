module com.mycompany.computergraphics2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.computergraphics2 to javafx.fxml;
    exports com.mycompany.computergraphics2;
}
