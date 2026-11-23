package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import entity.entity;
import object.OBJ_Fish;
import object.OBJ_FishingRod;

/**
 * Fishing minigame.
 *
 * Face water with a Fishing Rod and press R (or use the rod from the inventory).
 *   1. Wait for a bite. The bobber dips now and then to tease you; don't fall for it.
 *   2. When the "!" pops up, press Enter fast to hook the fish.
 *   3. Reel: hold Enter to lift the green bar, let go to drop it. Keep the fish inside
 *      the bar to fill the catch meter. If the meter empties, it gets away.
 *
 * The world pauses while you fish, so veggies can't sneak up on you.
 * Rain makes fish bite sooner. Night brings out Golden Carp. Marsh water has eels.
 */
public class FishingManager {

    gamePanel gp;
    Random rand = new Random();

    static final int IDLE = 0, WAITING = 1, BITE = 2, REELING = 3, RESULT = 4;
    public int phase = IDLE;

    // Water tiles you can cast into
    static final int[] WATER = {9, 10, 22, 41, 71, 72};
    static final int[] MARSH_WATER = {71, 72};

    int bobberX, bobberY;       // world position (center) of the bobber
    boolean inMarsh;
    int timer;                  // frames left in the current phase
    int nibbleTimer;            // counts down to the next fake dip
    int nibble;                 // frames left on the current dip

    OBJ_Fish hooked;

    // Reeling. Everything is 0 at the bottom of the bar and 1 at the top.
    double fishPos, fishTarget, fishSpeed;
    int fishRetarget;
    double zonePos, zoneVel, zoneSize;
    double progress;
    boolean wasEnterHeld;

    String resultText = "";
    entity resultItem;

    public int totalCaught = 0;

    public FishingManager(gamePanel gp) {
        this.gp = gp;
    }

    // ------------------------------------------------------------------ starting

    boolean hasRod() {
        return gp.player.searchItemInInventory(OBJ_FishingRod.objName) != 999;
    }

    static boolean isWater(int tileNum, int[] list) {
        for (int w : list) if (w == tileNum) return true;
        return false;
    }

    /** Called from the R key or from using the rod. Starts fishing if the player faces water. */
    public void tryCast() {
        if (!hasRod()) {
            gp.ui.addMessage("You need a Fishing Rod. The merchant sells them.");
            return;
        }
        if (gp.bossBattleOn) {
            gp.ui.addMessage("No time to fish now!");
            gp.gameState = gp.playState;
            return;
        }

        // Look one and then two tiles ahead of the player's feet
        int ts = gp.tileSize;
        int cx = gp.player.worldX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int cy = gp.player.worldY + gp.player.solidArea.y + gp.player.solidArea.height / 2;
        int dx = 0, dy = 0;
        switch (gp.player.direction) {
            case "up": dy = -1; break;
            case "down": dy = 1; break;
            case "left": dx = -1; break;
            default: dx = 1; break;
        }
        for (int step = 1; step <= 2; step++) {
            int col = cx / ts + dx * step;
            int row = cy / ts + dy * step;
            if (col < 0 || row < 0 || col >= gp.maxWorldCol || row >= gp.maxWorldRow) break;
            int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
            if (isWater(tileNum, WATER)) {
                bobberX = col * ts + ts / 2;
                bobberY = row * ts + ts / 2;
                inMarsh = isWater(tileNum, MARSH_WATER);
                startWaiting();
                return;
            }
        }
        gp.ui.addMessage("Face the water to cast your line.");
        if (gp.gameState == gp.characterState) gp.gameState = gp.playState;
    }

    void startWaiting() {
        gp.keyH.enterPressed = false;
        gp.player.cancelCharge();
        phase = WAITING;
        // Fish bite sooner in the rain
        int base = 150 + rand.nextInt(240);
        if (gp.eManager.weather.isRaining()) base = base * 6 / 10;
        timer = base;
        nibbleTimer = 40 + rand.nextInt(60);
        nibble = 0;
        gp.gameState = gp.fishingState;
        gp.playSE(29);
    }

    OBJ_Fish rollCatch() {
        int r = rand.nextInt(100);
        int carpChance = gp.isNight() ? 10 : 4;
        String kind;
        if (r < carpChance) kind = OBJ_Fish.GOLDEN_CARP;
        else if (r < carpChance + 10) kind = OBJ_Fish.OLD_BOOT;
        else if (r < carpChance + 40) kind = inMarsh ? OBJ_Fish.MARSH_EEL : OBJ_Fish.SUNFISH;
        else kind = OBJ_Fish.MINNOW;
        return new OBJ_Fish(gp, kind);
    }

    // ------------------------------------------------------------------ input

    /** Enter was pressed while fishing. */
    public void onEnter() {
        switch (phase) {
            case WAITING:
                // Pulled up too early
                finish("Nothing bit. Reel in and try again.", null);
                break;
            case BITE:
                startReeling();
                break;
            case RESULT:
                close();
                break;
        }
    }

