package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.ContactsController;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.ProfileController;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.view.adapters.InterestsAdapter;

public class WelcomeActivity extends AppCompatActivity {
    private ContactsController contactsController;
    private InterestsController interestsController;
    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        contactsController = ContactsController.getInstance(this);
        interestsController = InterestsController.getInstance(this);
        profileController = ProfileController.getInstance(this);

        contactsController.updateUsers();
        profileController.updateMyUser();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        TextView tvName = (TextView) findViewById(R.id.tvName);
        ListView lvInterests = (ListView) findViewById(R.id.lvInterests);
        Button btnContinue = (Button) findViewById(R.id.btnContinue);

        // Set values
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        tvName.setText(prefs.getString(getResources().getString(R.string.pref_fb_username), ""));

        final InterestsAdapter interestsAdapter = new InterestsAdapter(this, this, R.layout.interest, interestsController.getInterests());
        lvInterests.setAdapter(interestsAdapter);

        // Add actions
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Interest> interests = new ArrayList<>();
                for (Interest i : interestsAdapter.getFilteredItems()) {
                    interests.add(i);
                }

                profileController.getMyUser().setInterests(interests);
                profileController.writeInterests();

                Intent i = new Intent(WelcomeActivity.this, ContactsActivity.class);
                startActivity(i);
            }
        });
    }
}
