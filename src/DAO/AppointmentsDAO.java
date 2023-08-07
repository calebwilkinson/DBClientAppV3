package DAO;

import Model.Appointments;
import Model.Users;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;

/**
 * The interface Appointments dao.
 */
public interface AppointmentsDAO {
    /**
     * Gets all appointments.
     *
     * @return the all appointments
     */
    ObservableList<Appointments> getAllAppointments();

    /**
     * Insert appointment into database.
     *
     * @param id          the id
     * @param title       the title
     * @param description the description
     * @param location    the location
     * @param type        the type
     * @param start       the start
     * @param end         the end
     * @param customerID  the customer id
     * @param user        the user
     * @param contactID   the contact id
     * @return the int
     */
    int insert_appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, Users user, int contactID);

    /**
     * Gets an ID for a new appointment.
     *
     * @return the appointment id
     */
    int getAppointmentID();

    /**
     * Delete appointment from database with the given ID.
     *
     * @param id the id
     */
    void delete_appointment(int id);

    /**
     * Check appointment time overlap of appointment being created or updated. Return boolean true if there is
     * overlap with another appointment. False if not overlap.
     *
     * @param start      the start
     * @param end        the end
     * @param customerID the customer id
     * @return the boolean
     */
    boolean check_appointment_overlap(LocalDateTime start, LocalDateTime end, int customerID, int appointmentID);

    /**
     * Update appointment.
     *
     * @param id          the id
     * @param title       the title
     * @param description the description
     * @param location    the location
     * @param type        the type
     * @param start       the start
     * @param end         the end
     * @param contactID   the contact id
     * @param userID      the user id
     * @param customerID  the customer id
     * @param user        the user
     */
    void update_appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int contactID, int userID, int customerID, Users user);
}
