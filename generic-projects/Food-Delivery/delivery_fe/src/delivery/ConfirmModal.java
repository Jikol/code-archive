package delivery;

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

public class ConfirmModal {

    private static ConfirmModal instance = null;
    private static Stage window;
    private static Scene mainScene;
    private static boolean state;
    private static BorderPane layout;

    private static final int WIDTH = 170;
    private static final int HEIGHT = 120;

    private ConfirmModal() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.setMinWidth(WIDTH);
        window.setMinHeight(HEIGHT);
        window.setMaxWidth(WIDTH);
        window.setMaxHeight(HEIGHT);
        window.setScene(mainScene);
        window.setOnCloseRequest(e -> e.consume());
    }

    public static boolean show(String message) {
        Label label;
        label = new Label(message);
        label.setFont(new Font("Roboto", 15));

        Button okButton, abortButton;
        okButton = new Button("Ano");
        okButton.setFont(new Font("Roboto", 13));
        abortButton = new Button("Ne");
        abortButton.setFont(new Font("Roboto", 13));

        layout = new BorderPane();

        Pane labelPane = new Pane();
        labelPane.getChildren().add(label);

        HBox buttonPane = new HBox(18);
        buttonPane.getChildren().addAll(okButton, abortButton);
        buttonPane.setAlignment(Pos.CENTER);

        layout.setTop(labelPane);
        layout.setCenter(buttonPane);
        layout.setPadding(new Insets(18, 0, 0, 10));
        layout.setStyle("-fx-border-color: gray; -fx-border-radius: 7px");

        mainScene = new Scene(layout, WIDTH, HEIGHT);

        okButton.setOnAction(e -> {
            state = true;
            window.close();
            instance = null;
        });

        abortButton.setOnAction(e -> {
            state = false;
            window.close();
            instance = null;
        });

        if (instance == null) {
            instance = new ConfirmModal();
        }

        window.setX((MainWindow.getWindow().getX() + (MainWindow.getWindow().getWidth() / 2)) - (WIDTH / 2));
        window.setY((MainWindow.getWindow().getY() + (MainWindow.getWindow().getHeight() / 2)) - (HEIGHT / 2));
        window.showAndWait();

        return state;
    }
}
