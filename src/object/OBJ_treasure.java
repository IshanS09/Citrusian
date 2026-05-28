package object;

import entity.entity;
import main.gamePanel;

public class OBJ_treasure extends entity {

    gamePanel gp;
    public static final String objName = "Citrusian Helm";

    public OBJ_treasure(gamePanel gp) {
        super(gp);

        this.gp = gp;

        type = type_pickUp;
        name = objName;
        down1 = setup("/res/Objects/CitrusianHelmet", gp.tileSize, gp.tileSize);

    }
    public void setDialogues() {

        dialogues[0][0] = "After centuries, the Citrusian Helm is in the hands of a Fructari...";
        dialogues[0][0] = "The Rotroot Clan is no more...";
                
    }
    public boolean use(entity entity) {

        gp.gameState = gp.cutsceneState;
        gp.csManager.sceneNum = gp.csManager.ending;
        return true;

    }

}