package monster;

import entity.entity;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import main.gamePanel;

/**
 * Cracked planks left on the deck by Baron Rotjaw's ground slam. Purely visual;
 * fades out after a few seconds.
 */
public class DeckCrack extends entity {

    gamePanel gp;
    final int mapNum;
    final int cx, cy;              // where the slam hit
    final int[][] cracks;          // jagged lines: x1,y1,x2,y2,x3,y3 relative to the center
    final int[][] splinters;       // little wood chips: x, y, size
    static final int DURATION = 240;
    int counter = 0;

    public DeckCrack(gamePanel gp, int centerX, int centerY) {
        super(gp);
        this.gp = gp;
        this.mapNum = gp.currentMap;
        this.cx = centerX;
        this.cy = centerY;
        // Sort before every other entity so the cracks sit under the characters
        worldX = centerX;
        worldY = Integer.MIN_VALUE / 2;
        name = "Deck Crack";
        alive = true;

        Random r = new Random();
        int t = gp.tileSize;
        cracks = new int[7][];
        for (int i = 0; i < cracks.length; i++) {
            double a = i * Math.PI * 2 / cracks.length + r.nextDouble() * .5;
            double len = t * (0.9 + r.nextDouble() * .9);
            double bend = (r.nextDouble() - .5) * .8;
            cracks[i] = new int[] {
                0, 0,
                (int) (Math.cos(a) * len * .5), (int) (Math.sin(a) * len * .5 * .6),
                (int) (Math.cos(a + bend) * len), (int) (Math.sin(a + bend) * len * .6),
            };
        }
        splinters = new int[10][];
        for (int i = 0; i < splinters.length; i++) {
            double a = r.nextDouble() * Math.PI * 2;
            double d = t * (0.3 + r.nextDouble() * 1.2);
            splinters[i] = new int[] { (int) (Math.cos(a) * d), (int) (Math.sin(a) * d * .6), 4 + r.nextInt(5) };
        }
    }

    @Override
    public void update() {
        counter++;
        if (counter >= DURATION || gp.currentMap != mapNum) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int sx = cx - gp.player.worldX + gp.player.screenX;
        int sy = cy - gp.player.worldY + gp.player.screenY;
        float alpha = counter < DURATION - 60 ? 0.85f : 0.85f * (DURATION - counter) / 60f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));

        // dark hole in the middle
        g2.setColor(new Color(40, 24, 14));
        g2.fillOval(sx - 18, sy - 9, 36, 18);

        g2.setStroke(new BasicStroke(4));
        g2.setColor(new Color(45, 28, 16));
        for (int[] c : cracks) {
            g2.drawLine(sx + c[0], sy + c[1], sx + c[2], sy + c[3]);
            g2.drawLine(sx + c[2], sy + c[3], sx + c[4], sy + c[5]);
        }
        g2.setStroke(new BasicStroke(1));

        g2.setColor(new Color(196, 140, 88));
        for (int[] s : splinters) {
            g2.fillRect(sx + s[0], sy + s[1], s[2], 3);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
