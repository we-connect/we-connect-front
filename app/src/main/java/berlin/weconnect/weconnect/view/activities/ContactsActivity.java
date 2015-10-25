package berlin.weconnect.weconnect.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.ProfileController;
import berlin.weconnect.weconnect.controller.ContactsController;
import berlin.weconnect.weconnect.view.adapters.ContactsAdapter;

public class ContactsActivity extends AppCompatActivity {
    private ContactsController contactsController;
    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsController = ContactsController.getInstance(this);
        profileController = ProfileController.getInstance(this);

        profileController.readSuggestedContacts();
    }

    public void onResume() {
        super.onResume();

        // Load layout
        ListView lvContacts = (ListView) findViewById(R.id.lvContacts);

        final ContactsAdapter contactsAdapter = new ContactsAdapter(this, this, R.layout.contact, profileController.getSuggestedContacts());
        lvContacts.setAdapter(contactsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                Intent i = new Intent(ContactsActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

        return true;
    }
}
