package main;

import entity.entity;
import object.OBJ_Chest;
import object.OBJ_CitrusianHelmet;
import object.OBJ_DashItem;
import object.OBJ_FirePotion;
import object.OBJ_FireSeed;
import object.OBJ_Freeze;
import object.OBJ_Invisibility;
import object.OBJ_Lantern;
import object.OBJ_Regeneration;
import object.OBJ_SpeedBoost;
import object.OBJ_Tent;
import object.OBJ_axe;
import object.OBJ_axe_watermelon;
import object.OBJ_coin;
import object.OBJ_door;
import object.OBJ_door_bronze;
import object.OBJ_door_gold;
import object.OBJ_door_silver;
import object.OBJ_ironDoor;
import object.OBJ_key;
import object.OBJ_key_bronze;
import object.OBJ_key_silver;
import object.OBJ_mana;
import object.OBJ_picaxe;
import object.OBJ_pickaxe_pineapple;
import object.OBJ_potion;
import object.OBJ_rock;
import object.OBJ_shield_apple;
import object.OBJ_shield_blue;
import object.OBJ_shield_default;
import object.OBJ_shield_dragonfruit;
import object.OBJ_shield_grape;
import object.OBJ_sword_banana;
import object.OBJ_sword_default;

public class EntityGenerator {
    
    gamePanel gp;
    
    public EntityGenerator(gamePanel gp) {
        this.gp = gp;
    }
    public entity getObject(String itemName) {
        
        entity obj = null;
        
        switch (itemName) {
            case OBJ_axe.objName: obj = new OBJ_axe(gp); break;
            case OBJ_CitrusianHelmet.objName: obj = new OBJ_CitrusianHelmet(gp); break;
            case OBJ_sword_default.objName: obj = new OBJ_sword_default(gp); break;
            case OBJ_shield_default.objName: obj = new OBJ_shield_default(gp); break;
            case OBJ_key.objName: obj = new OBJ_key(gp); break;
            case OBJ_key_bronze.objName: obj = new OBJ_key_bronze(gp); break;
            case OBJ_key_silver.objName: obj = new OBJ_key_silver(gp); break;
            case OBJ_Lantern.objName: obj = new OBJ_Lantern(gp); break;
            case OBJ_potion.objName: obj = new OBJ_potion(gp); break;
            case OBJ_shield_blue.objName: obj = new OBJ_shield_blue(gp); break;
            case OBJ_Tent.objName: obj = new OBJ_Tent(gp); break;
            case OBJ_door.objName: obj = new OBJ_door(gp); break;
            case OBJ_door_bronze.objName: obj = new OBJ_door_bronze(gp); break;
            case OBJ_door_silver.objName: obj = new OBJ_door_silver(gp); break;
            case OBJ_door_gold.objName: obj = new OBJ_door_gold(gp); break;
            case OBJ_Chest.objName: obj = new OBJ_Chest(gp); break;
            case OBJ_picaxe.objName: obj = new OBJ_picaxe(gp); break;
            case OBJ_ironDoor.objName: obj = new OBJ_ironDoor(gp); break;
            case OBJ_FireSeed.objName: obj = new OBJ_FireSeed(gp, gp.player.attack); break;
            case OBJ_coin.objName: obj = new OBJ_coin(gp); break;
            case OBJ_mana.objName: obj = new OBJ_mana(gp); break;
            case OBJ_rock.objName: obj = new OBJ_rock(gp); break;
            case OBJ_DashItem.objName: obj = new OBJ_DashItem(gp); break;
            case OBJ_Freeze.objName: obj = new OBJ_Freeze(gp); break;
            case OBJ_SpeedBoost.objName: obj = new OBJ_SpeedBoost(gp); break;
            case OBJ_Invisibility.objName: obj = new OBJ_Invisibility(gp); break;
            case OBJ_FirePotion.objName: obj = new OBJ_FirePotion(gp); break;
            case OBJ_axe_watermelon.objName: obj = new OBJ_axe_watermelon(gp); break;
            case OBJ_pickaxe_pineapple.objName: obj = new OBJ_pickaxe_pineapple(gp); break;
            case OBJ_sword_banana.objName: obj = new OBJ_sword_banana(gp); break;
            case OBJ_shield_apple.objName: obj = new OBJ_shield_apple(gp); break;
            case OBJ_shield_dragonfruit.objName: obj = new OBJ_shield_dragonfruit(gp); break;
            case OBJ_shield_grape.objName: obj = new OBJ_shield_grape(gp); break;
            case OBJ_Regeneration.objName: obj = new OBJ_Regeneration(gp); break;
        }
        return obj;
    }
}
