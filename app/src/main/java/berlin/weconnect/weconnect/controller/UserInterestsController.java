package berlin.weconnect.weconnect.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        get();
    }

    /**
     * Calls webservice to get interests
     */
    public void get() {
        try {
            setUserInterests(new GetUserInterestsTask().execute().get());
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to post a user interest
     *
     * @param userInterest user interest to be posted
     */
    public void post(UserInterest userInterest) {
        try {
            new PostUserInterestTask().execute(userInterest).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to delete a user interest
     *
     * @param userInterest user interest to be deleted
     */
    public void delete(UserInterest userInterest) {
        try {
            new DeleteUserInterestTask().execute(userInterest).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public List<Interest> getInterestsByUser(@NonNull User user) {
        List<Interest> interests = new ArrayList<>();

        for (UserInterest userInterest : getUserInterests()) {
            if (userInterest != null && userInterest.getUser() != null && userInterest.getUser().getId().equals(user.getId()))
                interests.add(userInterest.getInterest());
        }

        return interests;
    }

    @Nullable
    public UserInterest getUserInterestByUserAndInterest(@Nullable User user, @Nullable Interest interest) {
        if (user != null && interest != null) {
            for (UserInterest userInterest : getUserInterests()) {
                if (userInterest != null && userInterest.getUser().getId().equals(user.getId()) && userInterest.getInterest().getId().equals(interest.getId()))
                    return userInterest;
            }
        }

        return null;
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
