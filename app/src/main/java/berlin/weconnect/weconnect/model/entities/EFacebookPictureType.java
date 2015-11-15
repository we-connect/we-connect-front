package berlin.weconnect.weconnect.model.entities;

public enum EFacebookPictureType {
    SMALL("small"), NORMAL("normal"), ALBUM("album"), LARGE("large"), SQUARE("square");

    private String value;

    // --------------------
    // Constructors
    // --------------------

    EFacebookPictureType(String value) {
        this.value = value;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getValue() {
        return value;
    }
}
