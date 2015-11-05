package berlin.weconnect.weconnect.controller;

public class WebController {
    // Model
    private String url;

    private static WebController instance;

    // --------------------
    // Constructors
    // --------------------

    private WebController() {
        init();
    }

    public static WebController getInstance() {
        if (instance == null) {
            instance = new WebController();
        }

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
