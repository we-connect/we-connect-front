package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.util.MailUtil;
import berlin.weconnect.weconnect.view.adapters.InterestCategoriesSelectionAdapter;

public class InterestsActivity extends SwipeRefreshBaseActivity implements View.OnClickListener {
    // View
    private ListView lvInterestCategories;
    private LinearLayout llQuestion;
    private TextView tvQuestion;
    private Button btnContinue;
    private LinearLayout llSpace;

    // Controller
    private InterestsController interestsController;
    private UsersController usersController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        interestsController = InterestsController.getInstance(this);
        usersController = UsersController.getInstance(this);

        // Skip if there is already a user
        if (usersController.getCurrentUser() != null && !usersController.getCurrentUser().getInterests().isEmpty()) {
            startActivity(new Intent(InterestsActivity.this, ContactsActivity.class));
            finish();
        }
    }

    public void onResume() {
        super.onResume();

        // Load layout
        lvInterestCategories = (ListView) findViewById(R.id.lvInterestCategories);
        llQuestion = (LinearLayout) getLayoutInflater().inflate(R.layout.ll_question, null);
        tvQuestion = (TextView) llQuestion.findViewById(R.id.tvQuestion);
        btnContinue = (Button) getLayoutInflater().inflate(R.layout.btn_continue, null);
        llSpace = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_space, null);

        // Set values
        tvQuestion.setText(R.string.what_are_you_interested_in);
        btnContinue.setText(R.string.continue_);

        if (lvInterestCategories.getHeaderViewsCount() < 1) {
            lvInterestCategories.addHeaderView(llSpace);
            lvInterestCategories.addHeaderView(llQuestion);
        }
        if (lvInterestCategories.getFooterViewsCount() < 1) {
            lvInterestCategories.addFooterView(btnContinue);
            lvInterestCategories.addFooterView(llSpace);
        }

        final InterestCategoriesSelectionAdapter interestCategoriesSelectionAdapter = new InterestCategoriesSelectionAdapter(this, R.layout.list_item_interest_category_selection, interestsController.getInterestCategories());
        lvInterestCategories.setAdapter(interestCategoriesSelectionAdapter);

        // Add actions
        btnContinue.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_interests;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_interests, menu);
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
                Intent i = new Intent(InterestsActivity.this, AboutActivity.class);
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
        if (v == btnContinue) {
            Intent i = new Intent(InterestsActivity.this, ContactsActivity.class);
            startActivity(i);
            finish();
        }
    }
}
