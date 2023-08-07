package DAO;

import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements UserDAO. Contains methods for database interaction with the users table.
 */
public class UserImpl implements UserDAO {
    ObservableList<Users> userList;

    /**
     * Default constructor. Gets all users in database, adds them to observable-arraylist accessible by all other class methods.
     */
    public UserImpl() {
        userList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users user = new Users(userID, userName, password);
                userList.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Given a valid userID the method will search database and return Users object with matching userID.
     * @param userID
     * @return
     */
    public Users getUserWithID(int userID) {
        Users user = null;
        try {
            String sql = "SELECT * FROM users WHERE User_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int user_ID = rs.getInt("User_ID");
                String user_name = rs.getString("User_Name");
                String password = rs.getString("Password");
                user = new Users(user_ID, user_name, password);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    /**
     * Returns observable list of all users.
     *
     * @return
     */
    @Override
    public ObservableList<Users> getAllUsers() {
        return userList;
    }
}
