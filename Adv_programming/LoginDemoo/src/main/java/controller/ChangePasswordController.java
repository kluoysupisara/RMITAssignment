package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Model;
import model.User;
import util.PasswordUtils;
import util.AlertUtils;
import util.StageUtils;

import java.sql.SQLException;

public class ChangePasswordController {
    @FXML private PasswordField oldPassword;
    @FXML private PasswordField newPassword;

    private Model model;
    private Stage stage;

    public ChangePasswordController(Model model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSavePassword() throws SQLException {
        System.out.println("handleSavePassword");
        String oldPass = oldPassword.getText();
        String newPass = newPassword.getText();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            AlertUtils.showWarning("Input Error", "Both fields are required.", stage);
            return;
        }

        User user = model.getCurrentUser();
        String encryptedOldInput = PasswordUtils.encrypt(oldPass);

        if (!encryptedOldInput.equals(user.getPassword())) {
            AlertUtils.showError("Authentication Failed", "Current password is incorrect.", stage);
            return;
        }

        String encryptedNewPass = PasswordUtils.encrypt(newPass);
        user.setPassword(encryptedNewPass);
        model.getUserDao().updatePassword(user.getUsername(), encryptedNewPass);

        AlertUtils.showInfo("Success", "Password changed successfully!", stage);
        stage.close();
    }
    @FXML
    private void handleCancel() {
        if (stage != null) {
            stage.close();
        }
    }
    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Change Password", 450, 250);
    }
}