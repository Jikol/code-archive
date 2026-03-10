package delivery;

import com.orm.Customer;
import com.orm.Drive;
import com.orm.Order;
import com.orm.Vehicle;
import com.orm.dao.*;
import delivery.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainWindow {

    private static MainWindow instance = null;
    private static Stage window;
    private static Scene mainScene;
    private static FXMLLoader loader;
    private static Parent root;
    private static AtomicReference<Customer> customer;
    private static List<Order> order;
    private static MainSceneController mainSceneController;
    private static OrderDetailSceneController orderDetailSceneController;
    private static NewCustomerSceneController newCustomerSceneController;
    private static NewAddressSceneController newAddressSceneController;
    private static NewOrderSceneController newOrderSceneController;
    private static AdminController adminController;
    private static NewDriveSceneController newDriveSceneController;

    private static int WIDTH;
    private static int HEIGHT;

    private MainWindow(Stage stage, String title, int width, int height) {
        window = stage;
        window.setTitle(title);
        window.setMinWidth(width + 10);
        window.setMinHeight(height + 40);
        window.setMaxWidth(width + 10);
        window.setMaxHeight(height + 40);
        window.setScene(mainScene);
        window.show();
        window.setOnCloseRequest(e -> {
            e.consume();
            this.close();
        });
    }

    public static void show(Stage stage, String title, int width, int height) throws Exception {
        WIDTH = width;
        HEIGHT = height;

        loader = new FXMLLoader(MainWindow.class.getResource("gui/MainScene.fxml"));
        root = loader.load();
        mainSceneController = loader.getController();

        mainScene = new Scene(root);

        if (instance == null) {
            instance = new MainWindow(stage, title, width, height);
        }

        customer = CustomerAuthentication.show(Database.getDatabase());
        mainSceneController.setCustomer(customer.get().getName(), customer.get().getSurname(), customer.get().getId());

        order = OrderRepository.SelectAllActiveByID(customer.get().getId(), Database.getDatabase());
        mainSceneController.fillActualOrders(order);
    }

    public static void showAdmin(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/AdminPanelScene.fxml"));
        root = loader.load();
        adminController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);

        adminController.showAdminAuth();
    }

    public static void showLoggedAdmin(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/AdminPanelScene.fxml"));
        root = loader.load();
        adminController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);
    }

    public static void showOrderDetailScene(String title, int idOrder) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/OrderDetailScene.fxml"));
        root = loader.load();
        orderDetailSceneController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);

        orderDetailSceneController.initializeDishes(idOrder);
    }

    public static void showNewOrderScene(String title, int idCustomer) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/NewOrderScene.fxml"));
        root = loader.load();
        newOrderSceneController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);

        newOrderSceneController.initializeRestaurants();
        newOrderSceneController.initPaymentType();
        newOrderSceneController.setCustomerId(idCustomer);
    }

    public static void showNewCustomerScene(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/NewCustomerScene.fxml"));
        root = loader.load();
        newCustomerSceneController = loader.getController();
        mainScene = new Scene(root, 510, 400);

        window.setScene(mainScene);
        window.setTitle(title);
        window.setMinWidth(510 + 10);
        window.setMinHeight(400 + 40);
        window.setMaxWidth(510 + 10);
        window.setMaxHeight(400 + 40);

        newCustomerSceneController.initComboBox();
    }

    public static void showMainScene(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/MainScene.fxml"));
        root = loader.load();
        mainSceneController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);
        window.setMinWidth(WIDTH + 10);
        window.setMinHeight(HEIGHT + 40);
        window.setMaxWidth(WIDTH + 10);
        window.setMaxHeight(HEIGHT + 40);

        customer = CustomerAuthentication.show(Database.getDatabase());
        mainSceneController.setCustomer(customer.get().getName(), customer.get().getSurname(), customer.get().getId());

        order = OrderRepository.SelectAllbyID(customer.get().getId(), Database.getDatabase());
        mainSceneController.fillActualOrders(order);
    }

    public static void showMainLoggedScreen(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/MainScene.fxml"));
        root = loader.load();
        mainSceneController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);
        window.setMinWidth(WIDTH + 10);
        window.setMinHeight(HEIGHT + 40);
        window.setMaxWidth(WIDTH + 10);
        window.setMaxHeight(HEIGHT + 40);

        mainSceneController.setCustomer(customer.get().getName(), customer.get().getSurname(), customer.get().getId());
        order = OrderRepository.SelectAllActiveByID(customer.get().getId(), Database.getDatabase());
        mainSceneController.fillActualOrders(order);
    }

    public static void showNewDrive(String title) throws Exception {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/NewDriveScene.fxml"));
        root = loader.load();
        newDriveSceneController = loader.getController();
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);

        List<Drive> drives = DriveRepository.SelectAll(Database.getDatabase());
        newDriveSceneController.initDrives(drives);

        List<Vehicle> vehicles = VehicleRepository.SelectAll(Database.getDatabase());
        newDriveSceneController.initVehicles(vehicles);

        newDriveSceneController.initEmployees();
    }

    public static void showNewAddressScene(String title, String scene) throws IOException {
        loader = new FXMLLoader(MainWindow.class.getResource("gui/NewAddressScene.fxml"));
        root = loader.load();
        newAddressSceneController = loader.getController();
        newAddressSceneController.sceneBefore = scene;
        newAddressSceneController.addressStreet.setPromptText(newCustomerSceneController.addressStreet);
        newAddressSceneController.addressNumber.setPromptText(newCustomerSceneController.addressNumber);
        newAddressSceneController.town.setPromptText(newCustomerSceneController.town);
        newAddressSceneController.zip.setPromptText(newCustomerSceneController.zip);
        mainScene = new Scene(root);

        window.setScene(mainScene);
        window.setTitle(title);
        window.setMinWidth(500 + 10);
        window.setMinHeight(350 + 40);
        window.setMaxWidth(500 + 10);
        window.setMaxHeight(350 + 40);
    }

    public static boolean close() {
        boolean state = ConfirmModal.show("Opravdu chceš odejít?");
        if (state) {
            window.close();
            Main.needDisconnected = true;
        }

        return state;
    }

    public static Stage getWindow() {
        return window;
    }

    public static AtomicReference<Customer> getCustomer() {
        return customer;
    }
}
