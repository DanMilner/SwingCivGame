package game.map.resources;

        import javax.swing.*;

public class Ice extends Resource {
    public Ice() {
        this.resourceImage = new ImageIcon("textures\\terrain\\ice.png");
        this.resourceType = ResourceTypes.SNOW;
    }
}
