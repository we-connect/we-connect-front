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
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.util.MailUtil;
import berlin.weconnect.weconnect.view.adapters.InterestCategoriesSelectionAdapter;

public class InterestsActivity extends SwipeRefreshBaseActivity {
    private InterestsController interestsController;

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        interestsController = InterestsController.getInstance();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final LinearLayout toolbar_wrapper = (LinearLayout) findViewById(R.id.toolbar_wrapper);
        final ListView lvInterestCategories = (ListView) findViewById(R.id.lvInterestCategories);
        final TextView tvQuestion = (TextView) getLayoutInflater().inflate(R.layout.tv_question, null);
        final Button btnContinue = (Button) getLayoutInflater().inflate(R.layout.btn_continue, null);

        // Set values
        tvQuestion.setText(R.string.what_are_you_interested_in);
        btnContinue.setText(R.string.continue_);

        if (lvInterestCategories.getHeaderViewsCount() < 1)
            lvInterestCategories.addHeaderView(tvQuestion);
        if (lvInterestCategories.getFooterViewsCount() < 1)
            lvInterestCategories.addFooterView(btnContinue);

        final InterestCategoriesSelectionAdapter interestCategoriesSelectionAdapter = new InterestCategoriesSelectionAdapter(this, R.layout.list_item_interest_category_selection, interestsController.getInterestCategories());
        lvInterestCategories.setAdapter(interestCategoriesSelectionAdapter);

        // Add actions
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InterestsActivity.this, ContactsActivity.class);
                startActivity(i);
                finish();
            }
        });
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
}
