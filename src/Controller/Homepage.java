package Controller;

import DAO.*;
import Model.Appointments;
import Model.Customers;
import Model.Users;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The type Homepage.
 */
public class Homepage implements Initializable {
    @FXML private TableView<Customers> CustomerTableView;
    @FXML private TableColumn<Customers, Integer> customerIdCol;
    @FXML private TableColumn<Customers, String> customerNameCol;
    @FXML private TableColumn<Customers, String> addressCol;
    @FXML private TableColumn<Customers, String> postalCol;
    @FXML private TableColumn<Customers, String> phoneCol;
    @FXML private TableColumn<Customers, Integer> divisionCol;

    @FXML private TableView<Appointments> AppointmentTableView;
    @FXML private TableColumn<Appointments, Integer> appointmentIdCol;
    @FXML private TableColumn<Appointments, String> titleCol;
    @FXML private TableColumn<Appointments, String> descriptionCol;
    @FXML private TableColumn<Appointments, String> typeCol;
    @FXML private TableColumn<Appointments, LocalDateTime> startCol;
    @FXML private TableColumn<Appointments, LocalDateTime> endCol;
    @FXML private TableColumn<Appointments, Integer> appointmentCustomerIdCol;
    @FXML private TableColumn<Appointments, Integer> appointmentUserIdCol;
    @FXML private TableColumn<Appointments, String> locationCol;
    @FXML private TableColumn<Appointments, String> contactCol;
    @FXML private Label userIDLabel;
    @FXML private RadioButton allButton;
    @FXML private RadioButton weekButton;
    @FXML private RadioButton monthButton;


    private final AppointmentsDAO appointment;
    private final DateTimeFormatter dtf;
    private Users current_user;

    /**
     * Instantiates a new Homepage.
     * Used AppointmentDAO object to create a new implementation.
     * Creates a DateTimeFormatter to enhance TableView displays.
     * Sets time for displaying upcoming Appointments.
     */
    public Homepage() {
        appointment = new AppointmentsImpl();
        dtf = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
    }

    /**
     * Logged user.
     * This method assists in Users data transfer from LoginPortal.
     * Sets User ID text in top left of page. Saves logged-in User to current_user.
     *
     * @param user the user
     */
    public void loggedUser(Users user) {
        userIDLabel.setText("User ID: " + user.getUser_ID());
        current_user = user;
        check_appointment_time(user);
    }

    /**
     * This method is used to set the AppointmentTableView based on All/Month/Week button selection.
     * @param allAppointments
     * @param weekAppointments
     * @param monthAppointments
     */
    public void onAppointmentToggleSelection(ObservableList<Appointments> allAppointments, ObservableList<Appointments> weekAppointments, ObservableList<Appointments> monthAppointments) {
        if (allButton.isSelected()) {
            AppointmentTableView.setItems(allAppointments);
        } else if (weekButton.isSelected()) {
            AppointmentTableView.setItems(weekAppointments);
        } else if (monthButton.isSelected()) {
            AppointmentTableView.setItems(monthAppointments);
        }
    }

