package entity;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.gamePanel;
import main.keyHandler;
import object.OBJ_FireSeed;
import object.OBJ_shield_default;
import object.OBJ_sword_default;

public class Player extends entity {

    keyHandler keyH;
    public final int screenX;
    public final int screenY;
    public boolean lightUpdated = false;
    public boolean attackCanceled = false;



    public Player(gamePanel gp, keyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize/2); // Center player on screen
        screenY = gp.screenHeight / 2 - (gp.tileSize/2);  // Center player on screen
        
        solidArea = new java.awt.Rectangle(); // Set solid area for collision detection
        solidArea.x = 8;   
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x; // Store default x position of solid area
        solidAreaDefaultY = solidArea.y; // Store default y position of solid area
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getImage(); 
        getAttackImage(); 
        getGuardImage();
        setItems(); 
    }

    public void setDefaultValues() {
        gp.currentMap = 0;
        worldX = gp.tileSize * 64;
        worldY = gp.tileSize * 61;
        defaultSpeed = 6;
        speed = defaultSpeed;
        direction = "down";
        level = 1;
        maxLife = 10;
        life = maxLife;
        maxSeed = 3;
        seed = maxSeed;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 10;
        coin = 100;
        currentWeapon = new OBJ_sword_default(gp);
        currentSHield = new OBJ_shield_default(gp);
        currentLight = null;
        projectile = new OBJ_FireSeed(gp, getAttack());
        attack = getAttack();
        defense = getDefense();

        getImage(); 
        getAttackImage(); 
        getGuardImage();
        setItems(); 
        setDialogue();

        invincible = false;
        invincibleCounter = 0;
        
    }
    public void setDefaultPositions() {
        gp.currentMap = 0;
        worldX = gp.tileSize * 64;
        worldY = gp.tileSize * 61;
        direction = "down";
    }
    public void setDialogue() {
        dialogues[0][0] = "You leveled up! Now at level " + level + "!";
    }
    public void restoreLifeandSeed() {
        life = maxLife;
        seed = maxSeed;
        speed = defaultSpeed;
        invincible = false;
        transparent = false;
        attacking = false;
        guarding = false;
        knockBack = false;
        lightUpdated = true;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon); 
        inventory.add(currentSHield);
    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        motion1_duration = currentWeapon.motion1_duration;
        motion2_duration = currentWeapon.motion2_duration;
        return attack = strength + currentWeapon.attackValue;  
    }
    public int getDefense() {
        return defense = dexterity + currentSHield.defenseValue;
    }
    public int getCurrentWeaponSlot() {
        int currentWeaponSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentWeapon) {
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }
    public int getCurrentShieldSlot() {
        int currentShieldSlot = 0;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i) == currentSHield) {
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }
    public void getImage() {

        up1 = setup("/res/player/OrangeUp1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/player/OrangeUp2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/player/OrangeDown1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/player/OrangeDown2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/player/OrangeLeft1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/player/OrangeLeft2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/player/OrangeRight1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/player/OrangeRight2", gp.tileSize, gp.tileSize);
    }
    public void getSleepingImage(BufferedImage image) {
        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }
    public void getAttackImage() {
        if(currentWeapon.type == type_sword) {
        AttackUp1 = setup("/res/player/AttackUp1", gp.tileSize, gp.tileSize); 
        AttackUp2 = setup("/res/player/AttackUp2", gp.tileSize, gp.tileSize); 
        AttackDown1 = setup("/res/player/AttackDown1", gp.tileSize, gp.tileSize);
        AttackDown2 = setup("/res/player/AttackDown2", gp.tileSize, gp.tileSize);
        AttackLeft1 = setup("/res/player/AttackLeft1", gp.tileSize , gp.tileSize); 
        AttackLeft2 = setup("/res/player/AttackLeft2", gp.tileSize , gp.tileSize); 
        AttackRight1 = setup("/res/player/AttackRight1", gp.tileSize, gp.tileSize);
        AttackRight2 = setup("/res/player/AttackRight2", gp.tileSize, gp.tileSize);
        }
        if(currentWeapon.type == type_axe) {
        AttackUp1 = setup("/res/player/AttackAxeUp1", gp.tileSize, gp.tileSize); 
        AttackUp2 = setup("/res/player/AttackAxeUp2", gp.tileSize, gp.tileSize); 
        AttackDown1 = setup("/res/player/AttackAxeDown1", gp.tileSize, gp.tileSize);
        AttackDown2 = setup("/res/player/AttackAxeDown2", gp.tileSize, gp.tileSize);
        AttackLeft1 = setup("/res/player/AttackAxeLeft1", gp.tileSize , gp.tileSize); 
        AttackLeft2 = setup("/res/player/AttackAxeLeft2", gp.tileSize , gp.tileSize);
        AttackRight1 = setup("/res/player/AttackAxeRight1", gp.tileSize, gp.tileSize);
        AttackRight2 = setup("/res/player/AttackAxeRight2", gp.tileSize, gp.tileSize);
        }
        if(currentWeapon.type == type_picaxe) {
        AttackUp1 = setup("/res/player/AttackPicUp1", gp.tileSize, gp.tileSize); 
        AttackUp2 = setup("/res/player/AttackPicUp2", gp.tileSize, gp.tileSize); 
        AttackDown1 = setup("/res/player/AttackPicDown1", gp.tileSize, gp.tileSize);
        AttackDown2 = setup("/res/player/AttackPicDown2", gp.tileSize, gp.tileSize);
        AttackLeft1 = setup("/res/player/AttackPicLeft1", gp.tileSize , gp.tileSize); 
        AttackLeft2 = setup("/res/player/AttackPicLeft2", gp.tileSize , gp.tileSize);
        AttackRight1 = setup("/res/player/AttackPicRight1", gp.tileSize, gp.tileSize);
        AttackRight2 = setup("/res/player/AttackPicRight2", gp.tileSize, gp.tileSize);
        }
    }
    public void getGuardImage() {
        guardUp = setup("/res/player/guardUp", gp.tileSize, gp.tileSize);
        guardDown = setup("/res/player/guardDown", gp.tileSize, gp.tileSize);
        guardLeft = setup("/res/player/guardLeft", gp.tileSize, gp.tileSize);
        guardRight = setup("/res/player/guardRight", gp.tileSize, gp.tileSize);
    }




    public void update() {  
        if(knockBack == true) {
        
        collisionOn = false; 
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, true);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        
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
        else if(attacking == true) {
            attacking();
        }
        else if(keyH.spacePressed == true) {
            guarding = true;
            guardCounter++;
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                
                direction = "up";
            } else if (keyH.downPressed) {
               
                direction = "down";
            } else if (keyH.leftPressed) {
                
                direction = "left";
            } else if (keyH.rightPressed) {
                
                direction = "right";
            }
            
            collisionOn = false; // Reset collision flag before checking for collisions
            gp.cChecker.checkTile(this); // Check for tile collisions
            
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.cChecker.checkEntity(this, gp.iTile);

            gp.eHandler.checkEvent(); 

            if(collisionOn == false && keyH.enterPressed == false) {
                // If a collision is detected, revert the movement
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

            if(gp.keyH.enterPressed == true && attackCanceled == false) {
                attacking = true;
                spriteCounter = 0;
            }
            attackCanceled = false; 
            gp.keyH.enterPressed = false;
            guarding = false; 
            guardCounter = 0;

            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        else {
            standCounter++;
            if(standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
            guarding = false;
            guardCounter = 0;
        }
       if(gp.keyH.shotKeyPressed == true && shotAvailableCounter == 30) {
    // Create new FireSeed with current attack value
    Projectile newProjectile = new OBJ_FireSeed(gp, getAttack());

    if(newProjectile.alive == false && newProjectile.haveResource(this) == true) {
        newProjectile.set(worldX, worldY, direction, true, this);
        newProjectile.subtractResource(this);

        for(int i = 0; i < gp.projectile[1].length; i++) {
            if(gp.projectile[gp.currentMap][i] == null) {
                gp.projectile[gp.currentMap][i] = newProjectile;
                break;
            }
        }

        shotAvailableCounter = 0;
        gp.playSE(11);
    }
}
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if(life > maxLife) {
            life = maxLife;
        }
        if(seed > maxSeed) {
            seed = maxSeed;
        }
        if(life <= 0) {
            loseLevel(); 
            gp.stopMusic();
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.playSE(10);
        }
    }

    public void pickUpObject(int i) {
        
        if(i != 999) {

           if(gp.obj[gp.currentMap][i].type == type_pickUp) {
            gp.obj[gp.currentMap][i].use(this);
            gp.obj[gp.currentMap][i] = null;
           } 
           else if(gp.obj[gp.currentMap][i].type == type_obstacle) {
                if(keyH.enterPressed == true) {
                    gp.obj[gp.currentMap][i].interact();
                }
           }
           else {
            String text;
            
            if(canObtainItem(gp.obj[gp.currentMap][i]) == true) {
                gp.playSE(1);
                text = "Picked up a " + gp.obj[gp.currentMap][i].name + "!";
            } 
            else {
                text = "Inventory Full!";
            }
             gp.ui.addMessage(text);
             gp.obj[gp.currentMap][i] = null;
           }
        }
    }

    public void interactNPC(int i) {
    if (i != 999) {
        if (gp.keyH.enterPressed) {
            attackCanceled = true;
            gp.gameState = gp.dialogueState;
            gp.npc[gp.currentMap][i].speak();
        }
        gp.npc[gp.currentMap][i].move(direction); 
    }
}


    public void contactMonster(int i) {
        if (i != 999) {
            if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
                gp.playSE(7);
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage < 1) {
                    damage = 1; 
                }
                life -= damage;
                invincible = true;
                transparent = true;
                
            } 
        }
    }

     public void damageMonster(int i, entity attacker, int attack, int knockBackPower) {
        if (i != 999 && gp.monster[gp.currentMap][i].invincible == false) {

    entity weapon = null;
    if (attacker == gp.player && gp.player.currentWeapon instanceof entity) {
        weapon = (entity) gp.player.currentWeapon;
        if (weapon.durability <= 0) {
            attack = 0; // broken weapon
        }
    }

    gp.playSE(6);

    if (knockBackPower > 0) {
        setKnockBack(gp.monster[gp.currentMap][i], attacker, knockBackPower);
    }

    if (gp.monster[gp.currentMap][i].offBalance) {
        attack *= 2;
    }

    int damage = attack - gp.monster[gp.currentMap][i].defense;
    if (damage < 0) damage = 0;

    gp.monster[gp.currentMap][i].life -= damage;
    gp.ui.addMessage(damage + " damage!");

    gp.monster[gp.currentMap][i].invincible = true;
    gp.monster[gp.currentMap][i].damageReaction();

    if (weapon != null && damage > 0) {
    if (weapon.durability > 0) {
        weapon.durability--;
        if (weapon.durability == 0) {
            gp.ui.addMessage("Your " + weapon.name + " is BROKEN!");
        }
    }
}


    if (gp.monster[gp.currentMap][i].life <= 0) {
        gp.monster[gp.currentMap][i].dying = true;
        gp.ui.addMessage("You killed the " + gp.monster[gp.currentMap][i].name + "!");
        gp.ui.addMessage("You gained " + gp.monster[gp.currentMap][i].exp + " exp!");
        exp += gp.monster[gp.currentMap][i].exp;
        checkLevelUp();
    }
}
}

    public void damageInteractiveTile(int i) {
    if(i != 999 &&
       gp.iTile[gp.currentMap][i].destructible == true &&
       gp.iTile[gp.currentMap][i].isCorrectItem(this) == true &&
       gp.iTile[gp.currentMap][i].invincible == false) {

        gp.iTile[gp.currentMap][i].playSE();
        gp.iTile[gp.currentMap][i].life--;
        gp.iTile[gp.currentMap][i].invincible = true;

        generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

        // 🔧 Reduce weapon durability if player is attacking with a weapon
        if (this == gp.player && gp.player.currentWeapon != null) {
            entity weapon = gp.player.currentWeapon;
            if (weapon.durability > 0) {
                weapon.durability--;
                if (weapon.durability == 0) {
                    gp.ui.addMessage("Your " + weapon.name + " is BROKEN!");
                }
            }
        }

        if(gp.iTile[gp.currentMap][i].life == 0) {
            gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
        }
    }
}

    public void damageProjectile(int i) {
        if(i != 999) {
            entity projectile = gp.projectile[gp.currentMap][i];
            projectile.alive = false; 
            generateParticle(projectile, projectile);
        }
    }

    public void checkLevelUp() {
    if (exp >= nextLevelExp) {
        level++;
        dexterity++;
        strength++;
        maxLife += 2;
        nextLevelExp = (int)(nextLevelExp * 2);
        attack = getAttack();
        defense = getDefense();

        setDialogue(); // ← call this AFTER level is updated

        gp.playSE(4);
        gp.gameState = gp.dialogueState;
        startDialogue(this, 0);
    }
}

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndex(gp.ui.playerSlotCol, gp.ui.playerSlotRow);

        if(itemIndex < inventory.size()) {
            entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == type_sword || selectedItem.type == type_axe || selectedItem.type == type_picaxe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            if(selectedItem.type == type_shield) {
                currentSHield = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_light) {
                if(currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            if(selectedItem.type == type_consumable) {
                
                if(selectedItem.use(this) == true) {
                    if(selectedItem.amount > 1) {
                        selectedItem.amount--;
                    } else {
                        inventory.remove(itemIndex);
                    }
                }  
            }
        }
    }
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i; 
                break;
            }
        }
        return itemIndex; 
    }
    public boolean canObtainItem(entity item) {
        
        boolean canObtain = false;

        entity newItem = gp.eGenerator.getObject(item.name);

        if(newItem.stackable == true) {
            int index = searchItemInInventory(newItem.name);
            
            if(index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else {
                if(inventory.size() != maxInventorySize) {
                    inventory.add(newItem);
                    canObtain = true;
                }
            }
        }
        else {
            if(inventory.size() != maxInventorySize) {
                inventory.add(newItem);
                canObtain = true;
            }
        }
        return canObtain;
    }

    public void draw(Graphics2D g2) {
    BufferedImage image = null;
    int tempScreenX = screenX;
    int tempScreenY = screenY;
    
    switch (direction) {
        case "up":
            if(attacking == false) {
                if(spriteNum == 1) {image = up1;}
                if(spriteNum == 2) {image = up2;}
            }
            if(attacking == true) {
                if(spriteNum == 1) {image = AttackUp1;}
                if(spriteNum == 2) {image = AttackUp2;}
            }
            if(guarding == true) {
                image = guardUp;
            }
            break;
        case "down":
            if(attacking == false) {
                if(spriteNum == 1) {image = down1;}
                if(spriteNum == 2) {image = down2;}
            }
            if(attacking == true) {
                if(spriteNum == 1) {image = AttackDown1;}
                if(spriteNum == 2) {image = AttackDown2;}
            }
            if(guarding == true) {
                image = guardDown;
            }
            break;
        case "left":
            if(attacking == false) {
                if(spriteNum == 1) {image = left1;}
                if(spriteNum == 2) {image = left2;}
            }
            if(attacking == true) {
                if(spriteNum == 1) {image = AttackLeft1;}
                if(spriteNum == 2) {image = AttackLeft2;}
            }
            if(guarding == true) {
                image = guardLeft;
            }
            break;
        case "right":
            if(attacking == false) {
                if(spriteNum == 1) {image = right1;}
                if(spriteNum == 2) {image = right2;}
            }
            if(attacking == true) {
                if(spriteNum == 1) {image = AttackRight1;}
                if(spriteNum == 2) {image = AttackRight2;}
            }
            if(guarding == true) {
                image = guardRight;
            }
            break;
    }
    
    // Set transparency for invincibility or invisibility
    if (transparent == true) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
    }
    if (gp.playerStatus.invisibilityActive) {  // ADD THIS
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
    }
    
    if(drawing == true) {
        g2.drawImage(image, tempScreenX, tempScreenY, null);
    }
    
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
}

    public void loseLevel() {
    if (level > 1) {
        level--;

        // You can make these scale however you want
        maxLife -= 2;
        strength -= 1;
        dexterity -= 1;

        // Clamp stats to minimums
        if (maxLife < 1) maxLife = 1;
        if (strength < 1) strength = 1;
        if (dexterity < 1) dexterity = 1;

        // Adjust EXP so they don’t instantly level up again
        exp = 0;

        // Recalculate the new EXP needed for next level
        nextLevelExp = calculateNextLevelExp();

        // Clamp current life if it exceeds new max
        if (life > maxLife) life = maxLife;
    }
}

public int calculateNextLevelExp() {
    return (int)(Math.pow(2, level) * 10);
}

   
}