package gui.manager;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManagerEdit1 extends AbstractWindow {

	ManagerEdit1() {
		windowEnum = WindowEnum.ManuallyEdit1;
		window = new Stage();
		window.getIcons().add(new Image(getClass().getResourceAsStream("Red_smart_parking_icon.png")));
		window.initModality(Modality.APPLICATION_MODAL);
	}

	public void display(final Stage primaryStage, final gui.manager.WindowEnum none, final ArrayList<AbstractWindow> prevWindows) {
		window.setTitle("ManagerEdit1");
		window.setWidth(400);
		window.setHeight(100);
		final VBox vbox = new VBox(8);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setAlignment(Pos.CENTER);
		final Scene scene = new Scene(vbox);

		final Button editButton = new Button("View & Edit");
		editButton.setOnAction(λ -> {

		});
		vbox.getChildren().add(editButton);
		// scene.getStylesheets().add(getClass().getResource("mainStyle.css").toExternalForm());
		window.setScene(scene);
		window.show();

	}
}
