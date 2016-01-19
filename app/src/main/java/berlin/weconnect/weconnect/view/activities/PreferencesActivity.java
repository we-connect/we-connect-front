package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.entities.EGender;
import berlin.weconnect.weconnect.model.entities.EMeetingPref;
import berlin.weconnect.weconnect.model.entities.EType;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.util.MailUtil;

public class PreferencesActivity extends BaseActivity implements View.OnClickListener {
    // Model
    private EType type;
    private EGender gender;
    private EMeetingPref meetingPref;
    private boolean btnContinueActivated;

    // View
    private TextView tvName;
    private LinearLayout llNewcomer;
    private CheckBox cbNewcomer;
    private LinearLayout llLocal;
    private CheckBox cbLocal;
    private LinearLayout llMale;
    private CheckBox cbMale;
    private LinearLayout llFemale;
    private CheckBox cbFemale;
    private LinearLayout llOnlySameGender;
    private CheckBox cbOnlySameGender;
    private LinearLayout llEverybody;
    private CheckBox cbEverybody;
    private Button btnContinue;

    // Controller
    private UsersController usersController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        usersController = UsersController.getInstance(this);

        // Skip if there is already a user
        if (usersController.getCurrentUser() != null) {
            startActivity(new Intent(PreferencesActivity.this, InterestsActivity.class));
            finish();
        }
    }

    public void onResume() {
        super.onResume();

        // Load layout
        tvName = (TextView) findViewById(R.id.tvName);
        llNewcomer = (LinearLayout) findViewById(R.id.llNewcomer);
        cbNewcomer = (CheckBox) findViewById(R.id.cbNewcomer);
        llLocal = (LinearLayout) findViewById(R.id.llLocal);
        cbLocal = (CheckBox) findViewById(R.id.cbLocal);
        llMale = (LinearLayout) findViewById(R.id.llMale);
        cbMale = (CheckBox) findViewById(R.id.cbMale);
        llFemale = (LinearLayout) findViewById(R.id.llFemale);
        cbFemale = (CheckBox) findViewById(R.id.cbFemale);
        llOnlySameGender = (LinearLayout) findViewById(R.id.llOnlySameGender);
        cbOnlySameGender = (CheckBox) findViewById(R.id.cbOnlySameGender);
        llEverybody = (LinearLayout) findViewById(R.id.llEverybody);
        cbEverybody = (CheckBox) findViewById(R.id.cbEverybody);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        // Set values
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tvName.setText(prefs.getString(getResources().getString(R.string.pref_fb_username), ""));

        cbNewcomer.setChecked(true);
        selectType(EType.NEWCOMER);

        // Add actions
        cbNewcomer.setOnClickListener(this);
        llNewcomer.setOnClickListener(this);
        cbLocal.setOnClickListener(this);
        llLocal.setOnClickListener(this);
        cbFemale.setOnClickListener(this);
        llFemale.setOnClickListener(this);
        cbMale.setOnClickListener(this);
        llMale.setOnClickListener(this);
        cbOnlySameGender.setOnClickListener(this);
        llOnlySameGender.setOnClickListener(this);
        cbEverybody.setOnClickListener(this);
        llEverybody.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_preferences;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gender, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_visit_us: {
                WebController webController = WebController.getInstance();
                webController.goToFacebookPage(this, getResources().getString(R.string.facebook_page_weconnect));
                break;
            }
            case R.id.menu_feedback: {
                MailUtil.sendFeedback(this);
                break;
            }
            case R.id.menu_about: {
                Intent i = new Intent(PreferencesActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            }
            case R.id.menu_logout: {
                FacebookController.getInstance(this).logout();
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == cbNewcomer) {
            selectType(EType.NEWCOMER);
        } else if (v == llNewcomer) {
            cbNewcomer.toggle();
            selectType(EType.NEWCOMER);
        } else if (v == cbLocal) {
            selectType(EType.LOCAL);
        } else if (v == llLocal) {
            cbLocal.toggle();
            selectType(EType.LOCAL);
        } else if (v == cbFemale) {
            selectGender(EGender.FEMALE);
        } else if (v == llFemale) {
            cbFemale.toggle();
            selectGender(EGender.FEMALE);
        } else if (v == cbMale) {
            selectGender(EGender.MALE);
        } else if (v == llMale) {
            cbMale.toggle();
            selectGender(EGender.MALE);
        } else if (v == cbOnlySameGender) {
            selectMeetingPreference(EMeetingPref.ONLY_OWN_GENDER);
        } else if (v == llOnlySameGender) {
            cbOnlySameGender.toggle();
            selectMeetingPreference(EMeetingPref.ONLY_OWN_GENDER);
        } else if (v == cbEverybody) {
            selectMeetingPreference(EMeetingPref.EVERYBODY);
        } else if (v == llEverybody) {
            cbEverybody.toggle();
            selectMeetingPreference(EMeetingPref.EVERYBODY);
        } else if (v == btnContinue) {
            if (btnContinueActivated) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                Resources res = getResources();

                User user = new User();
                user.setFacebookId(prefs.getString(res.getString(R.string.pref_fb_facebook_id), ""));
                user.setUsername(prefs.getString(res.getString(R.string.pref_fb_username), ""));
                user.setFirstName(prefs.getString(res.getString(R.string.pref_fb_firstname), ""));
                user.setLastName(prefs.getString(res.getString(R.string.pref_fb_lastname), ""));
                user.setEmail(prefs.getString(res.getString(R.string.pref_fb_email), ""));
                user.setPassword(res.getString(R.string.password));
                user.setType(type.getValue());
                user.setGender(gender.getValue());

                switch (meetingPref) {
                    case ONLY_OWN_GENDER: {
                        user.setMeetingPref(gender.getValue());
                        break;
                    }
                    case EVERYBODY: {
                        user.setMeetingPref(getResources().getString(R.string.both));
                        break;
                    }
                }

                usersController.post(user);
                usersController.get();

                if (usersController.getCurrentUser() != null) {
                    Intent i = new Intent(PreferencesActivity.this, InterestsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    /**
     * Sets the type
     *
     * @param t type
     */
    private void selectType(EType t) {
        switch (t) {
            case NEWCOMER: {
                type = EType.NEWCOMER;
                cbLocal.setChecked(!cbNewcomer.isChecked());
                break;
            }
            case LOCAL: {
                type = EType.LOCAL;
                cbNewcomer.setChecked(!cbLocal.isChecked());
                break;
            }
        }

        updateContinueButton();
    }

    /**
     * Sets the gender
     *
     * @param g gender
     */
    private void selectGender(EGender g) {
        switch (g) {
            case MALE: {
                gender = EGender.MALE;
                cbFemale.setChecked(!cbMale.isChecked());
                break;
            }
            case FEMALE: {
                gender = EGender.FEMALE;
                cbMale.setChecked(!cbFemale.isChecked());
                break;
            }
        }

        updateContinueButton();
    }

    /**
     * Sets the meeting preference
     *
     * @param p meeting pref
     */
    private void selectMeetingPreference(EMeetingPref p) {
        switch (p) {
            case ONLY_OWN_GENDER: {
                meetingPref = EMeetingPref.ONLY_OWN_GENDER;
                cbEverybody.setChecked(!cbOnlySameGender.isChecked());
                break;
            }
            case EVERYBODY: {
                meetingPref = EMeetingPref.EVERYBODY;
                cbOnlySameGender.setChecked(!cbEverybody.isChecked());
                break;
            }
        }

        updateContinueButton();
    }

    /**
     * Updates the status of the continue button
     */
    private void updateContinueButton() {
        btnContinueActivated = ((cbNewcomer.isChecked() || cbLocal.isChecked()) && (cbMale.isChecked() || cbFemale.isChecked()) && (cbOnlySameGender.isChecked() || cbEverybody.isChecked()));

        if (btnContinueActivated) {
            btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.button_active));
        } else {
            btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.button_inactive));
        }
    }
}
