package main;

import entity.entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import object.OBJ_coin;
import object.OBJ_heart;
import object.OBJ_mana;

public class UI {
    gamePanel gp;
    Graphics2D g2;
    Font delicatus;
    BufferedImage heartFull, heartHalf, heartBlank, seed_full, seed_empty, coin;
    BufferedImage citrusianHelmet;
    public boolean messageOn = false; 
    ArrayList<String> message = new ArrayList<>(); 
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false; 
    public String currentDialogue = ""; 
    public int commandNum = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public entity npc;
    int charIndex = 0;
    String combinedText = "";


    public UI(gamePanel gp) {
        this.gp = gp;
       
        try {
            InputStream is = getClass().getResourceAsStream("/res/fonts/Delicatus.ttf");
            delicatus = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            citrusianHelmet = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/res/Objects/CitrusianHelmet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   

        entity heart = new OBJ_heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2; 
        heartBlank = heart.image3;
        entity seed = new OBJ_mana(gp);
        seed_full = seed.image;
        seed_empty = seed.image2;
        entity Goldcoin = new OBJ_coin(gp);
        coin = Goldcoin.down1;

    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2) {
       this.g2 = g2;  
       g2.setFont(delicatus);
       g2.setColor(Color.white);

       if(gp.gameState == gp.titleState) {
           drawTitleScreen();
       }
       if(gp.gameState == gp.playState) {
           drawPlayerLife();
           drawMonsterLife();
           drawMessage();
       }
       if(gp.gameState == gp.pauseState) {
           drawPlayerLife();
           drawPauseScreen();
       }
       if(gp.gameState == gp.dialogueState) {
           drawPlayerLife();
           drawDialogueScreen();
       }
       if(gp.gameState == gp.cutsceneState) {
            if(gp.ui.npc != null && (gp.gameState != gp.cutsceneState || gp.csManager.allowDialogue)) {
                drawDialogueScreen();
            }
        }
       if(gp.gameState == gp.characterState) {
           drawCharacterScreen();
           drawInventory(gp.player, true);
       }
       if(gp.gameState == gp.optionsState) {
           drawOptionsScreen();
       }
       if(gp.gameState == gp.gameOverState) {
           drawGameOverScreen();
       }
       if(gp.gameState == gp.transitionState) {
           drawTransition();
       }
       if(gp.gameState == gp.tradeState) {
           drawTradeScreen();
       }
       if(gp.gameState == gp.sleepState) {
            drawSleepScreen();
       }
    }
    public void drawMonsterLife() {

        for(int i = 0; i < gp.monster[1].length; i++) {

            if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].inCamera() == true) {

            //     if(gp.monster[gp.currentMap][i].hpBarOn == true && gp.monster[gp.currentMap][i].boss == false) {

            //         double oneScale = (double)gp.tileSize / gp.monster[gp.currentMap][i].maxLife; 
            //         double hpBarValue = oneScale * gp.monster[gp.currentMap][i].life; 

            //         g2.setColor(new Color(35,35,35)); 
            //         g2.fillRect(gp.monster[gp.currentMap][i].getScreenX()-1, gp.monster[gp.currentMap][i].getScreenY()-16, gp.tileSize + 2, 12); 

            //         g2.setColor(new Color(255,0,30)); 
            //         g2.fillRect(gp.monster[gp.currentMap][i].getScreenX(), gp.monster[gp.currentMap][i].getScreenY() - 15, (int)hpBarValue, 10); 

            //         gp.monster[gp.currentMap][i].hpBarCounter++; 

            //     if(gp.monster[gp.currentMap][i].hpBarCounter > 600) { 
            //         gp.monster[gp.currentMap][i].hpBarCounter = 0; 
            //         gp.monster[gp.currentMap][i].hpBarOn = false; 
            //     } 
            // } 
            if (gp.monster[gp.currentMap][i].boss == true) {
                double oneScale = (double)gp.tileSize*8 / gp.monster[gp.currentMap][i].maxLife; 
                double hpBarValue = oneScale * gp.monster[gp.currentMap][i].life; 

                int x = gp.screenWidth/2 - (gp.tileSize*4);
                int y = gp.tileSize*12;

                g2.setColor(new Color(35,35,35)); 
                g2.fillRect(x-1, y-1, gp.tileSize*8 + 2, 22); 

                g2.setColor(new Color(255,0,30)); 
                g2.fillRect(x, y, (int)hpBarValue, 20); 

                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
                g2.setColor(Color.white);
                g2.drawString(gp.monster[gp.currentMap][i].name, x+4, y-10);
            }
        }
    }
}
    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < message.size(); i++) {
            if(message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 5, messageY + 5); // Shadow effect
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                int counter = messageCounter.get(i);
                counter++;
                messageCounter.set(i, counter);

                if(counter > 180) { // Message will disappear after 120 frames
                    message.remove(i);
                    messageCounter.remove(i);
                    i--;
                } else {
                    messageY += 40; // Move down for the next message
                }
            }
        }
    }
    public void drawPlayerLife() {
    int x = gp.tileSize / 2;
    int y = gp.tileSize / 2;
    int i = 0;

    // Draw empty hearts
    while (i < gp.player.maxLife / 2) {
        g2.drawImage(heartBlank, x, y, null);
        i++;
        x += gp.tileSize;
    }

    // Reset x and i to draw the actual life
    x = gp.tileSize / 2;
    i = 0;

    while (i < gp.player.life) {
        if (i + 1 < gp.player.life) {
            // Draw full heart (2 HP)
            g2.drawImage(heartFull, x, y, null);
            i += 2;
        } else {
            // Draw half heart (1 HP)
            g2.drawImage(heartHalf, x, y, null);
            i++;
        }
        x += gp.tileSize;
    }

    x = gp.tileSize/2;
    y = gp.tileSize*2;
    i = 0;
    while(i < gp.player.maxSeed) {
        g2.drawImage(seed_empty, x, y, null);
        i++;
        x += 45;
    }

    x = gp.tileSize/2;
    y = gp.tileSize*2;
    i = 0;
    while(i < gp.player.seed) {
        g2.drawImage(seed_full, x, y, null);
        i++;
        x += 45;
    }

}

