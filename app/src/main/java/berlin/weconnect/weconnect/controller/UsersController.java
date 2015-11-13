package berlin.weconnect.weconnect.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.EMeetingPref;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.DeleteUserTask;
import berlin.weconnect.weconnect.model.webservices.GetSuggestedUsersTask;
import berlin.weconnect.weconnect.model.webservices.GetUsersTask;
import berlin.weconnect.weconnect.model.webservices.PostUserTask;

public class UsersController {
    // Model
    private User currentUser;
    private List<User> users;
    private List<User> suggestedUsers;

    private static UsersController instance;

    // --------------------
    // Constructors
    // --------------------

    private UsersController() {
        init();
    }

    public static UsersController getInstance() {
        if (instance == null) {
            instance = new UsersController();
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
     * Determines whether a given user be displayed considering all filters
     *
     * @param user user to determine visibility of
     * @return whether user is visible or not
     */
    public boolean isVisible(@NonNull User user) {
        boolean isCurrentUser = getCurrentUser() != null && getCurrentUser().getFacebookId().equals(user.getFacebookId());
        boolean isDummyUser = user.getUsername() == null || user.getFacebookId() == null;
        boolean matchesMeetingPref = false;

        String meetingPref = getCurrentUser().getMeetingPref();
        if (meetingPref.equals(EMeetingPref.BOTH.getValue()) || meetingPref.equals(user.getGender()))
            matchesMeetingPref = true;

        return !isCurrentUser && !isDummyUser && matchesMeetingPref;
    }

    /**
     * Calls webservice to retrieve all users
     */
    public void get() {
        try {
            users = new GetUsersTask().execute().get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to post a user
     *
     * @param user user to be posted
     */
    public void post(User user) {
        try {
            new PostUserTask().execute(user).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to delete a user
     *
     * @param user user to be deleted
     */
    public void delete(User user) {
        try {
            new DeleteUserTask().execute(user).get();
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to get users having similar interests as @param user
     *
     * @param user user to find matches for
     */
    public void getSuggested(User user) {
        try {
            setSuggestedUsers(new GetSuggestedUsersTask().execute(user).get());
        } catch (@NonNull InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a user with a specific Facebook id
     *
     * @param facebookId Facebook id
     * @return user
     */
    @Nullable
    public User getUserByFacebookId(String facebookId) {
        if (getUsers() != null) {
            for (User u : getUsers()) {
                if (u.getFacebookId() != null && u.getFacebookId().equals(facebookId)) {
                    u.updateInterests();
                    return u;
                }
            }
        }

        return null;
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<User> getSuggestedUsers() {
        return suggestedUsers;
    }

    public void setSuggestedUsers(List<User> suggestedUsers) {
        this.suggestedUsers = suggestedUsers;
    }
}
