package pl.polsl.lab.mateusz.plonka.model;

/**
 * Simple implementation of own String
 *
 * @author Mateusz Płonka
 */
public class MyString {

    private String value;

    /**
     * Default constructor
     */
    public MyString() {
        this.value = "";
    }

    /**
     * Main constructor
     *
     * @param value String to store
     */
    public MyString(String value) {
        this.value = value;
    }

    /**
     * Getter of string value
     *
     * @return string value
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter of string value
     *
     * @param value string value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
