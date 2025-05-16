import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        try {
            // load the fxml in a static way
            // FXMLLoader loader = new FXMLLoader();
            // GridPane root = FXMLLoader.load(getClass().getResource("window1.fxml"));

            //load the fxml using instance method so that we can access the controller class
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("window1.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("calculateArea.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("image.fxml"));
            VBox root = loader.load();
            Scene scene1 = new Scene(root);

            primaryStage.setScene(scene1);
            primaryStage.setTitle("Image View");

            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        primaryStage.setTitle("Hello World!");
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();
    }
}
