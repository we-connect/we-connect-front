package berlin.weconnect.weconnect.model.entities;

public class Interest {
    private String id;
    private String name;
    private int icon;

    private transient boolean selected;

    // ------------------------
    // Constructors
    // ------------------------

    public Interest(String id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    // ------------------------
    // Methods
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
