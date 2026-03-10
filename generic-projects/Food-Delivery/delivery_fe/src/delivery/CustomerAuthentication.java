package delivery;

import com.orm.Customer;
import com.orm.dao.CustomerRepository;
import com.orm.dao.Database;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicReference;

public class CustomerAuthentication {

    private static CustomerAuthentication instance = null;
    private static Stage window;
    private static Scene mainScene;
    private static BorderPane layout;

    private static final int WIDTH = 285;
    private static final int HEIGHT = 165;

    private CustomerAuthentication() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Login zákazníka");
        window.setMinWidth(WIDTH);
        window.setMinHeight(HEIGHT);
        window.setMaxWidth(WIDTH);
        window.setMaxHeight(HEIGHT);
        window.setScene(mainScene);
        window.setOnCloseRequest(e -> e.consume());
    }

    public static AtomicReference<Customer> show(Database db) {
        HBox topBox = new HBox(45);
        Label label = new Label("Přístup zákazníka");
        label.setFont(Font.font("Roboto", FontWeight.EXTRA_BOLD, 14));
        topBox.setPadding(new Insets(10, 10, 0, 20));
        topBox.getChildren().add(label);

        Button newCustomerButton = new Button("Nový zákazník");
        newCustomerButton.setFont(Font.font("Roboto", FontWeight.SEMI_BOLD, 12));
        newCustomerButton.setStyle("-fx-background-radius: 7px");
        topBox.getChildren().add(newCustomerButton);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,20));
        grid.setVgap(8);
        grid.setHgap(15);

        Label nameLabel = new Label("Jméno: ");
        nameLabel.setFont(new Font("Roboto", 13));
        grid.add(nameLabel, 0, 1);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Zadej jméno...");
        nameInput.setFont(new Font("Roboto", 12));
        nameInput.setStyle("-fx-background-radius: 10px");
        grid.add(nameInput, 1, 1);

        Label phoneLabel = new Label("Telefon: (+420)");
        phoneLabel.setFont(new Font("Roboto", 13));
        grid.add(phoneLabel, 0,2);

        TextField phoneInput = new TextField();
        phoneInput.setPromptText("Zadej číslo...");
        phoneInput.setFont(new Font("Roboto", 12));
        phoneInput.setStyle("-fx-background-radius: 10px");
        grid.add(phoneInput, 1, 2);

        HBox bottomBox = new HBox(27);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(0,12,10,0));

        Button loginButton = new Button("Přihlásit se");
        loginButton.setFont(new Font("Roboto", 12));
        loginButton.setStyle("-fx-background-radius: 7px");
        bottomBox.getChildren().add(loginButton);

        Button closeButton = new Button("Odejít");
        closeButton.setFont(new Font("Roboto", 12));
        closeButton.setStyle("-fx-background-radius: 7px");
        bottomBox.getChildren().add(closeButton);

        layout = new BorderPane();
        layout.setStyle("-fx-border-color: gray; -fx-border-radius: 7px; -fx-background-radius: 7px");
        layout.setTop(topBox);
        layout.setCenter(grid);
        layout.setBottom(bottomBox);

        mainScene = new Scene(layout, WIDTH, HEIGHT);

        closeButton.setOnAction(e -> {
            boolean state = MainWindow.close();
            if (state) {
                window.close();
                instance = null;
            }
        });

        newCustomerButton.setOnAction(e -> {
            try {
                window.close();
                instance = null;
                MainWindow.showNewCustomerScene("Nový uživatel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        AtomicReference<Customer> customer = new AtomicReference<>(new Customer());

        loginButton.setOnAction(e -> {
            try {
                String phone = "+420" + phoneInput.getText();
                customer.set(CustomerRepository.SelectIdLogin(nameInput.getText(), phone, db));
                window.close();
                instance = null;
            } catch (Exception ex) {
                InformationWindow.show("Chybné údaje");
            }
        });

        instance = new CustomerAuthentication();
        if (instance == null) {
            instance = new CustomerAuthentication();
        }

        window.setX((MainWindow.getWindow().getX() + (MainWindow.getWindow().getWidth() / 2)) - (WIDTH / 2));
        window.setY((MainWindow.getWindow().getY() + (MainWindow.getWindow().getHeight() / 2)) - (HEIGHT / 2));
        window.showAndWait();

        return customer;
    }
}
