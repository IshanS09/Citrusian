package data;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataStorage implements Serializable {
   
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
}
