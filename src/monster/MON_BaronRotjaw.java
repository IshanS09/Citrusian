package monster;

import data.Progress;
import entity.entity;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.gamePanel;
import object.OBJ_ironDoor;
import object.OBJ_key_silver;
import object.OBJ_rock;

public class MON_BaronRotjaw extends entity {
    gamePanel gp;
    public static final String monName = "Baron Rotjaw";
    
    // Special ability variables
    private int slamTimer = 0;
    private int nextSlamTime;
    private boolean isSlammingGround = false;
    private int slamAnimationCounter = 0;
    private static final int SLAM_ANIMATION_DURATION = 60; // 1 second at 60 FPS
    
    // Slam animation sprites
    private BufferedImage slam1, slam2;

    public MON_BaronRotjaw(gamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;
        boss = true;
        name = monName;
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 45;
        life = maxLife;
        attack = 9;
        defense = 2;
        exp = 30;
        knockBackPower = 5;
        sleep = true;

        // Set random time for first slam (5-10 seconds = 300-600 frames at 60 FPS)
        nextSlamTime = new Random().nextInt(300) + 300;

        int size = gp.tileSize * 3;
        solidArea.x = 48;
        solidArea.y = 48;
        solidArea.width = size - 48;
        solidArea.height = size - 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width = 120;
        attackArea.height = 120;
        motion1_duration = 20;
        motion2_duration = 40;
        slamMotion_duration = 30;

        getImage();
        getAttackImage();
        getSlamImage();
        setDialogue();
    }

    public void getImage() {
        int i = 3;

        up1 = setup("/res/Monster/BaronRotjawUp1", gp.tileSize*i, gp.tileSize*i);
        up2 = setup("/res/Monster/BaronRotjawUp2", gp.tileSize*i, gp.tileSize*i);
        down1 = setup("/res/Monster/BaronRotjawDown1", gp.tileSize*i, gp.tileSize*i);
        down2 = setup("/res/Monster/BaronRotjawDown2", gp.tileSize*i, gp.tileSize*i);
        left1 = setup("/res/Monster/BaronRotjawLeft1", gp.tileSize*i, gp.tileSize*i);
        left2 = setup("/res/Monster/BaronRotjawLeft2", gp.tileSize*i, gp.tileSize*i);
        right1 = setup("/res/Monster/BaronRotjawRight1", gp.tileSize*i, gp.tileSize*i);
        right2 = setup("/res/Monster/BaronRotjawRight2", gp.tileSize*i, gp.tileSize*i);
    }

    public void getAttackImage() {
        int i = 3;
        
        AttackUp1 = setup("/res/Monster/BaronRotjaw_AttackUp1", gp.tileSize*i, gp.tileSize*i);
        AttackUp2 = setup("/res/Monster/BaronRotjaw_AttackUp2", gp.tileSize*i, gp.tileSize*i);
        AttackDown1 = setup("/res/Monster/BaronRotjaw_AttackDown1", gp.tileSize*i, gp.tileSize*i);
        AttackDown2 = setup("/res/Monster/BaronRotjaw_AttackDown2", gp.tileSize*i, gp.tileSize*i);
        AttackLeft1 = setup("/res/Monster/BaronRotjaw_AttackLeft1", gp.tileSize*i, gp.tileSize*i);
        AttackLeft2 = setup("/res/Monster/BaronRotjaw_AttackLeft2", gp.tileSize*i, gp.tileSize*i);
        AttackRight1 = setup("/res/Monster/BaronRotjaw_AttackRight1", gp.tileSize*i, gp.tileSize*i);
        AttackRight2 = setup("/res/Monster/BaronRotjaw_AttackRight2", gp.tileSize*i, gp.tileSize*i);
    }

    public void getSlamImage() {
        int i = 3;
        
        slam1 = setup("/res/Monster/BaronRotjaw_Slam1", gp.tileSize*i, gp.tileSize*i);
        slam2 = setup("/res/Monster/BaronRotjaw_Slam2", gp.tileSize*i, gp.tileSize*i);
    }

    public void setDialogue() {
        dialogues[0][0] = "I be Baron Rotjaw, scourge of the seven salads...";
        dialogues[0][1] = "I shall peel ye apart!";
    }

    public void setAction() {
        if(gp.playerStatus.isPlayerInvisible()) {
            getRandomDirection();
            return;
        }

        // Handle slam animation - CHECK THIS FIRST
        if(isSlammingGround) {
            slamAnimationCounter++;
            
            // Handle sprite animation for slam
            spriteCounter++;
            if(spriteCounter > slamMotion_duration) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
            
            if(slamAnimationCounter >= SLAM_ANIMATION_DURATION) {
                isSlammingGround = false;
                slamAnimationCounter = 0;
            }
            return; // Don't move during slam
        }

        // Handle ground slam ability
        if(!sleep) {
            slamTimer++;
            
            if(slamTimer >= nextSlamTime) {
                performGroundSlam();
                slamTimer = 0;
                // Set next slam time (5-10 seconds)
                nextSlamTime = new Random().nextInt(300) + 300;
                return; // Exit immediately after starting slam
            }
        }

        // Normal AI behavior
        if(getTileDistance(gp.player) < 10) {
            moveTowardPlayer(60);
        } else {
            getRandomDirection();
        }
        
        if(attacking == false) {
            checkAttackOrNot(60, gp.tileSize*4, gp.tileSize*4);
        }
    }

    private void performGroundSlam() {
        isSlammingGround = true;
        
        // Stop all movement
        collisionOn = false;
        
        // Play slam sound effect
        gp.playSE(8); // Use appropriate sound effect number
        
        // Spawn rocks in 4 cardinal directions
        spawnRockProjectile("up");
        spawnRockProjectile("down");
        spawnRockProjectile("left");
        spawnRockProjectile("right");
    }

    private void spawnRockProjectile(String direction) {
        // Create new rock projectile
        OBJ_rock rock = new OBJ_rock(gp);
        rock.set(worldX, worldY, direction, true, this);
        
        // Find empty slot in projectile array
        for(int i = 0; i < gp.projectile[1].length; i++) {
            if(gp.projectile[gp.currentMap][i] == null) {
                gp.projectile[gp.currentMap][i] = rock;
                break;
            }
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
    }

    public void checkDrop() {
        gp.bossBattleOn = false;
        Progress.baronRotjawDefeated = true; // Add this to Progress.java

        gp.stopMusic();
        gp.playMusic(0);

        // Remove iron door
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] != null && 
               gp.obj[gp.currentMap][i].name.equals(OBJ_ironDoor.objName)) {
                gp.playSE(19);
                gp.obj[gp.currentMap][i] = null;
                break;
            }
        }
       
        // Drop silver key
        dropItem(new OBJ_key_silver(gp));
    }
    
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = getScreenX();
        int tempScreenY = getScreenY();
        
        // If performing ground slam, use slam sprites
        if(isSlammingGround) {
            if(spriteNum == 1) {
                image = slam1;
            }
            if(spriteNum == 2) {
                image = slam2;
            }
            
            // Draw slam animation
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