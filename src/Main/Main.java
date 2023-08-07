package Main;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The type Main.
 */
public class Main extends Application {

    /**
     * Loads the Login Portal.
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/LoginPortal.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Main method. Opens database connection, launches arguments, and closes database connection on program exit.
     * @param args
     */
    public static void main(String[] args) {

        JDBC.openConnection();

        launch(args);

        JDBC.closeConnection();
    }
}