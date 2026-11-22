package main;

import entity.Player;
import entity.entity;
import object.OBJ_GoldenSeed;
import object.OBJ_potion;
import object.OBJ_sword_lime;

/**
 * Side quests handed out one at a time by the old apple (npc_apple).
 * Talk to him to get a quest, do it, then talk to him again for the reward.
 */
public class QuestManager {

    gamePanel gp;

    static final int KILL = 0;          // defeat N of one monster type
    static final int KILL_AT_NIGHT = 1; // defeat N of anything while it's night
    static final int FIND = 2;          // pick up an item

    static class Quest {
        final String title;
        final int type;
        final String target;   // monster name for KILL, item name for FIND
        final String targetLabel;
        final int needed;
        final String[] offer;
        final String[] thanks;

        Quest(String title, int type, String target, String targetLabel, int needed, String[] offer, String[] thanks) {
            this.title = title;
            this.type = type;
            this.target = target;
            this.targetLabel = targetLabel;
            this.needed = needed;
            this.offer = offer;
            this.thanks = thanks;
        }
    }

    // Golden Seed hiding spot: the quiet grove on the west side of the island
    static final int SEED_COL = 12;
    static final int SEED_ROW = 47;

    final Quest[] quests = {
        new Quest("Root Trouble", KILL, "Rotroot Bandit", "Rotroot Bandits", 6,
            new String[] {
                "Those carrot bandits up north keep raiding my orchard.\nDefeat 6 Rotroot Bandits and I'll make it worth your while.",
            },
            new String[] { "Ha! That'll teach those roots.\nHere, 80 Crop Coins for your trouble." }),
        new Quest("Tears in the Field", KILL, "Weeping Onion", "Weeping Onions", 3,
            new String[] {
                "Weeping Onions have taken over the northeast clearing.\nTheir gas makes my one good eye water like a river!",
                "Clear out 3 of them. Hold your breath in there.",
            },
            new String[] { "My eye thanks you. Take 100 Crop Coins\nand a Healing Potion." }),
        new Quest("Spud Stopper", KILL, "Spud Brute", "Spud Brutes", 2,
            new String[] {
                "The Spud Brutes in the south field charge anything that moves.",
                "Tip: when one starts shaking, step aside.\nLet it hit a tree, then strike while it's dizzy.\nDefeat 2 of them.",
            },
            new String[] { "Mashed! Here's 150 Crop Coins." }),
        new Quest("Night Watch", KILL_AT_NIGHT, null, "veggies at night", 5,
            new String[] {
                "When the sun goes down, the veggies grow stronger.\nI used to stand night watch, back before the eye.",
                "Defeat 5 veggies after dark and I'll give you\nmy old blade, the Lime Edge.",
            },
            new String[] { "You fight like I used to.\nThe Lime Edge is yours. Keep it sharp." }),
        new Quest("The Lost Seed", FIND, OBJ_GoldenSeed.objName, "Golden Seed", 1,
            new String[] {
                "One last thing. Long ago I buried a Golden Seed\nin the quiet grove on the west side of the island.",
                "My eye isn't what it was. Could you dig it up for me?",
            },
            new String[] {
                "The Golden Seed! I want you to have its strength.",
                "Your max health went up by one heart.\nThat's all I've got, young orange. Go save the island.",
            }),
    };

    public int current = 0;        // index of the quest being offered or worked on
    public int progress = 0;
    public boolean active = false;
    public boolean introDone = false;

    public QuestManager(gamePanel gp) {
        this.gp = gp;
    }

    public void reset() {
        current = 0;
        progress = 0;
        active = false;
        introDone = false;
    }

    public boolean allDone() {
        return current >= quests.length;
    }

    Quest quest() {
        return allDone() ? null : quests[current];
    }

    boolean ready() {
        return active && quest() != null && progress >= quest().needed;
    }

    /** What the apple says. Returns null when there's nothing quest-related to say. */
    public String[] talk() {
        Quest q = quest();
        if (q == null) return null;

        if (ready()) {
            if (!giveReward(current)) {
                return new String[] { "Your bag is full! Make some room and come back\nfor your reward." };
            }
            String[] lines = q.thanks;
            current++;
            progress = 0;
            active = false;
            gp.playSE(4);
            return lines;
        }
        if (active) {
            if (q.type == FIND) {
                return new String[] { "Still looking for the Golden Seed?\nTry the quiet grove on the west side of the island." };
            }
            return new String[] { q.title + ": " + progress + "/" + q.needed + " " + q.targetLabel + " so far.\nKeep at it!" };
        }

        active = true;
        progress = 0;
        if (q.type == FIND) {
            placeGoldenSeed();
        }
        gp.ui.addMessage("New quest: " + q.title);
        return q.offer;
    }

    boolean giveReward(int index) {
        Player p = gp.player;
        switch (index) {
            case 0:
                p.coin += 80;
                break;
            case 1:
                if (!p.canObtainItem(new OBJ_potion(gp))) return false;
                p.coin += 100;
                break;
            case 2:
                p.coin += 150;
                break;
            case 3:
                if (!p.canObtainItem(new OBJ_sword_lime(gp))) return false;
                break;
            case 4:
                p.maxLife += 2;
                p.life = p.maxLife;
                int seed = gp.player.searchItemInInventory(OBJ_GoldenSeed.objName);
                if (seed != 999) gp.player.inventory.remove(seed);
                break;
        }
        return true;
    }

    /** Called by Player.damageMonster when a monster is defeated. */
    public void onKill(String monsterName, boolean atNight) {
        Quest q = quest();
        if (!active || q == null || progress >= q.needed) return;
        boolean counts = (q.type == KILL && q.target.equals(monsterName)) || (q.type == KILL_AT_NIGHT && atNight);
        if (counts) {
            progress++;
            if (progress >= q.needed) {
                gp.ui.addMessage(q.title + " done! Talk to the old apple.");
            } else {
                gp.ui.addMessage(q.title + ": " + progress + "/" + q.needed);
            }
        }
    }

    /** Called when the player picks up a quest item. */
    public void onItemFound(String itemName) {
        Quest q = quest();
        if (active && q != null && q.type == FIND && q.target.equals(itemName)) {
            progress = q.needed;
            gp.ui.addMessage("Found the Golden Seed! Bring it to the old apple.");
        }
    }

    /** After the map is rebuilt (New Game+), put back a quest item that wasn't picked up yet. */
    public void restoreQuestItems() {
        Quest q = quest();
        if (active && q != null && q.type == FIND && progress < q.needed) {
            placeGoldenSeed();
        }
    }

    void placeGoldenSeed() {
        // Already on the map (e.g. loaded from a save)? Don't add a second one.
        for (entity o : gp.obj[0]) {
            if (o != null && OBJ_GoldenSeed.objName.equals(o.name)) return;
        }
        for (int i = 0; i < gp.obj[0].length; i++) {
            if (gp.obj[0][i] == null) {
                gp.obj[0][i] = new OBJ_GoldenSeed(gp);
                gp.obj[0][i].worldX = SEED_COL * gp.tileSize;
                gp.obj[0][i].worldY = SEED_ROW * gp.tileSize;
                return;
            }
        }
    }

    /** Short line for the HUD, or null when there's no active quest. */
    public String trackerText() {
        Quest q = quest();
        if (!active || q == null) return null;
        if (ready()) return q.title + ": return to the old apple";
        if (q.type == FIND) return q.title + ": find the Golden Seed (west grove)";
        return q.title + ": " + progress + "/" + q.needed + " " + q.targetLabel;
    }
}
