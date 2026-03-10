package delivery.controllers;

import com.orm.Address;
import com.orm.Customer;
import com.orm.Order;
import com.orm.dao.AddressRepository;
import com.orm.dao.CustomerRepository;
import com.orm.dao.Database;
import com.orm.dao.OrderRepository;
import delivery.CustomerAuthentication;
import delivery.InformationWindow;
import delivery.MainWindow;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.w3c.dom.Text;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NewCustomerSceneController {

    public TextField _name;
    public TextField _surname;
    public ComboBox gender;
    public TextField _phone;
    public DatePicker birthDate;

    public static boolean addressFilled = false;
    public static String addressStreet;
    public static String addressNumber;
    public static String town;
    public static String zip;

    public void initComboBox() {
        gender.getItems().addAll(
                "Muž",
                "Žena"
        );
    }

    public void newCustomerClose() throws Exception {
        MainWindow.showMainScene("Objednávkový systém jídla");
    }

    public void addAddress() throws Exception {
        MainWindow.showNewAddressScene("Nová adresa", "customer");
    }

    public void registerUser() throws Exception {
        if (_name.getText().matches("[a-zA-Z]+") && _surname.getText().matches("[a-zA-Z]+") && isNumber(_phone.getText()) && (_phone.getText().length() == 9) && !gender.getSelectionModel().isEmpty()) {
            if (addressFilled) {
                Address address = new Address();
                address.setStreet(addressStreet);
                address.setStreetNumber(addressNumber);
                address.setCity(town);
                address.setZip(Integer.parseInt(zip));
                AddressRepository.Insert(address, Database.getDatabase());
            }
            Customer customer = new Customer();
            customer.setName(_name.getText());
            customer.setSurname(_surname.getText());
            if (gender.getValue().toString().equals("Muž")) {
                customer.setGender("M");
            } else {
                customer.setGender("F");
            }
            customer.setPhoneNumber("+420" + _phone.getText());
            LocalDate localDate = birthDate.getValue();
            customer.setBirthDate(localDate.toString());
            if (addressFilled) {
                Address address = new Address();
                address.setId(AddressRepository.SelectLastId(Database.getDatabase()));
                customer.setAddress(address);
            }
            int rows = 0;
            rows = CustomerRepository.Insert(customer, Database.getDatabase());
            if (rows != 0) {
                MainWindow.showMainScene("Objednávkový systém jídla");
            } else {
                InformationWindow.show("Zadané údaje již existují");
            }
        } else {
            InformationWindow.show("Chybné údaje");
        }
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
