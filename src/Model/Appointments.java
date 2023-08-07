package Model;

/**
 * The type Appointments.
 */
public class Appointments {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private String Start;
    private String End;
    private int Customer_ID;
    private int User_ID;
    private String contact;

    /**
     * Instantiates a new Appointments.
     *
     * @param appointment_ID the appointment id
     * @param title          the title
     * @param description    the description
     * @param location       the location
     * @param type           the type
     * @param start          the start
     * @param end            the end
     * @param customer_ID    the customer id
     * @param user_ID        the user id
     * @param contact        the contact
     */
    public Appointments(int appointment_ID, String title, String description, String location, String type, String start, String end, int customer_ID, int user_ID, String contact) {
        this.Appointment_ID = appointment_ID;
        this.Title = title;
        this.Location = location;
        this.Description = description;
        this.Type = type;
        this.Start = start;
        this.End = end;
        this.Customer_ID = customer_ID;
        this.User_ID = user_ID;
        this.contact = contact;
    }

    /**
     * Gets contact.
     *
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets contact.
     *
     * @param contact the contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return Location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * Gets appointment id.
     *
     * @return the appointment id
     */
    public int getAppointment_ID() {
        return Appointment_ID;
    }

    /**
     * Sets appointment id.
     *
     * @param appointment_ID the appointment id
     */
    public void setAppointment_ID(int appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public String getEnd() {
        return End;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(String end) {
        End = end;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public String getStart() {
        return Start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(String start) {
        Start = start;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return Type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public int getCustomer_ID() {
        return Customer_ID;
    }

    /**
     * Sets customer id.
     *
     * @param customer_ID the customer id
     */
    public void setCustomer_ID(int customer_ID) {
        Customer_ID = customer_ID;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUser_ID() {
        return User_ID;
    }

    /**
     * Sets user id.
     *
     * @param user_ID the user id
     */
    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }
}
