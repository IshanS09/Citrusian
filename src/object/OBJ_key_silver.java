package object;

import entity.entity;
import main.gamePanel;

public class OBJ_key_silver extends entity {

    gamePanel gp;
    public static final String objName = "Silver Key";

    public OBJ_key_silver(gamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = objName;
        down1 = setup("/res/Objects/silverKey", gp.tileSize, gp.tileSize); // Load key image
        description = "[" + name + "]\nOpens doors...\nwhat else do you want?";
        price = 0;
        stackable = true;

        setDialogue();
    } 
    public void setDialogue() {
        dialogues[0][0] = "You used the " + name;
        dialogues[1][0] = "Wrong door pal... if it even is a door.";
    }
    public boolean use(entity entity) {
        
        int objIndex = getDetected(entity, gp.obj, "Silver Door");

        if(objIndex != 999) {
            startDialogue(this, 0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        } else {
            startDialogue(this, 1);
            return false;
        }
    }
}