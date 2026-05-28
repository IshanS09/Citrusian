package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import object.OBJ_DashItem;


public class keyHandler implements KeyListener {

    gamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, spacePressed, shiftPressed;

    public keyHandler(gamePanel gp) {
        this.gp = gp;
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// Add your logic here
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Add your logic here
        int code = e.getKeyCode();

        // Toggle fullscreen with T key (works in any game state)
        if (code == KeyEvent.VK_T) {
            gp.toggleFullScreen();
        }

        if(gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                   gp.playSE(9);
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
            } 
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                   gp.playSE(9);
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            } 
            if(code == KeyEvent.VK_ENTER) {
                enterPressed = true;
                if(gp.ui.commandNum == 0) {
                    // NEW GAME - Play intro cutscene
                    gp.setupGame();
                    gp.gameState = gp.cutsceneState;
                    gp.csManager.sceneNum = gp.csManager.intro;
                    gp.csManager.scenePhase = 0;
                    // Music will start after cutscene in scene_intro()
                } else if(gp.ui.commandNum == 1) {
                    // LOAD GAME - No cutscene, straight to gameplay
                    gp.saveLoad.load();
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                } else if(gp.ui.commandNum == 2) {
                    System.exit(0); // Exit the game
                }
            }
            
        }

    else if(gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
            upPressed = true;
            }  
            if (code == KeyEvent.VK_A) {
            leftPressed = true;
            }  
            if (code == KeyEvent.VK_S) {
            downPressed = true;
            }  
            if (code == KeyEvent.VK_D) {
            rightPressed = true;
            }
            if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
            }
            if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            }
            if(code == KeyEvent.VK_F) {
                shotKeyPressed = true;
                gp.playSE(11);
            }
            if(code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.optionsState;
            }
            if(code == KeyEvent.VK_SPACE) {
                spacePressed = true;
            }
            if(code == KeyEvent.VK_O) {
                gp.saveLoad.save();
                gp.ui.addMessage("Game Saved!");
                gp.playSE(9);
            }
            if(code == KeyEvent.VK_SHIFT) {
                shiftPressed = true;
                // Check if player has dash items
                boolean hasDash = false;
                int dashIndex = -1;
                
                for(int i = 0; i < gp.player.inventory.size(); i++) {
                    if(gp.player.inventory.get(i).name.equals(OBJ_DashItem.objName)) {
                        hasDash = true;
                        dashIndex = i;
                        break;
                    }
                }
                
                if(hasDash && !gp.playerStatus.isDashing) {
                    gp.playerStatus.activateDash();
                    // Consume one dash item
                    gp.player.inventory.get(dashIndex).amount--;
                    if(gp.player.inventory.get(dashIndex).amount == 0) {
                        gp.player.inventory.remove(dashIndex);
                    }
                }
            }

    }        
    else if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutsceneState) {
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    else if(gp.gameState == gp.characterState) {
        if (code == KeyEvent.VK_C) {
            gp.gameState = gp.playState; 
        }
        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
        playerInventory(code);
    }
    else if(gp.gameState == gp.optionsState) {
        
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 0: maxCommandNum = 4; break;
            case 2: maxCommandNum = 1; break;

        }
        
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 0 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                
                if(gp.ui.commandNum == 1 && gp.se.volumeScale > 0) {
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 0 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 1 && gp.se.volumeScale < 5) {
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }
    else if(gp.gameState == gp.gameOverState) {
        if(code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.resetGame(false);
                gp.playMusic(0);
            }
            else if(gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.resetGame(true);
            }
        }
    }
    else if(gp.gameState == gp.tradeState) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        
        if(gp.ui.subState == 0) {
            if(code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2;
                }
                gp.playSE(9);
            }
            if(code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
                gp.playSE(9);
            }
        } 
    }
    if(gp.ui.subState == 1) {
        npcInventory(code);
        if(code == KeyEvent.VK_ESCAPE) {
            gp.ui.subState = 0; // Exit the inventory
        }
    }
    if(gp.ui.subState == 2) {
        playerInventory(code);
        if(code == KeyEvent.VK_ESCAPE) {
            gp.ui.subState = 0; // Exit the inventory
        }
    }
}
public void playerInventory(int code) {
    if(code == KeyEvent.VK_W) {
            if(gp.ui.playerSlotRow != 0) {
                gp.ui.playerSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.playerSlotCol != 0) {
                gp.ui.playerSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gp.ui.playerSlotRow != 3) {
                gp.ui.playerSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.playerSlotCol != 4) {
                gp.ui.playerSlotCol++;
                gp.playSE(9);
            }
        }
}
public void npcInventory(int code) {
    if(code == KeyEvent.VK_W) {
            if(gp.ui.npcSlotRow != 0) {
                gp.ui.npcSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_A) {
            if(gp.ui.npcSlotCol != 0) {
                gp.ui.npcSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S) {
            if(gp.ui.npcSlotRow != 3) {
                gp.ui.npcSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D) {
            if(gp.ui.npcSlotCol != 4) {
                gp.ui.npcSlotCol++;
                gp.playSE(9);
            }
        }
}

@Override
public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_W) {
        upPressed = false;
    } if (code == KeyEvent.VK_A) {
        leftPressed = false;
    } if (code == KeyEvent.VK_S) {
        downPressed = false;
    } if (code == KeyEvent.VK_D) {
        rightPressed = false;
    } if(code == KeyEvent.VK_F) {
        shotKeyPressed = false;
    } if(code == KeyEvent.VK_ENTER) {
        enterPressed = false;
    } if(code == KeyEvent.VK_SPACE) {
        spacePressed = false;
    } if(code == KeyEvent.VK_SHIFT) { 
        shiftPressed = false;
    }

}

}