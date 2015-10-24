package weconnect.berlin.weconnect.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import weconnect.berlin.weconnect.R;

public class LoginActivity extends AppCompatActivity {

    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onResume() {
        super.onResume();

        // Load layout
        RelativeLayout rlContainer = (RelativeLayout) findViewById(R.id.container);

        // Add actions
        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }
}
