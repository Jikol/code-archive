package delivery.controllers;

import com.orm.*;
import com.orm.dao.*;
import delivery.InformationWindow;
import delivery.MainWindow;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class NewDriveSceneController {

    public TableView<Drive> drives;
    public TableColumn<Drive, String> rentalTime;
    public TableColumn<Drive, String> returnTime;
    public TableColumn<Drive, String> vehicle;

    public TableView<Vehicle> vehicles;
    public TableColumn<Vehicle, String> brand;
    public TableColumn<Vehicle, String> type;
    public TableColumn<Vehicle, String> restaurant;

    public ChoiceBox employeeChoice;
    public ChoiceBox vehicleChoice;

    private static int idEmployee = 0;
    private static int idVehicle = 0;

    public void initDrives(List<Drive> drivesList) {
        for (Drive drive : drivesList) {
            drive.setVehicleName(drive.getVehicle().getLicensePlate());
        }
        rentalTime.setCellValueFactory(new PropertyValueFactory<>("rentalTime"));
        returnTime.setCellValueFactory(new PropertyValueFactory<>("returnTime"));
        vehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
        drives.setItems(FXCollections.observableList(drivesList));
    }

    public void initVehicles(List<Vehicle> vehicleList) {
        for (Vehicle drive : vehicleList) {
            drive.setRestaurantName(drive.getRestaurant().getName());
        }
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        restaurant.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        vehicles.setItems(FXCollections.observableList(vehicleList));
    }

    public void initEmployees() {
        List<Employee> employeeList = EmployeeRepository.SelectAllWithoutDrive(Database.getDatabase());
        for (Employee employeeAll : employeeList) {
            employeeChoice.getItems().add(employeeAll);
        }

        employeeChoice.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            vehicleChoice.getItems().clear();
            int id = employeeList.get(new_value.intValue()).getRestaurant().getId();
            idEmployee = employeeList.get(new_value.intValue()).getId();
            List<Vehicle> vehicles = VehicleRepository.SelectAllByRestaurantId(id, Database.getDatabase());
            for (Vehicle vehicle : vehicles) {
                vehicleChoice.getItems().add(vehicle);
            }
            vehicleChoice.getSelectionModel().selectedIndexProperty().addListener((_ov, _value, _new_value) -> {
                idVehicle = vehicles.get(_new_value.intValue()).getId();
            });
        });
    }

    public void createNewDrive() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(idVehicle);
        Employee employee = new Employee();
        employee.setId(idEmployee);
        Drive drive = new Drive();
        drive.setVehicle(vehicle);
        drive.setEmployee(employee);
        int rows = DriveRepository.Insert(drive, Database.getDatabase());
        if (rows != 0) {
            InformationWindow.show("Jízda vložena");
            MainWindow.showLoggedAdmin("Administrace systému");
        } else {
            InformationWindow.show("Není vybrán zaměstnanec nebo vozidlo");
        }
    }

    public void close() throws Exception {
        MainWindow.showLoggedAdmin("Administrace systému");
    }
}
