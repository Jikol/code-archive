package delivery.controllers;

import delivery.InformationWindow;
import delivery.MainWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AdminController {

    private static FXMLLoader loader;
    private static Parent root;
    private static Stage window;
    private static Scene mainScene;

    public PasswordField password;

    public void showAdminAuth() throws IOException {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/AdminAuthentication.fxml"));
        root = loader.load();
        mainScene = new Scene(root);

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(mainScene);
        window.setOnCloseRequest(e -> e.consume());

        window.setX((MainWindow.getWindow().getX() + (MainWindow.getWindow().getWidth() / 2)) - (285 / 2));
        window.setY((MainWindow.getWindow().getY() + (MainWindow.getWindow().getHeight() / 2)) - (165 / 2));
        window.showAndWait();
    }

    public void showCustomerAuth() throws Exception {
        MainWindow.showMainLoggedScreen("Objednávkový systém jídla");
    }

    public void close() throws Exception {
        MainWindow.showMainLoggedScreen("Objednávkový systém jídla");
        window.close();
    }

    public void logAdmin() {
        if (password.getText().equals("1234")) {
            window.close();
        } else {
            InformationWindow.show("Nesprávné heslo");
        }
    }

    public void showNewDrive() throws Exception {
        MainWindow.showNewDrive("Nová jízda");
    }
}
