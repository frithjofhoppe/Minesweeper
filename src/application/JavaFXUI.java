package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        PlayField pl = new PlayField(this,5);
        root.setCenter(pl.getPlayingField());
        root.setBottom(sideBar());
        scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void rebuild(int probability)
    {
        if(probability < 2)
        {
            probability = 5;
        }

        PlayField pl = new PlayField(this,probability);
        root.setCenter(pl.getPlayingField());
    }

    public void finished(boolean isSuccessfullyFinished) {

        if (!isSuccessfullyFinished) {
            Message.msgInformation("BOMB", "You reached a bomb!!!");
        } else {
            Message.msgInformation("WIN", "You have win the game");
        }

        if (Message.msgPlayAgain()) {
            rebuild(0);
        } else {
            Platform.exit();
        }
    }

    public HBox sideBar() {
        HBox back = new HBox();
        Button restart = new Button();
        restart.setText("Restart");
        restart.setOnAction(e -> {
            rebuild(0);
            Message.msgInformation("Restart", "The has been rebuilded");
        });
        restart.getStyleClass().add("ControlButton");

        final Spinner<Integer> spinner = new Spinner<Integer>();

        final int initialValue = 5;

        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, initialValue);

        spinner.valueProperty().addListener(e -> {
            rebuild(spinner.getValue());
        });

        spinner.setValueFactory(valueFactory);
        back.getChildren().add(spinner);
        back.getChildren().add(restart);
        return back;
    }
}
