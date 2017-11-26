package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    PlayField pl;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        pl = new PlayField();
        root.setCenter(pl.getPlayingField());
        root.setRight(sideBar());
        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
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
