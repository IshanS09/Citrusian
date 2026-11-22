package tile_interactive;

import entity.entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import main.gamePanel;

public class IT_metalPlate extends interactiveTile {
    gamePanel gp;
    public static final String itName = "Metal Plate";

    public IT_metalPlate(gamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        name = itName;
        down1 = setup("/res/tiles_interactive/metalPlate", gp.tileSize, gp.tileSize);

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /** True when one of the big rocks is resting on this plate. */
    public boolean pressed() {
        for (entity n : gp.npc[gp.currentMap]) {
            if (n != null && n.linkedEntity == this) return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (pressed() && inCamera()) {
            // soft green glow so you can see which plates are done
            float pulse = 0.5f + 0.15f * (float) Math.sin(gp.frameCount / 12.0);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pulse));
            g2.setColor(new Color(120, 255, 140));
            g2.fillRoundRect(getScreenX() - 4, getScreenY() - 4, gp.tileSize + 8, gp.tileSize + 8, 18, 18);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

}
