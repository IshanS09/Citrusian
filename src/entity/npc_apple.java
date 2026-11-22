package entity;

import java.awt.Rectangle;
import java.util.Random;
import main.gamePanel;

public class npc_apple extends entity  {

     private int actionLockCounter = 0;

    public npc_apple(gamePanel gp) {
        super(gp);
        speed = 1;
        direction = "down";

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
        setDialogue();


    }

    public void getImage() {

        up1 = setup("/res/NPC/AppleUp1", gp.tileSize, gp.tileSize);
        up2 = setup("/res/NPC/AppleUp2", gp.tileSize, gp.tileSize);
        down1 = setup("/res/NPC/AppleDown1", gp.tileSize, gp.tileSize);
        down2 = setup("/res/NPC/AppleDown2", gp.tileSize, gp.tileSize);
        left1 = setup("/res/NPC/AppleLeft1", gp.tileSize, gp.tileSize);
        left2 = setup("/res/NPC/AppleLeft2", gp.tileSize, gp.tileSize);
        right1 = setup("/res/NPC/AppleRight1", gp.tileSize, gp.tileSize);
        right2 = setup("/res/NPC/AppleRight2", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        dialogues[1][0] = "Hello, young orange.";
        dialogues[1][1] = "I used to be a great citrusian warrior, \nuntil I got my eye scratched out.";
        dialogues[1][2] = "You must defeat the three Rotroot Generals and obtain the dungeon keys.";
        dialogues[1][3] = "The easiest one to defeat and find is Brocbrute. \nBaron Rotjaw ranks second in power among the generals. \nFinally, the most powerful and elusive of the three, the Pea King. \nAlhough, it is said he hides in plain sight.";
        dialogues[1][4] = "The keys will allow you to enter the Rotroot Dungeon where the evil \nMalibroc resides.";
        dialogues[1][5] = "Good luck on your journey, you'll need it.";
        dialogues[1][6] = "Oh, and if you want to help out an old warrior,\ntalk to me again. I have a few jobs for you.";

        dialogues[0][0] = "Tents can be used to rest and recover your health and fireseed.\nYou can buy them from the Lemon merchant.";
        dialogues[0][1] = "Be careful with your fireseed, it's a precious resource.\nYou can buy a potion to restore it or simply defeat veggies.";
        dialogues[0][2] = "Speaking of veggies, if you kill a herd of them, \nthey will respawn after a short while.\nI don't know how this works, but it happens.";
        dialogues[0][3] = "Don't forget your weapon and shield have durability.\nIf they break, they will deal/defend no damage.";

    }

    public void setAction() {
        
        actionLockCounter++;
        if(actionLockCounter == 120) {
        Random random = new Random();
        int i = random.nextInt(100) + 1;

        if(i <= 25) {
            direction = "up";
        }  if(i > 25 && i <= 50) {
            direction = "down";
        }  if(i > 50 && i <= 75) {
            direction = "left";
        }  if(i > 75 && i <= 100) {
            direction = "right";
        }

        actionLockCounter = 0;
        }
    }
    static final int QUEST_SET = 10; // dialogue set used for quest lines

    public void speak() {
      
        facePlayer();

        // First chat is the backstory, after that he hands out side quests
        if (!gp.questManager.introDone) {
            gp.questManager.introDone = true;
            startDialogue(this, 1);
            return;
        }
        String[] questLines = gp.questManager.talk();
        if (questLines != null) {
            java.util.Arrays.fill(dialogues[QUEST_SET], null);
            for (int i = 0; i < questLines.length; i++) {
                dialogues[QUEST_SET][i] = questLines[i];
            }
            startDialogue(this, QUEST_SET);
            return;
        }
        if (dialogueSet >= QUEST_SET) {
            dialogueSet = 0; // back to his usual tips once the quests are done
        }

        startDialogue(this, dialogueSet);

        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {  
           dialogueSet = 0;
        }
    }
    
}