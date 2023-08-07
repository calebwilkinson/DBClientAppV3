package Controller;

import DAO.*;
import Main.ToHomepage;
import Model.Countries;
import Model.Customers;
import Model.First_Lvl_Divisions;
import Model.Users;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The type Customer update.
 */
public class CustomerUpdate implements Initializable {
    @FXML private TextField customerIdTxt;
    @FXML private TextField customerNameTxt;
    @FXML private TextField customerAddressTxt;
    @FXML private TextField customerPostalTxt;
    @FXML private TextField customerPhoneTxt;
    @FXML private ComboBox<Countries> countryComboBox;
    @FXML private ComboBox<First_Lvl_Divisions> stateComboBox;
    private final First_Lvl_Div_DAO databaseDivision;
    private final CountriesDAO databaseCountry;
    private final CustomerDAO customer;
    private final ToHomepage toStage = new ToHomepage();
    private static Users current_user;

    /**
     * Instantiates a new Customer update.
     * Initializes First_Lvl_Div_ImplDAO, CountriesDAO, and CustomerDAO implementations.
     */
    public CustomerUpdate() {
        databaseDivision = new First_Lvl_Div_Impl();
        databaseCountry = new CountriesImpl();
        customer = new CustomerImpl();
    }

    /**
     * On save.
     * Gets value from fields on forms. Uses CustomerDAO update_customer()
     * method to update into the database.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(customerIdTxt.getText());
        String name = customerNameTxt.getText();
        String address = customerAddressTxt.getText() + ", " + stateComboBox.getValue();
        String postalCode = customerPostalTxt.getText();
        String phone = customerPhoneTxt.getText();
        int division = stateComboBox.getValue().getDivision_ID();
        customer.update_customer(id, name, address, postalCode, phone, division, current_user);

        toStage.toHomepage(actionEvent);
    }

    /**
     * Init data.
     * Adds listener on countryComboBox to be used in setting statesComboBox.
     * On countryComboBox selection.
     *
     * The lambda expression used on line 88 is used to run onCountryChoice() whenever the listener detects
     * change. onCountryChoice() will set the firstLevelComboBox to an observableList with values matching
     * to country choice.
     *
     * @param customer the customer
     * @param user     the user
     */
    public void initData(Customers customer, Users user) {
        current_user = user;
        ObservableList<Countries> countryList = databaseCountry.getAllCountries();
        countryComboBox.setItems(countryList);


        //This lambda expression is used to run onCountryChoice() whenever the listener detects change.
        //onCountryChoice() will set the firstLevelComboBox to an observableList with values matching to
        //country choice.
        countryComboBox.valueProperty().addListener((observableValue, countries, t1) -> onCountryChoice());

        customerNameTxt.setText(customer.getName());
        customerIdTxt.setText(String.valueOf(customer.getId()));
        customerAddressTxt.setText(customer.getAddress());
        customerPostalTxt.setText(customer.getPostal_code());
        int divisionId = customer.getDivision_id();
        First_Lvl_Divisions division = databaseDivision.getSpecificDivision(divisionId);
        int countryId = division.getCountry_ID();
        Countries country = databaseCountry.getSpecificCountry(countryId);
        ObservableList<First_Lvl_Divisions> subList = databaseDivision.getSubDivisions(countryId);
        stateComboBox.setItems(subList);
        countryComboBox.setValue(country);
        stateComboBox.setValue(division);
        customerPhoneTxt.setText(customer.getPhone());
    }

    /**
     * On country choice.
     * Used with listener in initData() to set the stateComboBox depending
     * on the choice selected in countryComboBox.
     */
    public void onCountryChoice() {
        Countries country = countryComboBox.getValue();
        ObservableList<First_Lvl_Divisions> states = databaseDivision.getSubDivisions(country.getId());
        stateComboBox.setItems(states);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Back to homepage.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void BackToHomepage(ActionEvent actionEvent) throws IOException {
        toStage.toHomepage(actionEvent);
    }
}
