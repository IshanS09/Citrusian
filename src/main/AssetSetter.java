package main;

import data.Progress;
import entity.entity;
import entity.npc_apple;
import entity.npc_bigRock;
import entity.npc_lemon;
import monster.MON_BaronRotjaw;
import monster.MON_BossBroccoli;
import monster.MON_SlimeKing;
import monster.MON_broccoli;
import monster.MON_carrot;
import monster.MON_peas;
import monster.MON_skellPeas;
import object.OBJ_Chest;
import object.OBJ_FirePotion;
import object.OBJ_Lantern;
import object.OBJ_SpeedBoost;
import object.OBJ_Tent;
import object.OBJ_door_bronze;
import object.OBJ_door_gold;
import object.OBJ_door_silver;
import object.OBJ_ironDoor;
import object.OBJ_potion;
import object.OBJ_sword_default;
import object.OBJ_treasure;
import tile_interactive.IT_destructibleWall;
import tile_interactive.IT_drytree;
import tile_interactive.IT_metalPlate;



public class AssetSetter {
    gamePanel gp;

    private static final int MAIN_MAP_ID = 0;

    public AssetSetter(gamePanel gp) {
        this.gp = gp;
    }
    public void setObject() {
        int mapNum = 0;
        int i = 0;

        gp.obj[mapNum][i] = new OBJ_door_bronze(gp);
        gp.obj[mapNum][i].worldX = 59 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 78 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_door_silver(gp);
        gp.obj[mapNum][i].worldX = 50 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 81 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_door_gold(gp);
        gp.obj[mapNum][i].worldX = 52 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 86 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Lantern(gp));
        gp.obj[mapNum][i].worldX = 67 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 61 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Tent(gp));
        gp.obj[mapNum][i].worldX = 67 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 62 * gp.tileSize;
        i++;

        mapNum = 2;
        i = 0;
        
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_potion(gp));
        gp.obj[mapNum][i].worldX = 65 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 48 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Tent(gp));
        gp.obj[mapNum][i].worldX = 50 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 46 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_Lantern(gp));
        gp.obj[mapNum][i].worldX = 45 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 54 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_sword_default(gp));
        gp.obj[mapNum][i].worldX = 33 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 45 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_FirePotion(gp));
        gp.obj[mapNum][i].worldX = 28 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 38 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_potion(gp));
        gp.obj[mapNum][i].worldX = 40 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 35 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_potion(gp));
        gp.obj[mapNum][i].worldX = 58 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 37 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_SpeedBoost(gp));
        gp.obj[mapNum][i].worldX = 52 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 29 * gp.tileSize;
        i++;
        gp.obj[mapNum][i] = new OBJ_Chest(gp);
        gp.obj[mapNum][i].setLoot(new OBJ_potion(gp));
        gp.obj[mapNum][i].worldX = 37 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 29 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_ironDoor(gp);
        gp.obj[mapNum][i].worldX = 33 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 32 * gp.tileSize;
        i++;

        mapNum = 3;
        i = 0;
        
        gp.obj[mapNum][i] = new OBJ_ironDoor(gp);
        gp.obj[mapNum][i].worldX = 57 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 35 * gp.tileSize;
        i++;

        gp.obj[mapNum][i] = new OBJ_treasure(gp);
        gp.obj[mapNum][i].worldX = 57 * gp.tileSize;
        gp.obj[mapNum][i].worldY = 32 * gp.tileSize;
        i++;


    }
    public void setNPC() {

        int mapNum = 0;
        int i = 0;
        
        gp.npc[mapNum][i] = new npc_apple(gp);
        gp.npc[mapNum][i].worldX = 62 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 45 * gp.tileSize;
        i++;

        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new npc_lemon(gp);
        gp.npc[mapNum][i].worldX = 14 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 11 * gp.tileSize;
        i++;

        mapNum = 2;
        i = 0;
        gp.npc[mapNum][i] = new npc_bigRock(gp);
        gp.npc[mapNum][i].worldX = 62 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 59 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_bigRock(gp);
        gp.npc[mapNum][i].worldX = 29 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 40 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_bigRock(gp);
        gp.npc[mapNum][i].worldX = 53 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 45 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_bigRock(gp);
        gp.npc[mapNum][i].worldX = 46 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 32 * gp.tileSize;
        i++;

        gp.npc[mapNum][i] = new npc_bigRock(gp);
        gp.npc[mapNum][i].worldX = 60 * gp.tileSize;
        gp.npc[mapNum][i].worldY = 31 * gp.tileSize;
        i++;




    }
    public void setMonster() {
        int i = 0;
        int mapNum = 0; 

        gp.monster[mapNum][i] = new MON_carrot(gp);
        gp.monster[mapNum][i].worldX = 31 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 21 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_carrot(gp);
        gp.monster[mapNum][i].worldX = 33 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 21 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_carrot(gp);
        gp.monster[mapNum][i].worldX = 34 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 25 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_peas(gp);
        gp.monster[mapNum][i].worldX = 82 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 68 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_peas(gp);
        gp.monster[mapNum][i].worldX = 85 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 69 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_peas(gp);
        gp.monster[mapNum][i].worldX = 82 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 70 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_peas(gp);
        gp.monster[mapNum][i].worldX = 81 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 73 * gp.tileSize;
        i++;

        gp.monster[mapNum][i] = new MON_peas(gp);
        gp.monster[mapNum][i].worldX = 77 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 73 * gp.tileSize;
        i++;
       
        mapNum = 2;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 46 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 62 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 62 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 58 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 62 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 50 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 60 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 60 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 55 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 45 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 51 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 51 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 43 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 51 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 43 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 55 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 46 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 43 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 48 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 38 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 55 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 38 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 56 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 31 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 41 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 37 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 29 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 42 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 29 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 46 * gp.tileSize;
        i++;
        gp.monster[mapNum][i] = new MON_skellPeas(gp);
        gp.monster[mapNum][i].worldX = 29 * gp.tileSize;
        gp.monster[mapNum][i].worldY = 53 * gp.tileSize;
        i++;
        
        mapNum = 3;
        i++;

        if(Progress.maliBrocDefeated == false) {
            gp.monster[mapNum][i] = new MON_BossBroccoli(gp);
            gp.monster[mapNum][i].worldX = 55 * gp.tileSize;
            gp.monster[mapNum][i].worldY = 38 * gp.tileSize;
            i++;
        }

        mapNum = 4;
        i++;

        if(Progress.baronRotjawDefeated == false) {
            gp.monster[mapNum][i] = new MON_BaronRotjaw(gp);
            gp.monster[mapNum][i].worldX = 49 * gp.tileSize;
            gp.monster[mapNum][i].worldY = 35 * gp.tileSize;
            i++;
        }

        mapNum = 5;
        i++;

        if(Progress.peaKingDefeated == false) {
            gp.monster[mapNum][i] = new MON_SlimeKing(gp);
            gp.monster[mapNum][i].worldX = 50 * gp.tileSize;
            gp.monster[mapNum][i].worldY = 29 * gp.tileSize;
            i++;
        }
    }

    public void setInteractiveTile() {
        int mapNum = 0;
        int i = 0;

      
        gp.iTile[mapNum][i] = new IT_drytree(gp, 59, 79);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 58, 79);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 58, 80);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 57, 80);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 56, 80);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 55, 80);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 54, 80);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 54, 81);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 53, 81);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 52, 81);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 51, 81);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 50, 82);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 49, 82);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 49, 83);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 49, 84);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 49, 85);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 50, 85);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 50, 86);
        i++;
        gp.iTile[mapNum][i] = new IT_drytree(gp, 51, 86);
        i++;
 

        mapNum = 2;
        i = 0;

        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 39, 62);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 40, 61);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 50, 59);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 54, 60);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 65, 61);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 66, 61);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 56, 50);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 56, 51);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 60, 47);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 61, 47);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 64, 42);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 65, 37);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 62, 35);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 55, 34);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 56, 34);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 46, 30);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 46, 31);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 45, 31);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 45, 32);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 45, 37);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 45, 38);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 34, 38);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 33, 38);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 35, 51);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 36, 51);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 40, 48);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 42, 53);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 43, 53);
        i++;
        gp.iTile[mapNum][i] = new IT_destructibleWall(gp, 44, 53);
        i++;


        gp.iTile[mapNum][i] = new IT_metalPlate(gp, 29, 53);
        i++;
        gp.iTile[mapNum][i] = new IT_metalPlate(gp, 43, 55);
        i++;
        gp.iTile[mapNum][i] = new IT_metalPlate(gp, 63, 49);
        i++;
        gp.iTile[mapNum][i] = new IT_metalPlate(gp, 56, 39);
        i++;
        gp.iTile[mapNum][i] = new IT_metalPlate(gp, 38, 30);
        i++;


    }

    public void respawnCarrots(int mapNum) {
    if(mapNum != MAIN_MAP_ID) return;
    
    int[][] carrotSpawns = {
        {33, 21},
        {32, 21},
        {34, 25}
    };
    
    for (int i = 0; i < 3; i++) {
        MON_carrot carrot = new MON_carrot(gp);
        carrot.worldX = gp.tileSize * carrotSpawns[i][0];
        carrot.worldY = gp.tileSize * carrotSpawns[i][1];
        carrot.name = MON_carrot.monName;
        carrot.alive = true;
        gp.monster[mapNum][i] = carrot; // Slots 0–2
    }
}

