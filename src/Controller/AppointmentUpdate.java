package Controller;

import DAO.*;
import Main.ToHomepage;
import Model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * The type Appointment update.
 * Controller for the Appointment Update screen.
 */
public class AppointmentUpdate implements Initializable {
    @FXML private TextField appointmentIdTxt;
    @FXML private TextField titleTxt;
    @FXML private TextField descriptionTxt;
    @FXML private TextField locationTxt;
    @FXML private TextField typeTxt;
    @FXML private TextField customerIdTxt;
    @FXML private TextField userIdTxt;
    @FXML private ComboBox<Contacts> contactsComboBox;
    @FXML private ComboBox<LocalTime> startComboBox;
    @FXML private ComboBox<LocalTime> endComboBox;
    @FXML private DatePicker dayTxt;
    private final AppointmentsDAO appointment;
    private final ContactsDAO contact;
    private final UserDAO user;
    private final ToHomepage toStage = new ToHomepage();

    /**
     * Instantiates a new Appointment update.
     * Instantiates AppointmentDAO, ContactDAO, and UserDAO implementations.
     */
    public AppointmentUpdate() {
        appointment = new AppointmentsImpl();
        contact = new ContactsImpl();
        user = new UserImpl();
    }

    /**
     * Init data.
     * Used to pass/initialize Appointments object from Homepage to Appointment Update screen.
     *
     * @param appointments the appointments
     */
    void initData(Appointments appointments) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

        appointmentIdTxt.setText(String.valueOf(appointments.getAppointment_ID()));
        ObservableList<Contacts> contactList = contact.getAllContacts();
        contactsComboBox.setItems(contactList);
        titleTxt.setText(appointments.getTitle());
        descriptionTxt.setText(appointments.getDescription());
        locationTxt.setText(appointments.getLocation());
        typeTxt.setText(appointments.getType());
        Contacts newContact = contact.getContactWithName(appointments.getContact());
        contactsComboBox.setValue(newContact);
        LocalDateTime startTime = LocalDateTime.parse(appointments.getStart(), dtf);
        LocalDateTime endTime = LocalDateTime.parse(appointments.getEnd(), dtf);
        dayTxt.setValue(startTime.toLocalDate());
        startComboBox.setValue(startTime.toLocalTime());
        endComboBox.setValue(endTime.toLocalTime());
        customerIdTxt.setText(String.valueOf(appointments.getCustomer_ID()));
        userIdTxt.setText(String.valueOf(appointments.getUser_ID()));
    }

    /**
     * On save.
     * Converts local time to UTC
     * Updates appointment using AppointmentDAO implementation
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        try {
            int id = Integer.parseInt(appointmentIdTxt.getText());
            String title = titleTxt.getText();
            String description = descriptionTxt.getText();
            String location = locationTxt.getText();
            String type = typeTxt.getText();
            int contactID = contactsComboBox.getValue().getContact_ID();
            int customerID = Integer.parseInt(customerIdTxt.getText());
            int userID = Integer.parseInt(userIdTxt.getText());
            Users current_user = user.getUserWithID(userID);

            LocalDate day = dayTxt.getValue();
            LocalTime tempStart = startComboBox.getValue();
            LocalTime tempEnd = endComboBox.getValue();

            if (tempEnd.isBefore(tempStart)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid appointment times");
                alert.setContentText("Appointment start time needs to be before appointment end time");
                alert.showAndWait();
            } else {
                LocalDateTime local = LocalDateTime.now();
                LocalDateTime UTC = LocalDateTime.now(ZoneId.of("UTC"));
                long betweenHours = ChronoUnit.HOURS.between(local, UTC);
                LocalTime converted_start;
                LocalTime converted_end;
                if (local.isBefore(UTC)) {
                    converted_start = tempStart.plusHours(betweenHours);
                    converted_end = tempEnd.plusHours(betweenHours);
                } else {
                    converted_start = tempStart.minusHours(betweenHours);
                    converted_end = tempEnd.minusHours(betweenHours);
                }

                LocalDateTime start = LocalDateTime.of(day, converted_start);
                LocalDateTime end = LocalDateTime.of(day, converted_end);

                boolean appointment_overlap = appointment.check_appointment_overlap(start, end, customerID, id);
                if (appointment_overlap) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Appointment Overlap");
                    alert.setContentText("Please change appointment times to ensure no overlap for the customer.");
                    alert.showAndWait();
                } else {
                    appointment.update_appointment(id, title, description, location, type, start, end, customerID, userID, contactID, current_user);
                }
                toStage.toHomepage(actionEvent);
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Invalid");
            alert.setContentText("Please enter in data correctly");
            alert.showAndWait();
            System.out.println("Exception: " + e);
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //8, 22 represents business opening times 8am-10pm EST
        int unconverted_start = 8;
        int unconverted_end = 22;

        //get UTC ZoneId
        LocalDateTime local = LocalDateTime.now();
        LocalDateTime EST = LocalDateTime.now(ZoneId.of("America/New_York"));
        long betweenHours = ChronoUnit.HOURS.between(local, EST);
        LocalTime converted_start;
        LocalTime converted_end;
        if (local.isBefore(EST)) {
            converted_start = LocalTime.of((int) (unconverted_start - betweenHours), 0);
            converted_end = LocalTime.of((int) (unconverted_end - betweenHours), 0);
        } else {

            converted_start = LocalTime.of((int) (unconverted_start + betweenHours), 0);
            converted_end = LocalTime.of((int) (unconverted_end + betweenHours), 0);
        }

        while (converted_start.isBefore(converted_end.plusSeconds(1))) {
            startComboBox.getItems().add(converted_start);
            endComboBox.getItems().add(converted_start);
            converted_start = converted_start.plusMinutes(30);
        }

        String number = String.valueOf(appointment.getAppointmentID());
        appointmentIdTxt.setText(number);
        ContactsDAO contact = new ContactsImpl();
        ObservableList<Contacts> contactList = contact.getAllContacts();
        contactsComboBox.setItems(contactList);
    }
}
