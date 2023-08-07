package Controller;

import DAO.AppointmentsDAO;
import DAO.AppointmentsImpl;
import DAO.ContactsDAO;
import DAO.ContactsImpl;
import Model.Appointments;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * The type Contact report.
 */
public class ContactReport implements Initializable {
    @FXML private ComboBox<Contacts> contactsComboBox;
    @FXML private TableView<Appointments> appointmentsTableView;
    @FXML private TableColumn<Appointments, Integer> appointmentID;
    @FXML private TableColumn<Appointments, String> appointmentTitle;
    @FXML private TableColumn<Appointments, String> appointmentType;
    @FXML private TableColumn<Appointments, String> appointmentDescription;
    @FXML private TableColumn<Appointments, LocalDateTime> appointmentStart;
    @FXML private TableColumn<Appointments, LocalDateTime> appointmentEnd;
    @FXML private TableColumn<Appointments, Integer> customerID;

    private final AppointmentsDAO appointment;

    /**
     * Instantiates a new Contact report.
     * Initializes AppointmentDAO implementation.
     */
    public ContactReport() {
        appointment = new AppointmentsImpl();
    }

    /**
     * On get schedule.
     *
     * This method iterates through all appointments and adds Appointments with
     * matching Contact Name to the one selected in ContactReport. Displays all matching Contact
     * Name appointments in a TableView.
     */
    public void onGetSchedule() {
        Contacts contact = contactsComboBox.getValue();
        String contact_name = contact.getContact_Name();
        ObservableList<Appointments> appointmentList = appointment.getAllAppointments();
        ObservableList<Appointments> appointmentsToSet = FXCollections.observableArrayList();
        //iterate through appointmentList and add appointments with matching contact name
        for (Appointments currentAppointment : appointmentList) {
            if (contact_name.equals(currentAppointment.getContact())) {
                appointmentsToSet.add(currentAppointment);
            }
        }

        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));

        appointmentsTableView.setItems(appointmentsToSet);
    }

    /**
     * On back.
     * Set scene to Homepage.
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
     * Overrides initialize.
     * Sets contactCombo box with all contacts.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactsDAO contacts = new ContactsImpl();
        ObservableList<Contacts> contactList = contacts.getAllContacts();
        contactsComboBox.setItems(contactList);
    }
}