    /**
     * On customer update.
     * Checks for selected customer from CustomerTableView.
     * Uses showCustomerDialog() to send data from Homepage to CustomerUpdate controller.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onCustomerUpdate(ActionEvent actionEvent) throws IOException {
        Customers customer = CustomerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected customer to update");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        } else {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/View/CustomerUpdate.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root.load());
            CustomerUpdate controller = root.getController();
            //pass the current_user object and customer object
            controller.initData(customer, current_user);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }

    /**
     * An override of initialize.
     * Sets Customer and Appointment TableView data using the .getAll() methods.
     *
     * Lambda expression used on line 200 to set AppointmentsTableView based on RadioButton selection.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerDAO customer = new CustomerImpl();
        ObservableList<Customers> customers = customer.getAllCustomers();
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postal_code"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division_id"));
        CustomerTableView.setItems(customers);

        AppointmentsDAO appointment = new AppointmentsImpl();
        ObservableList<Appointments> appointments = appointment.getAllAppointments();

        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        appointmentUserIdCol.setCellValueFactory(new PropertyValueFactory<>("user_ID"));

        AppointmentTableView.setItems(appointments);

        ToggleGroup toggleGroup = new ToggleGroup();
        allButton.setToggleGroup(toggleGroup);
        monthButton.setToggleGroup(toggleGroup);
        weekButton.setToggleGroup(toggleGroup);

        //lists to hold all appointments, apptmnts this month, and apptmnts this week
        ObservableList<Appointments> monthAppointments = FXCollections.observableArrayList();
        ObservableList<Appointments> weekAppointments = FXCollections.observableArrayList();

        //Setting the monthAppointments list.
        Month currentDate = LocalDateTime.now().toLocalDate().getMonth();
        for (Appointments currentAppointment : appointments) {
            LocalDateTime lcd = LocalDateTime.parse(currentAppointment.getStart(), dtf);
            Month appointmentDate = lcd.toLocalDate().getMonth();
            if (currentDate == appointmentDate) {
                monthAppointments.add(currentAppointment);
            }
        }

        //Setting the weekAppointments list.
        LocalDate date = LocalDateTime.now().toLocalDate();
        WeekFields thisWeekDate = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(thisWeekDate.weekOfWeekBasedYear());
        for (Appointments currentAppointment : appointments) {
            LocalDateTime lcd = LocalDateTime.parse(currentAppointment.getStart(), dtf);
            LocalDate appointmentDate = lcd.toLocalDate();
            WeekFields appointmentWeek = WeekFields.of(Locale.getDefault());
            int appointmentWeekNumber = appointmentDate.get(appointmentWeek.weekOfWeekBasedYear());
            if (weekNumber == appointmentWeekNumber) {
                weekAppointments.add(currentAppointment);
            }
        }

        //Lambda expression used to set AppointmentsTableView based on RadioButton selection.
        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            onAppointmentToggleSelection(appointments, weekAppointments, monthAppointments);
        });
    }

    /**
     * Check appointment time.
     * Finds all appointments with matching UserIDs to the current User logged in.
     * Checks to see if there is an upcoming appointment.
     * Display messages depending on upcoming appointment status.
     *
     * @param user the user
     */
    public void check_appointment_time (Users user) {
        ObservableList<Appointments> appointments = appointment.getAllAppointments();
        // boolean for upcoming appointment or not
        boolean isAppointment = false;
        for (Appointments current_appointment : appointments) {
            if (current_appointment.getUser_ID() == user.getUser_ID()) {
                // minus 1 minutes and plus 17, so before and after is captured within 15 minutes
                LocalDateTime timeNow = LocalDateTime.now().minusMinutes(1);
                LocalDateTime timeTo = timeNow.plusMinutes(17);

                //convert String appointment Start time to LocalDateTime.
                LocalDateTime current_appointment_time = LocalDateTime.parse(current_appointment.getStart(), dtf);
                if ((current_appointment_time.isAfter(timeNow) && current_appointment_time.isBefore(timeTo))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You have an Upcoming Appointment");
                    alert.setContentText("You have an upcoming appointments in the next 15 minutes.\n Appointment ID = " + current_appointment.getAppointment_ID() +
                                         " at " +current_appointment_time.format(dtf));
                    alert.showAndWait();
                    isAppointment = true;
                    break;
                }
            }
        }
        if (!isAppointment) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You have no upcoming Appointment");
            alert.setContentText("You have no upcoming appointments in the next 15 minutes.");
            alert.showAndWait();
        }
    }

    /**
     * On add customer.
     * used by the OnCustomerAdd method to pass a user
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("/View/CustomerAdd.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        CustomerAdd controller = root.getController();
        controller.initData(current_user);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * On appointment add creates and shows a new stage and scene for the AppointmentAdd form.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onAppointmentAdd(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AppointmentAdd.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * On delete customer gets selected customer from tableview.
     * Checks for null selection, if not null, asks for deletion confirmation. Uses CustomerDAO methods to
     * delete customer from database.
     */
    public void onDeleteCustomer() {
        CustomerDAO command = new CustomerImpl();
        Customers customer = CustomerTableView.getSelectionModel().getSelectedItem();

        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected customer to delete");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            boolean customer_appointment_check = command.delete_customer(customer.getId());
            if (result.isPresent() && result.get() == ButtonType.OK && !customer_appointment_check) {
                CustomerTableView.getItems().remove(customer);

                Alert alert_2 = new Alert(Alert.AlertType.INFORMATION);
                alert_2.setTitle("Customer Deleted");
                alert_2.setContentText("Customer: " + customer.getName() + ", ID: " + customer.getId() + " has been deleted.");
                alert_2.showAndWait();
            }
        }
    }

    /**
     * On delete appointment gets selected appointment from tableview.
     * Checks for null selection, if not null, asks for deletion confirmation.
     * On confirmation, AppointmentDAO methods used to delete appointment from database.
     *
     */
    public void onDeleteAppointment() {
        AppointmentsDAO command = new AppointmentsImpl();
        Appointments appointment = AppointmentTableView.getSelectionModel().getSelectedItem();

        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected appointment to delete");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                command.delete_appointment(appointment.getAppointment_ID());
                AppointmentTableView.getItems().remove(appointment);
                Alert alert_2 = new Alert(Alert.AlertType.INFORMATION);
                alert_2.setTitle("Appointment Cancelled");
                alert_2.setContentText("Appointment #" + appointment.getAppointment_ID() + " Type: " + appointment.getType() + " for Customer #" + appointment.getCustomer_ID() + " has been cancelled.");
                alert_2.showAndWait();
            }
        }
    }

    /**
     * On appointment update gets appointment selection from tableview.
     * Checks for null selection, if not null, sets stage and sends to AppointmentUpdate scene.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onAppointmentUpdate(ActionEvent actionEvent) throws IOException {
        Appointments selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected appointment to update");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        } else {
            FXMLLoader root = new FXMLLoader(getClass().getResource("/View/AppointmentUpdate.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root.load());
            AppointmentUpdate controller = root.getController();
            controller.initData(selectedAppointment);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }

    /**
     * On reports sends to reports scene.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML void onExit(ActionEvent event) {
        System.exit(0);
    }
}
