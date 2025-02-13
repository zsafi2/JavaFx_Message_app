import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.Objects;

public class GuiClient extends Application {

	TextField name; // Username input for login
	TextField messageInput; // TextField for typing messages

	Button sendMessage; // Button to send message
	Button personalChats; // Button for personal chats (no action set yet)
	Button connectServer; // Button to initiate server connection
	Button backtoClient;
	Button sendButton;
	Button backButton;

	ListView<HBox> userList;
	ListView<String> listItems; // ListView for displaying messages

	Client clientConnection; // Simulated client connection handler

	Label loginFeedback; // Feedback for login process

	String myName;


	@Override
	public void start(Stage primaryStage) {

		Scene loginScene = LoginClientGui();
		Scene chatScene = createClientGui();
		Scene personalChatList1 = openPersonalChatsWindow();

		backButton = new Button("Back");

		// Setup client connection (simulated)
		clientConnection = new Client(data -> {
			Platform.runLater(() -> listItems.getItems().add(data.toString()));
		});

		// Start client connection
		clientConnection.start();
		clientConnection.Users.add("me");

		// Define action for connectServer button
		connectServer.setOnAction(e -> {

			Message clientMessage = new Message();
			clientMessage.message = name.getText();
			clientMessage.login = true;
			clientConnection.send(clientMessage);

			PauseTransition pause = new PauseTransition(Duration.millis(100));
			pause.setOnFinished(event -> {
				if (!clientConnection.receivedMessage.validName) {
					name.clear();
					loginFeedback.setText("Username already taken. Try again.");
				} else {
					myName = clientMessage.message;

					primaryStage.setScene(chatScene);
					primaryStage.setTitle(myName + ": open chat");
				}
			});
			pause.play();
		});

		primaryStage.setOnCloseRequest(event -> {
			Platform.exit();
			System.exit(0);
		});

		personalChats.setOnAction(e -> {

			if (clientConnection != null) {
				userList.getItems().clear();
				for (String user : clientConnection.Users) {
					if (!Objects.equals(user, myName)) {
						Button openChat = new Button("Open Chat");
						openChat.setOnAction(a -> {
								Scene chatWindow = openChatWindow(user);
								primaryStage.setScene(chatWindow);
								primaryStage.setTitle(myName + " Chat with " + user);
						});
						Label userName = new Label(user);
						HBox hbox = new HBox(userName, openChat);
						hbox.setSpacing(30);
						userList.getItems().add(hbox);
					}
				}

			}
			primaryStage.setScene(personalChatList1);
			primaryStage.setTitle(myName + ": ALL Users");
		});

		backtoClient.setOnAction(e -> {
			primaryStage.setScene(chatScene);
			primaryStage.setTitle(myName);
		});

		if (backtoClient != null) {
			backButton.setOnAction(e -> {
				primaryStage.setScene(personalChatList1);
				primaryStage.setTitle(myName + ": ALL Users");
			});
		}

		primaryStage.setScene(loginScene);
		primaryStage.setTitle("Login Page");
		primaryStage.show();
	}

	private Scene openPersonalChatsWindow() {

		userList = new ListView<>();
		backtoClient = new Button("Back");

		VBox layout = new VBox(10);
		layout.getChildren().addAll(userList, backtoClient);
		layout.setPadding(new Insets(20));

		return new Scene(layout, 500, 500);
	}

