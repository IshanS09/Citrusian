package environment;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.gamePanel;

/**
 * Outdoor weather: clear, rain, fog and thunderstorms, plus a permanent mist in
 * the Rotting Marsh. Weather only shows (and only matters) outside.
 *
 * Rules:
 * - Rain and storms slow fire seeds and fire skulls and make them fizzle sooner.
 * - Fog cuts how far you can see, but veggies also have to get closer to spot you.
 * - Storms add lightning flashes and thunder.
 *
 * Start with a set weather for testing: java -Dcitrusian.weather=storm -cp bin main.Main
 */
public class Weather {

    gamePanel gp;
    Random rand = new Random();

    public static final int CLEAR = 0;
    public static final int RAIN = 1;
    public static final int FOG = 2;
    public static final int STORM = 3;
    static final String[] NAMES = {"clear", "rain", "fog", "storm"};

    public int current = CLEAR;
    int next = CLEAR;
    public float intensity = 0f;   // 0 = none, 1 = full strength (fades in and out)
    int timer;                     // frames left before the weather changes
    float marshMist = 0f;          // fades in while the player is in the Rotting Marsh

    // Rain
    static final int MAX_DROPS = 420;
    float[] dropX = new float[MAX_DROPS];
    float[] dropY = new float[MAX_DROPS];
    float[] dropSpeed = new float[MAX_DROPS];
    static final int MAX_SPLASHES = 40;
    int[] splashX = new int[MAX_SPLASHES];
    int[] splashY = new int[MAX_SPLASHES];
    int[] splashLife = new int[MAX_SPLASHES];

    // Fog
    BufferedImage fogMask;
    int fogMaskRadius = -1;
    static final int PUFFS = 7;
    float[] puffX = new float[PUFFS];
    float[] puffY = new float[PUFFS];
    float[] puffSpeed = new float[PUFFS];

    // Storm
    int flash = 0;          // frames of lightning flash left
    int nextStrike = 0;     // frames until the next strike
    int thunderDelay = -1;  // frames until thunder follows a flash

    public Weather(gamePanel gp) {
        this.gp = gp;
        for (int i = 0; i < MAX_DROPS; i++) resetDrop(i, true);
        for (int i = 0; i < PUFFS; i++) {
            puffX[i] = rand.nextInt(gp.screenWidth);
            puffY[i] = rand.nextInt(gp.screenHeight);
            puffSpeed[i] = 0.15f + rand.nextFloat() * 0.35f;
        }
        reset();

        String forced = System.getProperty("citrusian.weather");
        if (forced != null) {
            for (int i = 0; i < NAMES.length; i++) {
                if (NAMES[i].equalsIgnoreCase(forced)) force(i, 60 * 60 * 60); // an hour, for testing
            }
        }
    }

    /** Switches straight to a weather at full strength for the given number of frames. */
    public void force(int type, int frames) {
        current = next = type;
        intensity = type == CLEAR ? 0f : 1f;
        timer = frames;
        if (type == STORM) nextStrike = seconds(3, 6);
    }

    /** Clear skies for a new game or restart. */
    public void reset() {
        current = next = CLEAR;
        intensity = 0f;
        timer = seconds(60, 120);
        flash = 0;
        thunderDelay = -1;
    }

    int seconds(int min, int max) {
        return (min + rand.nextInt(max - min + 1)) * 60;
    }

    boolean outside() {
        return gp.currentArea == gp.outside;
    }

    boolean playing() {
        return gp.gameState != gp.titleState && gp.gameState != gp.gameOverState;
    }

    boolean inMarsh() {
        if (gp.currentMap != 0) return false;
        int col = gp.player.worldX / gp.tileSize;
        int row = gp.player.worldY / gp.tileSize;
        return col >= 13 && col <= 70 && row >= 104 && row <= 134;
    }

    // ---------- Rules other classes ask about ----------

    /** True when rain is heavy enough to dampen fire attacks. */
    public boolean isRaining() {
        return outside() && (current == RAIN || current == STORM) && intensity > 0.3f;
    }

    /** How foggy it is right now, 0 to 1 (weather fog or marsh mist, whichever is thicker). */
    public float fogAmount() {
        if (!outside()) return 0f;
        float weatherFog = current == FOG ? intensity : 0f;
        return Math.max(weatherFog, marshMist * 0.6f);
    }

