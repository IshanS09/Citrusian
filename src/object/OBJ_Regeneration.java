package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Regeneration extends entity {
    
    gamePanel gp;
    public static final String objName = "Regeneration";
    
    public OBJ_Regeneration(gamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        name = objName;
        down1 = setup("/res/Objects/regeneration", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nSlowly restores health\nover 10 seconds.";
        price = 80;
        stackable = true;
        
        setDialogue();
    }
    
    public void setDialogue() {
        dialogues[0][0] = "You used " + name + "!";
    }
    
    public boolean use(entity entity) {
        gp.playerStatus.activateRegeneration();
        return true; // Item consumed
    }
}