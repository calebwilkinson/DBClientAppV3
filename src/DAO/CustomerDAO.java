package DAO;

import Model.Customers;
import Model.Users;
import javafx.collections.ObservableList;

/**
 * The interface Customer dao.
 */
public interface CustomerDAO {
    /**
     * Gets all customers.
     *
     * @return the all customers
     */
    ObservableList<Customers> getAllCustomers();
    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    int getCustomerID();
    /**
     * Insert customer int.
     *
     * @param id       the id
     * @param name     the name
     * @param address  the address
     * @param postal   the postal
     * @param phone    the phone
     * @param division the division
     * @param user     the user
     * @return the int
     */
    int insert_customer(int id, String name, String address, String postal, String phone, int division, Users user);
    /**
     * Delete customer boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete_customer(int id);
    /**
     * Update customer.
     *
     * @param id       the id
     * @param name     the name
     * @param address  the address
     * @param postal   the postal
     * @param phone    the phone
     * @param division the division
     * @param user     the user
     */
    void update_customer(int id, String name, String address, String postal, String phone, int division, Users user);

}
