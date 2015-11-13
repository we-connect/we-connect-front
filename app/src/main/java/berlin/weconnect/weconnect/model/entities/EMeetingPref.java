package berlin.weconnect.weconnect.model.entities;

public enum EMeetingPref {
    ONLY_OWN_GENDER(""), EVERYBODY(""), BOTH("both");

    private String value;

    // --------------------
    // Constructors
    // --------------------

    EMeetingPref(String value) {
        this.value = value;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getValue() {
        return value;
    }
}
