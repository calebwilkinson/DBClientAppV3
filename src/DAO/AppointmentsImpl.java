package DAO;

import Model.Appointments;
import Model.Contacts;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Implements AppointmentsDAO. Contains methods for database interaction/manipulation involving
 * appointments table.
 */
public class AppointmentsImpl implements AppointmentsDAO {
    ObservableList<Appointments> appointmentList;

    /**
     * Default constructor. Gets all appointments from the database, adds them to observable list accessible by all other class methods.
     *
     */
    public AppointmentsImpl() {
        appointmentList = FXCollections.observableArrayList();
        ContactsDAO contact = new ContactsImpl();

        try {
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String description = rs.getString("Description");
                String type = rs.getString("Type");
                //get unadjusted time from database
                LocalDateTime unadjustedStart = (LocalDateTime) rs.getObject("Start");
                //separate date and time
                LocalDate unadjustedStartDate = unadjustedStart.toLocalDate();
                LocalTime unadjustedStartTime = unadjustedStart.toLocalTime();
                //get target zone ID
                ZoneId UTCZoneId = ZoneId.of("UTC");
                //set time to target Zone
                ZonedDateTime utcZoneStart = ZonedDateTime.of(unadjustedStartDate, unadjustedStartTime, UTCZoneId);
                //get local ZoneId
                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                //convert to instant so .atZone can be used
                Instant timeToUtc = utcZoneStart.toInstant();
                //convert UTC time to local time
                ZonedDateTime start = timeToUtc.atZone(localZoneId);

                //same done to unadjustedStart, do to unadjustedEnd
                LocalDateTime unadjustedEnd = (LocalDateTime) rs.getObject("End");
                LocalDate unadjustedEndDate = unadjustedEnd.toLocalDate();
                LocalTime unadjustedEndTime = unadjustedEnd.toLocalTime();
                ZonedDateTime utcZoneEnd = ZonedDateTime.of(unadjustedEndDate, unadjustedEndTime, UTCZoneId);
                Instant timeToUtcEnd = utcZoneEnd.toInstant();
                ZonedDateTime end = timeToUtcEnd.atZone(localZoneId);

                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Contacts newContact = contact.getContactWithID(contactID);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");
                Appointments appointment = new Appointments(appointmentID, title, description, location, type, start.format(dtf), end.format(dtf), customerID, userID, newContact.getContact_Name());
                appointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes appointment from appointments table in database.
     * @param id
     */
    public void delete_appointment(int id) {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to determine a new appointment ID. Checks database for existing appointment ID. Increments by 1
     * and returns next available appointment ID not used by any existing appointment.
     * @return
     */
    public int getAppointmentID() {
        int i = 1;
        try {
            String sql = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                if (appointmentID != i) {
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
     * Checks for appointment overlap.
     * Uses customerID to find other customer appointments.
     *
     * @param start
     * @param end
     * @param customerID
     * @return
     */
    public boolean check_appointment_overlap(LocalDateTime start, LocalDateTime end, int customerID, int appointmentID) {
        LocalDate date_to_check = start.toLocalDate();
        LocalTime start_time_to_check = start.toLocalTime();
        LocalTime end_time_to_check = end.toLocalTime();

        boolean appointment_overlap = false;
        try {
            String sql = "SELECT Start, End FROM appointments WHERE Customer_ID= ? AND Appointment_ID != ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setInt(2, appointmentID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                LocalDateTime unadjustedStart = (LocalDateTime) rs.getObject("Start");
                LocalDateTime unadjustedEnd = (LocalDateTime) rs.getObject("End");

                //get unadjusted time from database
                //separate date and time
                LocalDate unadjustedStartDate = unadjustedStart.toLocalDate();
                LocalTime unadjustedStartTime = unadjustedStart.toLocalTime();
                //get target zone ID
                ZoneId UTCZoneId = ZoneId.of("UTC");
                //set time to target Zone
                ZonedDateTime utcZoneStart = ZonedDateTime.of(unadjustedStartDate, unadjustedStartTime, UTCZoneId);
                //get local ZoneId
                ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
                //convert to instant so .atZone can be used
                Instant timeToUtc = utcZoneStart.toInstant();
                //convert UTC time to local time
                LocalDateTime unconvertedStart = timeToUtc.atZone(localZoneId).toLocalDateTime();
                LocalTime convertedStart = unconvertedStart.toLocalTime();

                //same done to unadjustedStart, do to unadjustedEnd
                LocalDate unadjustedEndDate = unadjustedEnd.toLocalDate();
                LocalTime unadjustedEndTime = unadjustedEnd.toLocalTime();
                ZonedDateTime utcZoneEnd = ZonedDateTime.of(unadjustedEndDate, unadjustedEndTime, UTCZoneId);
                Instant timeToUtcEnd = utcZoneEnd.toInstant();
                LocalTime convertedEnd = timeToUtcEnd.atZone(localZoneId).toLocalTime();

                LocalDate appointment_date = unconvertedStart.toLocalDate();


                if (appointment_date.equals(date_to_check)) {
                   if (!convertedStart.isBefore(start_time_to_check) && !convertedEnd.isBefore(start_time_to_check)) {
                       appointment_overlap = true;
                   }
                   if (!convertedStart.isAfter(end_time_to_check) && !convertedEnd.isAfter(end_time_to_check)) {
                       appointment_overlap = true;
                   }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointment_overlap;
    }

    /**
     * Updates appointment.
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @param user
     */
    public void update_appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID, Users user) {
        try {
            String sql = "UPDATE appointments SET Title= ?, Description= ?, Location= ?, Type= ?, Start= ?, End= ?, Last_Update= ?, Last_Updated_By= ?, Customer_ID= ?, User_ID= ?, Contact_ID= ? WHERE Appointment_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, String.valueOf(start));
            ps.setString(6, String.valueOf(end));
            ps.setString(7, String.valueOf(LocalDateTime.now()));
            ps.setString(8, user.getUserName());
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);
            ps.setInt(12, id);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Inserts a new appointment into the database.
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param user
     * @param contactID
     * @return
     */
    public int insert_appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, Users user, int contactID) {
        int rowsAffected = 0;
        try {
            String sql = "INSERT INTO APPOINTMENTS(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, description);
            ps.setString(4, location);
            ps.setString(5, type);
            ps.setString(6, String.valueOf(start));
            ps.setString(7, String.valueOf(end));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, user.getUserName());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, user.getUserName());
            ps.setInt(12, customerID);
            ps.setInt(13, user.getUser_ID());
            ps.setInt(14, contactID);

            rowsAffected = ps.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * Returns list of all appointments.
     * @return
     */
    @Override
    public ObservableList<Appointments> getAllAppointments() {
        return appointmentList;
    }
}
