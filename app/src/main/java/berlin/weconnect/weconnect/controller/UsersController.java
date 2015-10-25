package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.res.Resources;

import java.util.List;

import berlin.weconnect.weconnect.model.entities.Interest;
import berlin.weconnect.weconnect.model.entities.User;

public class UsersController {
    // Activity
    private Activity activity;

    // Model
    private List<User> users;
    private User me;

    private static UsersController instance;

    // --------------------
    // Constructors
    // --------------------

    private UsersController(Activity activity) {
        setActivity(activity);
        init();
    }

    public static UsersController getInstance(Activity activity) {
        if (instance == null) {
            instance = new UsersController(activity);
        }

        instance.setActivity(activity);

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
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

    public User getUserByFacebookId(String facebookId) {
        for (User u : getUsers()) {
            if (u.getFacebook_id() != null && u.getFacebook_id().equals(facebookId)) {
                return u;
            }
        }

        return null;
    }

    public User getUserByUsername(String username) {
        for (User u : getUsers()) {
            if (u.getUsername() != null && u.getUsername().equals(username)){
                return u;
            }
        }

        return null;
    }


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


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }
}
