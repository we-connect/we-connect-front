package berlin.weconnect.weconnect.model.entities;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import berlin.weconnect.weconnect.controller.UserInterestsController;

public class User {
    private String id;
    private String username;
    private String usernameCanonical;
    private String email;
    private String emailCanonical;
    private boolean enabled;
    private String salt;
    private String password;
    private String lastLogin;
    private boolean locked;
    private boolean expired;
    private List<String> roles;
    private boolean credentialExpires;
    private String facebookId;
    private String firstName;
    private String lastName;
    private String dateRegister;
    private String locale;

    private transient List<Interest> interests;
    private transient String profileUrl;
    private transient String profilePictureUrl;

    // --------------------
    // Constructors
    // --------------------

    public User() {
        interests = new ArrayList<>();
    }

    // --------------------
    // Methods
    // --------------------

    /**
     * Loads user's interests
     */
    public void updateInterests() {
        interests = UserInterestsController.getInstance().getInterestsByUser(this);
    }

    public void updateInterest(@NonNull Interest interest) {
        UserInterestsController userInterestsController = UserInterestsController.getInstance();

        UserInterest userInterest = userInterestsController.getUserInterestByUserAndInterest(this, interest);

        if (!interest.isSelected() && userInterest != null) {
            userInterestsController.delete(userInterest);
        }else if (interest.isSelected()) {
            userInterest = new UserInterest();
            userInterest.setUser(this);
            userInterest.setInterest(interest);
            userInterestsController.post(userInterest);
        }

        userInterestsController.get();
        updateInterests();
    }

    public boolean hasInterest(@NonNull Interest interest) {
        for (Interest i : getInterests()) {
            if (i.getId().equals(interest.getId()))
                return true;
        }

        return false;
    }

    @NonNull
    public String toString() {
        return "[User " + getId() + " " + getUsername() + "]";
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameCanonical() {
        return usernameCanonical;
    }

    public void setUsernameCanonical(String usernameCanonical) {
        this.usernameCanonical = usernameCanonical;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCanonical() {
        return emailCanonical;
    }

    public void setEmailCanonical(String emailCanonical) {
        this.emailCanonical = emailCanonical;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isCredentialExpires() {
        return credentialExpires;
    }

    public void setCredentialExpires(boolean credentialExpires) {
        this.credentialExpires = credentialExpires;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String date_register) {
        this.dateRegister = dateRegister;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}