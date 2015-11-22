package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class GenderActivity extends BaseActivity {
    private EType type;
    private EGender gender;
    private EMeetingPref meetingPref;
    private boolean btnContinueActivated;

    private UsersController usersController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        usersController = UsersController.getInstance();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final LinearLayout llNewcomer = (LinearLayout) findViewById(R.id.llNewcomer);
        final CheckBox cbNewcomer = (CheckBox) findViewById(R.id.cbNewcomer);
        final LinearLayout llLocal = (LinearLayout) findViewById(R.id.llLocal);
        final CheckBox cbLocal = (CheckBox) findViewById(R.id.cbLocal);
        final LinearLayout llMale = (LinearLayout) findViewById(R.id.llMale);
        final CheckBox cbMale = (CheckBox) findViewById(R.id.cbMale);
        final LinearLayout llFemale = (LinearLayout) findViewById(R.id.llFemale);
        final CheckBox cbFemale = (CheckBox) findViewById(R.id.cbFemale);
        final LinearLayout llOnlySameGender = (LinearLayout) findViewById(R.id.llOnlySameGender);
        final CheckBox cbOnlySameGender = (CheckBox) findViewById(R.id.cbOnlySameGender);
        final LinearLayout llEverybody = (LinearLayout) findViewById(R.id.llEverybody);
        final CheckBox cbEverybody = (CheckBox) findViewById(R.id.cbEverybody);
        final Button btnContinue = (Button) findViewById(R.id.btnContinue);

        // Set values
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tvName.setText(prefs.getString(getResources().getString(R.string.pref_fb_username), ""));

        cbNewcomer.setChecked(true);
        selectType(EType.NEWCOMER);

        // Add actions
        cbNewcomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(EType.NEWCOMER);
            }
        });
        llNewcomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbNewcomer.toggle();
                selectType(EType.NEWCOMER);
            }
        });

        cbLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(EType.LOCAL);
            }
        });
        llLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbLocal.toggle();
                selectType(EType.LOCAL);
            }
        });

        cbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender(EGender.FEMALE);
            }
        });
        llFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFemale.toggle();
                selectGender(EGender.FEMALE);
            }
        });

        cbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGender(EGender.MALE);
            }
        });
        llMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbMale.toggle();
                selectGender(EGender.MALE);
            }
        });

        cbOnlySameGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMeetingPreference(EMeetingPref.ONLY_OWN_GENDER);
            }
        });
        llOnlySameGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbOnlySameGender.toggle();
                selectMeetingPreference(EMeetingPref.ONLY_OWN_GENDER);
            }
        });

        cbEverybody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMeetingPreference(EMeetingPref.EVERYBODY);
            }
        });
        llEverybody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbEverybody.toggle();
                selectMeetingPreference(EMeetingPref.EVERYBODY);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnContinueActivated) {
                    testInternetConnection();

                    User user = usersController.getCurrentUser();

                    user.setType(type.getValue());
                    user.setGender(gender.getValue());

                    switch (meetingPref) {
                        case ONLY_OWN_GENDER: {
                            user.setMeetingPref(gender.getValue());
                            break;
                        }
                        case EVERYBODY: {
                            user.setMeetingPref("both");
                            break;
                        }
                    }

                    usersController.post(user);
                    usersController.get();
                    usersController.setCurrentUser(usersController.getUserByFacebookId(user.getFacebookId()));

                    Intent i = new Intent(GenderActivity.this, InterestsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        testInternetConnection();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_gender;
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

    private void selectType(EType t) {
        final CheckBox cbNewcomer = (CheckBox) findViewById(R.id.cbNewcomer);
        final CheckBox cbLocal = (CheckBox) findViewById(R.id.cbLocal);

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

    private void selectGender(EGender g) {

        final CheckBox cbMale = (CheckBox) findViewById(R.id.cbMale);
        final CheckBox cbFemale = (CheckBox) findViewById(R.id.cbFemale);

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

    private void selectMeetingPreference(EMeetingPref p) {
        final CheckBox cbOnlySameGender = (CheckBox) findViewById(R.id.cbOnlySameGender);
        final CheckBox cbEverybody = (CheckBox) findViewById(R.id.cbEverybody);

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

    private void updateContinueButton() {
        final CheckBox cbNewcomer = (CheckBox) findViewById(R.id.cbNewcomer);
        final CheckBox cbLocal = (CheckBox) findViewById(R.id.cbLocal);
        final CheckBox cbMale = (CheckBox) findViewById(R.id.cbMale);
        final CheckBox cbFemale = (CheckBox) findViewById(R.id.cbFemale);
        final CheckBox cbOnlySameGender = (CheckBox) findViewById(R.id.cbOnlySameGender);
        final CheckBox cbEverybody = (CheckBox) findViewById(R.id.cbEverybody);

        final Button btnContinue = (Button) findViewById(R.id.btnContinue);

        btnContinueActivated = ((cbNewcomer.isChecked() || cbLocal.isChecked()) && (cbMale.isChecked() || cbFemale.isChecked()) && (cbOnlySameGender.isChecked() || cbEverybody.isChecked()));

        if (btnContinueActivated) {
            btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.button_active));
        } else {
            btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.button_inactive));
        }
    }
}
