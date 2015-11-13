package berlin.weconnect.weconnect.view.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.entities.EMeetingPref;
import berlin.weconnect.weconnect.model.entities.EType;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.util.ListUtil;
import berlin.weconnect.weconnect.model.util.MailUtil;
import berlin.weconnect.weconnect.view.adapters.InterestsAdapter;

public class SettingsActivity extends BaseActivity {
    private EType type;
    private EMeetingPref meetingPref;

    private UsersController userController;
    private InterestsController interestsController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        userController = UsersController.getInstance();
        interestsController = InterestsController.getInstance();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final LinearLayout llNewcomer = (LinearLayout) findViewById(R.id.llNewcomer);
        final CheckBox cbNewcomer = (CheckBox) findViewById(R.id.cbNewcomer);
        final LinearLayout llLocal = (LinearLayout) findViewById(R.id.llLocal);
        final CheckBox cbLocal = (CheckBox) findViewById(R.id.cbLocal);
        final LinearLayout llMale = (LinearLayout) findViewById(R.id.llMale);
        final LinearLayout llFemale = (LinearLayout) findViewById(R.id.llFemale);
        final LinearLayout llOnlySameGender = (LinearLayout) findViewById(R.id.llOnlySameGender);
        final CheckBox cbOnlySameGender = (CheckBox) findViewById(R.id.cbOnlySameGender);
        final LinearLayout llEverybody = (LinearLayout) findViewById(R.id.llEverybody);
        final CheckBox cbEverybody = (CheckBox) findViewById(R.id.cbEverybody);
        final ListView lvInterests = (ListView) findViewById(R.id.lvInterests);
        final Button btnDone = (Button) findViewById(R.id.btnDone);

        // Set values
        User user = userController.getCurrentUser();
        cbNewcomer.setChecked(user.getType().equals(EType.NEWCOMER.getValue()));
        cbLocal.setChecked(user.getType().equals(EType.LOCAL.getValue()));
        cbOnlySameGender.setChecked(user.getMeetingPref().equals(user.getGender()));
        cbEverybody.setChecked(user.getMeetingPref().equals(EMeetingPref.BOTH.getValue()));
        final InterestsAdapter interestsAdapter = new InterestsAdapter(this, R.layout.interest, interestsController.getInterests());
        lvInterests.setAdapter(interestsAdapter);
        ListUtil.setListViewHeightBasedOnChildren(this, lvInterests);

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

        llMale.setVisibility(View.GONE);
        llFemale.setVisibility(View.GONE);

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

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            case android.R.id.home: {
                finish();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

        return true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
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

        updateType(type);
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

        updateMeetingPref(meetingPref);
    }

    private void updateType(EType type) {
        userController.getCurrentUser().setType(type.getValue());
    }

    private void updateMeetingPref(EMeetingPref meetingPref) {
        User user = userController.getCurrentUser();

        switch (meetingPref) {
            case ONLY_OWN_GENDER: {
                user.setMeetingPref(user.getGender());
                break;
            }
            case EVERYBODY: {
                user.setMeetingPref("both");
                break;
            }
        }
    }
}
