package monster;

import entity.entity;
import java.util.Random;
import main.gamePanel;
import object.OBJ_coin;
import object.OBJ_heart;
import object.OBJ_mana;
import object.OBJ_rock;

public class MON_skellPeas extends entity {
     gamePanel gp;

    public MON_skellPeas(gamePanel gp) {

    super(gp);
    this.gp = gp;

    type = type_monster;
    name = "Undead Peashooter";
    defaultSpeed = 2;
    speed = defaultSpeed;
    maxLife = 12;
    life = maxLife;
    attack = 5;
    defense = 1;
    exp = 6;
    projectile = new OBJ_rock(gp);
    knockBackPower = 4;

    solidArea.x = 4;
    solidArea.y = 4;
    solidArea.width = 48;
    solidArea.height = 48;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;


    getImage();

}
public void getImage() {
    up1 = setup("/res/Monster/SkeletonPea1", gp.tileSize, gp.tileSize);
    up2 = setup("/res/Monster/SkeletonPea2", gp.tileSize, gp.tileSize);
    down1 = setup("/res/Monster/SkeletonPea1", gp.tileSize, gp.tileSize);
    down2 = setup("/res/Monster/SkeletonPea2", gp.tileSize, gp.tileSize);
    left1 = setup("/res/Monster/SkeletonPea1", gp.tileSize, gp.tileSize);
    left2 = setup("/res/Monster/SkeletonPea2", gp.tileSize, gp.tileSize);
    right1 = setup("/res/Monster/SkeletonPea1", gp.tileSize, gp.tileSize);
    right2 = setup("/res/Monster/SkeletonPea2", gp.tileSize, gp.tileSize);
}
 
public void setAction() {
    if(gp.playerStatus.isPlayerInvisible()) {
        getRandomDirection();
        return;
    }

    if(onPath == true) {

        checkStopChasingOrNot(gp.player, 10, 100);

        searchPath(getGoalCol(gp.player), getGoalRow(gp.player));

        checkShootorNot(100, 30);

        
    } else {
     checkStartChasingOrNot(gp.player, 5, 100);
     
     getRandomDirection();

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

