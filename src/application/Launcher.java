package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Launcher extends Application {

    Scene scene;
    BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!Message.msgExitRequest()) {
                    event.consume();
                } else {
                    Platform.exit();
                }
            }
        });
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

    public void finished(boolean isSuccessfullyFinished) {

        if (!isSuccessfullyFinished) {
            Message.msgInformation("BOMB", "You reached a bomb!!!");
        } else {
            Message.msgInformation("WIN", "You have win the game");
        }

        if (Message.msgPlayAgain()) {
            PlayField pl = new PlayField(this);
            root.setCenter(pl.getPlayingField());
        } else {
            Platform.exit();
        }
    }

    public VBox sideBar() {
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
