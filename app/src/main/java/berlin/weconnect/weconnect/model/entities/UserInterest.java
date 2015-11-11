package berlin.weconnect.weconnect.model.entities;

public class UserInterest {
    private String id;
    private String date;

    private User user;
    private Interest interest;

    // --------------------
    // Methods
    // --------------------

    public String toString() {
        if (getInterest() != null)
            return "[UserInterest " + getId() + " " + user.toString() + " " + interest.toString() + "]";
        else
            return "[UserInterest " + getId() + " " + user.toString() + " [Interest ---]]";
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
