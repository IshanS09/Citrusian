package main;

import entity.PlayerDummy;
import entity.entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import monster.MON_BaronRotjaw;
import monster.MON_BossBroccoli;
import monster.MON_SlimeKing;
import object.OBJ_ironDoor;

public class CutsceneManager {

    gamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    public int sceneTimer = 0;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;
    
    public final int NA = 0;
    public final int maliBroc = 1;
    public final int ending = 2;
    public final int BaronRotjaw = 3;
    public final int pea_King = 4;
    public final int intro = 5;

    public boolean allowDialogue = false;

    // Where each boss cutscene drops its iron door: {map, col, row}.
    // The boss arena is always north of the door, the way in is the tile just south of it.
    public static final int[][] BOSS_DOORS = {
        {3, 57, 48},  // Malibroc
        {4, 50, 44},  // Baron Rotjaw
        {5, 51, 36},  // Pea King
    };

    boolean bossDefeated(int map) {
        switch(map) {
            case 3: return data.Progress.maliBrocDefeated;
            case 4: return data.Progress.baronRotjawDefeated;
            case 5: return data.Progress.peaKingDefeated;
        }
        return true;
    }

    /**
     * Run after loading a save. The cutscene door and the boss wake-up only exist while a
     * fight is running, so a save made mid-fight (or by an older version) can come back with
     * a sleeping boss and a door that never opens. This removes any boss door and, if the
     * boss is still alive, puts the player back outside the arena so walking in replays the
     * cutscene and starts the fight properly.
     */
    public void fixBossRoomsAfterLoad() {
        for (int[] b : BOSS_DOORS) {
            int map = b[0], col = b[1], row = b[2];
            for (int i = 0; i < gp.obj[map].length; i++) {
                entity o = gp.obj[map][i];
                if (o != null && OBJ_ironDoor.objName.equals(o.name)
                        && o.worldX == col * gp.tileSize && o.worldY == row * gp.tileSize) {
                    gp.obj[map][i] = null;
                }
            }
            if (gp.currentMap == map && !bossDefeated(map) && gp.player.worldY < row * gp.tileSize) {
                gp.player.worldX = col * gp.tileSize;
                gp.player.worldY = (row + 1) * gp.tileSize;
                gp.player.direction = "down";
            }
        }
    }

