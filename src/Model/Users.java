package Model;

/**
 * The type Users.
 */
public class Users {
    private final int user_ID;
    private final String userName;
    private final String password;

    /**
     * Instantiates a new Users.
     *
     * @param user_ID  the user id
     * @param userName the user name
     * @param password the password
     */
    public Users(int user_ID, String userName, String password) {
        this.user_ID = user_ID;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUser_ID() {
        return user_ID;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }
}
