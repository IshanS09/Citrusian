package monster;

import entity.entity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import main.gamePanel;
import object.OBJ_coin;
import object.OBJ_heart;

/**
 * Spud Brute: slow and tough. When you line up with it, it digs in and charges
 * in a straight line. If it misses and hits a wall it gets dazed and takes
 * double damage for a moment.
 */
public class MON_Potato extends entity {

    gamePanel gp;
    public static final String monName = "Spud Brute";

    static final int WALK = 0;
    static final int WINDUP = 1;
    static final int CHARGE = 2;
    static final int DAZED = 3;

    static final int WINDUP_LENGTH = 40;   // about 2/3 of a second to dodge
    static final int CHARGE_SPEED = 9;
    static final int CHARGE_MAX_TILES = 7;
    static final int DAZE_LENGTH = 90;
    static final int CHARGE_COOLDOWN = 150;

    int state = WALK;
    int stateCounter = 0;
    int cooldown = 60;
    boolean touchingPlayer = false;

    public MON_Potato(gamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_monster;
        name = monName;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxLife = 20;
        life = maxLife;
        attack = 6;
        defense = 2;
        exp = 10;
        knockBackPower = 8;

        solidArea.x = 28;
        solidArea.y = 48;
        solidArea.width = 72;
        solidArea.height = 60;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        int s = gp.tileSize * 2;
        up1 = setup("/res/Monster/PotatoUp1", s, s);
        up2 = setup("/res/Monster/PotatoUp2", s, s);
        down1 = setup("/res/Monster/PotatoDown1", s, s);
        down2 = setup("/res/Monster/PotatoDown2", s, s);
        left1 = setup("/res/Monster/PotatoLeft1", s, s);
        left2 = setup("/res/Monster/PotatoLeft2", s, s);
        right1 = setup("/res/Monster/PotatoRight1", s, s);
        right2 = setup("/res/Monster/PotatoRight2", s, s);
        AttackUp1 = setup("/res/Monster/Potato_AttackUp1", s, s);
        AttackUp2 = setup("/res/Monster/Potato_AttackUp2", s, s);
        AttackDown1 = setup("/res/Monster/Potato_AttackDown1", s, s);
        AttackDown2 = setup("/res/Monster/Potato_AttackDown2", s, s);
        AttackLeft1 = setup("/res/Monster/Potato_AttackLeft1", s, s);
        AttackLeft2 = setup("/res/Monster/Potato_AttackLeft2", s, s);
        AttackRight1 = setup("/res/Monster/Potato_AttackRight1", s, s);
        AttackRight2 = setup("/res/Monster/Potato_AttackRight2", s, s);
    }

    @Override
    public void setAction() {
        if (cooldown > 0) cooldown--;

        switch (state) {
            case WINDUP:
                speed = 0;
                stateCounter++;
                if (stateCounter >= WINDUP_LENGTH) {
                    state = CHARGE;
                    stateCounter = 0;
                    speed = CHARGE_SPEED;
                    gp.playSE(12);
                }
                return;

            case CHARGE:
                speed = CHARGE_SPEED;
                stateCounter++;
                // collisionOn still holds last frame's result here
                if (stateCounter > 1 && collisionOn) {
                    if (touchingPlayer) {
                        endCharge();
                    } else {
                        // Missed and slammed into something: dazed, takes double damage
                        state = DAZED;
                        stateCounter = 0;
                        speed = 0;
                        offBalance = true;
                        offBalanceCounter = 60 - DAZE_LENGTH; // base class clears it after 60 counts
                        gp.playSE(13);
                    }
                } else if (stateCounter * CHARGE_SPEED > CHARGE_MAX_TILES * gp.tileSize) {
                    endCharge();
                }
                return;

            case DAZED:
                speed = 0;
                stateCounter++;
                if (stateCounter >= DAZE_LENGTH) {
                    state = WALK;
                    speed = defaultSpeed;
                    cooldown = CHARGE_COOLDOWN;
                }
                return;

            default:
                break;
        }

        if (gp.playerStatus.isPlayerInvisible()) {
            getRandomDirection();
            return;
        }

        int distance = getTileDistance(gp.player);
        if (distance < 10) {
            moveTowardPlayer(60);
        } else {
            getRandomDirection();
        }

        if (cooldown == 0 && lineUpWithPlayer()) {
            state = WINDUP;
            stateCounter = 0;
            speed = 0;
        }
    }

    /** True if the player is roughly in the same row or column and in range; also turns to face them. */
    boolean lineUpWithPlayer() {
        int xDist = getXdistance(gp.player);
        int yDist = getYdistance(gp.player);
        int lane = gp.tileSize * 3 / 4;
        int range = gp.tileSize * 7;
        if (xDist < lane && yDist < range) {
            direction = gp.player.getCenterY() < getCenterY() ? "up" : "down";
            return true;
        }
        if (yDist < lane && xDist < range) {
            direction = gp.player.getCenterX() < getCenterX() ? "left" : "right";
            return true;
        }
        return false;
    }

    @Override
    public void checkCollision() {
        super.checkCollision();
        // Remember whether the thing we bumped into was the player (a hit) or a wall (a miss)
        touchingPlayer = gp.cChecker.checkPlayer(this);
    }

    void endCharge() {
        state = WALK;
        stateCounter = 0;
        speed = defaultSpeed;
        cooldown = CHARGE_COOLDOWN;
    }

    @Override
    protected BufferedImage overrideImage(BufferedImage image) {
        if (dying) return image;
        if (state == WINDUP) return attackFrame(1);
        if (state == CHARGE) return attackFrame(2);
        return image;
    }

    @Override
    public void draw(Graphics2D g2) {
        // Shake during the wind-up so the player can see the charge coming.
        // Drawn here with an offset rather than by moving worldX, which the game thread owns.
        if (state == WINDUP && !dying && !invincible && inCamera()) {
            int shake = (stateCounter / 3) % 2 == 0 ? -3 : 3;
            g2.drawImage(attackFrame(1), getScreenX() + shake, getScreenY(), null);
        } else {
            super.draw(g2);
        }

        // Spinning stars while dazed
        if (state == DAZED && !dying && inCamera()) {
            int cx = getScreenX() + gp.tileSize;
            int cy = getScreenY() + 28;
            g2.setColor(new Color(255, 225, 80));
            for (int i = 0; i < 3; i++) {
                double a = stateCounter / 8.0 + i * (Math.PI * 2 / 3);
                int sx = cx + (int) (Math.cos(a) * 26);
                int sy = cy + (int) (Math.sin(a) * 8);
                g2.fillRect(sx - 4, sy - 1, 9, 3);
                g2.fillRect(sx - 1, sy - 4, 3, 9);
            }
        }
    }

    @Override
    public void damageReaction() {
        // Too heavy to be interrupted mid-charge
        if (state == WALK) {
            actionLockCounter = 0;
        }
    }

    @Override
    public void checkDrop() {
        int roll = new Random().nextInt(100) + 1;
        if (roll <= 60) {
            dropItem(new OBJ_coin(gp));
        } else if (roll <= 85) {
            dropItem(new OBJ_heart(gp));
        }
    }
}
