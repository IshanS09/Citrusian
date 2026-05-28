package monster;

import data.Progress;
import entity.entity;
import main.gamePanel;
import object.OBJ_ironDoor;

public class MON_BossBroccoli extends entity {
    gamePanel gp;
    public static final String monName = "Malibroc";
    public boolean idleMode = false;

    public MON_BossBroccoli(gamePanel gp) {
    super(gp);

    this.gp = gp;

    type = type_monster;
    boss = true;
    name = monName;
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 75;
    life = maxLife;
    attack = 10;
    defense = 3;
    exp = 50;
    knockBackPower = 7;
    sleep = true;  
    
    int size = gp.tileSize * 4;
    solidArea.x = 48;
    solidArea.y = 48;
    solidArea.width = size - 64;
    solidArea.height = size - 64;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 170;
    attackArea.height = 170;
    motion1_duration = 25;
    motion2_duration = 50;

    getImage();
    getAttackImage();

}
public void getImage() {

    int i = 4;

    if(inRage == false) {
    up1 = setup("/res/Monster/BossBroccoliUp1", gp.tileSize*i, gp.tileSize*i);
    up2 = setup("/res/Monster/BossBroccoliUp2", gp.tileSize*i, gp.tileSize*i);
    down1 = setup("/res/Monster/BossBroccoliDown1", gp.tileSize*i, gp.tileSize*i);
    down2 = setup("/res/Monster/BossBroccoliDown2", gp.tileSize*i, gp.tileSize*i);
    left1 = setup("/res/Monster/BossBroccoliLeft1", gp.tileSize*i, gp.tileSize*i);
    left2 = setup("/res/Monster/BossBroccoliLeft2", gp.tileSize*i, gp.tileSize*i);
    right1 = setup("/res/Monster/BossBroccoliRight1", gp.tileSize*i, gp.tileSize*i);
    right2 = setup("/res/Monster/BossBroccoliRight2", gp.tileSize*i, gp.tileSize*i);

    idle1 = setup("/res/Monster/BossBroccoli_Idle1", gp.tileSize*i, gp.tileSize*i);
    idle2 = setup("/res/Monster/BossBroccoli_Idle2", gp.tileSize*i, gp.tileSize*i);
    }
    if(inRage == true) {
    up1 = setup("/res/Monster/BossBroccoli_phase2_up1", gp.tileSize*i, gp.tileSize*i);
    up2 = setup("/res/Monster/BossBroccoli_phase2_up2", gp.tileSize*i, gp.tileSize*i);
    down1 = setup("/res/Monster/BossBroccoli_phase2_down1", gp.tileSize*i, gp.tileSize*i);
    down2 = setup("/res/Monster/BossBroccoli_phase2_down2", gp.tileSize*i, gp.tileSize*i);
    left1 = setup("/res/Monster/BossBroccoli_phase2_left1", gp.tileSize*i, gp.tileSize*i);
    left2 = setup("/res/Monster/BossBroccoli_phase2_left2", gp.tileSize*i, gp.tileSize*i);
    right1 = setup("/res/Monster/BossBroccoli_phase2_right1", gp.tileSize*i, gp.tileSize*i);
    right2 = setup("/res/Monster/BossBroccoli_phase2_right2", gp.tileSize*i, gp.tileSize*i);
    }
}
public void getAttackImage() {

    int i = 4;
    if(inRage == false) {
    AttackUp1 = setup("/res/Monster/BossBroccoli_AttackUp1", gp.tileSize*i, gp.tileSize*i);
    AttackUp2 = setup("/res/Monster/BossBroccoli_AttackUp2", gp.tileSize*i, gp.tileSize*i);
    AttackDown1 = setup("/res/Monster/BossBroccoli_AttackDown1", gp.tileSize*i, gp.tileSize*i);
    AttackDown2 = setup("/res/Monster/BossBroccoli_AttackDown2", gp.tileSize*i, gp.tileSize*i);
    AttackLeft1 = setup("/res/Monster/BossBroccoli_AttackLeft1", gp.tileSize*i, gp.tileSize*i);
    AttackLeft2 = setup("/res/Monster/BossBroccoli_AttackLeft2", gp.tileSize*i, gp.tileSize*i);
    AttackRight1 = setup("/res/Monster/BossBroccoli_AttackRight1", gp.tileSize*i, gp.tileSize*i);
    AttackRight2 = setup("/res/Monster/BossBroccoli_AttackRight2", gp.tileSize*i, gp.tileSize*i);
    }
    if(inRage == true) { 
    AttackUp1 = setup("/res/Monster/BossBroccoli_phase2_AttackUp1", gp.tileSize*i, gp.tileSize*i);
    AttackUp2 = setup("/res/Monster/BossBroccoli_phase2_AttackUp2", gp.tileSize*i, gp.tileSize*i);
    AttackDown1 = setup("/res/Monster/BossBroccoli_phase2_AttackDown1", gp.tileSize*i, gp.tileSize*i);
    AttackDown2 = setup("/res/Monster/BossBroccoli_phase2_AttackDown2", gp.tileSize*i, gp.tileSize*i);
    AttackLeft1 = setup("/res/Monster/BossBroccoli_phase2_AttackLeft1", gp.tileSize*i, gp.tileSize*i);
    AttackLeft2 = setup("/res/Monster/BossBroccoli_phase2_AttackLeft2", gp.tileSize*i, gp.tileSize*i);
    AttackRight1 = setup("/res/Monster/BossBroccoli_phase2_AttackRight1", gp.tileSize*i, gp.tileSize*i);
    AttackRight2 = setup("/res/Monster/BossBroccoli_phase2_AttackRight2", gp.tileSize*i, gp.tileSize*i);
    }
}

public void setAction() {
    if(gp.playerStatus.isPlayerInvisible()) {
        getRandomDirection();
        return;
    }
    
    if(inRage == false && life < maxLife/2) {
        inRage = true;
        getImage();
        getAttackImage();
        defaultSpeed++;
        speed = defaultSpeed;
        attack += 3;
    }
    if(getTileDistance(gp.player) < 10) {
        moveTowardPlayer(60);
    } else {
    getRandomDirection();
  }
  if(attacking == false) {
    checkAttackOrNot(60, gp.tileSize*5, gp.tileSize*5);
  }

}
public void damageReaction() {
    actionLockCounter = 0;
}
public void checkDrop() {

    gp.bossBattleOn = false;
    Progress.maliBrocDefeated = true;

    gp.stopMusic();
    gp.playMusic(18);

    for(int i = 0; i < gp.obj[1].length; i++) {
        if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(OBJ_ironDoor.objName)) {
            gp.playSE(19);
            gp.obj[gp.currentMap][i] = null;
            break;
        }
    }
   
    // int dropChance = new Random().nextInt(100) + 1;

    // if(dropChance < 50) {
    //     dropItem(new OBJ_coin(gp));
    // }
    // if(dropChance >= 50 && dropChance < 75) {
    //     dropItem(new OBJ_heart(gp));
    // }
    // if(dropChance > 75 && dropChance < 100) {
    //     dropItem(new OBJ_mana(gp));
    // }
 }
}
