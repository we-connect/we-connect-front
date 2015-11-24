package berlin.weconnect.weconnect.view.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.controller.InterestsController;
import berlin.weconnect.weconnect.controller.UsersController;
import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.InterestCategory;
import berlin.weconnect.weconnect.model.entities.User;

public class InterestCategoriesSelectionAdapter extends ArrayAdapter<InterestCategory> implements Filterable {
    private Activity activity;

    private UsersController usersController;
    private InterestsController interestsController;

    // Filter
    @NonNull
    private List<InterestCategory> filteredItems = new ArrayList<>();
    private List<InterestCategory> originalItems = new ArrayList<>();
    private InterestFilter interestFilter;
    private final Object lock = new Object();

    // --------------------
    // Constructors
    // --------------------

    public InterestCategoriesSelectionAdapter(Activity activity, int resource, @NonNull List<InterestCategory> items) {
        super(activity, resource, items);
        this.activity = activity;
        this.filteredItems = items;
        this.originalItems = items;

        usersController = UsersController.getInstance();
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
    public InterestCategory getItem(int position) {
        return filteredItems.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final InterestCategory interestCategory = getItem(position);
        return getCategoryView(position, interestCategory, parent);
    }

    @NonNull
    private View getCategoryView(final int position, @NonNull final InterestCategory interestCategory, final ViewGroup parent) {
        // Layout inflater
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());

        // Load views
        final LinearLayout llInterestCategory = (LinearLayout) vi.inflate(R.layout.list_item_interest_category_selection, parent, false);
        final TextView tvCategoryName = (TextView) llInterestCategory.findViewById(R.id.tvCategoryName);
        final GridLayout grdInterests = (GridLayout) llInterestCategory.findViewById(R.id.grdInterests);

        // Set values
        if (interestCategory.getName() != null)
            tvCategoryName.setText(interestCategory.getName());

        // Set color
        int[] colors = activity.getResources().getIntArray(R.array.interests);
        llInterestCategory.setBackgroundColor(colors[position % colors.length]);

        // Iterate over all interests of this category
        for (final Interest interest : interestCategory.getInterests()) {
            final LinearLayout gridItemInterest = (LinearLayout) vi.inflate(R.layout.grid_item_interest_selection, parent, false);
            final CheckBox cb = (CheckBox) gridItemInterest.findViewById(R.id.cb);
            final TextView tvName = (TextView) gridItemInterest.findViewById(R.id.tvName);

            final User user = usersController.getCurrentUser();

            // Set values
            if (interest.getName() != null)
                tvName.setText(interest.getName());
            if (user.hasInterest(interest))
                cb.setChecked(true);

            // Add actions
            gridItemInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.toggle();
                    interest.setSelected(cb.isChecked());
                    user.updateInterest(interest);
                }
            });
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interest.setSelected(cb.isChecked());
                    user.updateInterest(interest);
                }
            });

            grdInterests.addView(gridItemInterest);
        }

        return llInterestCategory;
    }

    // --------------------
    // Methods - Filter
    // --------------------

    @NonNull
    public List<InterestCategory> getFilteredItems() {
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
     * Determines if an interest category shall be displayed
     *
     * @param interestCategory interest category
     * @return true if item is visible
     */
    protected boolean filterInterest(InterestCategory interestCategory) {
        return interestsController.isVisible(interestCategory);
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
            originalItems = interestsController.getInterestCategories();

            ArrayList<InterestCategory> values;
            synchronized (lock) {
                values = new ArrayList<>(originalItems);
            }

            final int count = values.size();
            final ArrayList<InterestCategory> newValues = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                final InterestCategory value = values.get(i);
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
            filteredItems = (List<InterestCategory>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}