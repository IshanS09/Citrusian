package object;

import entity.entity;
import main.gamePanel;

public class OBJ_door_silver extends entity {

    gamePanel gp;
    public static final String objName = "Silver Door";

    public OBJ_door_silver(gamePanel gp) {

        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = objName;
        down1 = setup("/res/Objects/sliverDoor", gp.tileSize, gp.tileSize); // Load door image
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
        dialogues[0][0] = "You need a Silver Key to open this door.";
    }
    public void interact() {
       
        startDialogue(this, 0);
       
    }
    
} 
