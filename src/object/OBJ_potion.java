package object;

import entity.entity;
import main.gamePanel;

public class OBJ_potion extends entity {
    
    gamePanel gp;
    public static final String objName = "Healing Potion";
   

    public OBJ_potion(gamePanel gp) {
        super(gp);

        this.gp = gp;

        name = objName;
        type = type_consumable;
        value = 5;
        down1 = setup("/res/Objects/Potion", gp.tileSize, gp.tileSize);
        description = "[Healing Potion]\nRecovers " + value + " half-hearts.";
        price = 40;
        stackable = true; 

        setDialogue();
    }
    public void setDialogue() {
        dialogues[0][0] = "You drank a " + name + "!\n" + "Your health recovered by " + value + " half-hearts.";
    }
    public boolean use(entity entity) {
        gp.ui.addMessage("Your health recovered by " + value + " half-hearts.");
        entity.life += value;
        gp.playSE(2);
        return true;
    }

}
