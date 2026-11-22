package monster;

import entity.entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import main.gamePanel;

/**
 * Tear gas cloud left by the Weeping Onion.
 * Does no damage. While the player stands in it they walk at half speed
 * and their stamina stops refilling.
 */
public class TearGas extends entity {

    gamePanel gp;
    final int mapNum;
    final int radius;
    final int duration;
    int counter = 0;

    public TearGas(gamePanel gp, int centerX, int centerY, int radiusTiles, int durationFrames) {
        super(gp);
        this.gp = gp;
        this.mapNum = gp.currentMap;
        this.radius = (int) (gp.tileSize * radiusTiles * 1.0);
        this.duration = durationFrames;
        // worldX/worldY is the top-left of the cloud so draw sorting works like other entities
        worldX = centerX - radius;
        worldY = centerY - radius;
        name = "Tear Gas";
        alive = true;
    }

    int centerX() { return worldX + radius; }
    int centerY() { return worldY + radius; }

    @Override
    public void update() {
        counter++;
        if (counter >= duration || gp.currentMap != mapNum) {
            alive = false;
            return;
        }
        int px = gp.player.worldX + gp.tileSize / 2;
        int py = gp.player.worldY + gp.tileSize / 2;
        int dx = px - centerX();
        int dy = py - centerY();
        if (dx * dx + dy * dy < radius * radius) {
            gp.playerStatus.applyTearGas(20);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int sx = worldX - gp.player.worldX + gp.player.screenX;
        int sy = worldY - gp.player.worldY + gp.player.screenY;
        if (sx > gp.screenWidth || sy > gp.screenHeight || sx + radius * 2 < 0 || sy + radius * 2 < 0) {
            return;
        }

        // Fade in over 15 frames, fade out over the last 40
        float alpha = 0.45f;
        if (counter < 15) alpha *= counter / 15f;
        if (duration - counter < 40) alpha *= (duration - counter) / 40f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));

        // A few soft puffs that drift slowly so the cloud looks alive
        Color[] shades = { new Color(190, 225, 150), new Color(165, 210, 125), new Color(210, 235, 175) };
        int[][] puffs = { {0, 0, 100}, {-45, -20, 60}, {40, -25, 65}, {-35, 30, 60}, {38, 28, 62}, {0, -45, 55}, {0, 42, 55} };
        int cx = sx + radius;
        int cy = sy + radius;
        for (int i = 0; i < puffs.length; i++) {
            double t = (counter + i * 37) / 40.0;
            int ox = (int) (puffs[i][0] * radius / 100.0 + Math.sin(t) * 6);
            int oy = (int) (puffs[i][1] * radius / 100.0 + Math.cos(t * 0.8) * 5);
            int r = puffs[i][2] * radius / 100;
            g2.setColor(shades[i % shades.length]);
            g2.fillOval(cx + ox - r, cy + oy - r, r * 2, r * 2);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