    /** Tiles a veggie needs to be within to spot you. Thick fog cuts it almost in half. */
    public int sightRange(int normal) {
        float fog = fogAmount();
        if (fog < 0.3f) return normal;
        return Math.max(2, Math.round(normal * (1f - 0.45f * fog)));
    }

    // ---------- Update ----------

    public void update() {
        if (gp.gameState == gp.playState) {
            advanceTimer();
            marshMist += ((inMarsh() ? 1f : 0f) - marshMist) * 0.02f;
            if (outside()) {
                moveRain();
                moveFog();
                updateStorm();
            }
        }
        updateSound();
    }

    void advanceTimer() {
        if (next != current) {
            // Fade the old weather out, then switch and fade the new one in
            intensity -= 1f / 180f;
            if (intensity <= 0f) {
                intensity = 0f;
                current = next;
                announce();
            }
            return;
        }
        if (current != CLEAR && intensity < 1f) {
            intensity = Math.min(1f, intensity + 1f / 180f);
        }
        if (--timer <= 0) {
            pickNext();
        }
    }

    void pickNext() {
        if (current != CLEAR) {
            next = CLEAR;
            timer = seconds(60, 120);
            return;
        }
        // Fog rolls in more often around dawn and at night
        Lighting l = gp.eManager.lighting;
        boolean misty = l != null && (l.dayState == l.dawn || l.dayState == l.night);
        int roll = rand.nextInt(100);
        if (roll < (misty ? 30 : 45)) {
            next = RAIN;
            timer = seconds(45, 90);
        } else if (roll < (misty ? 80 : 75)) {
            next = FOG;
            timer = seconds(40, 70);
        } else {
            next = STORM;
            timer = seconds(30, 50);
            nextStrike = seconds(3, 6);
        }
    }

    void announce() {
        if (!outside()) return;
        switch (current) {
            case RAIN:  gp.ui.addMessage("It's raining. Fire seeds won't fly as far."); break;
            case FOG:   gp.ui.addMessage("Fog rolls in. Veggies can't see you from as far."); break;
            case STORM: gp.ui.addMessage("A storm is coming! Fire seeds are weak in the rain."); break;
            default:    gp.ui.addMessage("The skies clear up."); break;
        }
    }

    void resetDrop(int i, boolean anywhere) {
        dropX[i] = rand.nextInt(gp.screenWidth + 200) - 100;
        dropY[i] = anywhere ? rand.nextInt(gp.screenHeight) : -rand.nextInt(80) - 20;
        dropSpeed[i] = 11f + rand.nextFloat() * 6f;
    }

    int activeDrops() {
        if (current == RAIN) return (int) (240 * intensity);
        if (current == STORM) return (int) (MAX_DROPS * intensity);
        return 0;
    }

    void moveRain() {
        int drops = activeDrops();
        float wind = current == STORM ? 4f : 1.5f;
        for (int i = 0; i < drops; i++) {
            dropX[i] += wind;
            dropY[i] += dropSpeed[i];
            if (dropY[i] > gp.screenHeight) {
                // Leave a little splash where some drops land
                if (rand.nextInt(4) == 0) addSplash((int) dropX[i], rand.nextInt(gp.screenHeight));
                resetDrop(i, false);
            }
        }
        for (int i = 0; i < MAX_SPLASHES; i++) {
            if (splashLife[i] > 0) splashLife[i]--;
        }
    }

    void addSplash(int x, int y) {
        for (int i = 0; i < MAX_SPLASHES; i++) {
            if (splashLife[i] == 0) {
                splashX[i] = x;
                splashY[i] = y;
                splashLife[i] = 10;
                return;
            }
        }
    }

    void moveFog() {
        for (int i = 0; i < PUFFS; i++) {
            puffX[i] += puffSpeed[i];
            if (puffX[i] > gp.screenWidth + 200) {
                puffX[i] = -200;
                puffY[i] = rand.nextInt(gp.screenHeight);
            }
        }
    }

    void updateStorm() {
        if (flash > 0) flash--;
        if (thunderDelay > 0 && --thunderDelay == 0) {
            gp.playSE(28);
            thunderDelay = -1;
        }
        if (current != STORM || intensity < 0.6f) return;
        if (--nextStrike <= 0) {
            flash = 14;
            thunderDelay = 20 + rand.nextInt(40);
            nextStrike = seconds(6, 14);
        }
    }

