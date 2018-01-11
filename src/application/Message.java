package application;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Represents a class which contains mutiple dilaogs to inform
 * the user about the play/software status
 *
 * @author Frithjof Hoppe
 * @version 1.0
 */
public class Message {

    /**
     * Opens a dialog which contains the requestion if the user
     * wants to play the game agin. This is normally use after
     * a lost or won game.
     *
     * @return: boolish value if the the user wants to play again
     */
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

    /**
     * Opens a dialog which contains an exits request, which positiv
     * cofirmation follows usually the end of the programm
     * @return: boolisch balue if the user wants to exit out of the programm
     */
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

    /**
     * Shows a dialog which individually set header and maintext
     *
     * @param header: Header of the dialog
     * @param textToShow: The text which is positioned in the middle of the dialog
     */
    public static void msgInformation(String header, String textToShow) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Information");
        a.setHeaderText(header);
        a.setContentText(textToShow);
        Optional<ButtonType> result = a.showAndWait();
    }
}
