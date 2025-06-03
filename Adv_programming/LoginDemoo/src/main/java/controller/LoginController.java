package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.CartItems;
import model.Model;
import model.ShoppingCart;
import model.User;
import util.StageUtils;

public class LoginController {
	@FXML
	private TextField name;
	@FXML
	private PasswordField password;
	@FXML
	private Label message;
	@FXML
	private Button login;
	@FXML
	private Button signup;

	private Model model;
	private Stage stage;
	
	public LoginController(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
	}
	
	@FXML
	public void initialize() {		
//		login.setOnAction(event -> handleLogin());
//
//		signup.setOnAction(event -> handleSignup());
	}
	@FXML
	private void handleLogin() {
		if (!name.getText().isEmpty() && !password.getText().isEmpty()) {
			User user;
			try {
				user = model.getUserDao().getUser(name.getText(), password.getText());
				if (user != null) {
					model.setCurrentUser(user);
					System.out.println("Login successful");
					loadHomeViewStage();
				} else {
					message.setText("Wrong username or password");
					message.setTextFill(Color.RED);
				}
			} catch (SQLException e) {
				message.setText(e.getMessage());
				message.setTextFill(Color.RED);
			}

		} else {
			message.setText("Empty username or password");
			message.setTextFill(Color.RED);
		}
		name.clear();
		password.clear();
	}
	@FXML
	private void handleSignup() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));

			SignupController signupController =  new SignupController(stage, model);

			loader.setController(signupController);
			VBox root = loader.load();

			signupController.showStage(root);

			message.setText("");
			name.clear();
			password.clear();

			stage.close();
		} catch (IOException e) {
			message.setText(e.getMessage());
		}
	} //end handleSignup

	private void loadHomeViewStage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
			HomeController homeController = new HomeController(stage, model);

			loader.setController(homeController);
			VBox root = loader.load();

			homeController.showStage(root);
			stage.close();
		} catch (IOException e) {
			message.setText(e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	private  void loadAdminDashboardStage() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashBoard.fxml"));
			HomeController controller = new HomeController(stage, model);

			loader.setController(controller);
			VBox root = loader.load();

			controller.showStage(root);
			stage.close();
		} catch (IOException e) {
			message.setText(e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	public void showStage(Pane root) {
		StageUtils.showStage(stage, root, "Welcome", 500, 300);
	}
}

