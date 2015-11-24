package berlin.weconnect.weconnect.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.InterestCategory;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.GetInterestsTask;

public class InterestsController {
    // Model
    private List<Interest> interests;
    private List<InterestCategory> interestCategories;

    private static InterestsController instance;

    // --------------------
    // Constructors
    // --------------------

    private InterestsController() {
        init();
    }

    public static InterestsController getInstance() {
        if (instance == null) {
            instance = new InterestsController();
        }

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
        get();
    }

    /**
     * Determines whether a given interest category be displayed considering all filters
     *
     * @param interestCategory interest category to determine visibility of
     * @return whether interest category is visible or not
     */
    public boolean isVisible(@Nullable InterestCategory interestCategory) {
        return interestCategory != null && interestCategory.getInterests() != null && !interestCategory.getInterests().isEmpty();
    }

    /**
     * Determines whether a given interest be displayed considering all filters
     *
     * @param interestCategory interestCategory to determine visibility of
     * @param user     user
     * @return whether interest is visible or not
     */
    public boolean isVisible(@Nullable InterestCategory interestCategory, User user) {
        return interestCategory != null && user != null && user.hasInterestCategory(interestCategory);
    }

    /**
     * Calls webservice to get interests
     */
    public void get() {
        try {
            interests = new GetInterestsTask().execute().get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<InterestCategory> getInterestCategories() {
        interestCategories = new ArrayList<>();

        for (Interest i : getInterests()) {
            String category = i.getCategory();

            if (containsCategory(category)) {
                InterestCategory ic = getCategoryByName(category);
                ic.getInterests().add(i);
            } else {
                InterestCategory ic = new InterestCategory(category);
                ic.getInterests().add(i);

                interestCategories.add(ic);
            }
        }

        return interestCategories;
    }

    private boolean containsCategory(String category) {
        for (InterestCategory ic : interestCategories) {
            if (ic.getName().equals(category))
                return true;
        }

        return false;
    }

    private InterestCategory getCategoryByName(String category) {
        for (InterestCategory ic : interestCategories) {
            if (ic.getName().equals(category))
                return ic;
        }

        return null;
    }

    public void setInterestCategories(List<InterestCategory> interestCategories) {
        this.interestCategories = interestCategories;
    }
}
