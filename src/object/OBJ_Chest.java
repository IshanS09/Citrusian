package object;

import entity.entity;
import main.gamePanel;

public class OBJ_Chest extends entity {

    gamePanel gp;
    public static final String objName = "Chest";


    public OBJ_Chest(gamePanel gp) {
       super(gp);
       this.gp = gp;

        type = type_obstacle;
        name = objName;
        image = setup("/res/Objects/chest", gp.tileSize, gp.tileSize);
        image2 = setup("/res/Objects/openedChest", gp.tileSize, gp.tileSize);
        down1 = image; 
        collision = true; 

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }
    public void setLoot(entity loot) {
        this.loot = loot;
        setDialogue();
    }
    public void setDialogue() {
        dialogues[0][0] = "You opened the chest and find a " + loot.name + "!";
        dialogues[1][0] = "You opened the chest and find a " + loot.name + "!\n But your inventory is full!";
        dialogues[2][0] = "The chest is already opened.";
    }
    public void interact() {
    if (!opened) {
        gp.playSE(3);

        if (gp.player.canObtainItem(loot)) {
            startDialogue(this, 0); // You got the item
            down1 = image2;
            opened = true;
        } else {
            startDialogue(this, 1); // Inventory full
        }
    } else {
        startDialogue(this, 2); // Already opened
    }
}
}

