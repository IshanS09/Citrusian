package monster;

import data.Progress;
import entity.entity;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.gamePanel;
import object.OBJ_Fireskull;
import object.OBJ_ironDoor;
import object.OBJ_key;

public class MON_SlimeKing extends entity {
    gamePanel gp;
    public static final String monName = "Pea King";
    
    // Slime split ability variables
    private int splitTimer = 0;
    private int nextSplitTime;
    private boolean isSplitting = false;
    private int splitAnimationCounter = 0;
    private static final int SPLIT_ANIMATION_DURATION = 60; // 1 second at 60 FPS
    private static final int MINISLIME_DURATION = 360; // 6 seconds
    private int miniSlimeTimer = 0;
    private boolean miniSlimesActive = false;
    
    // Split animation sprites
    private BufferedImage split1, split2;

    public MON_SlimeKing(gamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;
        boss = true;
        name = monName;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 60;
        life = maxLife;
        attack = 6;
        defense = 3;
        exp = 35;
        knockBackPower = 3;
        sleep = true;

        // Set random time for first split (8-12 seconds = 480-720 frames at 60 FPS)
        nextSplitTime = new Random().nextInt(240) + 480;

        int size = gp.tileSize * 3;
        solidArea.x = 32;
        solidArea.y = 32;
        solidArea.width = size - 48;
        solidArea.height = size - 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 170;
        attackArea.height = 170;
        motion1_duration = 25;
        motion2_duration = 50;
        splitMotion_duration = 30;

        // Set up projectile
        projectile = new OBJ_Fireskull(gp);

        getImage();
        getSplitImage();
        setDialogue();
    }

    public void getImage() {
        int i = 3;

        up1 = setup("/res/Monster/PeaKingDown1", gp.tileSize*i, gp.tileSize*i);
        up2 = setup("/res/Monster/PeaKingDown2", gp.tileSize*i, gp.tileSize*i);
        down1 = setup("/res/Monster/PeaKingDown1", gp.tileSize*i, gp.tileSize*i);
        down2 = setup("/res/Monster/PeaKingDown2", gp.tileSize*i, gp.tileSize*i);
        left1 = setup("/res/Monster/PeaKingDown1", gp.tileSize*i, gp.tileSize*i);
        left2 = setup("/res/Monster/PeaKingDown2", gp.tileSize*i, gp.tileSize*i);
        right1 = setup("/res/Monster/PeaKingDown1", gp.tileSize*i, gp.tileSize*i);
        right2 = setup("/res/Monster/PeaKingDown2", gp.tileSize*i, gp.tileSize*i);
    }

    public void getSplitImage() {
        int i = 3;
        
        split1 = setup("/res/Monster/PeaKingDown1", gp.tileSize*i, gp.tileSize*i);
        split2 = setup("/res/Monster/SplitImage2", gp.tileSize*i, gp.tileSize*i);
    }

    public void setDialogue() {
        dialogues[0][0] = "So you found me...";
        dialogues[0][1] = "No one can stop the reign of the Pea King!";
        dialogues[0][2] = "Prepare to be crushed!";
    }

    public void setAction() {
        if(gp.playerStatus.isPlayerInvisible()) {
            getRandomDirection();
            return;
        }

        // Handle split animation - CHECK THIS FIRST
        if(isSplitting) {
            splitAnimationCounter++;
            
            // Handle sprite animation for split
            spriteCounter++;
            if(spriteCounter > splitMotion_duration) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
            
            if(splitAnimationCounter >= SPLIT_ANIMATION_DURATION) {
                isSplitting = false;
                splitAnimationCounter = 0;
                miniSlimesActive = true;
                miniSlimeTimer = 0;
            }
            return; // Don't move during split
        }

        // Handle mini-slime duration
        if(miniSlimesActive) {
            miniSlimeTimer++;
            if(miniSlimeTimer >= MINISLIME_DURATION) {
                // Despawn mini-slimes
                despawnMiniSlimes();
                miniSlimesActive = false;
                invincible = false; // Become vulnerable again
            }
        }

        // Handle slime split ability
        if(!sleep && !miniSlimesActive) {
            splitTimer++;
            
            if(splitTimer >= nextSplitTime) {
                performSlimeSplit();
                splitTimer = 0;
                // Set next split time (8-12 seconds)
                nextSplitTime = new Random().nextInt(240) + 480;
                return; // Exit immediately after starting split
            }
        }

        // Normal AI behavior (only when not splitting and mini-slimes aren't active)
        if(!isSplitting && !miniSlimesActive) {
            if(getTileDistance(gp.player) < 10) {
                moveTowardPlayer(60);
            } else {
                getRandomDirection();
            }
            
            // Shoot rocks instead of melee
            if(shotAvailableCounter == 30) {
                checkShootorNot(200, 30);
            }
        }
    }

    private void performSlimeSplit() {
        isSplitting = true;
        invincible = true; // Become invulnerable during split
        
        // Play split sound effect
        gp.playSE(8); // Use appropriate sound effect number
        
        // Spawn 4 mini-slimes in cardinal directions
        spawnMiniSlime("up");
        spawnMiniSlime("down");
        spawnMiniSlime("left");
        spawnMiniSlime("right");
    }

    private void spawnMiniSlime(String direction) {
        // Calculate spawn position based on direction (increased distance)
        int spawnX = worldX;
        int spawnY = worldY;
        
        switch(direction) {
            case "up": spawnY -= gp.tileSize * 2; break;
            case "down": spawnY += gp.tileSize * 3; break;
            case "left": spawnX -= gp.tileSize * 3; break;
            case "right": spawnX += gp.tileSize * 3; break;
        }
        
        // Find empty slot in monster array
        for(int i = 0; i < gp.monster[1].length; i++) {
            if(gp.monster[gp.currentMap][i] == null) {
                gp.monster[gp.currentMap][i] = new MON_MiniSlime(gp);
                gp.monster[gp.currentMap][i].worldX = spawnX;
                gp.monster[gp.currentMap][i].worldY = spawnY;
                break;
            }
        }
    }

    private void despawnMiniSlimes() {
        // Remove all mini-slimes from the map
        for(int i = 0; i < gp.monster[1].length; i++) {
            if(gp.monster[gp.currentMap][i] != null && 
               gp.monster[gp.currentMap][i].name.equals(MON_MiniSlime.monName)) {
                gp.monster[gp.currentMap][i] = null;
            }
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
    }

    public void checkDrop() {
        gp.bossBattleOn = false;
        Progress.peaKingDefeated = true; // Add this to Progress.java

        // Despawn any remaining mini-slimes
        despawnMiniSlimes();

        gp.stopMusic();
        gp.playMusic(18);

        // Remove iron door
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null && 
               gp.obj[gp.currentMap][i].name.equals(OBJ_ironDoor.objName)) {
                gp.playSE(19);
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
       
        // Drop gold key
        dropItem(new OBJ_key(gp));
    }
    
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = getScreenX();
        int tempScreenY = getScreenY();
        
        // If performing slime split, use split sprites
        if(isSplitting) {
            if(spriteNum == 1) {
                image = split1;
            }
            if(spriteNum == 2) {
                image = split2;
            }
            
            // Draw split animation
            if(image != null) {
                g2.drawImage(image, tempScreenX, tempScreenY, null);
            }
            
            // Reset opacity if changed
            changeAlpha(g2, 1f);
        } else {
            // Use default entity drawing logic for normal movement/attacking
            super.draw(g2);
        }
    }
}
