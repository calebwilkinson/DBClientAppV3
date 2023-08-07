package DAO;

import Model.First_Lvl_Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type First lvl div contains methods for interaction with the first level divisions table in the database.
 */
public class First_Lvl_Div_Impl implements First_Lvl_Div_DAO{
    ObservableList<First_Lvl_Divisions> divisionList;
    ObservableList<First_Lvl_Divisions> subDivisionList;

    /**
     * Takes parameter divisionID and interacts with the database to return the specific division object with
     * the matching divisionID.
     *
     * @param divisionID
     * @return
     */
    public First_Lvl_Divisions getSpecificDivision(int divisionID) {
       First_Lvl_Divisions division = null;
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Division_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int division_ID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int country_ID = rs.getInt("Country_ID");
                division = new First_Lvl_Divisions(division_ID, divisionName, country_ID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return division;
    }

    /**
     * Takes and sorts based on the countryID parameter. Returns subDivisionList based on countryID.
     *
     * @param countryID
     * @return
     */
    public ObservableList<First_Lvl_Divisions> getSubDivisions(int countryID) {
        if (countryID == 1) {
            subDivisionList = FXCollections.observableArrayList(divisionList.subList(0, 51));
        } else if (countryID == 3) {
            subDivisionList = FXCollections.observableArrayList(divisionList.subList(52,64));
        } else if (countryID == 2) {
            subDivisionList = FXCollections.observableArrayList(divisionList.subList(64, 68));
        }
        return subDivisionList;
    }

    /**
     * Default constructor. Gets all first level divisions from the first level division table
     * sets the observable array list divisionList to hold all the divisions.
     */
    public First_Lvl_Div_Impl() {
        divisionList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionID = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");
                First_Lvl_Divisions divisions = new First_Lvl_Divisions(divisionID, division, countryID);
                divisionList.add(divisions);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns the divisionList containing all first level divisions in the first level division table from the database.
     * @return
     */
    @Override
    public ObservableList<First_Lvl_Divisions> getAllDivisions() {
        return divisionList;
    }
}
