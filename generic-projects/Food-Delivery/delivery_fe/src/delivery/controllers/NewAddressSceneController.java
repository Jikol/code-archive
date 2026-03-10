package delivery.controllers;

import com.orm.Address;
import com.orm.dao.AddressRepository;
import com.orm.dao.Database;
import delivery.InformationWindow;
import delivery.MainWindow;
import javafx.scene.control.TextField;

public class NewAddressSceneController {

    public TextField addressStreet;
    public TextField addressNumber;
    public TextField town;
    public TextField zip;
    public String sceneBefore;

    public static String _addressStreet;
    public static String _addressNumber;
    public static String _town;
    public static String _zip;

    public void addNewAddress() throws Exception {
        if (addressStreet.getText().isEmpty()) {
            _addressStreet = addressStreet.getPromptText();
        } else {
            _addressStreet = addressStreet.getText();
        }
        if (addressNumber.getText().isEmpty()) {
            _addressNumber = addressNumber.getPromptText();
        } else {
            _addressNumber = addressNumber.getText();
        }
        if (town.getText().isEmpty()) {
            _town = town.getPromptText();
        } else {
            _town = town.getText();
        }
        if (zip.getText().isEmpty()) {
            _zip = zip.getPromptText();
        } else {
            _zip = zip.getText();
        }

        if (_addressStreet.matches("[a-zA-Z]+") && _addressNumber.matches("[0-9/]+") && _town.matches("[a-zA-Z]+") && isNumber(_zip) && _zip.length() == 5) {
            NewCustomerSceneController.addressFilled = true;
            NewCustomerSceneController.addressStreet = _addressStreet;
            NewCustomerSceneController.addressNumber = _addressNumber;
            NewCustomerSceneController.town = _town;
            NewCustomerSceneController.zip = _zip;
            MainWindow.showNewCustomerScene("Nový zákazník");
        } else {
            InformationWindow.show("Chybné údaje");
        }
    }

    public void newAddressClose() throws Exception {
        if (sceneBefore.equals("customer")) {
            MainWindow.showNewCustomerScene("Nový zákazník");
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
