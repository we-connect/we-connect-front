package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import berlin.weconnect.weconnect.R;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onResume() {
        super.onResume();

        // Load layout
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        LoginButton loginButton = (LoginButton) findViewById(R.
                id.login_button);
        loginButton.setReadPermissions("public_profile");

        // TODO : remove
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = LoginActivity.this.getResources();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(res.getString(R.string.pref_fb_app_id), "");
                editor.putString(res.getString(R.string.pref_fb_facebook_id), "");
                editor.putString(res.getString(R.string.pref_fb_username), "John Doe");
                editor.putString(res.getString(R.string.pref_fb_firstname), "John");
                editor.putString(res.getString(R.string.pref_fb_lastname), "Doe");
                editor.putString(res.getString(R.string.pref_fb_profile_uri), "");
                editor.putString(res.getString(R.string.pref_fb_profile_picture_uri), "");
                editor.apply();

                Intent openStartingPoint = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(openStartingPoint);
                finish();
            }
        });

        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        // Add callback listener
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userAppId = loginResult.getAccessToken().getUserId();
                String id = Profile.getCurrentProfile().getId();
                String name = Profile.getCurrentProfile().getName();
                String firstName = Profile.getCurrentProfile().getFirstName();
                String lastName = Profile.getCurrentProfile().getLastName();
                Uri profileUri = Profile.getCurrentProfile().getLinkUri();
                Uri profilePictureUri = Profile.getCurrentProfile().getProfilePictureUri(50, 50);

                Resources res = LoginActivity.this.getResources();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString(res.getString(R.string.pref_fb_app_id), userAppId);
                editor.putString(res.getString(R.string.pref_fb_facebook_id), id);
                editor.putString(res.getString(R.string.pref_fb_username), name);
                editor.putString(res.getString(R.string.pref_fb_firstname), firstName);
                editor.putString(res.getString(R.string.pref_fb_lastname), lastName);
                editor.putString(res.getString(R.string.pref_fb_profile_uri), profileUri.getPath());
                editor.putString(res.getString(R.string.pref_fb_profile_picture_uri), profilePictureUri.getPath());
                editor.apply();

                Toast.makeText(LoginActivity.this, getResources().getString(R.string.logged_in_as) + name, Toast.LENGTH_LONG).show();

                Toast.makeText(LoginActivity.this, R.string.login_succeeded, Toast.LENGTH_LONG).show();
                Intent openStartingPoint = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(openStartingPoint);
                finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
