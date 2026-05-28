package main;

import data.Progress;

public class EventHandler {
    gamePanel gp;
    EventRect eventRect[][][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(gamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2; 
            eventRect[map][col][row].height = 2; 
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;

                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
          
        }
        
    }

    public void checkEvent() {
        int xDistance = Math.abs(gp.player.worldX- previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);

        if(distance > gp.tileSize) {
            canTouchEvent = true;
        }

        if(canTouchEvent == true) {
        if(hit(0, 21, 45, "any") == true) {teleport(1, 14, 15, gp.inside);}
        else if(hit(1, 14, 16, "any") == true) {teleport(0, 22, 45, gp.outside);}
        else if(hit(0,58,85,"any") == true) {teleport(2, 30,61, gp.dungeon);}
        else if(hit(2,30,60,"any") == true) {teleport(0, 57,85, gp.outside);}
        else if(hit(2,28,28,"any") == true) {teleport(3, 56,61, gp.dungeon);}
        else if(hit(3,55,61,"any") == true) {teleport(2, 28,29, gp.dungeon);}
        else if(hit(0,63,33,"any") == true) {healingPool();}
        else if(hit(3,57,47,"up") == true) {maliBroc();}
        else if(hit(0,77,16,"any") == true) {teleport(4,50,63, gp.outside);}
        else if(hit(4,50,64,"any") == true) {teleport(0,77,17, gp.outside);}
        else if(hit(4,50,43, "any") == true) {baronRotjaw();}
        else if(hit(0,81,42,"any") == true) {teleport(5,51,52, gp.outside);}
        else if(hit(5,51,54,"any") == true) {teleport(0,81,43, gp.outside);}
        else if(hit(5,51,35, "any") == true) {peaKing();}
        }
    }
    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;
        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;
            
            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
                // More forgiving: allow "any" direction OR match the required direction
                if (reqDirection.contentEquals("any") || gp.player.direction.contentEquals(reqDirection)) {
                    hit = true;
                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }
            
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }
        return hit;
    }
    public void teleport(int map, int col, int row, int area) {
        
        gp.gameState = gp.transitionState;
        gp.nextArea = area;
        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = false;
        gp.playSE(5);
    }
    public void healingPool() {
        
        String text = "You feel refreshed!";
        gp.ui.addMessage(text);
        gp.playSE(2);
        gp.player.life = gp.player.maxLife;
        gp.player.seed = gp.player.maxSeed;
        canTouchEvent = false;
    }
    public void maliBroc() {
        if(gp.bossBattleOn == false && Progress.maliBrocDefeated == false) {
            gp.gameState = gp.cutsceneState;
            gp.csManager.sceneNum = gp.csManager.maliBroc;
        }
    }
    public void baronRotjaw() {
        if(gp.bossBattleOn == false && Progress.baronRotjawDefeated == false) {
            gp.gameState = gp.cutsceneState;
            gp.csManager.sceneNum = gp.csManager.BaronRotjaw;
        }
    }

    public void peaKing() {
        if(gp.bossBattleOn == false && Progress.peaKingDefeated == false) {
            gp.gameState = gp.cutsceneState;
            gp.csManager.sceneNum = gp.csManager.pea_King;
        }
    }
}
