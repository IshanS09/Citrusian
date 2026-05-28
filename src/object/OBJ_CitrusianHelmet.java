package object;

import entity.entity;
import main.gamePanel;

public class OBJ_CitrusianHelmet extends entity {

    public static final String objName = "Helmet";

    public OBJ_CitrusianHelmet(gamePanel gp) {
        super(gp);
        name = objName;
        down1 = setup("/res/Objects/CitrusianHelmet", gp.tileSize, gp.tileSize);

}
}