    /** Esc: put the rod away. */
    public void cancel() {
        if (phase == RESULT) { close(); return; }
        phase = IDLE;
        gp.gameState = gp.playState;
        gp.ui.addMessage("You reel in your line.");
    }

    void startReeling() {
        phase = REELING;
        int d = hooked.difficulty;
        zoneSize = 0.34 - d * 0.04;      // harder fish get a smaller bar
        fishSpeed = 0.006 + d * 0.004;   // and swim faster
        fishPos = 0.5;
        fishTarget = 0.5;
        fishRetarget = 0;
        zonePos = 0.5 - zoneSize / 2;
        zoneVel = 0;
        progress = 0.3;
        gp.playSE(16);
    }

    void finish(String text, entity item) {
        phase = RESULT;
        resultText = text;
        resultItem = item;
        timer = 150;
        gp.keyH.enterPressed = false;
    }

    void close() {
        phase = IDLE;
        resultItem = null;
        gp.keyH.enterPressed = false;
        gp.gameState = gp.playState;
    }

    // ------------------------------------------------------------------ update

    public void update() {
        switch (phase) {
            case WAITING:
                timer--;
                if (nibble > 0) nibble--;
                if (--nibbleTimer <= 0) {
                    nibble = 10;
                    nibbleTimer = 50 + rand.nextInt(90);
                }
                if (timer <= 0) {
                    hooked = rollCatch();
                    phase = BITE;
                    // Rarer fish give you less time to react
                    timer = 50 - hooked.difficulty * 6;
                    gp.playSE(9);
                }
                break;

            case BITE:
                if (--timer <= 0) {
                    finish("Too slow! It got away.", null);
                    gp.playSE(15);
                }
                break;

            case REELING:
                updateReel();
                break;

            case RESULT:
                if (--timer <= 0) close();
                break;
        }
    }

    void updateReel() {
        // The fish darts toward a new spot every so often. Harder fish pick spots more often.
        if (--fishRetarget <= 0) {
            fishTarget = rand.nextDouble();
            fishRetarget = 25 + rand.nextInt(Math.max(10, 70 - hooked.difficulty * 12));
        }
        double diff = fishTarget - fishPos;
        if (Math.abs(diff) > fishSpeed) fishPos += Math.signum(diff) * fishSpeed;
        else fishPos = fishTarget;

        // Hold Enter to lift the bar, let go and it sinks
        boolean holding = gp.keyH.enterHeld;
        zoneVel += holding ? 0.0016 : -0.0013;
        zoneVel = Math.max(-0.03, Math.min(0.03, zoneVel));
        zonePos += zoneVel;
        if (zonePos < 0) { zonePos = 0; zoneVel = Math.abs(zoneVel) * 0.3; }
        if (zonePos > 1 - zoneSize) { zonePos = 1 - zoneSize; zoneVel = 0; }

        boolean inside = fishPos >= zonePos && fishPos <= zonePos + zoneSize;
        progress += inside ? 0.0055 : -0.0035;

        if (progress >= 1) {
            landCatch();
        } else if (progress <= 0) {
            finish("The " + (hooked.name.equals(OBJ_Fish.OLD_BOOT) ? "boot" : hooked.name) + " got away...", null);
            gp.playSE(15);
        }
    }

    void landCatch() {
        gp.playSE(29);
        if (!gp.player.canObtainItem(hooked)) {
            finish("Your bag is full. You let the " + hooked.name + " go.", hooked);
            return;
        }
        if (hooked.name.equals(OBJ_Fish.OLD_BOOT)) {
            finish("You fished up... an Old Boot.", hooked);
        } else {
            totalCaught++;
            finish("You caught a " + hooked.name + "!", hooked);
            gp.playSE(hooked.name.equals(OBJ_Fish.GOLDEN_CARP) ? 4 : 1);
        }
    }

    // ------------------------------------------------------------------ draw

    int toScreenX(int wx) { return wx - gp.player.worldX + gp.player.screenX; }
    int toScreenY(int wy) { return wy - gp.player.worldY + gp.player.screenY; }

    public void draw(Graphics2D g2) {
        if (phase == IDLE) return;
        int ts = gp.tileSize;

        if (phase == WAITING || phase == BITE || phase == REELING) {
            drawLineAndBobber(g2);
        }

        if (phase == WAITING) {
            hint(g2, "Wait for a bite...   (Esc to stop)");
        }

        if (phase == BITE) {
            int bx = toScreenX(bobberX), by = toScreenY(bobberY);
            g2.setFont(gp.ui.delicatus.deriveFont(java.awt.Font.BOLD, 64f));
            int bounce = (int) (Math.sin(gp.frameCount / 3.0) * 4);
            g2.setColor(Color.black);
            g2.drawString("!", bx - 6 + 3, by - ts / 2 + bounce + 3);
            g2.setColor(new Color(255, 214, 79));
            g2.drawString("!", bx - 6, by - ts / 2 + bounce);
            hint(g2, "Press ENTER!");
        }

        if (phase == REELING) {
            drawReelBar(g2);
            hint(g2, "Hold ENTER to raise the bar. Keep the fish inside it!");
        }

        if (phase == RESULT) {
            drawResult(g2);
        }
    }

