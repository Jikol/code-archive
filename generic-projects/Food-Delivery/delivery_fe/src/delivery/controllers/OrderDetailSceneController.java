package delivery.controllers;

import com.orm.Customer;
import com.orm.Dish;
import com.orm.Order;
import com.orm.dao.Database;
import com.orm.dao.DishRepository;
import com.orm.dao.OrderRepository;
import delivery.CustomerAuthentication;
import delivery.MainWindow;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OrderDetailSceneController {

    public TitledPane dishesContainer;
    public Label name;
    public Label desc;
    public Label rating;
    public Label creationTime;
    public Label handoverTime;
    public Label deliveryTime;
    public Label sumPrice;

    public void initializeDishes(int idOrder) {
        List<Dish> dishesList = DishRepository.SelectByOrderId(idOrder, Database.getDatabase());
        ArrayList<TreeItem> dishes = getDishes(dishesList);

        TreeItem rootNode = new TreeItem();
        rootNode.getChildren().addAll(dishes);
        TreeView treeDishes = new TreeView(rootNode);
        treeDishes.setShowRoot(false);

        VBox layout = new VBox();
        layout.getChildren().add(treeDishes);
        dishesContainer.setContent(layout);

        Order order = OrderRepository.Select(idOrder, Database.getDatabase());
        name.setText(order.getRestaurant().getName());
        desc.setText(order.getRestaurant().getDesc());
        if (order.getRestaurant().getRating() == 0) {
            rating.setText("nehodnoceno");
        } else {
            rating.setText(String.valueOf(order.getRestaurant().getRating()));
        }
        creationTime.setText(order.getCreationTime());
        handoverTime.setText(order.getHandoverTime());
        deliveryTime.setText(String.valueOf(Integer.parseInt(order.getCreationTime().substring(order.getCreationTime().lastIndexOf(":") + 1)) + order.getRestaurant().getDeliveryTime()));
        int sumAll = 0;
        for (Dish dish : dishesList) {
            sumAll += (dish.getPrice() * dish.getContent().getAmount());
        }
        sumPrice.setText("Celková cena: " + String.valueOf(sumAll) + ",-");
    }

    public void close() throws Exception {
        MainWindow.showMainLoggedScreen("Objednávkový systém jídla");
    }

    private ArrayList<TreeItem> getDishes(List<Dish> dishesList) {
        ArrayList<TreeItem> dishes = new ArrayList<>();
        for (Dish dish : dishesList) {
            TreeItem root = new TreeItem(dish.getName() + " | " + dish.getContent().getAmount() + "ks");
            root.setExpanded(true);
            root.getChildren().addAll(getDishesAttrs(dish));
            dishes.add(root);
        }

        return dishes;
    }

    private ArrayList<TreeItem> getDishesAttrs(Dish dish) {
        ArrayList<TreeItem> attrs = new ArrayList<>();

        TreeItem weight = new TreeItem("Gramáž: " + dish.getWeight() + "g");
        TreeItem price = new TreeItem("Cena: " + dish.getPrice() + ",-");

        attrs.add(weight);
        attrs.add(price);

        return attrs;
    }
}
