package application;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Message {

    public static boolean msgPlayAgain() {
        boolean back = false;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Information");
        a.setHeaderText("Restart");
        a.setContentText("You want to play again?");

        Optional<ButtonType> result = a.showAndWait();

        if (
                result.get() == ButtonType.OK
                ) {
            back = true;
        }

        return back;
    }

    public static boolean msgExitRequest() {
        boolean back = false;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Closing request");
        a.setHeaderText("Bye Bye..");
        a.setContentText("You really want to exit");

        Optional<ButtonType> result = a.showAndWait();

        if (
                result.get() == ButtonType.OK
                ) {
            back = true;

        }

        return back;
    }

    public static void msgInformation(String header, String textToShow) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Information");
        a.setHeaderText(header);
        a.setContentText(textToShow);
        Optional<ButtonType> result = a.showAndWait();
    }
}
