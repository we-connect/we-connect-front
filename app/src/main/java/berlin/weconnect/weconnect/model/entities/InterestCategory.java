package berlin.weconnect.weconnect.model.entities;

import java.util.ArrayList;
import java.util.List;

public class InterestCategory {
    private String id;
    private String name;
    private List<Interest> interests;

    // ------------------------
    // Constructors
    // ------------------------

    public InterestCategory(String name) {
        this.name = name;

        interests = new ArrayList<>();
    }

    // ------------------------
    // Getters / Setters
    // ------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }
}
