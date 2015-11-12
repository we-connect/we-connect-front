package berlin.weconnect.weconnect.model.entities;

import android.support.annotation.NonNull;

public class UserInterest {
    private String id;
    private String date;

    private User user;
    private Interest interest;

    // --------------------
    // Methods
    // --------------------

    @NonNull
    public String toString() {
        String u = (getUser() != null ? getUser().toString() : "[User ---]");
        String i = (getInterest() != null ? getInterest().toString() : "[Interest ---]");

        return "[UserInterest " + getId() + " " + u + " " + i + "]";
    }

    // --------------------
    // Getters / Setters
    // --------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }
}
