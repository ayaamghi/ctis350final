module edu.guilford.ctisfinal {
    requires javafx.graphics;

        requires javafx.controls;
        requires javafx.fxml;
    requires java.desktop;
    requires ai.djl.api;
    requires ai.djl.tokenizers;
    requires org.apache.commons.csv;
    requires java.naming;
    exports edu.guilford.ctisfinal.Backend;
    opens edu.guilford.ctisfinal.Backend to ai.djl.api;
    exports edu.guilford.ctisfinal.GUI;
    opens edu.guilford.ctisfinal.GUI to javafx.fxml;
    exports edu.guilford.ctisfinal.GUI.TabControllers;
    opens edu.guilford.ctisfinal.GUI.TabControllers to javafx.fxml;
    exports edu.guilford.ctisfinal.Backend.Inference;
    opens edu.guilford.ctisfinal.Backend.Inference to ai.djl.api;
    exports edu.guilford.ctisfinal.Backend.Inference.Embeddings;
    opens edu.guilford.ctisfinal.Backend.Inference.Embeddings to ai.djl.api;
}