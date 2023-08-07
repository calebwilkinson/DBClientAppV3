package DAO;

import Model.Appointments;
import Model.Customers;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The type Customer.
 */
public class CustomerImpl implements CustomerDAO{
    ObservableList<Customers> customerList;

    /**
     * Update a Customer
     *
     * @param id       the id
     * @param name     the name
     * @param address  the address
     * @param postal   the postal
     * @param phone    the phone
     * @param division the division
     * @param user     the user
     */
    public void update_customer(int id, String name, String address, String postal, String phone, int division, Users user) {
        try {
            String sql = "UPDATE customers SET Customer_Name= ?, Address= ?, Postal_Code= ?, Phone= ?, Last_Update= ?, Last_Updated_By= ?, Division_ID= ? WHERE Customer_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postal);
            ps.setString(4, phone);
            ps.setString(5,  String.valueOf(LocalDateTime.now()));
            ps.setString(6, user.getUserName());
            ps.setInt(7, division);
            ps.setInt(8, id);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Insert a new customer into the database.
     * @param id       the id
     * @param name     the name
     * @param address  the address
     * @param postal   the postal
     * @param phone    the phone
     * @param division the division
     * @param user     the user
     * @return
     */
    public int insert_customer(int id, String name, String address, String postal, String phone, int division, Users user) {
            int rowsAffected = 0;
            try {
                String sql = "INSERT INTO CUSTOMERS(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, address);
                ps.setString(4, postal);
                ps.setString(5, phone);
                ps.setString(6, String.valueOf(LocalDateTime.now()));
                ps.setString(7, user.getUserName());
                ps.setString(8, String.valueOf(LocalDateTime.now()));
                ps.setString(9, user.getUserName());
                ps.setInt(10, division);

                rowsAffected = ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return rowsAffected;
        }

    /**
     * Checks database for existing customerIDs.
     * Determines next ID by finding next available number incrementally.
     * @return
     */
    public int getCustomerID() {
        int i = 1;
        try {
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                if (customerID != i) {
                    break;
                } else {
                    i += 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Deletes a customer from the database based on the parameter id.
     *
     * @param id the id
     * @return
     */
    public boolean delete_customer(int id) {
        boolean customer_has_appointment = false;
        try {
            AppointmentsDAO appointment_db = new AppointmentsImpl();
            ObservableList<Appointments> appointmentList = appointment_db.getAllAppointments();
            for (int i = 0; i < appointmentList.size(); i++) {
                Appointments appointment = appointmentList.get(i);
                if (appointment.getCustomer_ID() == id) {
                    customer_has_appointment = true;
                }
            }

            if (customer_has_appointment == true) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("The customer you want to delete has appointments");
                alert.setContentText("Please delete all of the selected customer's appointments before deleting the customer.");
                alert.showAndWait();
            } else {
                String customer_sql = "DELETE FROM customers WHERE Customer_ID= ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(customer_sql);
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer_has_appointment;
    }

    /**
     * Default constructor for the CustomerImpl() class. Selects all Customers from the
     * database and adds them to customerList, an observableArrayList.
     */
    public CustomerImpl() {
        customerList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");
                Customers customer = new Customers(customerID, customerName, address, postalCode, phoneNumber, divisionID);
                customerList.add(customer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns customerList, an observableArrayList, containing all customers from the database.
     * @return
     */
    @Override
    public ObservableList<Customers> getAllCustomers() {
            return customerList;
    }
}
