package object;

import entity.entity;
import main.gamePanel;

public class OBJ_DashItem extends entity {
    
    gamePanel gp;
    public static final String objName = "Dash";
    
    public OBJ_DashItem(gamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        stackable = true;
        name = objName;
        down1 = setup("/res/Objects/dash", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nQuickly dash 4 tiles.\nPress SHIFT to use.\nPass through enemies!";
        price = 30;
        stackable = true;
        
        setDialogue();
    }
    
    public void setDialogue() {
        dialogues[0][0] = "You used a " + name + "!";
    }
    
    public boolean use(entity entity) {
        // This shouldn't be called from inventory
        // Dash is triggered by hotkey
        return false;
    }
}