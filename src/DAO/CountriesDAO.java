package DAO;

import Model.Countries;
import javafx.collections.ObservableList;

/**
 * The interface Countries dao.
 */
public interface CountriesDAO {
    /**
     * Gets all countries.
     *
     * @return the all countries
     */
    ObservableList<Countries> getAllCountries();

    /**
     * Gets specific country.
     *
     * @param countryID the country id
     * @return the specific country
     */
    Countries getSpecificCountry(int countryID);
}