    /** Drops the iron door behind the player, unless one is already there. */
    void lockBossDoor(int col, int row) {
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            entity o = gp.obj[gp.currentMap][i];
            if (o != null && OBJ_ironDoor.objName.equals(o.name)
                    && o.worldX == col * gp.tileSize && o.worldY == row * gp.tileSize) {
                o.temp = true;
                return;
            }
        }
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = new OBJ_ironDoor(gp);
                gp.obj[gp.currentMap][i].worldX = col * gp.tileSize;
                gp.obj[gp.currentMap][i].worldY = row * gp.tileSize;
                gp.obj[gp.currentMap][i].temp = true;
                gp.playSE(19);
                return;
            }
        }
    }

    /** Opens every door a boss cutscene locked on the current map. Called when a boss dies. */
    public void openBossDoors() {
        boolean opened = false;
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            entity o = gp.obj[gp.currentMap][i];
            if (o != null && o.temp && OBJ_ironDoor.objName.equals(o.name)) {
                gp.obj[gp.currentMap][i] = null;
                opened = true;
            }
        }
        if (opened) gp.playSE(19);
    }

    public CutsceneManager(gamePanel gp) {
        this.gp = gp;

        endCredit = "Developed by Ishan Singh\n" +
                "Assistant Programmer: Johnathan castillo\n" +
                "Beta Testing by Caleb Sanchez, Armaan Iyer\n" +
                "Art by Ishan Singh and Armaan Iyer\n" +
                "Music by Caleb Sanchez\n" +
                "Story by Ishan Singh\n\n" +
                "Thank you for playing!";
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch(sceneNum) {
            case maliBroc: scene_maliBroc(); break;
            case ending: scene_ending(); break;
            case BaronRotjaw:  scene_baronRotjaw(); break;
            case pea_King: scene_peaKing(); break;
            case intro: scene_intro(); break;
        }
    }

    public void scene_maliBroc() {

    if (scenePhase == 0) {
        gp.bossBattleOn = true;
        gp.ui.npc = null; 
 
        lockBossDoor(57, 48);


        for (int i = 0; i < gp.npc[1].length; i++) {
            if (gp.npc[gp.currentMap][i] == null) {
                gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                gp.npc[gp.currentMap][i].direction = gp.player.direction;
                break;
            }
        }

        gp.player.drawing = false;
        scenePhase++;
    }

    else if (scenePhase == 1) {
        gp.player.worldY -= 2;
        if (gp.player.worldY < gp.tileSize * 40) {
            scenePhase++;
        }
    }

    else if (scenePhase == 2) {
 
        for (int i = 0; i < gp.monster[1].length; i++) {
            if (gp.monster[gp.currentMap][i] != null &&
                gp.monster[gp.currentMap][i].name.equals(MON_BossBroccoli.monName)) {

                MON_BossBroccoli boss = (MON_BossBroccoli) gp.monster[gp.currentMap][i];
                boss.sleep = false;
                boss.idleMode = true; 
                scenePhase++;
                break;
            }
        }
    }

    else if (scenePhase == 3) {
   
        if (sceneTimer == 0) {
            gp.playSE(21); 
        }

        sceneTimer++;

        if (sceneTimer > 480) {

            for (int i = 0; i < gp.monster[1].length; i++) {
                if (gp.monster[gp.currentMap][i] != null &&
                    gp.monster[gp.currentMap][i].name.equals(MON_BossBroccoli.monName)) {
                    ((MON_BossBroccoli) gp.monster[gp.currentMap][i]).idleMode = false;
                    break;
                }
            }

            scenePhase++;
            sceneTimer = 0;
        }
    }

    else if (scenePhase == 4) {

        for (int i = 0; i < gp.npc[1].length; i++) {
            if (gp.npc[gp.currentMap][i] != null &&
                gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {

                gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                gp.npc[gp.currentMap][i] = null;
                break;
            }
        }

        gp.player.drawing = true;
        sceneNum = NA;
        scenePhase = 0;
        sceneTimer = 0;

        gp.stopMusic();
        gp.playMusic(20);
        gp.gameState = gp.playState;
        }
    }

    public void scene_baronRotjaw() {

    if (scenePhase == 0) {
        gp.bossBattleOn = true;

        // Lock the door
        lockBossDoor(50, 44);

        // Create player dummy
        for (int i = 0; i < gp.npc[1].length; i++) {
            if (gp.npc[gp.currentMap][i] == null) {
                gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                gp.npc[gp.currentMap][i].direction = gp.player.direction;
                break;
            }
        }

        gp.player.drawing = false;
        scenePhase++;
    }

    else if (scenePhase == 1) {
    // Move player forward
    gp.player.worldY -= 2;
    if (gp.player.worldY < gp.tileSize * 38) { // Adjust as needed
            scenePhase++;
        }
    }

    else if (scenePhase == 2) {
    // Wake up Baron Rotjaw and set him as the dialogue NPC
    for (int i = 0; i < gp.monster[1].length; i++) {
        if (gp.monster[gp.currentMap][i] != null &&
            gp.monster[gp.currentMap][i].name.equals(MON_BaronRotjaw.monName)) {

            gp.monster[gp.currentMap][i].sleep = false;
            
            // Set Baron Rotjaw as the NPC for dialogue
            gp.ui.npc = gp.monster[gp.currentMap][i];
            gp.ui.npc.dialogueSet = 0;
            gp.ui.npc.dialogueIndex = 0;
            gp.gameState = gp.cutsceneState;
            
            allowDialogue = true;  // ADD THIS
            
            scenePhase++;
            break;
        }
    }
}

    else if (scenePhase == 3) {
        // Wait for dialogue to complete
        // The dialogue screen will automatically advance scenePhase when complete
    }

    else if (scenePhase == 4) {
    // Restore player position from dummy
    for (int i = 0; i < gp.npc[1].length; i++) {
        if (gp.npc[gp.currentMap][i] != null &&
            gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {

            gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
            gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
            gp.npc[gp.currentMap][i] = null;
            break;
        }
    }

    gp.player.drawing = true;
    allowDialogue = false;  // ADD THIS
    sceneNum = NA;
    scenePhase = 0;
    sceneTimer = 0;

    gp.stopMusic(); 
    gp.playMusic(22);
    gp.gameState = gp.playState;
}
    }

    public void scene_peaKing() {

    if (scenePhase == 0) {
        gp.bossBattleOn = true;
        gp.ui.npc = null; // Clear any previous dialogue

        // Lock the door
        lockBossDoor(51, 36);

        // Create player dummy
        for (int i = 0; i < gp.npc[1].length; i++) {
            if (gp.npc[gp.currentMap][i] == null) {
                gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                gp.npc[gp.currentMap][i].direction = gp.player.direction;
                break;
            }
        }

        gp.player.drawing = false;
        scenePhase++;
    }

    else if (scenePhase == 1) {
        // Move player toward Slime King (adjust direction as needed)
        gp.player.worldY -= 2; // Change direction based on your map layout
        if (gp.player.worldY < gp.tileSize * 30) { // Adjust target position
            scenePhase++;
        }
    }

    else if (scenePhase == 2) {
    // Wake up Slime King and set him as the dialogue NPC
    for (int i = 0; i < gp.monster[1].length; i++) {
        if (gp.monster[gp.currentMap][i] != null &&
            gp.monster[gp.currentMap][i].name.equals(MON_SlimeKing.monName)) {

            gp.monster[gp.currentMap][i].sleep = false;
            
            // Set Slime King as the NPC for dialogue
            gp.ui.npc = gp.monster[gp.currentMap][i];
            gp.ui.npc.dialogueSet = 0;
            gp.ui.npc.dialogueIndex = 0;
            gp.gameState = gp.cutsceneState;
            
            allowDialogue = true;  // ADD THIS
            
            scenePhase++;
            break;
        }
    }
}

    else if (scenePhase == 3) {
        // Wait for dialogue to complete
    }

    else if (scenePhase == 4) {
    // Restore player position from dummy
    for (int i = 0; i < gp.npc[1].length; i++) {
        if (gp.npc[gp.currentMap][i] != null &&
            gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)) {

            gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
            gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
            gp.npc[gp.currentMap][i] = null;
            break;
        }
    }

    gp.player.drawing = true;
    gp.ui.npc = null; // Clear dialogue NPC
    allowDialogue = false;  // ADD THIS
    sceneNum = NA;
    scenePhase = 0;
    sceneTimer = 0;

    gp.stopMusic();
    gp.playMusic(26);
    gp.gameState = gp.playState;
}
    }

    public void scene_ending() {
        if(scenePhase == 0) {
            gp.stopMusic();
            gp.ui.npc = null;  // ADD THIS: Clear any NPC dialogue
            gp.gameState = gp.cutsceneState;  // Make sure you're in cutscene state
            scenePhase++;
        }
        if(scenePhase == 1) {
            gp.playSE(4);
            scenePhase++;
        }
        if(scenePhase == 2) {
            if(counterReached(300) == true) {
                scenePhase++;
            }
        }
        if(scenePhase == 3) {
            alpha+= 0.005f;
            if(alpha > 1f) {
                alpha = 1f;
            }
            drawBlackBackground(alpha);
            if(alpha == 1f) {
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 4) {
            drawBlackBackground(1f);
            alpha+= 0.005f;
            if(alpha > 1f) {
                alpha = 1f;
            }
            String text = "After centuries, the Citrusian Helm is \nin the hands of a fruit. \n" +
                "The Rotroot Clan is no more... for now...\n" + "As darkness still roams below the soil...";
            
            drawString(alpha, 48f, gp.screenHeight/2, text, 60);
            
            if(counterReached(600) == true) {
                gp.playSE(24);
                scenePhase++;
            }
        }
        if(scenePhase == 5) {
            drawBlackBackground(1f);

            drawString(1f, 120f, gp.screenHeight/2, "   Citrusian", 60);

            if(counterReached(480) == true) {
                scenePhase++;
            }
        }
        if(scenePhase == 6) {
            drawBlackBackground(1f);

            y= gp.screenHeight/2;
            drawString(1f, 38f, y, endCredit, 40);
            
            if(counterReached(480) == true) {
                scenePhase++;
            }
        }
        if(scenePhase == 7) {
            drawBlackBackground(1f);

            y--;
            drawString(1f, 38f, y, endCredit, 40);

            // Once the credits scroll off (or Enter is pressed), offer New Game+
            if(y < -400 || gp.keyH.enterPressed) {
                gp.keyH.enterPressed = false;
                scenePhase++;
            }
        }
        if(scenePhase == 8) {
            drawBlackBackground(1f);
            drawString(1f, 64f, gp.screenHeight/2 - 80, "Thank you for playing!", 60);
            drawString(1f, 36f, gp.screenHeight/2 + 20,
                "Press ENTER to start New Game+ " + (gp.ngPlus + 1) + "\n" +
                "Keep your level and gear. Face tougher veggies.", 50);

            if(gp.keyH.enterPressed) {
                gp.keyH.enterPressed = false;
                gp.startNewGamePlus();
            }
        }
    }

    public void scene_intro() {
    if (scenePhase == 0) {
        // Fade in from black
        drawBlackBackground(1f);
        scenePhase++;
    }
    
    if (scenePhase == 1) {
        drawBlackBackground(1f);
        alpha += 0.005f;
        if (alpha > 1f) {
            alpha = 1f;
        }
        
        String text = "Long ago, the Citrusians ruled the land...\n" +
                     "Until the Rotroot Clan rose from the soil...";
        
        drawString(alpha, 40f, gp.screenHeight/2, text, 60);
        
        if (counterReached(300) == true) {
            alpha = 0;
            scenePhase++;
        }
    }
    
    if (scenePhase == 2) {
        drawBlackBackground(1f);
        alpha += 0.005f;
        if (alpha > 1f) {
            alpha = 1f;
        }
        
        String text = "Now, a lone orange warrior must reclaim\n" +
                     "the legendary Citrusian Helm...\n" +
                     "And defeat the evil vegetables once and for all...";
        
        drawString(alpha, 40f, gp.screenHeight/2, text, 60);
        
        if (counterReached(360) == true) {
            scenePhase++;
        }
    }
    
    if (scenePhase == 3) {
        // Fade to gameplay
        alpha += 0.02f;
        if (alpha > 1f) {
            alpha = 1f;
        }
        drawBlackBackground(1f - alpha);
        
        if (alpha >= 1f) {
            alpha = 0;
            sceneNum = NA;
            scenePhase = 0;
            counter = 0;
            gp.gameState = gp.playState;
            gp.playMusic(0); // Start your main game music
        }
    }
}

    public boolean counterReached(int target) {
        boolean counterReached = false;
        
        counter++;
        if(counter > target) {
            counterReached = true;
            counter = 0;    
        }
        
        return counterReached;
    }
    public void drawBlackBackground(float alpha) {
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    g2.setColor(Color.black);
    
    // In fullscreen, we need to cover the ENTIRE screen (including letterboxing)
    // So we draw a much larger rectangle
    if (gp.isFullScreen()) {
        // Draw over entire panel - use very large coordinates to ensure full coverage
        g2.fillRect(-5000, -5000, 15000, 15000);
    } else {
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
    
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
}

public void drawString(float alpha, float fontSize, int startY, String text, int lineHeight) {
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    g2.setColor(Color.white);
    g2.setFont(gp.ui.delicatus.deriveFont(fontSize));
    String[] lines = text.split("\n");
    
    // Center the block vertically
    int totalHeight = lines.length * lineHeight;
    int y = startY - totalHeight / 2;  // centers the block around startY

    for (String line : lines) {
        int x = gp.ui.getXforCenteredText(line) - 300;
        g2.drawString(line, x, y);
        y += lineHeight;
    }

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
}
}