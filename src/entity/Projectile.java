package entity;

import main.gamePanel;

public class Projectile extends entity{
    entity user;

    public Projectile(gamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }

    public void update() {

    if(user == gp.player) {
        int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
        if(monsterIndex != 999) {
            gp.player.damageMonster(monsterIndex, this, attack, knockBackPower);
            generateParticle(user.projectile, gp.monster[gp.currentMap][monsterIndex]);
            alive = false;
        }
    }
    if(user != gp.player) {
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
        if(gp.player.invincible == false && contactPlayer == true) {
            damagePlayer(attack);
            // FIX: Use 'this' instead of user.projectile since user.projectile might be null
            generateParticle(this, gp.player);
            alive = false;
        }
    }
    
    switch(direction) {
        case "up": worldY -= speed; break;
        case "down": worldY += speed; break;
        case "left": worldX -= speed; break;
        case "right": worldX += speed; break;
    }

    life--;
    if(life <= 0) {
        alive = false;
    }

    spriteCounter++;
    if(spriteCounter > 12) {
        if(spriteNum ==1) {
            spriteNum = 2;
        }
        else if(spriteNum == 2) {
            spriteNum = 1;
        }
        spriteCounter = 0;
    }
}
    public boolean haveResource(entity user) {
        boolean haveResource = false;
        return haveResource;
    }
    public void subtractResource(entity user) {}

}
