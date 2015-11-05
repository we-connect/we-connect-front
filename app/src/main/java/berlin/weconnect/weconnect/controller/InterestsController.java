package berlin.weconnect.weconnect.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.GetInterestsTask;
import berlin.weconnect.weconnect.model.webservices.PostInterestsTask;

public class InterestsController {
    // Model
    private List<Interest> interests;

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
        callGetInterests();
    }

    /**
     * Determines whether a given interest be displayed considering all filters
     *
     * @param interest interest to determine visibility of
     * @return whether interest is visible or not
     */
    public boolean isVisible(Interest interest) {
        return interest != null;
    }

    /**
     * Calls webservice to retrieve all interests
     */
    public void callGetInterests() {
        try {
            interests = new GetInterestsTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to set interests of a user
     *
     * @param user user containing interests
     */
    public void callPostInterests(User user) {
        try {
            new PostInterestsTask().execute(user).get();
        } catch (InterruptedException | ExecutionException e) {
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
}
