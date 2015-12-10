package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.io.File;

import berlin.weconnect.weconnect.view.activities.LoginActivity;

public class FacebookController {
    private static final String TAG = "FacebookController";

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
        clearCache(activity);

        /*
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
        */

        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }

    /**
     * Clear the apllication's cache
     *
     * @param context context
     */
    public static void clearCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Deletes a directory recursively
     *
     * @param dir directory to delete
     * @return whether delete worked
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String c : children) {
                boolean success = deleteDir(new File(dir, c));
                if (!success) {
                    return false;
                }
            }
        }

        return (dir != null) && dir.delete();
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
