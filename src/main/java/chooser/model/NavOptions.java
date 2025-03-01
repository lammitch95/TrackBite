package chooser.model;

public class NavOptions {

    private final String name;
    private final String buttonId;
    private final String imagePath;

    public NavOptions(String name, String buttonId, String imagePath) {
        this.name = name;
        this.buttonId = buttonId;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getButtonId() {
        return buttonId;
    }

    public String getImagePath() {
        return imagePath;
    }

}
