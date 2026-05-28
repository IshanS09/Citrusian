package object;

import entity.entity;
import main.gamePanel;

public class OBJ_SpeedBoost extends entity {
    
    gamePanel gp;
    public static final String objName = "Speed Boost";
    
    public OBJ_SpeedBoost(gamePanel gp) {
        super(gp);
        this.gp = gp;
        
        type = type_consumable;
        stackable = true;
        name = objName;
        down1 = setup("/res/Objects/speedboost", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nDoubles your speed\nfor 5 seconds.";
        price = 60;
        stackable = true;
        
        setDialogue();
    }
    
    public void setDialogue() {
        dialogues[0][0] = "You used a " + name + "!";
    }
    
    public boolean use(entity entity) {
        gp.playerStatus.activateSpeedBoost();
        return true; // Item consumed
    }
}
