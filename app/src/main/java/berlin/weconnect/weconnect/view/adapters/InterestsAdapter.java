package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.model.Interest;

public class InterestsAdapter extends ArrayAdapter<Interest> implements Filterable {
    private InterestsController interestsController;

    // Filter
    private List<Interest> filteredItems = new ArrayList<>();
    private List<Interest> originalItems = new ArrayList<>();
    private InterestFilter interestFilter;
    private final Object lock = new Object();

    // --------------------
    // Constructors
    // --------------------

    public InterestsAdapter(Context context, Activity activity, int resource, List<Interest> items) {
        super(context, resource, items);

        this.filteredItems = items;
        this.originalItems = items;

        interestsController = InterestsController.getInstance(activity);

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
    public Interest getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final Interest interest = getItem(position);
        return getCardView(position, interest, parent);
    }

    private View getCardView(final int position, final Interest interest, final ViewGroup parent) {
        // Layout inflater
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());

        // Load views
        final LinearLayout llInterest = (LinearLayout) vi.inflate(R.layout.interest, parent, false);
        final CheckBox cb = (CheckBox) llInterest.findViewById(R.id.cb);
        final ImageView ivIcon = (ImageView) llInterest.findViewById(R.id.ivIcon);
        final TextView tvName = (TextView) llInterest.findViewById(R.id.tvName);

        // Set values
        llInterest.setBackgroundColor(interest.getColor());
        tvName.setText(interest.getName());


        // Add actions
        llInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb.toggle();
                interest.setSelected(cb.isChecked());
            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interest.setSelected(cb.isChecked());
            }
        });

        return llInterest;
    }

    // --------------------
    // Methods - Filter
    // --------------------

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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (List<Interest>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}