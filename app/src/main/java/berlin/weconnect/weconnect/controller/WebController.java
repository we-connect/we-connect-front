package berlin.weconnect.weconnect.controller;

import android.app.Activity;

public class WebController {
    // Activity
    private Activity activity;

    // Model
    private String url;

    private static WebController instance;

    // --------------------
    // Constructors
    // --------------------

    private WebController(Activity activity) {
        setActivity(activity);
        init();
    }

    public static WebController getInstance(Activity activity) {
        if (instance == null) {
            instance = new WebController(activity);
        }

        instance.setActivity(activity);

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

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
