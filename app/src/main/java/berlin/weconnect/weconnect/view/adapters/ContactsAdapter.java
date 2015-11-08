package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.controller.WebController;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.FacebookGetProfilePictureTask;
import berlin.weconnect.weconnect.view.activities.WebActivity;

public class ContactsAdapter extends ArrayAdapter<User> implements Filterable {
    private Activity activity;

    private UsersController usersController;
    private WebController webController;

    // Filter
    private List<User> filteredItems = new ArrayList<>();
    private List<User> originalItems = new ArrayList<>();
    private UserFilter userFilter;
    private final Object lock = new Object();

    // --------------------
    // Constructors
    // --------------------

    public ContactsAdapter(Activity activity, int resource, List<User> items) {
        super(activity, resource, items);
        this.activity = activity;
        this.filteredItems = items;
        this.originalItems = items;

        usersController = UsersController.getInstance();
        webController = WebController.getInstance();

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
        final ImageView ivProfile = (ImageView) llUser.findViewById(R.id.ivProfilePicture);
        final TextView tvName = (TextView) llUser.findViewById(R.id.tvName);

        // Set values
        if (user.getFacebook_id() != null) {
            Bitmap bmp = null;
            try {
                bmp = new FacebookGetProfilePictureTask().execute(user.getFacebook_id()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (bmp != null)
                ivProfile.setImageBitmap(bmp);
        }

        if (user.getFirst_name() != null)
            tvName.setText(user.getFirst_name());

        // Add actions
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : go to details page instead
                webController.setUrl(user.getFacebookUrl());

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
    protected boolean filterContact(User user) {
        return true; /*usersController.isVisible(user);*/
    }

    // --------------------
    // Inner classes
    // --------------------

    public class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            // Copy items
            originalItems = usersController.getSuggestedUsers();

            ArrayList<User> values;
            synchronized (lock) {
                values = new ArrayList<>(originalItems);
            }

            final int count = values.size();
            final ArrayList<User> newValues = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                final User value = values.get(i);
                if (filterContact(value)) {
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