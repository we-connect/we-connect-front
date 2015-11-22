package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.view.activities.LoginActivity;

public class FacebookController {
    // Activity
    private Activity activity;

    private static FacebookController instance;

    // --------------------
    // Constructors
    // --------------------

    private FacebookController() {
    }

    public static FacebookController getInstance(Activity activity) {
        if (instance == null) {
            instance = new FacebookController();
        }

        instance.setActivity(activity);

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Determines whether the user is currently logged in to Facebook
     *
     * @return whether user is logged in to Facebook or not
     */
    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null;
    }

    /**
     * Logs off from Facebook
     */
    public void logout() {
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
        LoginManager.getInstance().logOut();
        FacebookSdk.sdkInitialize(activity);

        Resources res = activity.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(res.getString(R.string.pref_fb_logged_in), false);
        editor.putString(res.getString(R.string.pref_fb_facebook_id), null);
        editor.putString(res.getString(R.string.pref_fb_username), null);
        editor.putString(res.getString(R.string.pref_fb_firstname), null);
        editor.putString(res.getString(R.string.pref_fb_lastname), null);
        editor.putString(res.getString(R.string.pref_fb_profile_uri), null);
        editor.putString(res.getString(R.string.pref_fb_profile_picture_uri), null);
        editor.apply();

        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
