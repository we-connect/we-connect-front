package berlin.weconnect.weconnect.model.entities;

import android.support.annotation.NonNull;

public class Interest {
    private String id;
    private String category;
    private String name;

    private transient int icon;
    private transient boolean selected;

    // ------------------------
    // Constructors
    // ------------------------

    public Interest(String id, String category, String name, int icon) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.icon = icon;
    }

    // --------------------
    // Methods
    // --------------------

    @NonNull
    public String toString() {
        return "[Interest " + getId() + " " + getCategory()+ " " + getName() + "]";
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
