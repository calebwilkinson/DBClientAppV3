package Model;

/**
 * The type First lvl divisions.
 */
public class First_Lvl_Divisions {
    private int division_ID;
    private String division;
    private int country_ID;

    /**
     * Instantiates a new First lvl divisions.
     *
     * @param division_ID the division id
     * @param division    the division
     * @param country_ID  the country id
     */
    public First_Lvl_Divisions(int division_ID, String division, int country_ID) {
        this.division_ID = division_ID;
        this.division = division;
        this.country_ID = country_ID;
    }

    /**
     * Gets division.
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets country id.
     *
     * @return the country id
     */
    public int getCountry_ID() {
        return country_ID;
    }

    /**
     * Sets country id.
     *
     * @param country_ID the country id
     */
    public void setCountry_ID(int country_ID) {
        this.country_ID = country_ID;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public int getDivision_ID() {
        return division_ID;
    }

    /**
     * Sets division id.
     *
     * @param division_ID the division id
     */
    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    public String toString() {
        return (division);
    }
}
