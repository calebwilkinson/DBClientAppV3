package Model;

/**
 * The type Contacts.
 */
public class Contacts {
    private int Contact_ID;
    private String Contact_Name;
    private String Email;

    /**
     * Instantiates a new Contacts.
     *
     * @param contactID   the contact id
     * @param contactName the contact name
     * @param email       the email
     */
    public Contacts(int contactID, String contactName, String email) {
        this.Contact_ID = contactID;
        this.Contact_Name = contactName;
        this.Email = email;
    }

    /**
     * Gets contact id.
     *
     * @return the contact id
     */
    public int getContact_ID() {
        return Contact_ID;
    }

    /**
     * Sets contact id.
     *
     * @param contact_ID the contact id
     */
    public void setContact_ID(int contact_ID) {
        Contact_ID = contact_ID;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContact_Name() {
        return Contact_Name;
    }

    /**
     * Sets contact name.
     *
     * @param contact_Name the contact name
     */
    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        Email = email;
    }

    public String toString(){
        return(Contact_Name);
    }
}
