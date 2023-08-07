package DAO;

import Model.First_Lvl_Divisions;
import javafx.collections.ObservableList;

/**
 * The interface First lvl div dao.
 */
public interface First_Lvl_Div_DAO {
    /**
     * Gets all divisions.
     *
     * @return the all divisions
     */
    ObservableList<First_Lvl_Divisions> getAllDivisions();
    /**
     * Gets sub divisions.
     *
     * @param countryID the country id
     * @return the sub divisions
     */
    ObservableList<First_Lvl_Divisions> getSubDivisions(int countryID);
    /**
     * Gets specific division.
     *
     * @param divisionID the division id
     * @return the specific division
     */
    First_Lvl_Divisions getSpecificDivision(int divisionID);
}
