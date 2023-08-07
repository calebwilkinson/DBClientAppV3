package Model;

/**
 * The type Countries.
 */
public class Countries {
    private int id;
    private String name;

    /**
     * Instantiates a new Countries.
     *
     * @param id   the id
     * @param name the name
     */
    public Countries(int id, String name){
        this.id = id;
        this.name = name;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return (name);
    }
}
