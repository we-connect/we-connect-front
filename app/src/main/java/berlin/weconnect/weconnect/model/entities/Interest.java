package berlin.weconnect.weconnect.model.entities;

public class Interest {
    private String id;
    private String name;
    private int color;
    private int icon;

    private transient boolean selected;

    // ------------------------
    // Constructors
    // ------------------------

    public Interest(String id, String name, int color, int icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    // ------------------------
    // Methods
    // ------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
