package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.ContactsController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.view.activities.WebActivity;

public class ContactsAdapter extends ArrayAdapter<User> implements Filterable {
    private ContactsController contactsController;
    private WebController webController;

    // Filter
    private List<User> filteredItems = new ArrayList<>();
    private List<User> originalItems = new ArrayList<>();
    private UserFilter userFilter;
    private final Object lock = new Object();

    private Activity activity;

    // --------------------
    // Constructors
    // --------------------

    public ContactsAdapter(Context context, Activity activity, int resource, List<User> items) {
        super(context, resource, items);

        this.filteredItems = items;
        this.originalItems = items;

        this.activity = activity;

        contactsController = ContactsController.getInstance(activity);
        webController = WebController.getInstance(activity);

        filter();
    }

    // --------------------
    // Methods
    // --------------------

    @Override
    public int getCount() {
        return filteredItems != null ? filteredItems.size() : 0;
    }

    @Override
    public User getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final User user = getItem(position);
        return getView(position, user, parent);
    }

    private View getView(final int position, final User user, final ViewGroup parent) {
        // Layout inflater
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());

        // Load views
        final LinearLayout llUser = (LinearLayout) vi.inflate(R.layout.contact, parent, false);
        final TextView tvName = (TextView) llUser.findViewById(R.id.tvName);

        // Set values
        tvName.setText(user.getFirst_name());


        // Add actions
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webController.setUrl("https://facebook.com/");
                // webController.setUrl("https://facebook.com/" + user.getFirst_name() + "." + user.getLast_name());

                Intent openStartingPoint = new Intent(activity, WebActivity.class);
                activity.startActivity(openStartingPoint);
            }
        });

        return llUser;
    }

    // --------------------
    // Methods - Filter
    // --------------------

    public List<User> getFilteredItems() {
        return filteredItems;
    }

    public void filter() {
        getFilter().filter("");
    }

    @Override
    public Filter getFilter() {
        if (userFilter == null) {
            userFilter = new UserFilter();
        }
        return userFilter;
    }

    /**
     * Determines if a user shall be displayed
     *
     * @param user user
     * @return true if item is visible
     */
    protected boolean filterInterest(User user) {
        return contactsController.isVisible(user);
    }

    // --------------------
    // Inner classes
    // --------------------

    public class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            // Copy items
            originalItems = contactsController.getUsers();

            ArrayList<User> values;
            synchronized (lock) {
                values = new ArrayList<>(originalItems);
            }

            final int count = values.size();
            final ArrayList<User> newValues = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                final User value = values.get(i);
                if (filterInterest(value)) {
                    newValues.add(value);
                }
            }

            results.values = newValues;
            results.count = newValues.size();

            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (List<User>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}