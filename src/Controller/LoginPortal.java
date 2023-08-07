package Controller;

import DAO.UserDAO;
import DAO.UserImpl;
import Main.ToHomepage;
import Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Login Portal class that contains methods to check for valid user login and to pass the logged-in user
 * data to the Homepage.
 */
public class LoginPortal implements Initializable {
    @FXML private Label zone_id_label;
    @FXML private TextField username_txt;
    @FXML private TextField password_txt;
    @FXML private Label loginPortal;
    @FXML private Label username;
    @FXML private Label password;
    @FXML private Button onExit;
    @FXML private Button onSignIn;

    Logger log = Logger.getLogger(LoginPortal.class.getName());
    FileHandler fh;

    /**
     * On sign in
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void onSignIn(ActionEvent actionEvent) throws IOException {
        String username = username_txt.getText();
        String password = password_txt.getText();

        UserDAO usersDAO = new UserImpl();

        boolean loginCheck = false;

        for (Users currentUser : usersDAO.getAllUsers()) {
            String username_to_check = currentUser.getUserName();
            String password_to_check = currentUser.getPassword();

            if ((username_to_check.equals(username)) && (password_to_check.equals(password))) {
                log.log(Level.INFO, "Login Information Valid. Username used: " + username);
                FXMLLoader root = new FXMLLoader(getClass().getResource("/View/Homepage.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root.load());
                Homepage controller = root.getController();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                controller.loggedUser(currentUser);
                loginCheck = true;
                break;
            }
        }
        if (!loginCheck) {
            if(Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Information Invalid");
                alert.setContentText("Please re-enter the correct login information.");
                alert.showAndWait();
            } else if (Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle bundle = ResourceBundle.getBundle("nat", Locale.FRANCE);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("LoginInvalid"));
                alert.setContentText(bundle.getString("LoginInvalidContext"));
                alert.showAndWait();
            }
            log.log(Level.WARNING, "Invalid Login Information. Username used: " + username);
        }
    }

    /**
     * Checks for default Locale. If French, French resource Bundle is used to set labels and other texts.
     * Logger loaded to track Sign In validation and User logged.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle bundle = ResourceBundle.getBundle("nat", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr")) {
                loginPortal.setText(bundle.getString("LoginPortal"));
                username.setText(bundle.getString("Username"));
                password.setText(bundle.getString("Password"));
                onExit.setText(bundle.getString("Exit"));
                onSignIn.setText(bundle.getString("SignIn"));
            }
        }

        ZoneId zone = ZoneId.systemDefault();
        zone_id_label.setText(String.valueOf(zone));

        try {
            fh = new FileHandler("login_activity.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleFormatter sf = new SimpleFormatter();
        fh.setFormatter(sf);
        log.addHandler(fh);
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }
}
