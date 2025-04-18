package edu.guilford.ctisfinal.GUI.TabControllers;

import ai.djl.translate.TranslateException;
import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.Inference.Embeddings.EmbeddingCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static edu.guilford.ctisfinal.Backend.Inference.Embeddings.SimilarityCalculator.getKHighestSimilarities;

public class TweetWriterController {

    ContextManager manager = ContextManager.getInstance();
    EmbeddingCreator embeddingCreator = manager.getEmbeddingCreator();

    @FXML
    private TextField text;
    @FXML private TextField followers;
    @FXML private Button predictButton;
    @FXML private Label resultLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private VBox similaritiesBox;

    @FXML
    public void initialize() {
        // Initialize the controller
        predictButton.setOnAction(event -> {
            String textInput = text.getText();
            float followersInput = Integer.parseInt(followers.getText());
            String result = null;

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
                    resultLabel.setText("Please pick a date");
                    resultLabel.setVisible(true);
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
                        followersInput, // followers
                        (float) epochSeconds   // or double, upcast as needed
                );

                result = String.format("Predicted popularity: %.0f", score);
                int k = 5; // or any number you like
                ArrayList<String> sims = getKHighestSimilarities(embedding, k, manager.getDf());

                similaritiesBox.getChildren().clear();
                for (String s : sims) {
                    Label lbl = new Label(s);
                    similaritiesBox.getChildren().add(lbl);
                }


            } catch (TranslateException e) {
                throw new RuntimeException(e);
            }
            System.out.println(result);
            resultLabel.setText(result);
            resultLabel.setVisible(true);
        });


}
}
