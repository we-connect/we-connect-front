package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.res.Resources;

import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.GetUsersTask;

public class ContactsController {
    // Activity
    private Activity activity;

    // Model
    private List<User> users;

    private static ContactsController instance;

    // --------------------
    // Constructors
    // --------------------

    private ContactsController(Activity activity) {
        setActivity(activity);
        init();
    }

    public static ContactsController getInstance(Activity activity) {
        if (instance == null) {
            instance = new ContactsController(activity);
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
     * Determines whether a given user be displayed considering all filters
     *
     * @param user user to determine visibility of
     * @return whether user is visible or not
     */
    public boolean isVisible(User user) {
        return true;
    }

    // --------------------
    // Methods
    // --------------------

    public void updateUsers() {
        try {
            users = new GetUsersTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

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

    public User getUserByUsername(String username) {
        if (getUsers() != null) {
            for (User u : getUsers()) {
                if (u.getUsername() != null && u.getUsername().equals(username)) {
                    return u;
                }
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
}
