package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {

    // Pinned to the ID Java computed for this class, so existing save.dat files still load
    // and adding a field later does not break them
    private static final long serialVersionUID = 7196435754422955874L;
   
    int level;
    int maxLife;
    int life;
    int maxSeed;
    int seed;
    int strength;
    int dexterity;
    int exp;
    int nextLevelExp;
    int coin;
    int currentWeaponDurability;
    int currentShieldDurability;

    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> itemAmounts = new ArrayList<>();
    int currentWeaponSlot;
    int currentShieldSlot;

    String mapObjectNames[][];
    int mapObjectWorldX[][];
    int mapObjectWorldY[][];
    String mapObjectLootNames[][];
    boolean mapObjectOpened[][];

    // Added later. Old saves load with these left at false/0/null,
    // and SaveLoad falls back to the starting spot when hasPosition is false.
    boolean hasPosition;
    int currentMap;
    int worldX;
    int worldY;
    String direction;
    int currentArea;
    boolean maliBrocDefeated;
    boolean baronRotjawDefeated;
    boolean peaKingDefeated;
    int stamina;
    int seedUpgrades;
    int staminaUpgrades;
    int parryUpgrades;

    // Side quests and New Game+
    int ngPlus;
    int questIndex;
    int questProgress;
    boolean questActive;
    boolean questIntroDone;
}
