package edu.guilford.ctisfinal.GUI.TabControllers;

import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.Map.State;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * * This class is responsible for displaying a map of the US with states colored with a tooltip
 */
public class MapController {

    @FXML
    private AnchorPane mapPane;

    /***
     * This method takes a count and a max value and returns a hex color string. based off of ratio
     * @param count
     * @param max
     * @return
     */
    private String getHexColor(int count, int max) {
        if (count <= 0) return "#EEEEEE";
        double ratio = Math.min(1.0, count / (double) max);
        int r = (int) (200 + 55 * ratio);
        int gb = (int) (200 - 150 * ratio);
        return String.format("#%02X%02X%02X", r, gb, gb);
    }


    @FXML
    public void initialize() throws IOException, ParserConfigurationException, SAXException {
        Map<String, Integer> freq = ContextManager
                .getInstance()
                .getUsMap()
                .getStates().stream()
                .collect(Collectors.toMap(State::getAbbreviation, State::getFrequency));
        int maxCount = freq.values().stream().mapToInt(i -> i).max().orElse(1);
        // 2) Also build a map of state → List<String> exampleTweets
        //    We grab up to 3 ‘content’ strings from your CsvParser rows.
        Map<String, List<String>> examples = ContextManager.getInstance().getDf()
                .getRows().stream()
                .filter(r -> {
                    String code = r.get("state");
                    return code != null && !code.isBlank();
                })
                .collect(Collectors.groupingBy(
                        r -> r.get("state").trim().toUpperCase(),
                        Collectors.mapping(r -> r.get("content"),
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.size() > 3
                                                ? list.subList(0, 3)
                                                : list
                                ))
                ));

        // 2) parse the SVG into a DOM
        InputStream svgStream = getClass().getResourceAsStream("/edu/guilford/ctisfinal/us.svg");
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(svgStream);

        // 3) for each <path id="XX">, build an SVGPath with the right fill
        NodeList paths = doc.getElementsByTagName("path");
        for (int i = 0; i < paths.getLength(); i++) {
            Element el = (Element) paths.item(i);
            String code = el.getAttribute("id");
            String d    = el.getAttribute("d");
            if (code == null || code.isEmpty() || d.isEmpty()) continue;

            // 4) Color as before
            int count = freq.getOrDefault(code, 0);
            SVGPath svg = new SVGPath();
            // allow mouse events anywhere inside the path’s bounding box
            svg.setPickOnBounds(true);
            svg.setContent(d);
            svg.setStroke(Color.BLACK);
            svg.setStrokeWidth(0.5);
            svg.setFill(Color.web(getHexColor(count, maxCount)));

            // 5) Build a Tooltip
            StringBuilder tip = new StringBuilder();
            tip.append("State: ").append(code).append("\n")
                    .append("Tweets: ").append(count).append("\n\n")
                    .append("Examples:\n");
            List<String> list = examples.getOrDefault(code, List.of());
            System.out.println(tip);
            if (list.isEmpty()) {
                tip.append("  (none)");
            } else {
                for (String t : list) {
                    // shorten very long tweets:
                    tip.append("  • ")
                            .append(t.length() > 50 ? t.substring(0, 47) + "…" : t)
                            .append("\n");
                }
            }

            Tooltip tooltip = new Tooltip(tip.toString());
            tooltip.setShowDelay(Duration.millis(200));
            tooltip.setStyle("-fx-font-size: 11px;");  // optional styling
            Tooltip.install(svg, tooltip);
            svg.setPickOnBounds(true);

// manual hover handling:
            svg.setOnMouseEntered(e -> {
                // show the tooltip near the mouse cursor
                tooltip.show(
                        svg,
                        e.getScreenX() + 10,
                        e.getScreenY() + 10
                );
            });
            svg.setOnMouseMoved(e -> {
                // move the tooltip if the mouse moves
                tooltip.setX(e.getScreenX() + 10);
                tooltip.setY(e.getScreenY() + 10);
            });
            svg.setOnMouseExited(e -> {
                tooltip.hide();
            });

// add to the pane
            mapPane.getChildren().add(svg);


        }
    }
}




