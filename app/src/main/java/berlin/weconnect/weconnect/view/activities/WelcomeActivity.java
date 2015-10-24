package berlin.weconnect.weconnect.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.model.Interest;
import berlin.weconnect.weconnect.view.adapters.InterestsAdapter;

public class WelcomeActivity extends AppCompatActivity {
    private InterestsController interestsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        interestsController = InterestsController.getInstance(this);
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
