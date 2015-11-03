package berlin.weconnect.weconnect.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Collections;

import berlin.weconnect.weconnect.R;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private Activity activity;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
    }

    public void onResume() {
        super.onResume();

        // Check if user is already logged in to Facebook with this app
        if (AccessToken.getCurrentAccessToken() != null) {
            writeProfileToPrefs(Profile.getCurrentProfile());
            onLoginSuccessful();
        }

        // Handle callback from login manager
        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                writeProfileToPrefs(Profile.getCurrentProfile());
                onLoginSuccessful();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, R.string.login_cancelled, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Writes Facebook profile information into preferences
     *
     * @param profile Facebook profile
     */
    private void writeProfileToPrefs(Profile profile) {
        if (profile != null) {
            String id = profile.getId();
            String name = profile.getName();
            String firstName = profile.getFirstName();
            String lastName = profile.getLastName();
            Uri profileUri = profile.getLinkUri();
            Uri profilePictureUri = profile.getProfilePictureUri(50, 50);

            Resources res = LoginActivity.this.getResources();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(res.getString(R.string.pref_fb_facebook_id), id);
            editor.putString(res.getString(R.string.pref_fb_username), name);
            editor.putString(res.getString(R.string.pref_fb_firstname), firstName);
            editor.putString(res.getString(R.string.pref_fb_lastname), lastName);
            editor.putString(res.getString(R.string.pref_fb_profile_uri), profileUri.getPath());
            editor.putString(res.getString(R.string.pref_fb_profile_picture_uri), profilePictureUri.getPath());
            editor.apply();
        }
    }

    /**
     * Handles behavior after a Facebook login has taken place
     */
    private void onLoginSuccessful() {
        Resources res = activity.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String username = prefs.getString(res.getString(R.string.pref_fb_username), "");

        Toast.makeText(LoginActivity.this, getResources().getString(R.string.logged_in_as) + " " + username, Toast.LENGTH_LONG).show();

        Intent openStartingPoint = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(openStartingPoint);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