public boolean allCarrotsDead(int mapNum) {
    for (int i = 0; i < 3; i++) {
        entity e = gp.monster[mapNum][i]; // Only check slots 0–2
        if (e != null && MON_carrot.monName.equals(e.name)) {
            if (e.alive && !e.dying) {
                return false;
            }
        }
    }
    return true;
}

public void respawnPeas(int mapNum) {
    if(mapNum != MAIN_MAP_ID) return;

    int[][] peaSpawns = {
        {82, 68},
        {85, 69},
        {82, 70},
        {81, 73},
        {77, 73}
    };

    for (int i = 0; i < 5; i++) {
        MON_peas peas = new MON_peas(gp);
        peas.worldX = gp.tileSize * peaSpawns[i][0];
        peas.worldY = gp.tileSize * peaSpawns[i][1];
        peas.name = MON_peas.monName;
        peas.alive = true;
        gp.monster[mapNum][5 + i] = peas; // Slots 5–9
    }
}


public boolean allPeasDead(int mapNum) {
    for (int i = 5; i < 10; i++) {
        entity e = gp.monster[mapNum][i];
        if (e != null && MON_peas.monName.equals(e.name)) {
            if (e.alive && !e.dying) {
                return false;
            }
        }
    }
    return true;
}

public void respawnBroccoli(int mapNum) {
    if(mapNum != MAIN_MAP_ID) return;

    MON_broccoli broccoli = new MON_broccoli(gp);
    broccoli.worldX = gp.tileSize * 34; // Replace with desired tile X
    broccoli.worldY = gp.tileSize * 62; // Replace with desired tile Y
    broccoli.name = MON_broccoli.monName;
    broccoli.alive = true;
    gp.monster[mapNum][10] = broccoli; // Just one slot
}

public boolean allBroccoliDead(int mapNum) {
    entity e = gp.monster[mapNum][10];
    return !(e != null && MON_broccoli.monName.equals(e.name) && e.alive && !e.dying);
}

}
