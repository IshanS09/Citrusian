package main;

import entity.PlayerDummy;
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
 
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = new OBJ_ironDoor(gp);
                gp.obj[gp.currentMap][i].worldX = 57 * gp.tileSize;
                gp.obj[gp.currentMap][i].worldY = 48 * gp.tileSize;
                gp.obj[gp.currentMap][i].temp = true;
                gp.playSE(19);
                break;
            }
        }


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
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = new OBJ_ironDoor(gp);
                gp.obj[gp.currentMap][i].worldX = 50 * gp.tileSize; // Adjust coordinates as needed
                gp.obj[gp.currentMap][i].worldY = 44 * gp.tileSize;
                gp.obj[gp.currentMap][i].temp = true;
                gp.playSE(19);
                break;
            }
        }

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
    System.out.println("Phase 1: Player worldY = " + gp.player.worldY + ", Target = " + (gp.tileSize * 40)); // DEBUG
    if (gp.player.worldY < gp.tileSize * 38) { // Adjust as needed
            System.out.println("Phase 1 complete, moving to phase 2"); // DEBUG
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
            System.out.println("Set dialogue, gameState = " + gp.cutsceneState);
            gp.gameState = gp.cutsceneState;
            
            allowDialogue = true;  // ADD THIS
            
            scenePhase++;
            break;
        }
    }
    System.out.println("End of phase 2, scenePhase = " + scenePhase);
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
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = new OBJ_ironDoor(gp);
                gp.obj[gp.currentMap][i].worldX = 51 * gp.tileSize; // Adjust coordinates as needed
                gp.obj[gp.currentMap][i].worldY = 36 * gp.tileSize;
                gp.obj[gp.currentMap][i].temp = true;
                gp.playSE(19);
                break;
            }
        }

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