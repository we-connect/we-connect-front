package berlin.weconnect.weconnect.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import berlin.weconnect.weconnect.R;
import berlin.weconnect.weconnect.model.entities.User;
import berlin.weconnect.weconnect.model.webservices.GetSuggestedUsersTask;

public class ProfileController {
    // Activity
    private Activity activity;

    // Model
    private User myUser;
    private List<User> suggestedContacts;

    private static ContactsController contactsController;

    private static ProfileController instance;

    // --------------------
    // Constructors
    // --------------------

    private ProfileController(Activity activity) {
        setActivity(activity);
        init();
    }

    public static ProfileController getInstance(Activity activity) {
        if (instance == null) {
            instance = new ProfileController(activity);
        }

        instance.setActivity(activity);

        return instance;
    }

    // --------------------
    // Methods
    // --------------------

    public void init() {
        contactsController = ContactsController.getInstance(activity);
        suggestedContacts = new ArrayList<>();
    }

    public void updateMyUser() {
        List<User> users = contactsController.getUsers();
        if (users != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            String myUsername = prefs.getString(activity.getResources().getString(R.string.pref_fb_username), null);
            String myFacebookId = prefs.getString(activity.getResources().getString(R.string.pref_fb_facebookid), null);

            setMyUser(contactsController.getUserByFacebookId(myFacebookId));
            Log.d("User", getMyUser().getUsername());
        }
    }

    public void readSuggestedContacts() {
        try {
            setSuggestedContacts(new GetSuggestedUsersTask().execute(getMyUser().getInterests()).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void writeInterests() {
        // new PostInterestsTask().execute(getMyUser()).get();
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public void setActivity(Activity activity) {
        this.activity = activity;
    }


    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    public List<User> getSuggestedContacts() {
        return suggestedContacts;
    }

    public void setSuggestedContacts(List<User> suggestedContacts) {
        this.suggestedContacts = suggestedContacts;
    }
}
