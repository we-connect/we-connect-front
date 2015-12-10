package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.entities.EMeetingPref;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.util.MailUtil;
import berlin.weconnect.weconnect.model.webservices.PutUserTask;
import berlin.weconnect.weconnect.view.adapters.InterestCategoriesSelectionAdapter;
import berlin.weconnect.weconnect.view.dialogs.DeleteAccountDialog;

public class SettingsActivity extends BaseActivity implements View.OnClickListener, DeleteAccountDialog.OnCompleteListener {
    // Model
    private EMeetingPref meetingPref;

    // Controller
    private UsersController usersController;
    private InterestsController interestsController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        usersController = UsersController.getInstance(this);
        interestsController = InterestsController.getInstance(this);
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final LinearLayout llMeetingPref = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_meeting_pref, null);
        final LinearLayout llOnlySameGender = (LinearLayout) llMeetingPref.findViewById(R.id.llOnlySameGender);
        final CheckBox cbOnlySameGender = (CheckBox) llMeetingPref.findViewById(R.id.cbOnlySameGender);
        final LinearLayout llEverybody = (LinearLayout) llMeetingPref.findViewById(R.id.llEverybody);
        final CheckBox cbEverybody = (CheckBox) llMeetingPref.findViewById(R.id.cbEverybody);
        final LinearLayout llQuestion = (LinearLayout) getLayoutInflater().inflate(R.layout.ll_question, null);
        final TextView tvQuestion = (TextView) llQuestion.findViewById(R.id.tvQuestion);
        final ListView lvInterestCategories = (ListView) findViewById(R.id.lvInterestCategories);
        final Button btnContinue = (Button) getLayoutInflater().inflate(R.layout.btn_continue, null);
        final View vSeparatorLine = getLayoutInflater().inflate(R.layout.separator_line, null);
        final Button btnDeleteMyAccount = (Button) getLayoutInflater().inflate(R.layout.btn_delete_my_account, null);
        final LinearLayout llSpace = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_space, null);

        // Set values
        User user = usersController.getCurrentUser();
        cbOnlySameGender.setChecked(user.getMeetingPref().equals(user.getGender()));
        cbEverybody.setChecked(user.getMeetingPref().equals(EMeetingPref.BOTH.getValue()));
        tvQuestion.setText(R.string.what_are_you_interested_in);
        btnContinue.setText(R.string.done);

        if (lvInterestCategories.getHeaderViewsCount() < 1) {
            lvInterestCategories.addHeaderView(llSpace);
            lvInterestCategories.addHeaderView(llMeetingPref);
            lvInterestCategories.addHeaderView(llQuestion);
        }

        if (lvInterestCategories.getFooterViewsCount() < 1) {
            lvInterestCategories.addFooterView(btnContinue);
            lvInterestCategories.addFooterView(vSeparatorLine);
            lvInterestCategories.addFooterView(btnDeleteMyAccount);
            lvInterestCategories.addFooterView(llSpace);
        }

        final InterestCategoriesSelectionAdapter interestCategoriesSelectionAdapter = new InterestCategoriesSelectionAdapter(this, R.layout.list_item_interest_category_selection, interestsController.getInterestCategories());
        lvInterestCategories.setAdapter(interestCategoriesSelectionAdapter);

        // Add actions
        cbOnlySameGender.setOnClickListener(this);
        llOnlySameGender.setOnClickListener(this);
        cbEverybody.setOnClickListener(this);
        llEverybody.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnDeleteMyAccount.setOnClickListener(this);
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
            case R.id.menu_about: {
                Intent i = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(i);
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

    /**
     * Sets meeting preference
     *
     * @param p meeting pref
     */
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

    /**
     * Updates meeting preference
     *
     * @param meetingPref meeting pref
     */
    private void updateMeetingPref(EMeetingPref meetingPref) {
        User user = usersController.getCurrentUser();

        switch (meetingPref) {
            case ONLY_OWN_GENDER: {
                user.setMeetingPref(user.getGender());
                break;
            }
            case EVERYBODY: {
                user.setMeetingPref(getResources().getString(R.string.both));
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        // Load layout
        final LinearLayout llMeetingPref = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_meeting_pref, null);
        final LinearLayout llOnlySameGender = (LinearLayout) llMeetingPref.findViewById(R.id.llOnlySameGender);
        final CheckBox cbOnlySameGender = (CheckBox) llMeetingPref.findViewById(R.id.cbOnlySameGender);
        final LinearLayout llEverybody = (LinearLayout) llMeetingPref.findViewById(R.id.llEverybody);
        final CheckBox cbEverybody = (CheckBox) llMeetingPref.findViewById(R.id.cbEverybody);
        final Button btnContinue = (Button) getLayoutInflater().inflate(R.layout.btn_continue, null);
        final Button btnDeleteMyAccount = (Button) getLayoutInflater().inflate(R.layout.btn_delete_my_account, null);

        if (v == cbOnlySameGender) {
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
            new PutUserTask().execute(usersController.getCurrentUser());
            finish();
        } else if (v == btnDeleteMyAccount) {
            final Resources res = getResources();
            Bundle b = new Bundle();
            b.putString(res.getString(R.string.bundle_dialog_title), res.getString(R.string.delete_account));
            b.putString(res.getString(R.string.bundle_message), res.getString(R.string.delete_account_question));

            DeleteAccountDialog deleteAccountDialog = new DeleteAccountDialog();
            deleteAccountDialog.setArguments(b);
            deleteAccountDialog.setCancelable(false);
            deleteAccountDialog.show(getFragmentManager(), DeleteAccountDialog.TAG);
        }
    }

    // --------------------
    // Methods - Callbacks
    // --------------------

    @Override
    public void onDeleteAccount() {
        User user = usersController.getCurrentUser();
        usersController.deleteUser(user);
        FacebookController.getInstance(this).logout();
    }
}
