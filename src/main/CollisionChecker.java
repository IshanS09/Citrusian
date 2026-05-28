package main;
import java.awt.Rectangle;

import entity.entity;

public class CollisionChecker {
    gamePanel gp;
    
    public CollisionChecker(gamePanel gp ) {
        
        this.gp = gp;
    }

   public void checkTile(entity entity) {

    int entityLeftWorldX = entity.worldX + entity.solidArea.x;
    int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
    int entityTopWorldY = entity.worldY + entity.solidArea.y;
    int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

    int entityLeftCol = entityLeftWorldX / gp.tileSize;
    int entityRightCol = entityRightWorldX / gp.tileSize;
    int entityTopRow = entityTopWorldY / gp.tileSize;
    int entityBottomRow = entityBottomWorldY / gp.tileSize;

    int tileNum1, tileNum2;

    String direction = entity.direction;
    if(entity.knockBack == true) {
        direction = entity.knockBackDirection;
    }

    switch (direction) {
        case "up":
            entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            if (isInBounds(entityLeftCol, entityTopRow) && isInBounds(entityRightCol, entityTopRow)) {
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            break;

        case "down":
            entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            if (isInBounds(entityLeftCol, entityBottomRow) && isInBounds(entityRightCol, entityBottomRow)) {
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            break;

        case "left":
            entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            if (isInBounds(entityLeftCol, entityTopRow) && isInBounds(entityLeftCol, entityBottomRow)) {
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            break;

        case "right":
            entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            if (isInBounds(entityRightCol, entityTopRow) && isInBounds(entityRightCol, entityBottomRow)) {
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
            }
            break;
    }
}

// Add this helper method somewhere in your class:
private boolean isInBounds(int col, int row) {
    return col >= 0 && col < gp.maxWorldCol && row >= 0 && row < gp.maxWorldRow;
}


   public int checkObject(entity entity, boolean player) {
    int index = 999; // Default value if no collision occurs

    String direction = entity.direction;
    if(entity.knockBack == true) {
        direction = entity.knockBackDirection;
    }

    for (int i = 0; i < gp.obj[1].length; i++) {
        if (gp.obj[gp.currentMap][i] != null) {
            // Backup original positions
            int entityLeftWorldX = entity.worldX + entity.solidArea.x;
            int entityTopWorldY = entity.worldY + entity.solidArea.y;

            int objectLeftWorldX = gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x;
            int objectTopWorldY = gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y;

            // Set positions for collision check
            entity.solidArea.x = entityLeftWorldX;
            entity.solidArea.y = entityTopWorldY;
            gp.obj[gp.currentMap][i].solidArea.x = objectLeftWorldX;
            gp.obj[gp.currentMap][i].solidArea.y = objectTopWorldY;

            // Simulate movement
            switch (direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            // Check collision
            if (entity.solidArea.intersects(gp.obj[gp.currentMap][i].solidArea)) {
                if (gp.obj[gp.currentMap][i].collision) {
                    entity.collisionOn = true;
                }
                if (player) {
                    index = i;
                }
            }

            // Reset solid areas
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.obj[gp.currentMap][i].solidArea.x = gp.obj[gp.currentMap][i].solidAreaDefaultX;
            gp.obj[gp.currentMap][i].solidArea.y = gp.obj[gp.currentMap][i].solidAreaDefaultY;
        }
    }
    return index;
}
public int checkEntity(entity entity, entity[][] target) {

    int index = 999; // Default value if no collision occurs

    String direction = entity.direction;
    if(entity.knockBack == true) {
        direction = entity.knockBackDirection;
    }

    // Check collision with target entities
    for (int i = 0; i < target[1].length; i++) {
        if (target[gp.currentMap][i] != null) {
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;

            target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
            target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

            switch(direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }

            if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                if(target[gp.currentMap][i] != entity) { // Prevent self-collision
                    entity.collisionOn = true;
                    index = i; // Return the index of the colliding entity
                }
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
            target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
        }
    }
    return index;
}


public boolean checkPlayer(entity entity) {

        boolean contactPlayer = false;

          // Set positions for collision check
            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y =gp.player.worldY + gp.player.solidArea.y;

            // Simulate movement
        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }
        
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        
        return contactPlayer;

    }
}
