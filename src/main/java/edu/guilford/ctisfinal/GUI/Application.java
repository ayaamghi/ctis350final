package edu.guilford.ctisfinal.GUI;


import edu.guilford.ctisfinal.Backend.ContextManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/guilford/ctisfinal/FXML/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        if(Screen.getScreens().size()  >= 2) {
            // Get the list of screens
            Screen secondScreen = Screen.getScreens().get(1); // Assuming the second screen is at index 1
            Rectangle2D bounds = secondScreen.getVisualBounds();
            stage.setX(bounds.getMaxX());
            stage.setY(bounds.getMaxY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight()/2);
            System.out.println(bounds.getWidth());
            System.out.println(bounds.getHeight()/2);
        }
        else {
            stage.setX(0);
            stage.setY(0);

            stage.setWidth(1080.0);
            stage.setHeight(947.5);

        }


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}