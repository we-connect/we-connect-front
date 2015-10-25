package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.Profile;
import java.util.Arrays;
import berlin.weconnect.weconnect.R;
import android.net.Uri;

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
                editor.putString(res.getString(R.string.pref_fb_facebookid), "42");
                editor.putString(res.getString(R.string.pref_fb_username), "florian");
                editor.putString(res.getString(R.string.pref_fb_email), "flo@bar.de");
                editor.apply();

                Intent openStartingPoint = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(openStartingPoint);
                finish();
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        // Add callback listener
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // TODO : save profile info to prefs
                 String userAppId = loginResult.getAccessToken().getUserId();
                 String Id = Profile.getCurrentProfile().getId();
                 String Name = Profile.getCurrentProfile().getName();
                String First = Profile.getCurrentProfile().getFirstName();
                 String Last = Profile.getCurrentProfile().getLastName();
                Uri userLink = Profile.getCurrentProfile().getLinkUri();
                Uri userBildLink = Profile.getCurrentProfile().getProfilePictureUri(50, 50);

             //   Resources res = LoginActivity.this.getResources();
             //   SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
             //   SharedPreferences.Editor editor = prefs.edit();

             //   editor.putString(res.getString(R.string.pref_fb_Appid), loginResult.getAccessToken().getUserId());
             //   editor.putString(res.getString(R.string.pref_fb_facebookid), Profile.getCurrentProfile().getId());
             //   editor.putString(res.getString(R.string.pref_fb_username), Profile.getCurrentProfile().getName());
             //   editor.putString(res.getString(R.string.pref_fb_firstname), Profile.getCurrentProfile().getFirstName());
             //   editor.putString(res.getString(R.string.pref_fb_lastname), Profile.getCurrentProfile().getLastName());
             //   editor.apply();

                Toast.makeText(LoginActivity.this, "Logged-In to Facebook:" + Name, Toast.LENGTH_LONG).show();




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
