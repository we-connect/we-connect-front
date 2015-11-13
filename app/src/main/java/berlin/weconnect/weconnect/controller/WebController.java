package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.view.activities.WebActivity;

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

    /**
     * Opens web activity and opens @page
     *
     * @param activity activity
     * @param page     Facebook page to be opened
     */
    public void goToFacebookPage(Activity activity, String page) {
        Intent facebookAppIntent;
                /*
                try {
                    // TODO : find way to open profile page in Facebook app
                    // facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + user.getFacebook_id()));
                    // facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + user.getFacebook_id()));
                    // facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + user.getFacebook_id()));
                    // activity.startActivity(facebookAppIntent);
                } catch (ActivityNotFoundException e) {
                    // Open standard browser calling Facebook page
                    facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/" + user.getFacebook_id()));
                    activity.startActivity(facebookAppIntent);
                }
                */
        Resources res = activity.getResources();

        setUrl(res.getString(R.string.url_facebook) + page);
        facebookAppIntent = new Intent(activity, WebActivity.class);
        activity.startActivity(facebookAppIntent);
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
