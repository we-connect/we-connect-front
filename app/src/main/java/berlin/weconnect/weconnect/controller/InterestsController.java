package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.Interest;

public class InterestsController {
    // Activity
    private Activity activity;

    // Model
    private List<Interest> interests;

    private static InterestsController instance;

    // --------------------
    // Constructors
    // --------------------

    private InterestsController(Activity activity) {
        setActivity(activity);
        init();
    }

    public static InterestsController getInstance(Activity activity) {
        if (instance == null) {
            instance = new InterestsController(activity);
        }

        instance.setActivity(activity);

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
        interests = new ArrayList<>();

        int[] colors = getResources().getIntArray(R.array.interests);

        interests.add(new Interest("Arts", colors[0], R.drawable.arts));
        interests.add(new Interest("Sports", colors[1],R.drawable.sport));
        interests.add(new Interest("Music", colors[2], R.drawable.music));
        interests.add(new Interest("Food", colors[3], R.drawable.food));
        interests.add(new Interest("Education", colors[4],R.drawable.education));


        // TODO : load possible interests from backend
    }

    /**
     * Determines whether a given card interest be displayed considering all filters
     *
     * @param interest interest to determine visibility of
     * @return whether interest is visible or not
     */
    public boolean isVisible(Interest interest) {
        return true;
    }

    // --------------------
    // Methods
    // --------------------


    // --------------------
    // Methods - Util
    // --------------------

    private Resources getResources() {
        return activity.getResources();
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }
}
