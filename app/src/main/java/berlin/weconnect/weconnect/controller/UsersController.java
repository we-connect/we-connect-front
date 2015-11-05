package berlin.weconnect.weconnect.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.User;
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
        callGetUsers();
    }

    /**
     * Determines whether a given user be displayed considering all filters
     *
     * @param user user to determine visibility of
     * @return whether user is visible or not
     */
    public boolean isVisible(User user) {
        // Exclude logged in user from search results
        return getCurrentUser() != null && !getCurrentUser().getFacebook_id().equals(user.getFacebook_id());
    }

    /**
     * Calls webservice to retrieve all users
     */
    public void callGetUsers() {
        try {
            users = new GetUsersTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to create a new user
     *
     * @param user user to be created
     */
    public void callPostUser(User user) {
        try {
            new PostUserTask().execute(user).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls webservice to retrieve users having similar interests as @param user
     *
     * @param user user to find matches for
     */
    public void callGetSuggestedUsers(User user) {
        try {
            suggestedUsers = new GetSuggestedUsersTask().execute(user).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a user with a specific Facebook id
     *
     * @param facebookId Facebook id
     * @return user
     */
    public User getUserByFacebookId(String facebookId) {
        if (getUsers() != null) {
            for (User u : getUsers()) {
                if (u.getFacebook_id() != null && u.getFacebook_id().equals(facebookId)) {
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
