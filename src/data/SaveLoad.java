package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import main.gamePanel;

public class SaveLoad {
    gamePanel gp;

    public SaveLoad(gamePanel gp) {
        this.gp = gp;
    }
    
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            
            DataStorage ds = new DataStorage();

            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxSeed = gp.player.maxSeed;
            ds.seed = gp.player.seed;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            for(int i = 0; i < gp.player.inventory.size(); i++) {
                ds.itemNames.add(gp.player.inventory.get(i).name);
                ds.itemAmounts.add(gp.player.inventory.get(i).amount);
            }

            ds.currentWeaponSlot = gp.player.getCurrentWeaponSlot();
            ds.currentShieldSlot = gp.player.getCurrentShieldSlot();
            ds.currentWeaponDurability = gp.player.currentWeapon.durability;
            ds.currentShieldDurability = gp.player.currentSHield.durability;

            ds.mapObjectNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.obj[1].length; i++) {
                    if(gp.obj[mapNum][i] == null) {
                        ds.mapObjectNames[mapNum][i] = "N/A";
                    } else {
                        ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
                        ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
                        ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
                        if(gp.obj[mapNum][i].loot != null) {
                            ds.mapObjectLootNames[mapNum][i] = gp.obj[mapNum][i].loot.name;
                        }
                        ds.mapObjectOpened[mapNum][i] = gp.obj[mapNum][i].opened;
                    }
                }
            }

           oos.writeObject(ds);

        } catch (java.io.IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }
    public void load() {
        try {
            ObjectInputStream ois  = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            DataStorage ds = (DataStorage) ois.readObject();

            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxSeed = ds.maxSeed;
            gp.player.seed = ds.seed;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.nextLevelExp = ds.nextLevelExp;
            gp.player.coin = ds.coin;

            gp.player.inventory.clear();
            for(int i = 0; i < ds.itemNames.size(); i++) {
                gp.player.inventory.add(gp.eGenerator.getObject(ds.itemNames.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmounts.get(i);
            }

            gp.player.currentWeapon = gp.player.inventory.get(ds.currentWeaponSlot);
            gp.player.currentSHield = gp.player.inventory.get(ds.currentShieldSlot);

            if (gp.player.currentWeapon != null) {
                gp.player.currentWeapon.durability = ds.currentWeaponDurability;
            }
            if (gp.player.currentSHield != null) {
                gp.player.currentSHield.durability = ds.currentShieldDurability;
            }

            gp.player.getAttack();
            gp.player.getDefense();
            gp.player.getAttackImage();

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.obj[1].length; i++) {
                if(ds.mapObjectNames[mapNum][i].equals("N/A")) {
                    gp.obj[mapNum][i] = null;
                } else {
                gp.obj[mapNum][i] = gp.eGenerator.getObject(ds.mapObjectNames[mapNum][i]);
                
                // Add null check here
                if(gp.obj[mapNum][i] != null) {
                    gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
                    gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
                    if(ds.mapObjectLootNames[mapNum][i] != null) {
                        gp.obj[mapNum][i].setLoot(gp.eGenerator.getObject(ds.mapObjectLootNames[mapNum][i]));
                    }
                    gp.obj[mapNum][i].opened = ds.mapObjectOpened[mapNum][i];
                    if(gp.obj[mapNum][i].opened == true) {
                        gp.obj[mapNum][i].down1 = gp.obj[mapNum][i].image2; 
                    } 
                }
            }
        }
    }

        } catch(Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }
}