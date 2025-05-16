import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CalculateAreaController {
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    public void calculateButtonHandler(ActionEvent event) throws IOException {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("CalculateArea.fxml"));
        System.out.println("It works!");
        System.out.println("Width: " + widthTextField.getText());
        System.out.println("Height: " + heightTextField.getText());
    }
}
