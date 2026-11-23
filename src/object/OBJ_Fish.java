package object;

import entity.entity;
import main.gamePanel;

/** Everything you can pull out of the water. Eat fish to heal, or sell them to the merchant. */
public class OBJ_Fish extends entity {

    gamePanel gp;

    public static final String MINNOW = "Minnow";
    public static final String SUNFISH = "Sunfish";
    public static final String MARSH_EEL = "Marsh Eel";
    public static final String GOLDEN_CARP = "Golden Carp";
    public static final String OLD_BOOT = "Old Boot";

    /** How hard it fights on the line, 0 (a boot) to 4 (the carp). */
    public int difficulty;
    boolean edible = true;

    public OBJ_Fish(gamePanel gp, String kind) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        stackable = true;
        name = kind;

        String image;
        switch (kind) {
            case SUNFISH:
                image = "sunfish"; value = 4; price = 40; difficulty = 2;
                description = "[Sunfish]\nA bright pond fish.\nEat: heals " + value + " half-hearts.";
                break;
            case MARSH_EEL:
                image = "marshEel"; value = 6; price = 70; difficulty = 3;
                description = "[Marsh Eel]\nOnly bites in the marsh.\nEat: heals " + value + " half-hearts\nand refills stamina.";
                break;
            case GOLDEN_CARP:
                image = "goldenCarp"; value = 0; price = 240; difficulty = 4;
                description = "[Golden Carp]\nVery rare. Bites more at night.\nEat: full heal. Or sell it\nfor a fortune.";
                break;
            case OLD_BOOT:
                image = "oldBoot"; value = 0; price = 4; difficulty = 0; edible = false;
                description = "[Old Boot]\nSoggy. Not food.\nThe merchant might take it.";
                break;
            default: // MINNOW
                name = MINNOW;
                image = "minnow"; value = 2; price = 16; difficulty = 1;
                description = "[Minnow]\nSmall but tasty.\nEat: heals " + value + " half-hearts.";
                break;
        }
        down1 = setup("/res/Objects/" + image, gp.tileSize, gp.tileSize);
    }

    public boolean use(entity user) {
        if (!edible) {
            gp.ui.addMessage("You can't eat a boot. Try selling it.");
            return false;
        }
        if (name.equals(GOLDEN_CARP)) {
            user.life = user.maxLife;
            gp.ui.addMessage("You ate the Golden Carp. Fully healed!");
        } else {
            user.life = Math.min(user.maxLife, user.life + value);
            gp.ui.addMessage("You ate a " + name + ". Healed " + value + " half-hearts.");
        }
        if (name.equals(MARSH_EEL)) {
            gp.player.stamina = gp.player.maxStamina;
        }
        gp.playSE(2);
        return true;
    }
}
