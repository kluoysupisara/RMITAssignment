package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;
import util.StageUtils;

public class PaymentController {
    @FXML private TextField codeField;
    @FXML private Label message;

    private Stage stage;
    private Model model;
    private Runnable onPaymentSuccess;
    private Stage parentStage;

    public PaymentController(Stage parentStage, Model model, Runnable onPaymentSuccess) {
        this.stage = new Stage();
        this.model = model;
        this.parentStage = parentStage;
        this.onPaymentSuccess = onPaymentSuccess;
    }

    @FXML
    private void handleConfirm() {
        String code = codeField.getText().trim();
        if (!code.matches("\\d{6}")) {
            message.setText("Please enter a valid 6-digit code.");
            message.setTextFill(Color.RED);
            return;
        }

        // ✅ Payment successful
        onPaymentSuccess.run();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        stage.close(); // Close without saving
    }
    public void showStage(Pane root) {
        StageUtils.showStage(stage, root, "Payment", 300, 200);
    }
}