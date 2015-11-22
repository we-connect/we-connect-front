package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import berlin.weconnect.weconnect.model.entities.EFacebookPictureType;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.util.ConnectionUtil;
import berlin.weconnect.weconnect.model.webservices.FacebookGetProfilePictureTask;
import berlin.weconnect.weconnect.view.activities.BaseActivity;
import berlin.weconnect.weconnect.view.dialogs.ContactDialog;

public class ContactsAdapter extends ArrayAdapter<User> implements Filterable {
    private Activity activity;

    private UsersController usersController;

    // Filter
    @NonNull
    private List<User> filteredItems = new ArrayList<>();
    private List<User> originalItems = new ArrayList<>();
    private UserFilter userFilter;
    private final Object lock = new Object();

    // --------------------
    // Constructors
    // --------------------

    public ContactsAdapter(Activity activity, int resource, @NonNull List<User> items) {
        super(activity, resource, items);
        this.activity = activity;
        this.filteredItems = items;
        this.originalItems = items;

        usersController = UsersController.getInstance();

        filter();
    }

    // --------------------
    // Methods
    // --------------------

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public User getItem(int position) {
        return filteredItems.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final User user = getItem(position);
        return getView(user, parent);
    }

    @NonNull
    private View getView(@NonNull final User user, final ViewGroup parent) {
        // Layout inflater
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());

        Resources res = activity.getResources();
        user.updateInterests();

        // Load views
        final LinearLayout llUser = (LinearLayout) vi.inflate(R.layout.list_item_contact, parent, false);
        final ImageView ivProfilePicture = (ImageView) llUser.findViewById(R.id.ivProfilePicture);
        final TextView tvName = (TextView) llUser.findViewById(R.id.tvName);
        final TextView tvSharedInterests = (TextView) llUser.findViewById(R.id.tvSharedInterests);

        // Set values
        if (user.getFacebookId() != null) {
            Bitmap bmp = null;
            try {
                bmp = new FacebookGetProfilePictureTask().execute(user.getFacebookId(), EFacebookPictureType.NORMAL.getValue()).get();
            } catch (@NonNull InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (bmp != null)
                ivProfilePicture.setImageBitmap(bmp);
        }

        if (user.getFirstName() != null)
            tvName.setText(user.getFirstName());

        List<Interest> sharedInterests = user.getSharedInterestsWith(usersController.getCurrentUser());
        tvSharedInterests.setText(String.format(res.getQuantityString(R.plurals.shared_interests, sharedInterests.size()), sharedInterests.size()));

        // Add actions
        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) activity).testInternetConnection();

                if (ConnectionUtil.isNetworkAvailable()) {
                    ContactDialog dialog = new ContactDialog();
                    Resources res = activity.getResources();
                    Bundle bundle = new Bundle();
                    bundle.putString(res.getString(R.string.bundle_dialog_title), res.getString(R.string.contact));
                    bundle.putString(res.getString(R.string.bundle_contact_facebook_id), user.getFacebookId());
                    dialog.setArguments(bundle);
                    dialog.show(activity.getFragmentManager(), ContactDialog.TAG);
                }
            }
        });

        return llUser;
    }

    // --------------------
    // Methods - Filter
    // --------------------

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
        return usersController.isVisible(user);
    }

    // --------------------
    // Inner classes
    // --------------------

    public class UserFilter extends Filter {
        @NonNull
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
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            filteredItems = (List<User>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}