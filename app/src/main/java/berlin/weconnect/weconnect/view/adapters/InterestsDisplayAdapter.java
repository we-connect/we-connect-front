package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.model.entities.Interest;

public class InterestsDisplayAdapter extends ArrayAdapter<Interest> implements Filterable {
    private Activity activity;

    private InterestsController interestsController;

    // Filter
    @NonNull
    private List<Interest> filteredItems = new ArrayList<>();
    private List<Interest> originalItems = new ArrayList<>();
    private InterestFilter interestFilter;
    private final Object lock = new Object();

    // --------------------
    // Constructors
    // --------------------

    public InterestsDisplayAdapter(Activity activity, int resource, @NonNull List<Interest> items) {
        super(activity, resource, items);
        this.activity = activity;
        this.filteredItems = items;
        this.originalItems = items;

        interestsController = InterestsController.getInstance();

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
    public Interest getItem(int position) {
        return filteredItems.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final Interest interest = getItem(position);
        return getInterestView(position, interest, parent);
    }

    @NonNull
    private View getInterestView(final int position, @NonNull final Interest interest, final ViewGroup parent) {
        // Layout inflater
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());

        // Load views
        final LinearLayout llInterest = (LinearLayout) vi.inflate(R.layout.list_item_interest_display, parent, false);
        final ImageView ivIcon = (ImageView) llInterest.findViewById(R.id.ivIcon);
        final TextView tvName = (TextView) llInterest.findViewById(R.id.tvName);

        // Set values
        if (interest.getName() != null)
            tvName.setText(interest.getName());
        if (interest.getIcon() != 0)
            ivIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), interest.getIcon()));

        // Set color
        int[] colors = activity.getResources().getIntArray(R.array.interests);
        llInterest.setBackgroundColor(colors[position % colors.length]);

        return llInterest;
    }

    // --------------------
    // Methods - Filter
    // --------------------

    @NonNull
    public List<Interest> getFilteredItems() {
        return filteredItems;
    }

    public void filter() {
        getFilter().filter("");
    }

    @Override
    public Filter getFilter() {
        if (interestFilter == null) {
            interestFilter = new InterestFilter();
        }
        return interestFilter;
    }

    /**
     * Determines if an interest shall be displayed
     *
     * @param interest interest
     * @return true if item is visible
     */
    protected boolean filterInterest(Interest interest) {
        return interestsController.isVisible(interest);
    }

    // --------------------
    // Inner classes
    // --------------------

    public class InterestFilter extends Filter {
        @NonNull
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            // Copy items
            originalItems = interestsController.getInterests();

            ArrayList<Interest> values;
            synchronized (lock) {
                values = new ArrayList<>(originalItems);
            }

            final int count = values.size();
            final ArrayList<Interest> newValues = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                final Interest value = values.get(i);
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
        protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
            filteredItems = (List<Interest>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}