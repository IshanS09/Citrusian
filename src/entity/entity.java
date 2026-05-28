package entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.UtilityTool;
import main.gamePanel;
import monster.MON_BossBroccoli;

public class entity {

    gamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, idle1, idle2; 
    public BufferedImage AttackUp1, AttackUp2, AttackDown1, AttackDown2, AttackLeft1, AttackLeft2, AttackRight1, AttackRight2, 
    guardUp, guardDown, guardLeft, guardRight; 
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public String dialogues[][] = new String[20][20];
    public entity attacker;
    public entity linkedEntity;
    public boolean temp = false;
    

    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1; 
    public int dialogueSet = 0;
    public int dialogueIndex = 0; 
    public boolean collisionOn = false; 
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean attackCanceled = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;
    public String knockBackDirection;
    public boolean guarding = false;
    public boolean transparent = false;
    public boolean offBalance = false;
    public entity loot;
    public boolean opened = false;
    public boolean inRage = false;
    public boolean sleep = false;
    public boolean drawing = true;

    public int spriteCounter = 0;
    public int standCounter;
    public int actionLockCounter = 0; 
    public int invincibleCounter = 0; 
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    public int hpBarCounter = 0;
    int knockBackCounter = 0;
    public int guardCounter = 0;
    public int offBalanceCounter = 0;

    public String name;
    public int defaultSpeed;
    public int maxLife;
    public int speed;
    public int life;
    public int maxSeed;
    public int seed;  
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int motion1_duration;
    public int motion2_duration;
    public int splitMotion_duration;
    public int slamMotion_duration;
    public entity currentWeapon;
    public entity currentSHield;
    public entity currentLight;
    public Projectile projectile;
    public int durability;
    public int maxDurability;
    public boolean boss;
    public boolean stunned = false;
    public int stunnedCounter = 0;

    public ArrayList<entity> inventory = new ArrayList<>(); 
    public final int maxInventorySize = 20;
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int price;
    public int knockBackPower = 0;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    public int type; 
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickUp = 7;
    public final int type_obstacle = 8;
     public final int type_light = 9;
    public final int type_picaxe = 10;
   

