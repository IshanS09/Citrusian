package object;

import entity.entity;
import main.gamePanel;

public class OBJ_coin extends entity {

    gamePanel gp;
    public static final String objName = "Crop Coin";

    public OBJ_coin(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickUp;
        name = objName;

        // Random value between 30 and 50 (inclusive)
        value = (int)(Math.random() * 21) + 30;

        down1 = setup("/res/Objects/coin", gp.tileSize, gp.tileSize);
    }

    public boolean use(entity entity) {
        gp.playSE(1);
        gp.ui.addMessage("Crop Coin +" + value);
        gp.player.coin += value;
        return true;
    }
}
