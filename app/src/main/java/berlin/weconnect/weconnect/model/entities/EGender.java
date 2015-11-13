package berlin.weconnect.weconnect.model.entities;

public enum EGender {
    MALE("male"), FEMALE("female");

    private String value;

    // --------------------
    // Constructors
    // --------------------

    EGender(String value) {
        this.value = value;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getValue() {
        return value;
    }
}
