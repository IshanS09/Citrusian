package monster;

import entity.entity;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.gamePanel;
import object.OBJ_coin;
import object.OBJ_mana;

/**
 * Weeping Onion: weak in a fight, but gets close and puffs out a tear gas cloud
 * that slows you down. It also bursts into a small cloud when it dies.
 */
public class MON_Onion extends entity {

    gamePanel gp;
    public static final String monName = "Weeping Onion";

    static final int WALK = 0;
    static final int PUFF = 1;
    static final int PUFF_RELEASE = 25;   // frame the gas comes out
    static final int PUFF_LENGTH = 50;
    static final int GAS_COOLDOWN = 240;  // 4 seconds between clouds

    int state = WALK;
    int puffCounter = 0;
    int gasCooldown = 90;

    public MON_Onion(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = monName;
        defaultSpeed = 2;
        speed = defaultSpeed;
        maxLife = 8;
        life = maxLife;
        attack = 3;
        defense = 0;
        exp = 6;
        knockBackPower = 3;

        // Sprite is drawn at 2 tiles; the bulb sits in the middle of the frame
        solidArea.x = 36;
        solidArea.y = 40;
        solidArea.width = 56;
        solidArea.height = 68;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        int s = gp.tileSize * 2;
        up1 = setup("/res/Monster/OnionUp1", s, s);
        up2 = setup("/res/Monster/OnionUp2", s, s);
        down1 = setup("/res/Monster/OnionDown1", s, s);
        down2 = setup("/res/Monster/OnionDown2", s, s);
        left1 = setup("/res/Monster/OnionLeft1", s, s);
        left2 = setup("/res/Monster/OnionLeft2", s, s);
        right1 = setup("/res/Monster/OnionRight1", s, s);
        right2 = setup("/res/Monster/OnionRight2", s, s);
        AttackUp1 = setup("/res/Monster/Onion_AttackUp1", s, s);
        AttackUp2 = setup("/res/Monster/Onion_AttackUp2", s, s);
        AttackDown1 = setup("/res/Monster/Onion_AttackDown1", s, s);
        AttackDown2 = setup("/res/Monster/Onion_AttackDown2", s, s);
        AttackLeft1 = setup("/res/Monster/Onion_AttackLeft1", s, s);
        AttackLeft2 = setup("/res/Monster/Onion_AttackLeft2", s, s);
        AttackRight1 = setup("/res/Monster/Onion_AttackRight1", s, s);
        AttackRight2 = setup("/res/Monster/Onion_AttackRight2", s, s);
    }

    @Override
    public void setAction() {
        if (gasCooldown > 0) gasCooldown--;

        if (state == PUFF) {
            speed = 0;
            puffCounter++;
            if (puffCounter == PUFF_RELEASE) {
                releaseGas(2, 240);
            }
            if (puffCounter >= PUFF_LENGTH) {
                state = WALK;
                speed = defaultSpeed;
                gasCooldown = GAS_COOLDOWN;
            }
            return;
        }

        if (gp.playerStatus.isPlayerInvisible()) {
            getRandomDirection();
            return;
        }

        int distance = getTileDistance(gp.player);
        if (distance < gp.eManager.weather.sightRange(8)) {
            moveTowardPlayer(45);
        } else {
            getRandomDirection();
        }

        if (distance <= 3 && gasCooldown == 0) {
            faceTarget();
            state = PUFF;
            puffCounter = 0;
            speed = 0;
        }
    }

    void faceTarget() {
        if (getXdistance(gp.player) > getYdistance(gp.player)) {
            direction = gp.player.getCenterX() < getCenterX() ? "left" : "right";
        } else {
            direction = gp.player.getCenterY() < getCenterY() ? "up" : "down";
        }
    }

    void releaseGas(int radiusTiles, int duration) {
        int cx = worldX + gp.tileSize;       // sprite is 2 tiles wide, so this is its middle
        int cy = worldY + gp.tileSize + 8;
        gp.particleList.add(new TearGas(gp, cx, cy, radiusTiles, duration));
        gp.playSE(12);
    }

    @Override
    protected BufferedImage overrideImage(BufferedImage image) {
        if (state == PUFF && !dying) {
            return attackFrame(puffCounter < PUFF_RELEASE ? 1 : 2);
        }
        return image;
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;
    }

    @Override
    public void checkDrop() {
        // Pops into a small cloud on death
        releaseGas(1, 150);

        int roll = new Random().nextInt(100) + 1;
        if (roll <= 45) {
            dropItem(new OBJ_coin(gp));
        } else if (roll <= 75) {
            dropItem(new OBJ_mana(gp));
        }
    }
}
