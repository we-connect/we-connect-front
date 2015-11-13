package berlin.weconnect.weconnect.model.entities;

public enum EType {
    NEWCOMER("newcomer"), LOCAL("local");

    private String value;

    // --------------------
    // Constructors
    // --------------------

    EType(String value) {
        this.value = value;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getValue() {
        return value;
    }
}
