package berlin.weconnect.weconnect.model;

public class Interest {
    private String name;
    private int color;
    private String icon;

    private transient boolean selected;

    // ------------------------
    // Constructors
    // ------------------------

    public Interest(String name, int color, String icon) {
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
