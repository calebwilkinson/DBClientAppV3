package DAO;

import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Contacts.
 * Implements ContactsDAO interface.
 */
public class ContactsImpl implements ContactsDAO {
    /**
     * The Contact list.
     */
    ObservableList<Contacts> contactList;

    /**
     * Instantiates a new Contacts.
     * Gets all contacts and adds them to ObservableArrayList.
     */
    public ContactsImpl() {
        contactList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contacts contact = new Contacts(contactID, contactName, email);
                contactList.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Selects contact with matching parameter passed ID.
     * Returns a Contact object.
     *
     * @param contactID the contact id
     * @return
     */
    public Contacts getContactWithID(int contactID) {
        Contacts contact = null;
        try {
            String sql = "SELECT * FROM contacts WHERE Contact_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, contactID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contact_ID = rs.getInt("Contact_ID");
                String contact_name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contact = new Contacts(contact_ID, contact_name, email);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contact;
    }

    /**
     * Selects a contact from database with matching Contact_name.
     * Returns a Contacts object.
     *
     * @param contactName the contact name
     * @return
     */
    public Contacts getContactWithName(String contactName) {
        Contacts contact = null;
        try {
            String sql = "SELECT * FROM contacts WHERE Contact_Name= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, contactName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contact_ID = rs.getInt("Contact_ID");
                String contact_name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contact = new Contacts(contact_ID, contact_name, email);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contact;
    }

    /**
     * Returns ObservableArrayList of all contacts.
     * @return
     */
    @Override
    public ObservableList<Contacts> getAllContacts() {
        return contactList;
    }
}
