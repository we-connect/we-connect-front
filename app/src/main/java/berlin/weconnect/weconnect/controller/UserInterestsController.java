package berlin.weconnect.weconnect.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.entities.UserInterest;
import berlin.weconnect.weconnect.model.webservices.DeleteUserInterestTask;
import berlin.weconnect.weconnect.model.webservices.GetUserInterestsTask;
import berlin.weconnect.weconnect.model.webservices.PostUserInterestTask;

public class UserInterestsController {
    // Model
    private List<UserInterest> userInterests;

    private static UserInterestsController instance;

    // --------------------
    // Constructors
    // --------------------

    private UserInterestsController() {
        init();
    }

    public static UserInterestsController getInstance() {
        if (instance == null) {
            instance = new UserInterestsController();
        }

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
        callGetUserInterests();
    }

    /**
     * Calls webservice to get interests
     */
    public void callGetUserInterests() {
        try {
            userInterests = new GetUserInterestsTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to post a user interest
     *
     * @param userInterest user interest to be posted
     */
    public void callPostUserInterest(UserInterest userInterest) {
        try {
            new PostUserInterestTask().execute(userInterest).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to delete a user interest
     *
     * @param userInterest user interest to be deleted
     */
    public void callDeleteUserInterest(UserInterest userInterest) {
        try {
            new DeleteUserInterestTask().execute(userInterest).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<Interest> getInterestsByUser(User user) {
        List<Interest> interests = new ArrayList<>();

        for (UserInterest userInterest : getUserInterests()) {
            if (userInterest != null && userInterest.getUser() != null && userInterest.getUser().getId().equals(user.getId()))
                interests.add(userInterest.getInterest());
        }

        return interests;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public List<UserInterest> getUserInterests() {
        return userInterests;
    }

    public void setUserInterests(List<UserInterest> userInterests) {
        this.userInterests = userInterests;
    }
}