    void drawLineAndBobber(Graphics2D g2) {
        int ts = gp.tileSize;
        // Rod tip: a little in front of the player, up high
        int px = gp.player.screenX + ts / 2, py = gp.player.screenY + ts / 3;
        switch (gp.player.direction) {
            case "up": py -= ts / 2; break;
            case "down": py += ts / 4; px += ts / 3; break;
            case "left": px -= ts / 2; break;
            default: px += ts / 2; break;
        }
        int bx = toScreenX(bobberX), by = toScreenY(bobberY);

        // Gentle bob, a dip on a nibble, pulled under on a bite or while reeling
        int bob = (int) (Math.sin(gp.frameCount / 15.0) * 2);
        if (phase == WAITING && nibble > 0) bob = 5;
        if (phase == BITE) bob = 8 + (gp.frameCount / 3 % 2) * 3;
        if (phase == REELING) bob = 6 + (int) (Math.sin(gp.frameCount / 2.0) * 3);
        by += bob;

        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(240, 240, 240, 200));
        int midX = (px + bx) / 2, midY = Math.max(py, by) + 10; // a little sag
        g2.drawLine(px, py, midX, midY);
        g2.drawLine(midX, midY, bx, by - 8);
        g2.setStroke(new BasicStroke(1f));

        // Ripple
        g2.setColor(new Color(255, 255, 255, 90));
        int rip = 10 + (gp.frameCount / 4 % 8);
        g2.drawOval(bx - rip, by + 2 - rip / 3, rip * 2, rip * 2 / 3);

        // Bobber: red top, white bottom
        if (phase != REELING) {
            g2.setColor(Color.white);
            g2.fillOval(bx - 8, by - 8, 16, 16);
            g2.setColor(new Color(229, 57, 53));
            g2.fillArc(bx - 8, by - 8, 16, 16, 0, 180);
            g2.setColor(new Color(20, 16, 20));
            g2.drawOval(bx - 8, by - 8, 16, 16);
        }
    }

    void drawReelBar(Graphics2D g2) {
        int barW = 60, barH = 360;
        int x = gp.screenWidth - 220;
        int y = gp.screenHeight / 2 - barH / 2;

        gp.ui.drawSubWindow(x - 20, y - 40, barW + 110, barH + 70);

        // Water column
        g2.setColor(new Color(36, 76, 120));
        g2.fillRect(x, y, barW, barH);

        // Catch bar (green while the fish is inside it)
        boolean inside = fishPos >= zonePos && fishPos <= zonePos + zoneSize;
        int zoneTop = y + (int) ((1 - zonePos - zoneSize) * barH);
        int zoneH = (int) (zoneSize * barH);
        g2.setColor(inside ? new Color(110, 220, 110, 220) : new Color(110, 200, 110, 140));
        g2.fillRect(x + 3, zoneTop, barW - 6, zoneH);
        g2.setColor(new Color(220, 255, 220));
        g2.drawRect(x + 3, zoneTop, barW - 7, zoneH - 1);

        // Fish
        int fy = y + (int) ((1 - fishPos) * barH) - 20;
        if (hooked.down1 != null) {
            g2.drawImage(hooked.down1, x + barW / 2 - 20, fy, 40, 40, null);
        }

        // Catch meter beside the bar
        int mx = x + barW + 16, mw = 16;
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(mx, y, mw, barH);
        int fillH = (int) (Math.max(0, Math.min(1, progress)) * barH);
        float hue = (float) (progress * 0.33); // red to green
        g2.setColor(Color.getHSBColor(hue, 0.8f, 0.95f));
        g2.fillRect(mx, y + barH - fillH, mw, fillH);
        g2.setColor(Color.white);
        g2.drawRect(x, y, barW, barH);
        g2.drawRect(mx, y, mw, barH);
    }

    void drawResult(Graphics2D g2) {
        int w = 620, h = 200;
        int x = gp.screenWidth / 2 - w / 2, y = gp.screenHeight / 2 - h / 2 - 60;
        gp.ui.drawSubWindow(x, y, w, h);
        int textX = x + 40;
        if (resultItem != null && resultItem.down1 != null) {
            g2.drawImage(resultItem.down1, x + 30, y + h / 2 - 48, 96, 96, null);
            textX = x + 150;
        }
        g2.setColor(Color.white);
        g2.setFont(gp.ui.delicatus.deriveFont(34f));
        g2.drawString(resultText, textX, y + h / 2);
        g2.setFont(gp.ui.delicatus.deriveFont(22f));
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("ENTER to continue", textX, y + h / 2 + 46);
    }

    void hint(Graphics2D g2, String text) {
        g2.setFont(gp.ui.delicatus.deriveFont(28f));
        int x = gp.ui.getXforCenteredText(text);
        int y = gp.screenHeight - 60;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
}