    public entity(gamePanel gp) {
        this.gp = gp;
    }
    public void setAction() {}
    public void damageReaction() {}
    public void speak() {}
    public void facePlayer() {
        switch(gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }
    public void move(String direction) {}
    public void startDialogue(entity entity, int setNum) {
        gp.gameState = gp.dialogueState;
        gp.ui.npc = entity;
        dialogueSet = setNum;
    }
    public int getScreenX() {
        int screenX = worldX - gp.player.worldX + gp.player.screenX; 
        return screenX;
    }
    public int getScreenY() {
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        return screenY;
    }
    public int getLeftX() {
        return worldX + solidArea.x;
    }
    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }
    public int getTopY() {
        return worldY + solidArea.y;
    }
    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }
    public int getCol() {
        return (worldX + solidArea.x) / gp.tileSize;
    }
    public int getRow() {
        return (worldY + solidArea.y) / gp.tileSize;
    }
    public int getCenterX() {
        int centerX = worldX + up1.getWidth()/2;
        return centerX;
    }
    public int getCenterY() {
        int centerY = worldY + up1.getHeight()/2;
        return centerY;
    }
    public int getXdistance(entity target) {
        int xDistance = Math.abs(getCenterX() - target.getCenterX());
        return xDistance;
    }
    public int getYdistance(entity target) {
        int yDistance = Math.abs(getCenterY() - target.getCenterY());
        return yDistance;
    }
    public int getTileDistance(entity target) {
        int tileDistance = (getXdistance(target) + getYdistance(target))/gp.tileSize;
        return tileDistance;
    }
    public int getGoalCol(entity target) {
        int goalCol = (target.worldX + gp.player.solidArea.x)/gp.tileSize;
        return goalCol;
    }
    public int getGoalRow(entity target) {
        int goalRow = (target.worldY + gp.player.solidArea.y)/gp.tileSize;
        return goalRow;
    }
    public void resetCounters() {
        spriteCounter = 0;
        actionLockCounter = 0;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        dyingCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        offBalanceCounter = 0;
        guardCounter = 0;
    }
    public void setLoot(entity loot) {}
    public void interact() {

    }
    public boolean use(entity entity) {
        return false;
    }
    public void checkDrop() {

    }
    public void dropItem(entity droppedItem) {
        for(int i = 0; i < gp.obj[1].length; i++) {
            if(gp.obj[gp.currentMap][i] == null) {
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }
    public Color getParticleColor() {
        Color color = null;
        return color;
    }
    public int getParticleSize() {
        int size = 0;
        return size;
    }
    public int getParticleSpeed() {
        int speed = 0;
        return speed;
    }
    public int getParticleMaxLife() {
        int maxLife = 0;
        return maxLife;
    }
    public void generateParticle(entity generator, entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }
    public void generateIceParticles(entity generator) {
    Color iceBlue1 = new Color(150, 220, 255); // Light blue
    Color iceBlue2 = new Color(200, 240, 255); // Almost white
    Color iceBlue3 = new Color(100, 180, 230); // Darker blue
    
    // Create ice particles in random directions
    for(int i = 0; i < 5; i++) {
        int size = (int)(Math.random() * 4) + 2; // 2-6 pixels
        int speed = 1;
        int maxLife = 20;
        int xd = (int)(Math.random() * 3) - 1; // -1, 0, or 1
        int yd = (int)(Math.random() * 3) - 1;
        
        Color color = (i % 3 == 0) ? iceBlue1 : (i % 3 == 1) ? iceBlue2 : iceBlue3;
        
        Particle particle = new Particle(gp, generator, color, size, speed, maxLife, xd, yd);
        gp.particleList.add(particle);
    }
}
    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this); 
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this); 

        if(this.type == type_monster && contactPlayer == true) {
            damagePlayer(attack);
        }
    }
    public void update() {

    if(stunned) {
        stunnedCounter--;
        
        // Generate ice particles every 10 frames while frozen
        if(stunnedCounter % 10 == 0) {
            generateIceParticles(this);
        }
        
        if(stunnedCounter <= 0) {
            stunned = false;
        }
        
        // Still animate sprite while frozen
        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        return; // Don't move or attack while stunned
    }
    
    if(sleep == false) {
        if(knockBack == true) {
        
        checkCollision();
        
        if(collisionOn == true) {
            knockBackCounter = 0;
            knockBack = false;
            speed = defaultSpeed;
        }  
        else if(collisionOn == false) {
            switch (knockBackDirection) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        } 

        knockBackCounter++;
        if(knockBackCounter == 10) {
            knockBackCounter = 0;
            knockBack = false;
            speed = defaultSpeed; 
        }
    } 

    if(attacking == true) {
        attacking();
    } else {

        setAction();
        checkCollision();

        if(collisionOn == false) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 20) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    if(invincible) {
        invincibleCounter++;
        if (invincibleCounter > 40) {
            invincible = false;
            invincibleCounter = 0;
        }
    }
    
    if(shotAvailableCounter < 30) {
        shotAvailableCounter++;
    }
    if(offBalance == true) {
        offBalanceCounter++;
        if(offBalanceCounter > 60) {
            offBalance = false;
            offBalanceCounter = 0;
        }
    }
  }
}

    public void checkAttackOrNot(int rate, int straight, int horizontal) {
        
        boolean targetInRange = false;
        int xDistance = getXdistance(gp.player);
        int yDistance = getYdistance(gp.player);

        switch (direction) {
            case "up":
                if(gp.player.getCenterY() < getCenterY() && yDistance < straight && xDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "down":
                if(gp.player.getCenterY() > getCenterY() && yDistance < straight && xDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "left":
                if(gp.player.getCenterX() < getCenterX() && xDistance < straight && yDistance < horizontal) {
                    targetInRange = true;
                }
                break;
            case "right":
                if(gp.player.getCenterX() > getCenterX() && xDistance < straight && yDistance < horizontal) {
                    targetInRange = true;
                }
                break;
        }
        if(targetInRange == true) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }
    }
    public void checkShootorNot(int rate, int shotInterval) {
        int i = new Random().nextInt(rate)+1;
        if(i > 99 && projectile.alive == false && shotAvailableCounter == shotInterval) {
            projectile.set(worldX, worldY, direction, true, this);
            for(int ii = 0; ii < gp.projectile[1].length; ii++) {
                if(gp.projectile[gp.currentMap][ii] == null) {
                    gp.projectile[gp.currentMap][ii] = projectile;
                    break;
                }
            }
            
            int xDistance = Math.abs(gp.player.worldX - worldX);
            int yDistance = Math.abs(gp.player.worldY - worldY);
            int tileDistance = (xDistance + yDistance) / gp.tileSize;

            if (tileDistance <= 5) { 
                gp.playSE(12);
            }
            shotAvailableCounter = 0;
        }
    }
    public void checkStartChasingOrNot(entity target, int distance, int rate) {
        if(getTileDistance(target) < distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = true;
            }
        }
    }
    public void checkStopChasingOrNot(entity target, int distance, int rate) {
        if(getTileDistance(target) > distance) {
            int i = new Random().nextInt(rate);
            if(i == 0) {
                onPath = false;
            }
        }
    }
    public void getRandomDirection() {
        actionLockCounter++;
        
       if(actionLockCounter == 120) {
        
        Random random = new Random();
        int i = random.nextInt(100) + 1;

        if(i <= 25) {
            direction = "up";
        }  if(i > 25 && i <= 50) {
            direction = "down";
        }  if(i > 50 && i <= 75) {
            direction = "left";
        }  if(i > 75 && i <= 100) {
            direction = "right";
        }

        actionLockCounter = 0;
      }
    }
    public void moveTowardPlayer(int interval) {
    
    actionLockCounter++;
    
    if(actionLockCounter > interval) {
        if(getXdistance(gp.player) > getYdistance(gp.player)) {
            if(gp.player.getCenterX() < getCenterX()) {
                direction = "left";
            } else {
                direction = "right";
            }
        } else if(getXdistance(gp.player) < getYdistance(gp.player)) {
            if(gp.player.getCenterY() < getCenterY()) {
                direction = "up";
            } else {
                direction = "down";
            }
        }
        actionLockCounter = 0;
    }
}
public String getOppositeDirection(String direction) {
        String oppositeDirection = "";
        switch(direction) {
            case "up": oppositeDirection = "down"; break;
            case "down": oppositeDirection = "up"; break;
            case "left": oppositeDirection = "right"; break;
            case "right": oppositeDirection = "left"; break;
        }
        return oppositeDirection;
    }
    public void attacking() {

    spriteCounter++;

    if (spriteCounter <= motion1_duration) {
        spriteNum = 1;  // Attack start frame
    } 
    if (spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
        spriteNum = 2;  // Attack active frame


        int currentWorldX = worldX;
        int currentWorldY = worldY;
        int solidAreaWidth = solidArea.width;
        int solidAreaHeight = solidArea.height;

 
        switch (direction) {
            case "up":    worldY -= attackArea.height; break;
            case "down":  worldY += attackArea.height; break;
            case "left":  worldX -= attackArea.width;  break;
            case "right": worldX += attackArea.width;  break;
        }

        // Set solid area to attack area for collision detection
        solidArea.width = attackArea.width;
        solidArea.height = attackArea.height;

        // Damage logic depending on type
        if (type == type_monster) {
            if (gp.cChecker.checkPlayer(this)) {
                damagePlayer(attack);
            }
        } else {
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            gp.player.damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            gp.player.damageInteractiveTile(iTileIndex);

            int projectileIndex = gp.cChecker.checkEntity(this, gp.projectile);
            gp.player.damageProjectile(projectileIndex);
        }

        // Revert to original position and solid area
        worldX = currentWorldX;
        worldY = currentWorldY;
        solidArea.width = solidAreaWidth;
        solidArea.height = solidAreaHeight;

    } 
    if (spriteCounter > motion2_duration) {
        // Attack animation finished, reset
        spriteNum = 1;
        spriteCounter = 0;
        attacking = false;
    }
}



    public void damagePlayer(int attack) {

    if (gp.player.invincible == false) {

        int damage = attack - gp.player.defense;
        String canGuardDirection = getOppositeDirection(direction);

        if (gp.player.guarding && gp.player.direction.equals(canGuardDirection)) {

            entity shield = gp.player.currentSHield;

            // Handle broken shield
            if (shield != null && shield.durability <= 0) {
                damage /= 2; // Shield is broken, reduced effect
            } else {
                if (gp.player.guardCounter < 10) {
                    damage = 0;
                    gp.playSE(16);
                    setKnockBack(this, gp.player, knockBackPower);
                    offBalance = true;
                    spriteCounter -= 60;
                } else {
                    damage /= 3;
                    gp.playSE(15);
                }

                // Reduce durability only if damage was blocked and shield exists
                if (shield != null && gp.player.guarding && gp.player.direction.equals(canGuardDirection)) {
                    if (shield.durability > 0) {
                        shield.durability--;
                        if (shield.durability == 0) {
                            gp.ui.addMessage("Your " + shield.name + " is BROKEN!");
                        }
                    }
                }
            }

        } else {
            gp.playSE(7);
            if (damage < 1) {
                damage = 1;
            }
        }

        if (damage != 0) {
            gp.player.transparent = true;
            setKnockBack(gp.player, this, knockBackPower);
        }

        gp.player.life -= damage;
        gp.player.invincible = true;
    }
    }

    
    public void reduceDurability() {
    if (durability > 0) {
        durability--;
    }
    
    }
    
    public void setKnockBack(entity target, entity attacker, int knockBackPower) {
        
        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }
    public boolean inCamera() {
        boolean inCamera = false;
        if (worldX + gp.tileSize*5 > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize*5 > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            inCamera = true;
        }
        return inCamera;
    }