    void updateSound() {
        boolean wantRain = playing() && outside() && (current == RAIN || current == STORM) && intensity > 0.15f;
        gp.setRainSound(wantRain, current == STORM);
    }

    // ---------- Draw ----------

    /** Overcast tint and fog, drawn over the world and under the night darkness. */
    public void draw(Graphics2D g2) {
        if (!outside()) return;
        drawTint(g2);
        drawFog(g2);
    }

    /** Raindrops and lightning, drawn on top of the darkness so they still show at night. */
    public void drawOverlay(Graphics2D g2) {
        if (!outside()) return;
        if (activeDrops() > 0) {
            Graphics2D g = (Graphics2D) g2.create();
            if (gp.isFullScreen()) {
                // In full screen this runs unscaled, so match the game's scaling
                double scale = Math.min((double) gp.getWidth() / gp.screenWidth, (double) gp.getHeight() / gp.screenHeight);
                g.translate((gp.getWidth() - gp.screenWidth * scale) / 2, (gp.getHeight() - gp.screenHeight * scale) / 2);
                g.scale(scale, scale);
            }
            drawDrops(g);
            g.dispose();
        }
        if (flash <= 0) return;
        float a = Math.min(0.7f, flash / 14f * 0.7f);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2.setColor(new Color(235, 240, 255));
        g2.fillRect(0, 0, Math.max(gp.getWidth(), gp.screenWidth), Math.max(gp.getHeight(), gp.screenHeight));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    /** Gray-blue tint so a rainy day looks overcast. */
    void drawTint(Graphics2D g2) {
        if (activeDrops() == 0) return;
        float tint = (current == STORM ? 0.34f : 0.22f) * intensity;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, tint));
        g2.setColor(new Color(40, 55, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    void drawDrops(Graphics2D g2) {
        int drops = activeDrops();
        // Drops are a bit softer at night so they don't glow
        float alpha = gp.isNight() ? 0.45f : 0.7f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(200, 218, 255));
        g2.setStroke(new BasicStroke(2f));
        float slant = current == STORM ? 0.35f : 0.15f;
        for (int i = 0; i < drops; i++) {
            int len = (int) (dropSpeed[i] * 2f);
            g2.drawLine((int) dropX[i], (int) dropY[i], (int) (dropX[i] + len * slant), (int) dropY[i] + len);
        }
        g2.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < MAX_SPLASHES; i++) {
            if (splashLife[i] > 0) {
                int r = 12 - splashLife[i];
                g2.drawOval(splashX[i] - r, splashY[i] - r / 3, r * 2, r * 2 / 3);
            }
        }
        g2.setStroke(new BasicStroke(1f));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    void drawFog(Graphics2D g2) {
        float fog = fogAmount();
        if (fog <= 0.01f) return;

        // The lantern pushes the fog back further
        int radius = gp.tileSize * 3;
        if (gp.player.currentLight != null) {
            radius = Math.max(radius, gp.player.currentLight.lightRadius);
        }
        if (fogMask == null || radius != fogMaskRadius) buildFogMask(radius);

        // Drifting puffs give the fog some movement
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, fog * 0.5f)));
        for (int i = 0; i < PUFFS; i++) {
            int size = 260 + i * 30;
            RadialGradientPaint puff = new RadialGradientPaint(puffX[i], puffY[i], size / 2f,
                    new float[] {0f, 1f},
                    new Color[] {new Color(215, 220, 225, 170), new Color(215, 220, 225, 0)});
            g2.setPaint(puff);
            g2.fillOval((int) puffX[i] - size / 2, (int) puffY[i] - size / 2, size, size);
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, fog * 0.88f)));
        g2.drawImage(fogMask, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    /** A gray sheet with a clear hole around the player, built once per radius. */
    void buildFogMask(int radius) {
        fogMask = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fogMask.createGraphics();
        int cx = gp.player.screenX + gp.tileSize / 2;
        int cy = gp.player.screenY + gp.tileSize / 2;
        Color fogColor = new Color(205, 210, 215);
        RadialGradientPaint paint = new RadialGradientPaint(cx, cy, radius * 2.2f,
                new float[] {0f, 0.3f, 0.55f, 1f},
                new Color[] {
                    new Color(205, 210, 215, 0),
                    new Color(205, 210, 215, 40),
                    new Color(205, 210, 215, 190),
                    fogColor
                });
        g.setPaint(paint);
        g.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g.dispose();
        fogMaskRadius = radius;
    }
}