public void drawDialogueScreen() {
    int x = gp.tileSize * 3;
    int y = gp.tileSize / 2;
    int width = gp.screenWidth - (gp.tileSize * 6);
    int height = gp.tileSize * 4;
    drawSubWindow(x, y, width, height);
    
    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
    x += gp.tileSize;
    y += gp.tileSize;

    if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
        
        // currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];

        char characters[] = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

        if(charIndex < characters.length) {
            gp.playSE(17);
            String s = String.valueOf(characters[charIndex]);
            combinedText += s;
            currentDialogue = combinedText;
            charIndex++;
        }

        if(gp.keyH.enterPressed == true) {
            charIndex = 0;
            combinedText = "";

            if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState) {
                npc.dialogueIndex++;
                gp.keyH.enterPressed = false; 
            }
        }
    } else {
        npc.dialogueIndex = 0;

        if(gp.gameState == gp.dialogueState) {
            gp.gameState = gp.playState;
        }
        if(gp.gameState == gp.cutsceneState) {
            gp.csManager.scenePhase++;
        }
    }

    for(String line : currentDialogue.split("\n")) {
        g2.drawString(line, x, y);
        y += 40; 
    }
    

    }
    public void drawTitleScreen() {
        g2.setColor(new Color(80, 100, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Citrusian";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 2;

        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5); // Draw shadow effect

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2;
        y += gp.tileSize * 2; 
        g2.drawImage(citrusianHelmet, x - (gp.tileSize * 2), y - (gp.tileSize * 2)+30, gp.tileSize * 4, gp.tileSize * 4, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4; // Move down for the next option
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y); 
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize; // Move down for the next option
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y); 
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize; // Move down for the next option
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y); 
        }
    }
    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);

    }
    public void drawCharacterScreen() {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 8;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + 40;
        final int lineHeight = 35;

        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("FireSeed", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Crop Coin", textX, textY);
        textY += lineHeight + 25;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 40;
        g2.drawString("Shield", textX, textY);
        textY += lineHeight;
        
        
        int tailX = (frameX + frameWidth) - 20;
        textY = frameY + 40;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.seed + "/" + gp.player.maxSeed);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-24, null);
        textY += gp.tileSize;

        g2.drawImage(gp.player.currentSHield.down1, tailX - gp.tileSize, textY-24, null);
        textY += gp.tileSize;

    }
    public void drawInventory(entity entity, boolean cursor) {

    int frameX = 0;
    int frameY = 0;
    int frameWidth = 0;
    int frameHeight = 0;
    int slotCol = 0;
    int slotRow = 0;

    if(entity == gp.player) {
        frameX = (gp.tileSize * 9) + 250;
        frameY = gp.tileSize;
        frameWidth = gp.tileSize * 6;
        frameHeight = gp.tileSize * 5;
        slotCol = playerSlotCol;
        slotRow = playerSlotRow;
    } else {
        frameX = gp.tileSize * 2;
        frameY = gp.tileSize;
        frameWidth = gp.tileSize * 6;
        frameHeight = gp.tileSize * 5;
        slotCol = npcSlotCol;
        slotRow = npcSlotRow;
    }

    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    final int slotXstart = frameX + 20;
    final int slotYstart = frameY + 20;
    int slotX = slotXstart;
    int slotY = slotYstart;
    int slotSize = gp.tileSize + 3;

    for(int i = 0; i < entity.inventory.size(); i++) {

        if(entity.inventory.get(i) == entity.currentWeapon || 
           entity.inventory.get(i) == entity.currentSHield || 
           entity.inventory.get(i) == entity.currentLight) {
            g2.setColor(new Color(240, 190, 90));
            g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
        }

        g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

        // // ==== Durability Drawing Logic ====
        // entity item = entity.inventory.get(i);
        // if(item.type == item.type_sword || 
        //    item.type == item.type_shield || 
        //    item.type == item.type_light || 
        //    item.type == item.type_picaxe) {

        //     int dur = item.durability;
        //     int maxDur = item.maxDurability;
        //     float ratio = (float) dur / maxDur;

        //     Color color;
        //     if (ratio > 0.6f) color = Color.white;
        //     else if (ratio > 0.3f) color = Color.yellow;
        //     else color = Color.red;

        //     String text = dur <= 0 ? "BROKEN" : dur + "/" + maxDur;
        //     g2.setFont(g2.getFont().deriveFont(18F));
        //     g2.setColor(color);
        //     g2.drawString(text, slotX + 4, slotY + gp.tileSize - 4);
        // }
        // // ==================================

        if(entity == gp.player && entity.inventory.get(i).amount > 1) {
            g2.setFont(g2.getFont().deriveFont(32F));
            String s = "" + entity.inventory.get(i).amount;
            int amountX = getXforAlignText(s, slotX + 54);
            int amountY = slotY + gp.tileSize;

            g2.setColor(new Color(60, 60, 60));
            g2.drawString(s, amountX, amountY);

            g2.setColor(Color.white);
            g2.drawString(s, amountX - 3, amountY - 3);
        }

        slotX += slotSize;

        if(i == 4 || i == 9 || i == 14) {
            slotX = slotXstart;
            slotY += slotSize;
        }
    }

    // ===== Cursor and Tooltip =====
    if(cursor == true) {
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3;

        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndex(slotCol, slotRow);

        if(itemIndex < entity.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            entity hoveredItem = entity.inventory.get(itemIndex);

            for(String line : hoveredItem.description.split("\n")) {
                g2.setColor(Color.white);
                g2.drawString(line, textX, textY);
                textY += 32;
            }

            // Tooltip Durability for Equipment
            if(hoveredItem.type == hoveredItem.type_sword || 
               hoveredItem.type == hoveredItem.type_shield || 
               hoveredItem.type == hoveredItem.type_axe || 
               hoveredItem.type == hoveredItem.type_light|| 
               hoveredItem.type == hoveredItem.type_picaxe) {

                String durabilityText;
                if (hoveredItem.durability <= 0) {
                    durabilityText = "Durability: BROKEN";
                    g2.setColor(Color.red);
                } else {
                    durabilityText = "Durability: " + hoveredItem.durability + "/" + hoveredItem.maxDurability;
                    float ratio = (float) hoveredItem.durability / hoveredItem.maxDurability;
                    if (ratio > 0.6f) g2.setColor(Color.green);
                    else if (ratio > 0.3f) g2.setColor(Color.yellow);
                    else g2.setColor(Color.red);
                }

                g2.drawString(durabilityText, textX, textY);
            }
        }
    }
}



    public void drawGameOverScreen() {
    g2.setColor(new Color(0,0,0,150));
    
    // Cover entire screen in fullscreen mode
    if(gp.isFullScreen()) {
        g2.fillRect(-5000, -5000, 15000, 15000);
    } else {
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
    
    int x;
    int y;
    String text;
    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
    text = "Game Over";
    g2.setColor(Color.black);
    x= getXforCenteredText(text);
    y = gp.tileSize * 4;
    g2.drawString(text, x, y);
    g2.setColor(Color.white);
    g2.drawString(text, x-4, y-4);
    g2.setFont(g2.getFont().deriveFont(50f));
    text = "Try Again";
    x = getXforCenteredText(text);
    y += gp.tileSize*4;
    g2.drawString(text, x, y);
    if(commandNum == 0) {
        g2.drawString(">", x-40, y);
    }
    text = "Quit";
    x = getXforCenteredText(text);
    y += 55;
    g2.drawString(text, x, y);
    if(commandNum == 1) {
        g2.drawString(">", x-40, y);
    }
}
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.tileSize * 6;
        int frameY = gp. tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: options_top(frameX, frameY); break;
            case 1: options_control(frameX, frameY);break;
            case 2: options_endGameConfirmation(frameX, frameY); break;
        }
    }
    public void options_top(int frameX, int frameY) {
    int textX;
    int textY;

    String text = "Options";
    textX = getXforCenteredText(text)-50;
    textY = frameY + (gp.tileSize / 2) + 20;
    g2.drawString(text, textX, textY);

    // Vertical spacing between lines
    int spacing = gp.tileSize; // 64 pixels

    // MUSIC
    textX = frameX + gp.tileSize;
    textY += spacing;
    g2.drawString("Music", textX, textY);
    if(commandNum == 0) {
        g2.drawString(">", textX-25, textY);
    }

    // SE
    textY += spacing;
    g2.drawString("Sound Effects", textX, textY);
    if(commandNum == 1) {
        g2.drawString(">", textX-25, textY);
    }

    // CONTROL
textY += spacing;
g2.drawString("Controls", textX, textY);
if(commandNum == 2) {
    g2.drawString(">", textX-25, textY);
    if(gp.keyH.enterPressed == true) {
        subState = 1;
        gp.keyH.enterPressed = false;
    }
}

// END GAME
textY += spacing;
g2.drawString("End Game", textX, textY);
if(commandNum == 3) {
    g2.drawString(">", textX-25, textY);
    if(gp.keyH.enterPressed == true) {
        subState = 2;
        commandNum = 0;
        gp.keyH.enterPressed = false; // ← add this too
    }
}

// BACK
textY += spacing * 2;
g2.drawString("Back", textX, textY);
if(commandNum == 4) {
    g2.drawString(">", textX-25, textY);
    if(gp.keyH.enterPressed == true) {
        gp.gameState = gp.playState;
        commandNum = 0;
    }
}


    //MUSIC VOLUME
    textY += gp.tileSize;
    g2.drawRect(textX + 200, textY - 400, 200, 24);
    int volumeWidth = 40 * gp.music.volumeScale;
    g2.fillRect(textX+200, textY-400, volumeWidth, 24);

    textY += gp.tileSize;
    g2.drawRect(textX + 200, textY - 400, 200, 24);
    volumeWidth = 40 * gp.se.volumeScale;
    g2.fillRect(textX+200, textY-400, volumeWidth, 24);

}
public void options_control(int frameX, int frameY) {
    int textX;
    int textY;

    String text = "Controls";
    textX = getXforCenteredText(text)-40;
    textY = frameY + gp.tileSize-20;
    g2.drawString(text, textX-20, textY);

    textX = frameX + gp.tileSize;
    textY += gp.tileSize;
    g2.drawString("Movement", textX, textY); textY+=gp.tileSize;
    g2.drawString("Attack/Confirm", textX, textY); textY+=gp.tileSize;
    g2.drawString("Shoot", textX, textY); textY+=gp.tileSize;
    g2.drawString("Inventory/Status", textX, textY); textY+=gp.tileSize;
    g2.drawString("Guard", textX, textY); textY+=gp.tileSize;
    g2.drawString("Pause", textX, textY); textY+=gp.tileSize;
    g2.drawString("Save", textX, textY); textY+=gp.tileSize;
    g2.drawString("Options", textX, textY); textY+=gp.tileSize;
    g2.drawString("Fullscreen", textX, textY); textY+=gp.tileSize;

    textX = frameX + gp.tileSize * 6;
    textY = (frameY + gp.tileSize * 2) - 20;
    g2.drawString("WASD", textX, textY); textY+=gp.tileSize;
    g2.drawString("ENTER", textX, textY); textY+=gp.tileSize;
    g2.drawString("F", textX, textY); textY+=gp.tileSize;
    g2.drawString("C", textX, textY); textY+=gp.tileSize;
    g2.drawString("SPACE", textX, textY); textY+=gp.tileSize;
    g2.drawString("P", textX, textY); textY+=gp.tileSize;
    g2.drawString("O", textX, textY); textY+=gp.tileSize;
    g2.drawString("ESC", textX, textY); textY+=gp.tileSize;
    g2.drawString("T", textX, textY); textY+=gp.tileSize;

   textX = frameX + gp.tileSize;
textY = frameY + gp.tileSize * 11; // move "Back" far enough below the keybindings
g2.drawString("Back", textX+175, textY-100);
if(commandNum == 0) {
    g2.drawString(">", textX+150, textY-100);
    if(gp.keyH.enterPressed == true) {
        subState = 0;
        commandNum = 2; // go back with selector still on "Controls"
        gp.keyH.enterPressed = false;
    }
}

}
public void options_endGameConfirmation(int frameX, int frameY) {
   int textX = frameX + gp.tileSize;
   int textY = frameY + gp.tileSize;

   currentDialogue = "   Are you sure you want \nto return to the title screen?";

   for(String line: currentDialogue.split("\n")) {
        g2.drawString(line, textX, textY);
        textY+=40;
   }

   String text = "Yes";
   textX = getXforCenteredText(text)-100;
   textY += gp.tileSize*3;
   g2.drawString(text, textX, textY - 150);   
   if(commandNum == 0) {
        g2.drawString(">", textX-25, textY-150);
        if(gp.keyH.enterPressed == true) {
            gp.stopMusic();
            subState = 0;
            gp.gameState = gp.titleState;
            gp.resetGame(true);
        }
   }

   text = "No";
   textX = getXforCenteredText(text)-100;
   textY += gp.tileSize;
   g2.drawString(text, textX, textY - 150);   
   if(commandNum == 1) {
    g2.drawString(">", textX-25, textY-150);
    if(gp.keyH.enterPressed == true) {
        subState = 0;
        commandNum = 2; 
        gp.keyH.enterPressed = false;
    }
}
}

