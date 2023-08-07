package Controller;

import DAO.AppointmentsDAO;
import DAO.AppointmentsImpl;
import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppointmentReport implements Initializable {
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private Label appointmentNumberLabel;

    private final AppointmentsDAO appointments;

    /**
     * Default constructor.
     * Creates new AppointmentsImpl() for an AppointmentDAO object.
     */
    public AppointmentReport() {
        appointments = new AppointmentsImpl();
    }

    /**
     * Override of initialize method. Sets combo boxes for type and month. Uses a basic sorter
     * to iterate through all appointments, adds types to a list, and remove type duplicates.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Appointments> allAppointments = appointments.getAllAppointments();
        ObservableList<String> typeList = FXCollections.observableArrayList();
        for (Appointments current_appointment : allAppointments) {
            typeList.add(current_appointment.getType());
        }

        //ObservableList to hold the final list ridden of duplicates.
        ObservableList<String> typeListNoOverlap = FXCollections.observableArrayList();

        //Create set to get rid of duplicates in typeList.
        Set set = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        set.addAll(typeList);

        //Add set members to typeListNoOverlap
        for(Object current : set) {
            typeListNoOverlap.add((String) current);
        }

        typeComboBox.setItems(typeListNoOverlap);
        ObservableList<String> months = FXCollections.observableArrayList();
        String[] allMonths = new DateFormatSymbols().getMonths();
        Collections.addAll(months, allMonths);
        monthComboBox.setItems(months);
    }

    /**
     * Get type and month selection. Checks for null selections. Interacts with database
     * to find total number of customer appointments for given type and month.
     */
    public void onGetAppointments() {
        String selectedType = typeComboBox.getSelectionModel().getSelectedItem();
        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toLowerCase();

        if (selectedMonth.isEmpty() || selectedType.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Please select an appointment type and month.");
            alert.showAndWait();
        } else {
            int i = 0;
            ObservableList<Appointments> allAppointments = appointments.getAllAppointments();
            for (Appointments current_appointment : allAppointments) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
                LocalDateTime month = LocalDateTime.parse(current_appointment.getStart(), dtf);
                String current_month = month.getMonth().toString().toLowerCase();
                System.out.println(current_month + selectedMonth + current_appointment.getType() + selectedType);
                if (selectedMonth.equals(current_month) && selectedType.equals(current_appointment.getType())) {
                    i+=1;
                }
            }
            appointmentNumberLabel.setText("Total Number of Customer Appointments: " + i);
        }
    }

    /**
     * Back to Reports screen.
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
