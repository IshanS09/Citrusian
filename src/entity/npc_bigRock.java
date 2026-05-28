package entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import main.gamePanel;
import object.OBJ_ironDoor;
import tile_interactive.IT_metalPlate;

public class npc_bigRock extends entity {
    public static final String npcName = "Big Rock";

    public npc_bigRock(gamePanel gp) {
        super(gp);

        name = npcName;
        speed = 4;
        direction = "down";

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 6;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 44;
        solidArea.height = 40;

        getImage();
        setDialogue();
    }
    public void getImage() {

        up1 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        up2 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        down1 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        down2 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        left1 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        left2 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        right1 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
        right2 = setup("/res/NPC/bigRock", gp.tileSize, gp.tileSize);
    }
    
    public void setDialogue() {
        dialogues[0][0] = "I'm a Rock.";
    }
    public void update() {}

    public void speak() {
      
        facePlayer();
        startDialogue(this, dialogueSet);

        dialogueSet++;
        if(dialogues[dialogueSet][0] == null) {  
           dialogueSet = 0;
        }
    }
    
    public void move(String d) {
        this.direction = d;
        checkCollision();

        if(collisionOn == false) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        detectPlate();
    }

    public void detectPlate() {
    ArrayList<entity> plateList = new ArrayList<>();
    ArrayList<entity> rockList = new ArrayList<>();

    for (int i = 0; i < gp.iTile[1].length; i++) {

        if(gp.iTile[gp.currentMap][i] != null && 
        gp.iTile[gp.currentMap][i].name != null &&
        gp.iTile[gp.currentMap][i].name.equals(IT_metalPlate.itName)) {
            plateList.add(gp.iTile[gp.currentMap][i]);
        }
    }
    
    for (int i = 0; i < gp.npc[1].length; i++) {

        if(gp.npc[gp.currentMap][i] != null && 
            gp.npc[gp.currentMap][i].name.equals(npc_bigRock.npcName)) {
           rockList.add(gp.npc[gp.currentMap][i]);
        }
    }

    int count = 0;

    for(int i = 0; i < plateList.size(); i++) {
        int xDistance = Math.abs(worldX - plateList.get(i).worldX);
        int yDistance = Math.abs(worldY - plateList.get(i).worldY);
        int distance = Math.max(xDistance, yDistance);

        if(distance < 8) {
            if(linkedEntity == null) {
                linkedEntity = plateList.get(i);
                gp.playSE(3);
            }
        } else {
            if(linkedEntity == plateList.get(i)) {
                linkedEntity = null;
            }
        }
    }

    for(int i = 0; i < rockList.size(); i++) {
        if(rockList.get(i).linkedEntity != null) {
            count++;
        } 
    }

    if(count == rockList.size()) {
            for(int i = 0; i < gp.obj[1].length; i++) {
                if(gp.obj[gp.currentMap][i] != null && 
                gp.obj[gp.currentMap][i].name.equals("Iron Door")) {
                    gp.obj[gp.currentMap][i] = null;
                    gp.playSE(19);
                }
            }
        }
}

}