public void drawTransition() {
    counter++;
    g2.setColor(new Color(0, 0, 0, counter * 4));
    
    // In fullscreen, draw a much larger rectangle to cover letterboxing
    if(gp.isFullScreen()) {
        g2.fillRect(-5000, -5000, 15000, 15000);
    } else {
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
    
    if(counter == 60) {
        counter = 0;
        gp.gameState = gp.playState;
        gp.currentMap = gp.eHandler.tempMap;
        gp.player.worldX = gp.eHandler.tempCol * gp.tileSize;
        gp.player.worldY = gp.eHandler.tempRow * gp.tileSize;
        gp.eHandler.previousEventX = gp.player.worldX;
        gp.eHandler.previousEventY = gp.player.worldY;
        gp.changeArea();
    }
}
public void drawTradeScreen() {
    switch (subState) {
        case 0:
            trade_select();
            break;
        case 1:
            trade_buy();
            break;
        case 2:
            trade_sell();
            break;
    }
    gp.keyH.enterPressed = false;
}
public void trade_select() {
    npc.dialogueSet = 0;
    drawDialogueScreen();

    int x = gp.tileSize * 15;
    int y = gp.tileSize * 4;
    int width = gp.tileSize * 3;
    int height = (int)(gp.tileSize * 3.5);
    drawSubWindow(x, y, width, height);

    x += gp.tileSize;
    y += gp.tileSize;  
    g2.drawString("Buy", x, y);
    if(commandNum == 0) {
        g2.drawString(">", x - 24, y);
        if(gp.keyH.enterPressed == true) {
            subState = 1; // Go to buy state
            gp.keyH.enterPressed = false;
        }
    }
    y+= gp.tileSize;
    
    g2.drawString("Sell", x, y);
     if(commandNum == 1) {
        g2.drawString(">", x - 24, y);
        if(gp.keyH.enterPressed == true) {
            subState = 2; // Go to sell state
            gp.keyH.enterPressed = false;
        }
    }
    y+= gp.tileSize;
    
    g2.drawString("Leave", x, y);
     if(commandNum == 2) {
        g2.drawString(">", x - 24, y);
        if(gp.keyH.enterPressed == true) {
            commandNum = 0; // Reset commandNum
            npc.startDialogue(npc, 1);
            gp.gameState = gp.dialogueState; // Go back to play state
        }
    }
    y+= gp.tileSize;

}
public void trade_buy() {
    drawInventory(gp.player, false);
    drawInventory(npc, true);  

    int x = gp.tileSize * 2;
    int y = gp.tileSize * 9;
    int width = (int)(gp.tileSize * 6);
    int height = (int)(gp.tileSize * 1.5);
    drawSubWindow(x, y, width, height);
    g2.drawString("[ESC] Back", x+24, y+60);

    x = (int)(gp.tileSize * 13);
    y = gp.tileSize * 9;
    width = (int)(gp.tileSize * 6);
    height = (int)(gp.tileSize * 1.5);
    drawSubWindow(x, y, width, height);
    g2.drawString("Crop Coins: " + gp.player.coin, x+24, y+60);

    int itemIndex = getItemIndex(npcSlotCol, npcSlotRow);
    if(itemIndex < npc.inventory.size()) {
        x = (int)(gp.tileSize * 5.5);
        y = (int)(gp.tileSize * 5.5);
        width = (int)(gp.tileSize * 2.5);
        height = gp.tileSize;
        drawSubWindow(x, y, width, height);
        g2.drawImage(coin, x+10, y+18, 32, 32, null);

        int price = npc.inventory.get(itemIndex).price;
        String text = "" + price;
        x = getXforAlignText(text, gp.tileSize * 7);
        g2.drawString(text, x, y+38);

        if(gp.keyH.enterPressed == true) {
            if(npc.inventory.get(itemIndex).price > gp.player.coin) {
                subState = 0;
                gp.gameState = gp.dialogueState;
                npc.startDialogue(npc, 2);
            }
            else {
                if(gp.player.canObtainItem(npc.inventory.get(itemIndex)) == true) {
                    gp.player.coin -= npc.inventory.get(itemIndex).price;
                } else {
                    subState = 0;
                    npc.startDialogue(npc, 3);
                    gp.gameState = gp.dialogueState;
                }
            }
        }
    }
}
public void trade_sell() {
    
    drawInventory(gp.player, true);

    int x;
    int y;
    int width;
    int height;

    x = gp.tileSize * 2;
    y = gp.tileSize * 9;
    width = gp.tileSize * 6;
    height = (int)(gp.tileSize * 1.5);
    drawSubWindow(x, y, width, height);
    g2.drawString("[ESC] Back", x+24, y+60);

    x = (int)(gp.tileSize * 12.9);
    y = gp.tileSize * 9;
    width = gp.tileSize * 6;
    height = (int)(gp.tileSize * 1.5);
    drawSubWindow(x, y, width, height);
    g2.drawString("Crop Coins: " + gp.player.coin, x+20, y+60);

    int itemIndex = getItemIndex(playerSlotCol, playerSlotRow);
    if(itemIndex < gp.player.inventory.size()) {
        x = (int)(gp.tileSize * 15.5);
        y = (int)(gp.tileSize * 5.5);
        width = (int)(gp.tileSize * 2.5);
        height = gp.tileSize;
        drawSubWindow(x, y, width, height);
        g2.drawImage(coin, x+10, y+18, 32, 32, null);

        int price = gp.player.inventory.get(itemIndex).price/2;
        String text = "" + price;
        x = getXforAlignText(text, gp.tileSize * 17);
        g2.drawString(text, x, y+38);

        if(gp.keyH.enterPressed == true) {
            if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon || 
               gp.player.inventory.get(itemIndex) == gp.player.currentSHield) {
                commandNum = 0;
                subState = 0;
                npc.startDialogue(npc, 4);
            } else {
                if(gp.player.inventory.get(itemIndex).amount > 1) {
                    gp.player.inventory.get(itemIndex).amount--;
                } else {
                    gp.player.inventory.remove(itemIndex);
                }
                
                gp.player.coin += price;
            }
        }
    }

}
public void drawSleepScreen() {
    counter++;

    // Phase 1: Fade to black
    if(counter < 120) {
        gp.eManager.lighting.filterAlpha += 0.01f;
        if(gp.eManager.lighting.filterAlpha > 1f) {
            gp.eManager.lighting.filterAlpha = 1f;
        }
    }

    // Phase 2: Fade back to normal
    else if(counter < 240) {
        gp.eManager.lighting.filterAlpha -= 0.01f;
        if(gp.eManager.lighting.filterAlpha < 0f) {
            gp.eManager.lighting.filterAlpha = 0f;
        }
    }

    // End of sleep sequence
    else {
        counter = 0;
        gp.player.getImage();
        gp.eManager.lighting.dayState = gp.eManager.lighting.day;
        gp.gameState = gp.playState;
    }
}

public int getItemIndex(int slotCol, int slotRow) {
    int itemIndex = slotCol + (slotRow * 5);
    return itemIndex;
}
    
public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(87, 48, 28, 210 ); // Semi-transparent brown
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255); // White color for the border
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
        
}
public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
}
public int getXforAlignText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
}


}

        


       