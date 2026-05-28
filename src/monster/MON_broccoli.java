package monster;

import entity.entity;
import java.util.Random;
import main.gamePanel;
import object.OBJ_coin;
import object.OBJ_heart;
import object.OBJ_key_bronze;
import object.OBJ_mana;

public class MON_broccoli extends entity {
    gamePanel gp;
    public static final String monName = "Brocbrute";
    public static boolean firstKillDrop = false;
    
    public MON_broccoli(gamePanel gp) {
    super(gp);
    this.gp = gp;

    type = type_monster;
    boss = true;
    name = monName; 
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 40;
    life = maxLife;
    attack = 8;
    defense = 2;
    exp = 10;
    knockBackPower = 5;

    solidArea.x = 64;
    solidArea.y = 32;
    solidArea.width = 80;
    solidArea.height = 84;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 104;
    attackArea.height = 104;
    motion1_duration = 40;
    motion2_duration = 85;

    getImage();
    getAttackImage();
}

public void getImage() {

    int i = 3;

    up1 = setup("/res/Monster/broccoliUp1", gp.tileSize*i, gp.tileSize*i);
    up2 = setup("/res/Monster/broccoliUp2", gp.tileSize*i, gp.tileSize*i);
    down1 = setup("/res/Monster/broccoliDown1", gp.tileSize*i, gp.tileSize*i);
    down2 = setup("/res/Monster/broccoliDown2", gp.tileSize*i, gp.tileSize*i);
    left1 = setup("/res/Monster/broccoliLeft1", gp.tileSize*i, gp.tileSize*i);
    left2 = setup("/res/Monster/broccoliLeft2", gp.tileSize*i, gp.tileSize*i);
    right1 = setup("/res/Monster/broccoliRight1", gp.tileSize*i, gp.tileSize*i);
    right2 = setup("/res/Monster/broccoliRight2", gp.tileSize*i, gp.tileSize*i);
}
public void getAttackImage() {

    int i = 3;

    AttackUp1 = setup("/res/Monster/broccoliAttackUp1", gp.tileSize*i, gp.tileSize*i);
    AttackUp2 = setup("/res/Monster/broccoliAttackUp2", gp.tileSize*i, gp.tileSize*i);
    AttackDown1 = setup("/res/Monster/broccoliAttackDown1", gp.tileSize*i, gp.tileSize*i);
    AttackDown2 = setup("/res/Monster/broccoliAttackDown2", gp.tileSize*i, gp.tileSize*i);
    AttackLeft1 = setup("/res/Monster/broccoliAttackLeft1", gp.tileSize*i, gp.tileSize*i);
    AttackLeft2 = setup("/res/Monster/broccoliAttackLeft2", gp.tileSize*i, gp.tileSize*i);
    AttackRight1 = setup("/res/Monster/broccoliAttackRight1", gp.tileSize*i, gp.tileSize*i);
    AttackRight2 = setup("/res/Monster/broccoliAttackRight2", gp.tileSize*i, gp.tileSize*i);
}
 
public void setAction() {
    if(gp.playerStatus.isPlayerInvisible()) {
        getRandomDirection();
        return;
    }

    if(getTileDistance(gp.player) < 10) {
        moveTowardPlayer(60);
    } else {
    getRandomDirection();
  }
  if(attacking == false) {
    checkAttackOrNot(60, gp.tileSize*4, gp.tileSize*2);
  }

}
public void damageReaction() {
    actionLockCounter = 0;
}
public void checkDrop() {

    // First kill guaranteed key drop
    if(firstKillDrop == false) {
        dropItem(new OBJ_key_bronze(gp));
        firstKillDrop = true;  // Mark as dropped so this happens only once
        return; // Stop here so it doesn't also drop normal loot this time
    }

    // Normal drop system:
    int dropChance = new Random().nextInt(100) + 1;

    if(dropChance < 50) {
        dropItem(new OBJ_coin(gp));
    }
    else if(dropChance < 75) {
        dropItem(new OBJ_heart(gp));
    }
    else if(dropChance < 100) {
        dropItem(new OBJ_mana(gp));
    }
}

}
