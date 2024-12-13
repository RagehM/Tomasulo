package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	public static void display(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(400);
		Label l = new Label();
		l.setText(message);
		Button close = new Button("Close the window");
		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				window.close();
			}
		});
		VBox pane = new VBox(10);
		pane.getChildren().addAll(l, close);
		pane.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pane);
		window.setScene(scene);
		window.showAndWait();
	}

	public static void display2(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(400);
		Label l = new Label();
		l.setText(message);
		Button close = new Button("Okay");
		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				window.close();
			}
		});
		VBox pane = new VBox(10);
		pane.getChildren().addAll(l, close);
		pane.setAlignment(Pos.CENTER);
		Scene scene = new Scene(pane);
		window.alwaysOnTopProperty();
		window.setScene(scene);
		window.showAndWait();
	}

}
