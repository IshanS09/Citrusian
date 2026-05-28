package monster;

import entity.entity;
import java.util.Random;
import main.gamePanel;
import object.OBJ_coin;
import object.OBJ_heart;
import object.OBJ_mana;
import object.OBJ_rock;

public class MON_carrot extends entity {
    gamePanel gp;
    public static final String monName = "Rotroot Bandit";
    
    public MON_carrot(gamePanel gp) {
    super(gp);

    this.gp = gp;

    type = type_monster;
    name = monName;
    defaultSpeed = 2;
    speed = defaultSpeed;
    maxLife = 10;
    life = maxLife;
    attack = 5;
    defense = 1;
    exp = 5;
    projectile = new OBJ_rock(gp);
    knockBackPower = 6;

    solidArea.x = 8;
    solidArea.y = 8;
    solidArea.width = 80;
    solidArea.height = 84;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 104;
    attackArea.height = 104;
    motion1_duration = 40;
    motion2_duration = 85;

    getImage();

}
public void getImage() {
    int i = 2;
    
    up1 = setup("/res/Monster/CarrotUp1", gp.tileSize*i, gp.tileSize*i);
    up2 = setup("/res/Monster/CarrotUp2", gp.tileSize*i, gp.tileSize*i);
    down1 = setup("/res/Monster/CarrotDown1", gp.tileSize*i, gp.tileSize*i);
    down2 = setup("/res/Monster/CarrotDown2", gp.tileSize*i, gp.tileSize*i);
    left1 = setup("/res/Monster/CarrotLeft1", gp.tileSize*i, gp.tileSize*i);
    left2 = setup("/res/Monster/CarrotLeft2", gp.tileSize*i, gp.tileSize*i);
    right1 = setup("/res/Monster/CarrotRight1", gp.tileSize*i, gp.tileSize*i);
    right2 = setup("/res/Monster/CarrotRight2", gp.tileSize*i, gp.tileSize*i);
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
    checkShootorNot(150, 30);
  }

}
public void damageReaction() {
    actionLockCounter = 0;
}
public void checkDrop() {
   
    int dropChance = new Random().nextInt(100) + 1;

    if(dropChance < 50) {
        dropItem(new OBJ_coin(gp));
    }
    if(dropChance >= 50 && dropChance < 75) {
        dropItem(new OBJ_heart(gp));
    }
    if(dropChance > 75 && dropChance < 100) {
        dropItem(new OBJ_mana(gp));
    }
}
}

