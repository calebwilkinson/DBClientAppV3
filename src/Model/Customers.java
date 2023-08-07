package Model;

/**
 * The type Customers.
 */
public class Customers {
    private int id;
    private String name;
    private String address;
    private String postal_code;
    private String phone;
    private int division_id;

    /**
     * Instantiates a new Customers.
     *
     * @param id          the id
     * @param name        the name
     * @param address     the address
     * @param postal_code the postal code
     * @param phone       the phone
     * @param division_id the division id
     */
    public Customers(int id, String name, String address, String postal_code, String phone, int division_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal_code = postal_code;
        this.phone = phone;
        this.division_id = division_id;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostal_code() {
        return postal_code;
    }

    /**
     * Sets postal code.
     *
     * @param postal_code the postal code
     */
    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public int getDivision_id() {
        return division_id;
    }

    /**
     * Sets division id.
     *
     * @param division_id the division id
     */
    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }
}
