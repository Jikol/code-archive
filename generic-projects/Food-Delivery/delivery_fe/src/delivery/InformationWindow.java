package delivery;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class InformationWindow {

    private static InformationWindow instance = null;
    private static Stage window;
    private static Scene mainScene;
    private static Pane layout;

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    private InformationWindow(String message) {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(mainScene);
    }

    public static void show(String message) {
        layout = new Pane();
        layout.setStyle("-fx-border-color: gray; -fx-border-radius: 7px");

        Label label;
        label = new Label(message);
        label.setFont(new Font("Roboto", 18));
        label.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().add(label);

        mainScene = new Scene(layout);

        if (instance == null) {
            instance = new InformationWindow(message);
        }

        window.setX((MainWindow.getWindow().getX() + (MainWindow.getWindow().getWidth() / 2)) - (WIDTH / 2));
        window.setY((MainWindow.getWindow().getY() + (MainWindow.getWindow().getHeight() / 2)) - (HEIGHT / 2));

        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(e -> {
            window.close();
            instance = null;
        });

        delay.play();
        window.showAndWait();
    }
}
