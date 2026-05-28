package entity;

import main.gamePanel;
import object.OBJ_DashItem;
import object.OBJ_FirePotion;
import object.OBJ_Freeze;
import object.OBJ_Invisibility;
import object.OBJ_Lantern;
import object.OBJ_Regeneration;
import object.OBJ_SpeedBoost;
import object.OBJ_Tent;
import object.OBJ_axe;
import object.OBJ_axe_watermelon;
import object.OBJ_picaxe;
import object.OBJ_pickaxe_pineapple;
import object.OBJ_potion;
import object.OBJ_shield_apple;
import object.OBJ_shield_blue;
import object.OBJ_shield_default;
import object.OBJ_shield_dragonfruit;
import object.OBJ_shield_grape;
import object.OBJ_sword_banana;
import object.OBJ_sword_default;

public class npc_lemon extends entity {
    public npc_lemon(gamePanel gp) {
        super(gp);
        direction = "down";
        getImage();
        setDialogue();
        setItems();
    }

    public void getImage() {

        up1 = setup("/res/NPC/lemon1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/NPC/lemon2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/NPC/lemon1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/NPC/lemon2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/NPC/lemon1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/NPC/lemon2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/NPC/lemon1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/NPC/lemon2", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        dialogues[0][0] = "iF yOu dOn'T hAvE cRoP CoInS,\n i WiLl bItE yOu.\n juSt kIdDiNg! \n i wIlL bItE yOu eItHeR wAy.";
        dialogues[1][0] = "bRinG mE mOrE cRoP CoIns nExT tIme!!!";
        dialogues[2][0] = "dO yOu wAnT mE tO bItE yOu?!\n yOu DoN't hAvE eNoUgH cRoP CoInS!";
        dialogues[3][0] = "yOu trYiNg tO tAkE tHe whOle StoRe?!\n yOu cAn't hOlD aNy mOrE!";
        dialogues[4][0] = "yOu cAn'T sElL yOuR cUrReNt wEaPoN oR sHiElD!";

    }
    public void setItems() {
        //WEAPONS
        inventory.add(new OBJ_sword_default(gp));
        inventory.add(new OBJ_axe(gp));
        inventory.add(new OBJ_picaxe(gp));
        inventory.add(new OBJ_sword_banana(gp));
        inventory.add(new OBJ_axe_watermelon(gp));
        inventory.add(new OBJ_pickaxe_pineapple(gp));

        // SHIELDS
        inventory.add(new OBJ_shield_default(gp));
        inventory.add(new OBJ_shield_blue(gp));
        inventory.add(new OBJ_shield_apple(gp));
        inventory.add(new OBJ_shield_grape(gp));
        inventory.add(new OBJ_shield_dragonfruit(gp));

        // POTIONS
        inventory.add(new OBJ_potion(gp));
        inventory.add(new OBJ_FirePotion(gp));

        // POWERS
        inventory.add(new OBJ_DashItem(gp));
        inventory.add(new OBJ_Freeze(gp));
        inventory.add(new OBJ_SpeedBoost(gp));
        inventory.add(new OBJ_Invisibility(gp));
        inventory.add(new OBJ_Regeneration(gp));

        // LIGHTING
        inventory.add(new OBJ_Lantern(gp));

        // TENT
        inventory.add(new OBJ_Tent(gp));  
    }
    public void speak() {
        
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;

    }
}
