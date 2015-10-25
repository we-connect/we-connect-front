package berlin.weconnect.weconnect.view.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.GetUsersTask;
import berlin.weconnect.weconnect.view.adapters.InterestsAdapter;

public class WelcomeActivity extends AppCompatActivity {
    private InterestsController interestsController;
    private UsersController usersController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        interestsController = InterestsController.getInstance(this);
        usersController = UsersController.getInstance(this);

        // Check if user already exists in database
        try {
            List<User> users = new GetUsersTask().execute().get();
            if (users != null) {
                Resources res = WelcomeActivity.this.getResources();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
                String myUsername = prefs.getString(res.getString(R.string.pref_fb_username), null);
                String myFacebookId = prefs.getString(res.getString(R.string.pref_fb_facebookid), null);

                usersController.setUsers(users);
                usersController.setMe(usersController.getUserByFacebookId(myFacebookId));
                User me = usersController.getMe();

                Toast.makeText(this, "I am  " + me.getUsername(), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(this, "InterruptedException", Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "ExecutionException", Toast.LENGTH_LONG).show();
        }
    }

    public void onResume() {
        super.onResume();

        // Load layout
        TextView tvName = (TextView) findViewById(R.id.tvName);
        ListView lvInterests = (ListView) findViewById(R.id.lvInterests);
        Button btnContinue = (Button) findViewById(R.id.btnContinue);

        final InterestsAdapter interestsAdapter = new InterestsAdapter(this, this, R.layout.interest, interestsController.getInterests());
        lvInterests.setAdapter(interestsAdapter);

        // Add actions
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                for (Interest i : interestsAdapter.getFilteredItems()) {
                    if (i.isSelected())
                        sb.append(i.getName() + " ");
                }

                Toast.makeText(WelcomeActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