	private Scene openChatWindow(String user) {

		// List for displaying message history
		ListView<String> messageHistory = new ListView<>();

		// Check if there are existing chats with this user and load them
		if (clientConnection.chats.containsKey(user)) {
			ArrayList<String> history = clientConnection.chats.get(user);
			messageHistory.getItems().addAll(history);
		}

		// TextField for writing new messages
		TextField messageInput = new TextField();
		messageInput.setPromptText("Write a message...");

		// Send button to send the message
		sendButton = new Button("Send");

		sendButton.setOnAction(e -> {

			String inputText = messageInput.getText();
			if (!inputText.isEmpty()) {
				// Prepare and send message
				Message data = new Message();
				data.message = inputText;
				data.personalMessage = true;
				data.messagetoAll = false;
				data.Username = myName;
				data.sendPerson = user;
				data.login = false;
				clientConnection.send(data);

				// Add message to local chat history and clear input
				messageHistory.getItems().add("Me: " + inputText);
				messageInput.clear();

				// Add message to chats HashMap if it's not already there
				clientConnection.chats.computeIfAbsent(user, k -> new ArrayList<>()).add("Me: " + inputText);
			}
		});

		Button refresh = new Button("Refresh");

		refresh.setOnAction(e ->{
			messageHistory.getItems().clear();
			if (clientConnection.chats.containsKey(user)) {
				ArrayList<String> history = clientConnection.chats.get(user);
				messageHistory.getItems().addAll(history);
			}
		});

		// Label for the refresh button
		Label checkNewMessageLabel = new Label("Check for new Messages:");
		checkNewMessageLabel.setStyle("-fx-text-fill: red");


		HBox titleArea = new HBox(40, backButton, new Label("Chat with " + user));

		// Layout setup for refresh area
		HBox refreshArea = new HBox(10, checkNewMessageLabel, refresh);
		refreshArea.setAlignment(Pos.CENTER);

		HBox sendArea = new HBox(10, messageInput, sendButton);
		sendArea.setAlignment(Pos.CENTER);

		// Layout adjustments
		VBox chatLayout = new VBox(10);
		chatLayout.setPadding(new Insets(20));
		chatLayout.getChildren().addAll( titleArea, messageHistory, sendArea, refreshArea);

		return new Scene(chatLayout, 500, 500);
	}

	public Scene LoginClientGui() {

		Label login = new Label("Login");
		login.setFont(new Font("Arial", 30));

		Label loginQuestion = new Label("Enter a Username");
		loginQuestion.setFont(new Font("Arial", 14));

		name = new TextField();
		name.setFont(new Font("Arial", 12));
		name.setPromptText("Username");

		connectServer = new Button("Connect");
		connectServer.setFont(new Font("Arial", 12));

		loginFeedback = new Label();
		loginFeedback.setFont(new Font("Arial", 10));
		loginFeedback.setStyle("-fx-text-fill: red;");

		VBox loginMenu = new VBox(10);
		loginMenu.setAlignment(Pos.CENTER);
		loginMenu.setPadding(new Insets(20));
		loginMenu.getChildren().addAll(login, loginQuestion, name, connectServer, loginFeedback);

		return new Scene(loginMenu, 400, 400);
	}

	public Scene createClientGui() {

		listItems = new ListView<>();
		listItems.setStyle("-fx-background-color: lightgray; -fx-text-fill: black; -fx-font-family: Arial;");

		messageInput = new TextField();
		messageInput.setFont(new Font("Arial", 14)); // Larger font size for better visibility
		sendMessage = new Button("Send to All");
		sendMessage.setFont(new Font("Arial", 14));

		sendMessage.setOnAction(e -> {
			Message clientMessage = new Message();
			clientMessage.Username = myName;
			clientMessage.message = messageInput.getText();
			clientMessage.messagetoAll = true;
			clientMessage.login = false;
			clientConnection.send(clientMessage);
			messageInput.clear();
		});

		Label newLabel = new Label("For individual and personal Chats Click ->");
		newLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");


		personalChats = new Button("Personal Chats");
		personalChats.setFont(new Font("Arial", 14));
		personalChats.setPrefHeight(30);


		HBox inputArea = new HBox(10, messageInput, sendMessage);
		inputArea.setStyle("-fx-background-color: #e6e6e6;"); // Light gray background for input area

		HBox chatsArea = new HBox(10, newLabel, personalChats);

		VBox bottomArea = new VBox(0, inputArea, chatsArea);
		bottomArea.setStyle("-fx-background-color: #cccccc;"); // Slightly darker shade for the bottom area

		BorderPane mainLayout = new BorderPane();
		mainLayout.setCenter(listItems);
		mainLayout.setBottom(bottomArea);
		mainLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;"); // Increased padding for more spacing

		return new Scene(mainLayout, 500, 500); // Increased scene size for better visibility
	}

	public static void main(String[] args) {
		launch(args);
	}
}
