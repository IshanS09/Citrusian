package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import entity.Player;
import entity.entity;
import main.gamePanel;

public class SaveLoad {
    private static final String SAVE_FILE = "save.dat";
    gamePanel gp;

    public SaveLoad(gamePanel gp) {
        this.gp = gp;
    }
    
    /** Writes the save file. Returns true if it was written successfully. */
    public boolean save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(SAVE_FILE)))) {
            
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

            // Where you are and what you've beaten
            ds.hasPosition = true;
            ds.currentMap = gp.currentMap;
            ds.worldX = gp.player.worldX;
            ds.worldY = gp.player.worldY;
            ds.direction = gp.player.direction;
            ds.currentArea = gp.currentArea;
            ds.maliBrocDefeated = Progress.maliBrocDefeated;
            ds.baronRotjawDefeated = Progress.baronRotjawDefeated;
            ds.peaKingDefeated = Progress.peaKingDefeated;

            // Stamina and merchant upgrades
            ds.stamina = gp.player.stamina;
            ds.seedUpgrades = gp.player.seedUpgrades;
            ds.staminaUpgrades = gp.player.staminaUpgrades;
            ds.parryUpgrades = gp.player.parryUpgrades;

            ds.ngPlus = gp.ngPlus;
            ds.questIndex = gp.questManager.current;
            ds.questProgress = gp.questManager.progress;
            ds.questActive = gp.questManager.active;
            ds.questIntroDone = gp.questManager.introDone;

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
           return true;

        } catch (java.io.IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    public boolean hasSave() {
        return new File(SAVE_FILE).isFile();
    }

    /** Reads the save file. Returns false if there is no save or it can't be read. */
    public boolean load() {
        if (!hasSave()) {
            return false;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(SAVE_FILE)))) {

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

            gp.player.seedUpgrades = ds.seedUpgrades;
            gp.player.staminaUpgrades = ds.staminaUpgrades;
            gp.player.parryUpgrades = ds.parryUpgrades;
            gp.player.maxStamina = Player.BASE_STAMINA + ds.staminaUpgrades * 20;
            gp.player.stamina = ds.hasPosition ? Math.min(ds.stamina, gp.player.maxStamina) : gp.player.maxStamina;
            gp.player.cancelCharge();

            gp.ngPlus = ds.ngPlus;
            gp.questManager.current = ds.questIndex;
            gp.questManager.progress = ds.questProgress;
            gp.questManager.active = ds.questActive;
            gp.questManager.introDone = ds.questIntroDone;

            Progress.maliBrocDefeated = ds.maliBrocDefeated;
            Progress.baronRotjawDefeated = ds.baronRotjawDefeated;
            Progress.peaKingDefeated = ds.peaKingDefeated;

            // Put the player back where they saved. Saves from before this feature
            // have no position, so those start at the usual spot.
            if (ds.hasPosition) {
                gp.currentMap = ds.currentMap;
                gp.player.worldX = ds.worldX;
                gp.player.worldY = ds.worldY;
                gp.player.direction = ds.direction != null ? ds.direction : "down";
                gp.currentArea = ds.currentArea != 0 ? ds.currentArea : gp.outside;
            } else {
                gp.player.setDefaultPositions();
                gp.currentArea = gp.outside;
            }
            gp.nextArea = gp.currentArea;
            gp.bossBattleOn = false;

            // Rebuild the inventory, skipping any item the game no longer knows about.
            // Slot numbers shift when an item is skipped, so track the new weapon/shield slots.
            gp.player.inventory.clear();
            int weaponSlot = -1;
            int shieldSlot = -1;
            for(int i = 0; i < ds.itemNames.size(); i++) {
                entity item = gp.eGenerator.getObject(ds.itemNames.get(i));
                if (item == null) {
                    System.out.println("Skipping unknown saved item: " + ds.itemNames.get(i));
                    continue;
                }
                item.amount = ds.itemAmounts.get(i);
                if (i == ds.currentWeaponSlot) weaponSlot = gp.player.inventory.size();
                if (i == ds.currentShieldSlot) shieldSlot = gp.player.inventory.size();
                gp.player.inventory.add(item);
            }

            if (weaponSlot >= 0) {
                gp.player.currentWeapon = gp.player.inventory.get(weaponSlot);
            }
            if (shieldSlot >= 0) {
                gp.player.currentSHield = gp.player.inventory.get(shieldSlot);
            }

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

            // Rebuild monsters so beaten bosses stay gone. setMonster() only fills
            // slots, so clear out the ones left over from the title screen first.
            for (int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                java.util.Arrays.fill(gp.monster[mapNum], null);
            }
            gp.aSetter.setMonster();
            return true;

        } catch(Exception e) {
            System.out.println("Error loading game: " + e.getMessage());
            return false;
        }
    }
}