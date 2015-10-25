package berlin.weconnect.weconnect.view.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.ProfileController;
import berlin.weconnect.weconnect.controller.ContactsController;

public class SettingsActivity extends BaseActivity {
    private ContactsController contactsController;
    private InterestsController interestsController;
    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactsController = ContactsController.getInstance(this);
        interestsController = InterestsController.getInstance(this);
        profileController = ProfileController.getInstance(this);

        contactsController.updateUsers();
        profileController.updateMyUser();

        setDisplayHomeAsUpEnabled(true);
    }

    public void onResume() {
        super.onResume();

        // Load layout
        ListView lvInterests = (ListView) findViewById(R.id.lvInterests);
        Button btnDone = (Button) findViewById(R.id.btnDone);

        final InterestsAdapter interestsAdapter = new InterestsAdapter(this, this, R.layout.interest, interestsController.getInterests());
        lvInterests.setAdapter(interestsAdapter);

        // Add actions
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Interest> interests = new ArrayList<>();
                for (Interest i : interestsAdapter.getFilteredItems()) {
                    interests.add(i);
                }

                profileController.getMyUser().setInterests(interests);
                profileController.writeInterests();
                finish();
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

        // return true;
    }
}
