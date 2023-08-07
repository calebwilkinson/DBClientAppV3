package Controller;

import DAO.*;
import Main.ToHomepage;
import Model.Countries;
import Model.First_Lvl_Divisions;
import Model.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Enables customer new customer creation. This class is the controller for
 * the Customer Add scene.
 */
public class CustomerAdd implements Initializable {
    @FXML private TextField customerIdTxt;
    @FXML private TextField customerNameTxt;
    @FXML private TextField customerAddressTxt;
    @FXML private TextField customerPostalTxt;
    @FXML private TextField customerPhoneTxt;
    @FXML private ComboBox<Countries> countryComboBox;
    @FXML private ComboBox<First_Lvl_Divisions> firstLevelComboBox;
    private final CustomerDAO customer;
    private final ToHomepage toStage = new ToHomepage();
    private static Users current_user;

    /**
     * This is the default constructor for the CustomerAdd class.
     * It creates a new customerDAO implementation to be used throughout
     * the class by methods to interact with the database.
     */
    public CustomerAdd() {
         customer = new CustomerImpl();
    }

    /**
     * Overrides initialize.
     * Sets countries for countryComboBox and creates/sets on the form a new CustomerID
     * for the new customer. A listener is added to countryComboBox and a lambda expression is used
     * to run onCountryChoice() on the listener.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String number = String.valueOf(customer.getCustomerID());
        customerIdTxt.setText(number);
        CountriesDAO country = new CountriesImpl();
        ObservableList<Countries> countryList = country.getAllCountries();
        countryComboBox.setItems(countryList);

        /**
         * This lambda expression is used to run onCountryChoice() whenever the listener detects change.
         * onCountryChoice() will set the firstLevelComboBox to an observableList with values matching to
         * country choice.
         */
        countryComboBox.valueProperty().addListener((observableValue, countries, t1) -> onCountryChoice());
    }

    /**
     * This method gets the user selected value from the countryComboBox and sets the states object
     * to hold the matching state/province list using the getSubDivisions() method from the
     * First_Lvl_Div_DAO.
     */
    public void onCountryChoice() {
        First_Lvl_Div_DAO firstLvlDiv = new First_Lvl_Div_Impl();
        Countries country = countryComboBox.getValue();
        ObservableList<First_Lvl_Divisions> states = firstLvlDiv.getSubDivisions(country.getId());
        firstLevelComboBox.setItems(states);
    }

    /**
     * Gets user input from Customer information fields. Utilizes the insert_customer() method
     * from the CustomerDAO to insert into the database. Catches user input format errors and
     * prints out the exception.
     *
     * @param actionEvent the action event. If no format errors caught, sends scene to Homepage.
     * @throws IOException the io exception
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(customerIdTxt.getText());
            String name = customerNameTxt.getText();
            String address = customerAddressTxt.getText() + ", " + firstLevelComboBox.getValue();
            String postalCode = customerPostalTxt.getText();
            String phone = customerPhoneTxt.getText();
            int division = firstLevelComboBox.getValue().getDivision_ID();
            customer.insert_customer(id, name, address, postalCode, phone, division, current_user);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Invalid");
            alert.setContentText("Please enter in data correctly");
            alert.showAndWait();
            System.out.println("Exception: " + e);
        }

        FXMLLoader root = new FXMLLoader(getClass().getResource("/View/Homepage.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        Homepage controller = root.getController();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        controller.loggedUser(current_user);

    }

    /**
     * This toStage object of the toHomePage class utilizes the toHomepage
     * method to change the scene to HomePage.
     *
     * @param actionEvent the action event. Changes the scene to HomePage.
     * @throws IOException the io exception
     */
    public void BackToHomepage(ActionEvent actionEvent) throws IOException {
        toStage.toHomepage(actionEvent);
    }

    /**
     * Used to transfer Users object from Homepage to CustomerAdd scene.
     * @param user
     */
    public void initData(Users user) {
        current_user = user;
    }
}
