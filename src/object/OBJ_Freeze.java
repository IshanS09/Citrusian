package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Freeze extends entity {
    
    gamePanel gp;
    public static final String objName = "Freeze";
    
    public OBJ_Freeze(gamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        stackable = true;
        name = objName;
        down1 = setup("/res/Objects/freeze", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nFreezes nearby enemies\nfor 5 seconds!";
        price = 60;
        stackable = true;
        
        setDialogue();
    }
    
    public void setDialogue() {
        dialogues[0][0] = "You used " + name + "!";
    }
    
    public boolean use(entity entity) {
        gp.playerStatus.activateFreeze();
        return true; // Item consumed
    }
}