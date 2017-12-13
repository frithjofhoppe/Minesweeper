package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URI;

public class JavaFXUI implements MineSweeper {

    Stage primaryStage;
    Scene scene;
    BorderPane root;
    Spinner<Integer> spinner = new Spinner<Integer>();

    JavaFXUI(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        start();
    }

    private void start() {
        primaryStage.getIcons().add(new Image("ressources/bombl.png"));
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
        root.setBottom(control());
        scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Microsoft - MineSweeper");
        primaryStage.show();
        Message.msgInformation("Welcome to minesweeper","Left-mouse-button: mark a field \nRight-mouse-button: open a field");
    }

    public void rebuild(int probability)
    {
        if(probability < 2)
        {
            probability = 5;
        }

        spinner.getValueFactory().setValue(probability);
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

    public HBox control() {
        HBox back = new HBox();
        VBox spinnerBox = new VBox();
        VBox buttonBox = new VBox();
        back.getStyleClass().add("ControlBox");
        Button restart = new Button();
        restart.setText("Restart");
        restart.setOnAction(e -> {
            rebuild(0);
            Message.msgInformation("Restart", "The has been rebuilded");
        });
        restart.getStyleClass().add("ControlButton");



        final int initialValue = 5;

        // Value factory.
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, initialValue);

        spinner.valueProperty().addListener(e -> {
            rebuild(spinner.getValue());
        });

        spinner.setValueFactory(valueFactory);

        Label label = new Label("Probability of bombs");
        Hyperlink hyperlink = new Hyperlink("Manual");
        hyperlink.setOnMouseClicked( e ->{
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://en.wikipedia.org/wiki/Minesweeper_(video_game)#Overview"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        buttonBox.getChildren().add(restart);
        buttonBox.getChildren().add(hyperlink);
        spinnerBox.getChildren().add(label);
        spinnerBox.getChildren().add(spinner);
        back.getChildren().add(spinnerBox);
        back.getChildren().add(buttonBox);
        return back;
    }
}