public void draw(Graphics2D g2) {
    BufferedImage image = null;
    if (inCamera() == true) {
        int tempScreenX = getScreenX();
        int tempScreenY = getScreenY();
        
        // 🟢 IDLE MODE (for Malibroc during dialogue)
        if (this instanceof MON_BossBroccoli && ((MON_BossBroccoli)this).idleMode) {
            MON_BossBroccoli boss = (MON_BossBroccoli)this;
            // Alternate between idle frames
            if (boss.spriteNum == 1) image = boss.idle1;
            else image = boss.idle2;
            // Slow idle breathing animation
            boss.spriteCounter++;
            if (boss.spriteCounter > 45) {
                boss.spriteNum = (boss.spriteNum == 1) ? 2 : 1;
                boss.spriteCounter = 0;
            } 
            g2.drawImage(image, tempScreenX, tempScreenY, null);
            
            // Draw hitboxes for idle mode
            // drawSolidArea(g2, tempScreenX, tempScreenY);
            // drawAttackArea(g2, tempScreenX, tempScreenY);
            return;
        }
        
        // 🔽 Regular animation
        switch (direction) {
            case "up":
                if(!attacking) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                } else {
                    if(spriteNum == 1) {image = AttackUp1;}
                    if(spriteNum == 2) {image = AttackUp2;}
                }
                break;
            case "down":
                if(!attacking) {
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                } else {
                    if(spriteNum == 1) {image = AttackDown1;}
                    if(spriteNum == 2) {image = AttackDown2;}
                }
                break;
            case "left":
                if(!attacking) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                } else {
                    if(spriteNum == 1) {image = AttackLeft1;}
                    if(spriteNum == 2) {image = AttackLeft2;}
                }
                break;
            case "right":
                if(!attacking) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                } else {
                    if(spriteNum == 1) {image = AttackRight1;}
                    if(spriteNum == 2) {image = AttackRight2;}
                }
                break;
        }
        
        // 🟡 Invincibility fade
        if(invincible) { 
            hpBarOn = true; 
            hpBarCounter = 0; 
            changeAlpha(g2, 0.4f); 
        }
        
        // 🔴 Dying animation
        if(dying) {
            dyingAnimation(g2);
        }
        
        // 🖼️ Draw sprite
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        
        // 🎯 Draw hitboxes (collision and attack areas)
        // drawSolidArea(g2, tempScreenX, tempScreenY);
        // drawAttackArea(g2, tempScreenX, tempScreenY);
        
        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); 
    }
}


    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 5;

        if(dyingCounter <= 1) {
            changeAlpha(g2, 0f);
        }
        if(dyingCounter > 1 && dyingCounter <= i*2) {
            changeAlpha(g2, 1f);     
        }
        if(dyingCounter > i*2 && dyingCounter <= i*3) {
            changeAlpha(g2, 0f);      
        }
        if(dyingCounter > i*3 && dyingCounter <= i*4) {
            changeAlpha(g2, 1f);      
        }
        if(dyingCounter > i*4 && dyingCounter <= i*5) {
            changeAlpha(g2, 0f);       
        }
        if(dyingCounter > i*5 && dyingCounter <= i*6) {
            changeAlpha(g2, 1f);       
        }
        if(dyingCounter > i*6 && dyingCounter <= i*7) {
            changeAlpha(g2, 0f);       
        }
        if(dyingCounter > i*7 && dyingCounter <= i*8) {
            changeAlpha(g2, 1f);    
        }
        if(dyingCounter > i*8) {
            alive = false; 
        }

    }

    public void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
    

    public BufferedImage setup(String imagePath, int width, int height) {
    UtilityTool uTool = new UtilityTool();

    try (InputStream is = getClass().getResourceAsStream(imagePath + ".png")) {

        if (is == null) {
            throw new RuntimeException("Missing resource: " + imagePath + ".png");
        }

        BufferedImage image = ImageIO.read(is);
        image = uTool.scaleImage(image, width, height);
        return image;

    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search() == true) {
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                if(enLeftX > nextX) {
                    direction = "left";
                } 
                if(enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                direction = "up";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn == true) {
                    direction = "right";
                }
            }
        }
    }
    public int getDetected(entity user, entity target[][], String targetName) {
    int index = 999;

    // Check the tile in the direction the player is facing
    int nextWorldX = user.getLeftX();
    int nextWorldY = user.getTopY();

    switch(user.direction) {
        case "up":
            nextWorldY -= gp.tileSize;
            break;
        case "down":
            nextWorldY += gp.tileSize;
            break;
        case "left":
            nextWorldX -= gp.tileSize;
            break;
        case "right":
            nextWorldX += gp.tileSize;
            break;
    }

    int targetCol = nextWorldX / gp.tileSize;
    int targetRow = nextWorldY / gp.tileSize;

    // Also include the current tile
    int userCol = user.getCol();
    int userRow = user.getRow();

    for (int i = 0; i < target[gp.currentMap].length; i++) {
        entity t = target[gp.currentMap][i];
        if (t != null && t.name != null && t.name.equals(targetName)) {
            int objCol = t.getCol();
            int objRow = t.getRow();

            if ((objCol == targetCol && objRow == targetRow) ||   // In front of player
                (objCol == userCol && objRow == userRow)) {       // On same tile as player
                index = i;
                break;
            }
        }
    }

    return index;
}
// public void drawSolidArea(Graphics2D g2, int screenX, int screenY) {
//     g2.setColor(new Color(0, 255, 0, 100)); // semi-transparent green
//     g2.fillRect(
//         screenX + solidArea.x,
//         screenY + solidArea.y,
//         solidArea.width,
//         solidArea.height
//     );

//     g2.setColor(Color.GREEN); // outline
//     g2.drawRect(
//         screenX + solidArea.x,
//         screenY + solidArea.y,
//         solidArea.width,
//         solidArea.height
//     );
// }
// public void drawAttackArea(Graphics2D g2, int screenX, int screenY) {
//     g2.setColor(new Color(255, 0, 0, 100)); // semi-transparent red
//     g2.fillRect(
//         screenX + attackArea.x,
//         screenY + attackArea.y,
//         attackArea.width,
//         attackArea.height
//     );

//     g2.setColor(Color.RED);
//     g2.drawRect(
//         screenX + attackArea.x,
//         screenY + attackArea.y,
//         attackArea.width,
//         attackArea.height
//     );
// }
}
