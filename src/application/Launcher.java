package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {

    Scene scene;
    BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        PlayField pl = new PlayField(this);
        root.setCenter(pl.getPlayingField());
        root.setRight(sideBar());
        scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public  void finished()  {
        PlayField pl = new PlayField(this);
        root.setCenter(pl.getPlayingField());
    }

    public VBox sideBar()
    {
        VBox back = new VBox();
        Button start = new Button();
        start.setText("Start");
        Button reset = new Button();
        reset.setText("Stop");
        start.getStyleClass().add("ControlButton");
        reset.getStyleClass().add("ControlButton");
        back.getChildren().add(start);
        back.getChildren().add(reset);
        return back;
    }
}
