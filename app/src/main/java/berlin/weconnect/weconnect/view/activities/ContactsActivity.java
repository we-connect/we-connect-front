package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.FacebookController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.view.adapters.ContactsAdapter;

public class ContactsActivity extends SwipeRefreshBaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private UsersController usersController;
    private ContactsAdapter contactsAdapter;

    // Properties
    private static int REFRESH_DELAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(false);

        REFRESH_DELAY = getResources().getInteger(R.integer.refresh_delay);

        usersController = UsersController.getInstance();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        final ListView lvContacts = (ListView) findViewById(R.id.lvContacts);
        final LinearLayout toolbarWrapper = (LinearLayout) findViewById(R.id.toolbar_wrapper);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        usersController.callGetSuggestedUsers(usersController.getCurrentUser());

        contactsAdapter = new ContactsAdapter(this, R.layout.contact, usersController.getSuggestedUsers());
        lvContacts.setAdapter(contactsAdapter);

        srl.setEnabled(false);
        srl.setOnRefreshListener(this);
        srl.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        updateSwipeRefreshProgressBarTop(srl);
        registerHideableHeaderView(toolbarWrapper);
        enableActionBarAutoHide(lvContacts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                Intent i = new Intent(ContactsActivity.this, SettingsActivity.class);
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
    protected int getLayoutResource() {
        return R.layout.activity_contacts;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new ReloadContactsTask().execute();
            }
        }, REFRESH_DELAY);
    }

    /**
     * Updates the list view
     */
    private void updateListView() {
        contactsAdapter.filter();
    }

    // --------------------
    // Inner classes
    // --------------------

    public class ReloadContactsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {
            // Load suggested contacts
            usersController.callGetSuggestedUsers(usersController.getCurrentUser());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

            srl.setRefreshing(false);
            Snackbar.make(srl, R.string.reloaded_contacts, Snackbar.LENGTH_LONG).show();
            updateListView();
        }
    }
}
