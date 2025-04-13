module edu.guilford.final {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.guilford.final to javafx.fxml;
    exports edu.guilford.final;
}