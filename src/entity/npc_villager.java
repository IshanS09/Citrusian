package entity;

import java.awt.Rectangle;
import java.util.Random;
import main.gamePanel;

/**
 * A friendly fruit who lives in the Citrus Grove. Wanders slowly and has a few
 * lines that cycle each time you talk to them. Sprites are /res/NPC/<prefix><Dir><1|2>.
 */
public class npc_villager extends entity {

    private final String spritePrefix;
    private final String[][] lines;

    public npc_villager(gamePanel gp, String name, String spritePrefix, String[][] lines) {
        super(gp);
        this.name = name;
        this.spritePrefix = spritePrefix;
        this.lines = lines;
        type = type_npc;
        speed = 1;
        direction = "down";

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        setDialogue();
    }

    public void getImage() {
        String p = "/res/NPC/" + spritePrefix;
        up1 = setup(p + "Up1", gp.tileSize, gp.tileSize);
        up2 = setup(p + "Up2", gp.tileSize, gp.tileSize);
        down1 = setup(p + "Down1", gp.tileSize, gp.tileSize);
        down2 = setup(p + "Down2", gp.tileSize, gp.tileSize);
        left1 = setup(p + "Left1", gp.tileSize, gp.tileSize);
        left2 = setup(p + "Left2", gp.tileSize, gp.tileSize);
        right1 = setup(p + "Right1", gp.tileSize, gp.tileSize);
        right2 = setup(p + "Right2", gp.tileSize, gp.tileSize);
    }

    public void setDialogue() {
        for (int set = 0; set < lines.length; set++) {
            for (int i = 0; i < lines[set].length; i++) {
                dialogues[set][i] = lines[set][i];
            }
        }
    }

    @Override
    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 150) {
            int i = new Random().nextInt(100);
            direction = i < 25 ? "up" : i < 50 ? "down" : i < 75 ? "left" : "right";
            actionLockCounter = 0;
        }
    }

    @Override
    public void speak() {
        facePlayer();
        startDialogue(this, dialogueSet);
        dialogueSet++;
        if (dialogues[dialogueSet][0] == null) {
            dialogueSet = 0;
        }
    }
}
