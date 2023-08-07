package DAO;

import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * The type Countries.
 */
public class CountriesImpl implements CountriesDAO{
    ObservableList<Countries> countryList;

    /**
     * Selects Country from database with matching countryID as the ID passed in parameter.
     * @param countryID the country id
     * @return
     */
    public Countries getSpecificCountry(int countryID) {
        Countries country = null;
        try {
            String sql = "SELECT * FROM countries WHERE Country_ID= ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int country_ID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                country = new Countries(country_ID, countryName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return country;
    }

    /**
     * Selects all countries from database and adds them to Observable Array List countryList.
     */
    public CountriesImpl() {
        countryList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * from countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries country = new Countries(countryID, countryName);
                countryList.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Returns Observable Array List countryList containing all countries.
     * @return
     */
    @Override
    public ObservableList<Countries> getAllCountries() {
        return countryList;
    }
}
