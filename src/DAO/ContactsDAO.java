package DAO;

import Model.Contacts;
import javafx.collections.ObservableList;

/**
 * The interface Contacts dao.
 */
public interface ContactsDAO {
    /**
     * Gets all contacts.
     *
     * @return the all contacts
     */
    ObservableList<Contacts> getAllContacts();

    /**
     * Gets contact with name.
     *
     * @param contactName the contact name
     * @return the contact with name
     */
    Contacts getContactWithName(String contactName);

    /**
     * Gets contact with id.
     *
     * @param contactID the contact id
     * @return the contact with id
     */
    Contacts getContactWithID(int contactID);
}
