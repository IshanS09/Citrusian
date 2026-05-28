package object;

import entity.entity;
import main.gamePanel;

public class OBJ_ironDoor extends entity {
    gamePanel gp;
    public static final String objName = "Iron Door";

    public OBJ_ironDoor(gamePanel gp) {
        
        super(gp);
        this.gp = gp;


        type = type_obstacle;
        name = objName;
        down1 = setup("/res/Objects/ironDoor", gp.tileSize, gp.tileSize); // Load door image
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
        dialogues[0][0] = "You need to activate pressure plates to open this door.";
    }
    public void interact() {
       
        startDialogue(this, 0);
       
    }
    
}
