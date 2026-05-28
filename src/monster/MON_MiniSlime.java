package monster;

import entity.entity;
import main.gamePanel;

public class MON_MiniSlime extends entity {
    gamePanel gp;
    public static final String monName = "Pea Footsoldier";

    public MON_MiniSlime(gamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_monster;
        name = monName;
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 999; // Can't be killed
        life = maxLife;
        attack = 4;
        defense = 0;
        exp = 0; // No exp from mini-slimes
        knockBackPower = 2;

        int size = gp.tileSize;
        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = size - 16;
        solidArea.height = size - 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        int i = 2;

        up1 = setup("/res/Monster/MiniSlimeDown1", gp.tileSize*i, gp.tileSize*i);
        up2 = setup("/res/Monster/MiniSlimeDown2", gp.tileSize*i, gp.tileSize*i);
        down1 = setup("/res/Monster/MiniSlimeDown1", gp.tileSize*i, gp.tileSize*i);
        down2 = setup("/res/Monster/MiniSlimeDown2", gp.tileSize*i, gp.tileSize*i);
        left1 = setup("/res/Monster/MiniSlimeDown1", gp.tileSize*i, gp.tileSize*i);
        left2 = setup("/res/Monster/MiniSlimeDown2", gp.tileSize*i, gp.tileSize*i);
        right1 = setup("/res/Monster/MiniSlimeDown1", gp.tileSize*i, gp.tileSize*i);
        right2 = setup("/res/Monster/MiniSlimeDown2", gp.tileSize*i, gp.tileSize*i);
    }

    public void setAction() {
        if(gp.playerStatus.isPlayerInvisible()) {
            getRandomDirection();
            return;
        }

        // Always chase the player aggressively
        moveTowardPlayer(60);
    }

    public void damageReaction() {
        // Mini-slimes don't react to damage (can't be killed)
        actionLockCounter = 0;
    }

    public void checkDrop() {
        // Mini-slimes don't drop anything
    }

}