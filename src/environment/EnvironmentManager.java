package environment;

import java.awt.Graphics2D;

import main.gamePanel;

public class EnvironmentManager {
    gamePanel gp;
    public Lighting lighting;

    public EnvironmentManager(gamePanel gp) {
        this.gp = gp;
    }
    public void setup() {
        lighting = new Lighting(gp); 
    }
    public void update() {
        lighting.update();
    }
    public void draw(Graphics2D g2) {
        lighting.draw(g2);
    }
}
