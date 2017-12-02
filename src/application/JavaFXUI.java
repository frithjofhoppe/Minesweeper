package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class JavaFXUI implements MineSweeper {

    Stage primaryStage;
    Scene scene;
    BorderPane root;

    JavaFXUI(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        start();
    }

    private void start() {
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

    public void rebuild()
    {
        PlayField pl = new PlayField(this);
        root.setCenter(pl.getPlayingField());
    }

    public void finished(boolean isSuccessfullyFinished) {

        if (!isSuccessfullyFinished) {
            Message.msgInformation("BOMB", "You reached a bomb!!!");
        } else {
            Message.msgInformation("WIN", "You have win the game");
        }

        if (Message.msgPlayAgain()) {
            rebuild();
        } else {
            Platform.exit();
        }
    }

    public VBox sideBar() {
        VBox back = new VBox();
        Button restart = new Button();
        restart.setText("Restart");
        restart.setOnAction(e -> {
            rebuild();
            Message.msgInformation("Restart", "The has been rebuilded");
        });
        Button mark = new Button();
        mark.setText("Stop");
        restart.getStyleClass().add("ControlButton");
        mark.getStyleClass().add("ControlButton");
        back.getChildren().add(restart);
        back.getChildren().add(mark);
        return back;
    }
}
