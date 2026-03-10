package delivery.controllers;

import com.orm.Customer;
import com.orm.Order;
import com.orm.Restaurant;
import com.orm.dao.Database;
import com.orm.dao.OrderRepository;
import delivery.CustomerAuthentication;
import delivery.MainWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MainSceneController {

    public Label customerLabel;
    public TableView<Order> actualOrders;
    public TableColumn<Order, String> creationColumn;
    public TableColumn<Order, String> handoverColumn;
    public TableColumn<Order, String> paymentColumn;
    public TableColumn<Order, String> restaurantColumn;
    public TableColumn<Order, Void> detailColumn;
    private static int idCustomer = 0;

    public void setCustomer(String name, String surname, int id) {
        idCustomer = id;
        customerLabel.setText("Zákazník: " + name + " " + surname);
    }

    public void setVoidCustomer() {
        customerLabel.setText("Zákazník: ");
    }

    public void fillActualOrders(List<Order> orders) {
        creationColumn.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        handoverColumn.setCellValueFactory(new PropertyValueFactory<>("handoverTime"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        restaurantColumn.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        addDetailButton();
        actualOrders.setItems(FXCollections.observableList(orders));
    }

    public void setVoidOrder() {
        actualOrders.getItems().clear();
    }

    public void showNewOrder() throws Exception {
        MainWindow.showNewOrderScene("Nová objednávka", idCustomer);
    }

    private void addDetailButton() {
        Callback<TableColumn<Order, Void>, TableCell<Order, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {
                    Button btn = new Button("Detail");

                    {
                        btn.setOnAction(e -> {
                            Order order = getTableView().getItems().get(getIndex());
                            try {
                                MainWindow.showOrderDetailScene("Detail objednávky", order.getId());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        detailColumn.setCellFactory(cellFactory);
    }

    public void userLogout() {
        AtomicReference<Customer> customer;
        List<Order> orders;
        this.setVoidCustomer();
        this.setVoidOrder();
        customer = CustomerAuthentication.show(Database.getDatabase());
        orders = OrderRepository.SelectAllbyID(customer.get().getId(), Database.getDatabase());
        this.setCustomer(customer.get().getName(), customer.get().getSurname(), customer.get().getId());
        this.fillActualOrders(orders);
    }

    public void showAdminAuth() throws Exception {
        MainWindow.showAdmin("Správce systému");
    }
}
