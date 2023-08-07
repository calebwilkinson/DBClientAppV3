package Controller;

import DAO.*;
import Main.ToHomepage;
import Model.Contacts;
import Model.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * The type Appointment add.
 */
public class AppointmentAdd implements Initializable {
    @FXML private TextField appointmentIdTxt;
    @FXML private TextField titleTxt;
    @FXML private TextField descriptionTxt;
    @FXML private TextField locationTxt;
    @FXML private TextField typeTxt;
    @FXML private ComboBox<Contacts> contactsComboBox;
    @FXML private DatePicker dayTxt;
    @FXML private ComboBox<LocalTime> startComboBox;
    @FXML private ComboBox<LocalTime> endComboBox;
    @FXML private TextField customerIdTxt;
    @FXML private TextField userIdTxt;

    private final AppointmentsDAO appointment;
    private final UserDAO user;
    private final ToHomepage toStage = new ToHomepage();

    /**
     * Instantiates a new Appointment add.
     * Instantiates AppointmentDAO and UserDAO implementations.
     */
    public AppointmentAdd() {
        appointment = new AppointmentsImpl();
        user = new UserImpl();
    }

    /**
     * Set customerID for the new customer. Set start and end time options to local time.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //8, 22 represents business opening times 8am-10pm EST
        int unconverted_start = 8;
        int unconverted_end = 22;

        //get local ZoneId
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
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

    /**
     * On save.
     * Checks for appointment overlap and input errors.
     * Converts local time to UTC for database insertion.
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
            Contacts contact = contactsComboBox.getValue();
            int contactID = contact.getContact_ID();
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
                    appointment.insert_appointment(id, title, description, location, type, start, end, customerID, current_user, contactID);
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
}
