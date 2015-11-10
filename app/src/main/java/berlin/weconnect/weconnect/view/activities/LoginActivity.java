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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.EGender;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;

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

        LoginButton btnLogin = (LoginButton) findViewById(R.id.btnLogin);

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
            Uri profilePictureUri = profile.getProfilePictureUri(100, 100);

            Resources res = LoginActivity.this.getResources();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(res.getString(R.string.pref_fb_logged_in), true);
            editor.putString(res.getString(R.string.pref_fb_facebook_id), id);
            editor.putString(res.getString(R.string.pref_fb_username), name);
            editor.putString(res.getString(R.string.pref_fb_firstname), firstName);
            editor.putString(res.getString(R.string.pref_fb_lastname), lastName);
            editor.putString(res.getString(R.string.pref_fb_email), id + "@weconnect.berlin");
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
        String facebookId = prefs.getString(res.getString(R.string.pref_fb_facebook_id), "");

        UsersController usersController = UsersController.getInstance();
        User user;

        // Check if user already exists
        if (usersController.getUserByFacebookId(facebookId) != null) {
            user = usersController.getUserByFacebookId(facebookId);
        } else {
            user = new User();
            user.setFacebook_id(prefs.getString(res.getString(R.string.pref_fb_facebook_id), ""));
            user.setUsername(prefs.getString(res.getString(R.string.pref_fb_username), ""));
            user.setFirst_name(prefs.getString(res.getString(R.string.pref_fb_firstname), ""));
            user.setLast_name(prefs.getString(res.getString(R.string.pref_fb_lastname), ""));
            user.setEmail(prefs.getString(res.getString(R.string.pref_fb_email), ""));
            user.setPassword("password");
            user.setEnabled(true);
            usersController.callPostUser(user);
        }

        // Set current user
        usersController.setCurrentUser(user);

        // Show welcome message
        String username = prefs.getString(res.getString(R.string.pref_fb_username), "");
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.logged_in_as) + " " + username, Toast.LENGTH_LONG).show();

        // Check if user already has defined gender preferences
        EGender gender = usersController.getCurrentUser().getGender();

        // Check if user already has defined preferred interests
        List<Interest> interests = usersController.getCurrentUser().getInterests();

        if (gender == null) {
            startActivity(new Intent(LoginActivity.this, GenderActivity.class));
        } else if (interests != null && interests.isEmpty()) {
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
