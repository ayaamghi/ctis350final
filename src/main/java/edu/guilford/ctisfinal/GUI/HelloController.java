package edu.guilford.ctisfinal.GUI;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HelloController {

    @FXML
    private VBox container;


    @FXML
    public void initialize() throws Exception {

        InputStream svgStream = getClass().getResourceAsStream("/edu/guilford/ctisfinal/us.svg");
        Map<String, SVGPath> stateShapes = loadStatesFromSvg(svgStream);
        Slider timeSlider = new Slider(0, 100, 0); // Simulated time range
        timeSlider.setShowTickLabels(true);
        timeSlider.setShowTickMarks(true);

        Label timestampLabel = new Label();
        timeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            long unixTimestamp = convertSliderToUnix(newVal.doubleValue());
            timestampLabel.setText("Timestamp: " + unixTimestamp);
            updateMapColors(stateShapes, unixTimestamp);
        });
        VBox layout = new VBox();
        layout.setPadding(new Insets(10));

        Pane mapPane = new Pane();
        mapPane.getChildren().addAll(stateShapes.values());

        layout.getChildren().addAll(mapPane, timeSlider, timestampLabel);

        container.getChildren().addAll(mapPane, timeSlider, timestampLabel);


    }
    @FXML

    private long convertSliderToUnix(double sliderVal) {
        long start = Instant.parse("2020-01-01T00:00:00Z").getEpochSecond();
        long end = Instant.parse("2020-12-31T23:59:59Z").getEpochSecond();
        return start + (long)((sliderVal / 100.0) * (end - start));
    }
    Random rand = new Random();

    private void updateMapColors(Map<String, SVGPath> stateShapes, long timestamp) {
        for (Map.Entry<String, SVGPath> entry : stateShapes.entrySet()) {
            int tweetCount = rand.nextInt(1000); // Simulate frequency
            Color fill = getColorForCount(tweetCount);
            entry.getValue().setFill(fill);
        }
    }

    private Color getColorForCount(int count) {
        int red = Math.min(255, count / 4);
        return Color.rgb(red, 100, 150);
    }

    public Map<String, SVGPath> loadStatesFromSvg(InputStream svgStream) throws Exception {
        Map<String, SVGPath> statePaths = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(svgStream);

        NodeList paths = doc.getElementsByTagName("path");

        for (int i = 0; i < paths.getLength(); i++) {
            Element path = (Element) paths.item(i);
            String stateId = path.getAttribute("id");
            String d = path.getAttribute("d");

            if (!stateId.isEmpty() && !d.isEmpty()) {
                SVGPath svgPath = new SVGPath();
                svgPath.setContent(d);
                svgPath.setId(stateId);
                svgPath.setFill(Color.LIGHTGRAY);
                svgPath.setStroke(Color.BLACK);
                svgPath.setStrokeWidth(0.5);
                statePaths.put(stateId, svgPath);
            }
        }

        return statePaths;
    }

}