package environment;

import java.awt.Graphics2D;

import main.gamePanel;

public class EnvironmentManager {
    gamePanel gp;
    public Lighting lighting;
    public Weather weather;

    public EnvironmentManager(gamePanel gp) {
        this.gp = gp;
    }
    public void setup() {
        lighting = new Lighting(gp); 
        weather = new Weather(gp);
    }
    public void update() {
        lighting.update();
        weather.update();
    }
    /** Rain and fog. Called in the game's own coordinates, before the darkness. */
    public void drawWeather(Graphics2D g2) {
        weather.draw(g2);
    }
    public void draw(Graphics2D g2) {
        lighting.draw(g2);
        weather.drawOverlay(g2);
    }
}
