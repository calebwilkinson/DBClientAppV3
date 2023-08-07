package DAO;

import Model.Users;
import javafx.collections.ObservableList;

/**
 * The interface User dao.
 */
public interface UserDAO {
    /**
     * Gets all users.
     *
     * @return the all users
     */
    ObservableList<Users> getAllUsers();
    /**
     * Gets user with id.
     *
     * @param userID the user id
     * @return the user with id
     */
    Users getUserWithID(int userID);
}
