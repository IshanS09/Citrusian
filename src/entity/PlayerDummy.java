package entity;

import main.gamePanel;

public class PlayerDummy extends entity {

    public static final String npcName = "Dummy";

    public PlayerDummy(gamePanel gp) {
        super(gp);

        name = npcName;
        getImage();
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
}
