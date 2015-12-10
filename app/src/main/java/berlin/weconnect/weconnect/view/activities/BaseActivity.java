package berlin.weconnect.weconnect.view.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.util.ConnectionUtil;
import berlin.weconnect.weconnect.view.dialogs.NoInternetDialog;

public abstract class BaseActivity extends AppCompatActivity implements NoInternetDialog.OnCompleteListener {
    // --------------------
    // Methods - Lifecycle
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        // Load views
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void onResume() {
        super.onResume();

        testInternetConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Opens a dialog if there is not internet connection
     */
    public void testInternetConnection() {
        if (!ConnectionUtil.isNetworkAvailable()) {
            final Resources res = getResources();
            Bundle b = new Bundle();
            b.putString(res.getString(R.string.bundle_dialog_title), res.getString(R.string.no_internet_connection));

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.setArguments(b);
            noInternetDialog.setCancelable(false);
            noInternetDialog.show(getFragmentManager(), NoInternetDialog.TAG);
        }
    }

    // --------------------
    // Getters / Setters
    // --------------------

    protected abstract int getLayoutResource();

    @NonNull
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    protected void setDisplayHomeAsUpEnabled(boolean enabled) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public void onRetry() {
        testInternetConnection();
    }

    @Override
    public void onCloseApp() {
        finish();
        System.exit(0);
    }
}
