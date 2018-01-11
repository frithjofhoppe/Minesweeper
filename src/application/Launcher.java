package application;

import application.interfaces.MineSweeper;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        MineSweeper ui = new JavaFXUI(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
