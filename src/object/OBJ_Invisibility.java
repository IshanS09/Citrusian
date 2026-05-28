package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Invisibility extends entity {
    
    gamePanel gp;
    public static final String objName = "Invisibility";
    
    public OBJ_Invisibility(gamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        stackable = true;
        name = objName;
        down1 = setup("/res/Objects/invis", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nBecome invisible for\n5 seconds. Enemies\nignore you!";
        price = 75;
        stackable = true;
        
        setDialogue();
    }
    
    public void setDialogue() {
        dialogues[0][0] = "You used " + name + "!";
    }
    
    public boolean use(entity entity) {
        gp.playerStatus.activateInvisibility();
        return true; // Item consumed
    }
}