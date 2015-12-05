package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.UsersController;

public class LoginActivity extends BaseActivity {

    private CallbackManager callbackManager;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final LoginButton btnLogin = (LoginButton) findViewById(R.id.btnLogin);

        // Check if user is already logged in to Facebook with this app
        if (FacebookController.getInstance(this).isLoggedIn()) {
            writeProfileToPrefs(Profile.getCurrentProfile());
            onLoginSuccessful();
        }

        callbackManager = CallbackManager.Factory.create();
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    /**
     * Writes Facebook profile information into preferences
     *
     * @param profile Facebook profile
     */
    private void writeProfileToPrefs(@Nullable Profile profile) {
        if (profile != null) {
            String id = profile.getId();
            String name = profile.getName();
            String firstName = profile.getFirstName();
            String lastName = profile.getLastName();
            Uri profileUri = profile.getLinkUri();
            Uri profilePictureUri = profile.getProfilePictureUri(100, 100);

            Resources res = LoginActivity.this.getResources();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(res.getString(R.string.pref_fb_logged_in), true);
            editor.putString(res.getString(R.string.pref_fb_facebook_id), id);
            editor.putString(res.getString(R.string.pref_fb_username), name);
            editor.putString(res.getString(R.string.pref_fb_firstname), firstName);
            editor.putString(res.getString(R.string.pref_fb_lastname), lastName);
            editor.putString(res.getString(R.string.pref_fb_email), id + res.getString(R.string.at_weconnect_berlin));
            editor.putString(res.getString(R.string.pref_fb_profile_uri), profileUri.getPath());
            editor.putString(res.getString(R.string.pref_fb_profile_picture_uri), profilePictureUri.getPath());
            editor.apply();
        }
    }

    /**
     * Handles behavior after a Facebook login has taken place
     */
    private void onLoginSuccessful() {
        UsersController usersController = UsersController.getInstance(this);

        if (usersController.getCurrentUser() == null) {
            startActivity(new Intent(LoginActivity.this, PreferencesActivity.class));
        } else if (usersController.getCurrentUser().getInterests().isEmpty()) {
            startActivity(new Intent(LoginActivity.this, InterestsActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, ContactsActivity.class));
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
