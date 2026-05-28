package object;

import entity.entity;
import main.gamePanel;

public class OBJ_FirePotion extends entity {
    
    gamePanel gp;
    public static final String objName = "Fireseed Potion";
   

    public OBJ_FirePotion(gamePanel gp) {
        super(gp);

        this.gp = gp;

        name = objName;
        type = type_consumable;
        value = 2;
        down1 = setup("/res/Objects/firepotion", gp.tileSize, gp.tileSize);
        description = "[Fireseed Potion]\nRecovers " + value + " fireseeds.";
        price = 50;
        stackable = true; 

        setDialogue();
    }
    public void setDialogue() {
        dialogues[0][0] = "You drank a " + name + "!\n" + "Your mana recovered by " + value + " fireseeds.";
    }
    public boolean use(entity entity) {
        gp.ui.addMessage("Your mana recovered by " + value + " fireseeds.");
        entity.seed += value;
        gp.playSE(2);
        return true;
    }

}