package delivery.controllers;

import com.orm.*;
import com.orm.dao.*;
import delivery.EditingCell;
import delivery.InformationWindow;
import delivery.MainWindow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class NewOrderSceneController {

    public Label foodType;
    public Label rating;
    public ChoiceBox restaurantChoice;
    public Label totalPrice;
    public TableView actualDishes;
    public TableColumn nameColumn;
    public TableColumn idColumn;
    public TableColumn weightColumn;
    public TableColumn priceColumn;
    public TableColumn amountColumn;
    public TableColumn addColumn;
    public TableColumn removeColumn;
    public ComboBox paymentType;
    public TextField tip;

    public Callback<TableColumn, TableCell> cellFactory = p -> new EditingCell();
    private static int totalPriceValue = 0;
    private List<Dish> dishList = new ArrayList<>();
    private static int idRestaurant = 0;
    private static int idCustomer = 0;

    public void initializeRestaurants() {
        List<RestaurantAll> restaurantList = RestaurantRepository.SelectAll(Database.getDatabase());
        for (RestaurantAll restaurant : restaurantList) {
            restaurantChoice.getItems().add(restaurant);
        }

        restaurantChoice.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (restaurantList.get(new_value.intValue()).getFoodType().equals("italian")) {
                foodType.setText("Italská");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("czech")) {
                foodType.setText("Česká");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("chinese")) {
                foodType.setText("Čínská");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("mexican")) {
                foodType.setText("Mexická");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("indian")) {
                foodType.setText("Indická");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("seafood")) {
                foodType.setText("Mořské plody");
            } else if (restaurantList.get(new_value.intValue()).getFoodType().equals("steakhouse")) {
                foodType.setText("Steak house");
            }
            if (String.valueOf(restaurantList.get(new_value.intValue()).getRating()).isEmpty()) {
                rating.setText("nehodnoceno");
            } else {
                rating.setText(String.valueOf(restaurantList.get(new_value.intValue()).getRating()));
            }
            actualDishes.setEditable(true);
            idRestaurant = restaurantList.get(new_value.intValue()).getId();
            List<Dish> dishesList = DishRepository.SelectByRestaurantId(idRestaurant, Database.getDatabase());
            fillDish(dishesList);
        });
    }

    public void fillDish(List<Dish> dishes) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(cellFactory);
        amountColumn.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<Dish, String>>) t ->
                        t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setAmount(t.getNewValue())
        );

        actualDishes.setItems(FXCollections.observableList(dishes));
        addAddButton();
        addRemoveButton();
    }

    private void addAddButton() {
        Callback<TableColumn, TableCell> cellFactory = new Callback<>() {

            @Override
            public TableCell call(final TableColumn param) {
                final TableCell cell = new TableCell<>() {
                    Button btn = new Button("Přidat");

                    {
                        btn.setOnAction(e -> {
                            Dish dish = (Dish)getTableView().getItems().get(getIndex());
                            if (dish.getAmount().equals("0")) {
                                InformationWindow.show("Nezvoleno množství");
                            } else {
                                if (isNumber(dish.getAmount())) {
                                    getTableRow().setStyle("-fx-background-color: #baffba;");
                                    totalPriceValue += (dish.getPrice() * Integer.parseInt(dish.getAmount()));
                                    totalPrice.setText(totalPriceValue + " ,-");
                                    Content content = new Content();
                                    content.setAmount(Integer.parseInt(dish.getAmount()));
                                    dish.setContent(content);
                                    dishList.add(dish);
                                } else {
                                    InformationWindow.show("Počet jídla není číslo");
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
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

        addColumn.setCellFactory(cellFactory);
    }

    private void addRemoveButton() {
        Callback<TableColumn, TableCell> cellFactory = new Callback<>() {

            @Override
            public TableCell call(final TableColumn param) {
                final TableCell cell = new TableCell<>() {
                    Button btn = new Button("Odebrat");

                    {
                        btn.setOnAction(e -> {
                            Dish dish = (Dish)getTableView().getItems().get(getIndex());
                            if (dish.getAmount().equals("0")) {
                                InformationWindow.show("Není co odebrat");
                            } else {
                                dish.setAmount("0");
                                getTableView().getItems().set(getIndex(), dish);
                                getTableRow().setStyle("-fx-background-color: #ffffff");
                                totalPriceValue -= (dish.getPrice() * Integer.parseInt(dish.getAmount()));
                                totalPrice.setText(totalPriceValue + " ,-");
                            }
                        });
                    }

                    @Override
                    public void updateItem(Object item, boolean empty) {
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

        removeColumn.setCellFactory(cellFactory);
    }

    public void close() throws Exception {
        MainWindow.showMainLoggedScreen("Objednávkový systém jídla");
    }

    public void createNewOrder() {
        Employee employee = EmployeeRepository.SelectMaxEmployee(idRestaurant, Database.getDatabase());
        Order order = new Order();
        if (!paymentType.getSelectionModel().isEmpty() && isNumber(tip.getText())) {
            if (paymentType.getValue().toString().equals("Penězi")) {
                order.setPaymentType("cash");
            } else if (paymentType.getValue().toString().equals("Kartou")) {
                order.setPaymentType("card");
            } else {
                order.setPaymentType("online");
            }
            order.setTip(Integer.parseInt(tip.getText()));
            order.setEmployee(employee);
            Restaurant restaurant = new Restaurant();
            restaurant.setId(idRestaurant);
            order.setRestaurant(restaurant);
            Customer customer = new Customer();
            customer.setId(idCustomer);
            order.setCustomer(customer);
            List<Content> contents = new ArrayList<>();
            for (Dish dish : dishList) {
                Content content = new Content();
                content.setDish(dish);
                content.setAmount(Integer.parseInt(dish.getAmount()));
                contents.add(content);
            }
            int rows = OrderRepository.Insert(order, contents, Database.getDatabase());
            if (rows != 0) {
                InformationWindow.show("Objednávka vytvořena");
            }
        } else {
            InformationWindow.show("Nezvoleno!");
        }
    }

    public void initPaymentType() {
        paymentType.getItems().addAll(
                "Online",
                "Penězi",
                "Kartou"
        );
    }

    public void setCustomerId(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    private boolean isNumber(String input) {
        try {
            int number = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
