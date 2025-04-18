package edu.guilford.ctisfinal.GUI.TabControllers;

import edu.guilford.ctisfinal.Backend.ContextManager;
import edu.guilford.ctisfinal.Backend.CsvParser;
import edu.guilford.ctisfinal.Backend.TopicVisualizer.TopicClustering;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

import java.util.ArrayList;
import java.util.List;

public class TopicVisualizerController {

    @FXML
    private Accordion topicAccordion;

    @FXML
    public void initialize() {
//        // example topics; replace with your real list
//        ArrayList<String> topics = new ArrayList<>();
//
//        for(int i = 0; i < 100; i++) {
//            String topic = ContextManager.getInstance().getDf().get(i, "content");
//            topics.add(topic);
//        }
//        try {
//            List<List<String>> clusters =
//                    TopicClustering.clusterTopics(topics, 0.75f);
//
//            topicAccordion.getPanes().clear();
//            for (int i = 0; i < clusters.size(); i++) {
//                List<String> cluster = clusters.get(i);
//                TitledPane pane = new TitledPane();
//                pane.setText("Cluster " + (i+1));
//                ListView<String> lv = new ListView<>(
//                        FXCollections.observableArrayList(cluster)
//                );
//                pane.setContent(lv);
//                topicAccordion.getPanes().add(pane);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
