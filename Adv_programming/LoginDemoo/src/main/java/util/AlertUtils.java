package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AlertUtils {

    public static void showInfo(String title, String message, Window owner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // optional
        alert.setContentText(message);
        alert.initModality(Modality.WINDOW_MODAL);
        if (owner != null) {
            alert.initOwner(owner); // block input to parent
        }

        alert.getButtonTypes().setAll(ButtonType.CLOSE); // just a Close button
        alert.showAndWait(); // wait until closed
    }

    public static void showWarning(String title, String message, Window owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initModality(Modality.WINDOW_MODAL);
        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.getButtonTypes().setAll(ButtonType.CLOSE);
        alert.showAndWait();
    }
}

