package edu.guilford.ctisfinal.GUI.TabControllers;

import ai.djl.translate.TranslateException;
import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static edu.guilford.ctisfinal.Backend.Inference.Embeddings.SimilarityCalculator.getKHighestSimilarities;

public class TweetWriterController {

    ContextManager manager = ContextManager.getInstance();

    @FXML
    private TextField text;
    @FXML private TextField followers;
    @FXML private Button predictButton;
    @FXML private Label resultLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private VBox similaritiesBox;

    @FXML private TextField neighborsField;
    @FXML
    public void initialize() {

        UnaryOperator<TextFormatter.Change> digitFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        };

        followers.setTextFormatter(new TextFormatter<>(digitFilter));
        neighborsField.setTextFormatter(new TextFormatter<>(digitFilter));

        neighborsField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (Integer.parseInt(newValue) > manager.getDf().getRowCount()) {
                    newValue = manager.getDf().getRowCount() + "";
                    neighborsField.setText(newValue);
                }
    });


        // Initialize the controller
        predictButton.setOnAction(event -> {
            String textInput = text.getText();
            float followersInput;
            int neighborsInput;
            try {
                 followersInput = Integer.parseInt(followers.getText());
                 neighborsInput = Integer.parseInt(neighborsField.getText());
                if (textInput.isEmpty()) {
                    text.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                    return;
                } else {
                    text.setStyle(null);
                }
            } catch (NumberFormatException e) {
                resultLabel.setText("Please fill out all fields.");
                resultLabel.setVisible(true);
                return;
            }
            String result;

            //check to make sure that none of the boxes are empty

            float[] embedding;
            try {
                embedding = ContextManager
                        .getInstance()
                        .getEmbeddingCreator().getEmbedding(textInput);

            } catch (TranslateException e) {
                throw new RuntimeException(e);
            }

            try {
                LocalDate date = datePicker.getValue();
                if (date == null) {
                    datePicker.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                    return;
                }

                // 2) parse time
                LocalTime time = LocalTime.parse(
                        timeField.getText(),
                        DateTimeFormatter.ofPattern("H:mm")
                );

                // 3) combine to epoch seconds
                LocalDateTime dt = LocalDateTime.of(date, time);
                long epochSeconds = dt
                        .atZone(ZoneId.systemDefault())
                        .toEpochSecond();

                // 4) call your prediction
                float score = manager.getModel().predict(
                        embedding,
                        followersInput,
                        (float) epochSeconds
                );

                result = String.format("Predicted popularity: %.0f", score);
                ArrayList<String> sims = getKHighestSimilarities(embedding, neighborsInput, manager.getDf());

                similaritiesBox.getChildren().clear();
                for (String s : sims) {
                    Label lbl = new Label(s);
                    similaritiesBox.getChildren().add(lbl);
                }


            } catch (TranslateException e) {
                throw new RuntimeException(e);
            }
            resultLabel.setText(result);
            resultLabel.setVisible(true);
        });


}
}
