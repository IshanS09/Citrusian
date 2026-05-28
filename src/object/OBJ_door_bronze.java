package object;

import entity.entity;
import main.gamePanel;

public class OBJ_door_bronze extends entity {

    gamePanel gp;
    public static final String objName = "Bronze Door";

    public OBJ_door_bronze(gamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = objName;
        down1 = setup("/res/Objects/bronzeDoor", gp.tileSize, gp.tileSize); // Load door image
        collision = true; // Set collision to true for the door object

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDialogue();

    }
    public void setDialogue() {
        dialogues[0][0] = "You need a Bronze Key to open this door.";
    }
    public void interact() {
       
        startDialogue(this, 0);
       
    }
    
} 
