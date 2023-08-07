package Controller;

import DAO.*;
import Model.Countries;
import Model.Customers;
import Model.First_Lvl_Divisions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The type Customer country report. Controller for the CustomerCountryReport form.
 * Allows an user to select a country and get the total number of customers for selected country.
 */
public class CustomerCountryReport implements Initializable {
    @FXML private ComboBox<Countries> countryComboBox;
    @FXML private Label customersPerCountry;

    private final CustomerDAO customers;
    private final CountriesDAO countries;
    private final First_Lvl_Div_DAO divisions;

    /**
     * Default constructor. Initializes implementations for each DAO interface used throughout the class.
     */
    public CustomerCountryReport() {
        customers = new CustomerImpl();
        countries = new CountriesImpl();
        divisions = new First_Lvl_Div_Impl();
    }

    /**
     * onGetTotalCustomers().
     * Get country selection. Iterate through all customers and if have they have a divisionId belonging
     * to the selected country, add 1 to counter. Set text to show what country selected and how many customers
     * the country has.
     */
    public void onGetTotalCustomers() {
        int i = 0;
        ObservableList<Customers> customerList = customers.getAllCustomers();
        int country = countryComboBox.getValue().getId();
        String country_name = countryComboBox.getValue().getName();

        for (Customers current_customer : customerList) {
            First_Lvl_Divisions division = divisions.getSpecificDivision(current_customer.getDivision_id());
            if (country == division.getCountry_ID()) {

                i+=1;
            }
        }
        customersPerCountry.setText("Total number of customers in " + country_name + ": " + i);
    }

    /**
     * On back. Set stage and scene to Reports.fxml. Show stage.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Set countryComboBox to all countries in countryList.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Countries> countryList = countries.getAllCountries();
        countryComboBox.setItems(countryList);
    }
}
