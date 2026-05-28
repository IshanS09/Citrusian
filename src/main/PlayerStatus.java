package main;

public class PlayerStatus {
    gamePanel gp;
    
    // Status effect flags
    public boolean speedBoostActive = false;
    public boolean invisibilityActive = false;
    public boolean isDashing = false;
    public boolean regenerationActive = false;
    
    // Status effect timers (in frames, 60 fps)
    private int speedBoostTimer = 0;
    private int invisibilityTimer = 0;
    private int dashTimer = 0;
    private int regenerationTimer = 0;
    private int regenTickTimer = 0;
    
    // Status effect durations
    private static final int SPEED_BOOST_DURATION = 300; // 5 seconds
    private static final int INVISIBILITY_DURATION = 180; // 3 seconds
    private static final int DASH_DURATION = 15; // ~0.25 seconds
    private static final int REGENERATION_DURATION = 300; // 10 seconds
    private static final int REGEN_TICK_RATE = 60; // Heal every 1 second
    
    // Original player speed (to restore after buff)
    private int originalSpeed;
    
    public PlayerStatus(gamePanel gp) {
        this.gp = gp;
    }
    
    public void update() {
        // Update speed boost
        if(speedBoostActive) {
            speedBoostTimer++;
            if(speedBoostTimer >= SPEED_BOOST_DURATION) {
                deactivateSpeedBoost();
            }
        }
        
        // Update invisibility
        if(invisibilityActive) {
            invisibilityTimer++;
            if(invisibilityTimer >= INVISIBILITY_DURATION) {
                deactivateInvisibility();
            }
        }
        
        // Update dash
        if(isDashing) {
            dashTimer++;
            performDash();
            if(dashTimer >= DASH_DURATION) {
                isDashing = false;
                dashTimer = 0;
            }
        }
        
        // Update regeneration
        if(regenerationActive) {
            regenerationTimer++;
            regenTickTimer++;
            
            // Heal every second
            if(regenTickTimer >= REGEN_TICK_RATE) {
                if(gp.player.life < gp.player.maxLife) {
                    gp.player.life++;
                }
                regenTickTimer = 0;
            }
            
            if(regenerationTimer >= REGENERATION_DURATION) {
                deactivateRegeneration();
            }
        }
    }
    
    // ===== SPEED BOOST =====
    public void activateSpeedBoost() {
        if(!speedBoostActive) {
            originalSpeed = gp.player.speed;
            gp.player.speed = originalSpeed * 2;
            speedBoostActive = true;
            speedBoostTimer = 0;
            gp.playSE(9); // Play activation sound
            gp.ui.addMessage("Speed Boost Activated!");
        }
    }
    
    private void deactivateSpeedBoost() {
        gp.player.speed = originalSpeed;
        speedBoostActive = false;
        speedBoostTimer = 0;
        gp.ui.addMessage("Speed Boost Ended");
    }
    
    // ===== INVISIBILITY =====
    public void activateInvisibility() {
        if(!invisibilityActive) {
            invisibilityActive = true;
            invisibilityTimer = 0;
            gp.playSE(9); // Play activation sound
            gp.ui.addMessage("Invisibility Activated!");
        }
    }
    
    private void deactivateInvisibility() {
        invisibilityActive = false;
        invisibilityTimer = 0;
        gp.ui.addMessage("Invisibility Ended");
    }
    
    // ===== DASH =====
    public void activateDash() {
        if(!isDashing) {
            isDashing = true;
            dashTimer = 0;
            gp.player.invincible = true; // Invincible while dashing
            gp.playSE(9); // Play dash sound
        }
    }
    
    private void performDash() {
        int dashSpeed = 12; // Fast movement speed
        
        // Move in the direction player is facing
        switch(gp.player.direction) {
            case "up":
                gp.player.worldY -= dashSpeed;
                break;
            case "down":
                gp.player.worldY += dashSpeed;
                break;
            case "left":
                gp.player.worldX -= dashSpeed;
                break;
            case "right":
                gp.player.worldX += dashSpeed;
                break;
        }
        
        // End dash and remove invincibility
        if(dashTimer >= DASH_DURATION - 1) {
            gp.player.invincible = false;
        }
    }
    
    // ===== FREEZE =====
    public void activateFreeze() {
        int freezeRadius = 10; // tiles
        int freezeDuration = 180; // 3 seconds
        
        // Freeze all monsters within radius
        for(int i = 0; i < gp.monster[1].length; i++) {
            if(gp.monster[gp.currentMap][i] != null) {
                int distance = gp.player.getTileDistance(gp.monster[gp.currentMap][i]);
                if(distance <= freezeRadius) {
                    gp.monster[gp.currentMap][i].stunned = true;
                    gp.monster[gp.currentMap][i].stunnedCounter = freezeDuration;
                }
            }
        }
        
        gp.playSE(9); // Play freeze sound
        gp.ui.addMessage("Enemies Frozen!");
    }
    
    // Helper method to check if player is invisible (for monster AI)
    public boolean isPlayerInvisible() {
        return invisibilityActive;
    }
    
    // ===== REGENERATION =====
    public void activateRegeneration() {
        if(!regenerationActive) {
            regenerationActive = true;
            regenerationTimer = 0;
            regenTickTimer = 0;
            gp.playSE(2); // Play activation sound
            gp.ui.addMessage("Regeneration Activated!");
        }
    }
    
    private void deactivateRegeneration() {
        regenerationActive = false;
        regenerationTimer = 0;
        regenTickTimer = 0;
        gp.ui.addMessage("Regeneration Ended");
    }
}