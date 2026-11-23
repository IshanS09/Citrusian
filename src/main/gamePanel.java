package main;
import ai.PathFinder;
import data.SaveLoad;
import entity.Player;
import entity.entity;
import environment.EnvironmentManager;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import tile.tileManager;
import tile_interactive.interactiveTile;

public class gamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 32; 
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 64x64 tile
    public final int maxScreenCol = 22; 
    public final int maxScreenRow = 14;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels wide
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels tall
    public final int maxWorldCol = 152; // grown east for Zest Harbor and Citrus Grove, with open sea past the shore
    public final int maxWorldRow = 152; // grown south for the Rotting Marsh, with open sea past the shore
    public final int maxMap = 10;
    public int currentMap = 0;  

    // Full screen management
    private JFrame window;
    private GraphicsDevice device;
    private boolean isFullScreen = false;
    private java.awt.Dimension windowedSize;
    private java.awt.Point windowedLocation;

    
    public tileManager tileM = new tileManager(this); // Initialize tileManager with gamePanel instance
    public keyHandler keyH = new keyHandler(this); 
    Thread gameThread;
    public PathFinder pFinder = new PathFinder(this); // Initialize PathFinder with gamePanel instance
    public EnvironmentManager eManager = new EnvironmentManager(this); // Initialize EnvironmentManager with gamePanel instance
    SaveLoad saveLoad = new SaveLoad(this); // Initialize SaveLoad with gamePanel instance
    public EntityGenerator eGenerator = new EntityGenerator(this); // Initialize EntityGenerator with gamePanel instance
    final int FPS = 60; // Frames per second
    public CollisionChecker cChecker = new CollisionChecker(this); // Initialize CollisionChecker with gamePanel instance
    public AssetSetter aSetter = new AssetSetter(this); // Initialize AssetSetter with gamePanel instance
    public UI ui = new UI(this); // Initialize UI with gamePanel instance
    public EventHandler eHandler = new EventHandler(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    public PlayerStatus playerStatus;
   
    private boolean waitingToRespawnCarrots = false;
    private int carrotRespawnCounter = 0;
    private boolean carrotsJustRespawned = false;

    private boolean waitingToRespawnPeas = false;
    private int peasRespawnCounter = 0;
    private boolean peasJustRespawned = false;

    boolean waitingToRespawnBroccoli = false;
    int broccoliRespawnCounter = 0;

    // Onions and potatoes come back 10 seconds after the whole group is beaten
    int onionRespawnCounter = 0;
    int potatoRespawnCounter = 0;
    int marshRespawnCounter = 0;
    static final int NEW_ENEMY_RESPAWN_FRAMES = 600;

    public int frameCount = 0;

    // Player instance
    Sound music = new Sound(); // Initialize Sound instance
    Sound se = new Sound(); // Initialize Sound instance
    Sound weatherSound = new Sound(); // Rain loop, separate so it can play under the music
    boolean rainPlaying = false;
    int rainVolume = -1;
    public Player player = new Player(this, keyH);
    public entity obj[][] = new entity[maxMap][20]; // Array to hold game objects
    public entity npc[][] = new entity[maxMap][10]; // Array to hold entities
    public interactiveTile iTile[][] = new interactiveTile[maxMap][50];
    public entity monster[][] = new entity[maxMap][40];
    public entity projectile[][] = new entity[maxMap][20]; 
    // public ArrayList<entity> projectileList = new ArrayList<>(); 
    public ArrayList<entity> particleList = new ArrayList<>();
    ArrayList<entity> entityList = new ArrayList<>(); 
    
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int sleepState = 9;
    public final int cutsceneState = 10;
    public final int fishingState = 11;

    public boolean bossBattleOn = false;

    // New Game+ cycle: 0 on a first playthrough, 1 after beating it once, and so on
    public int ngPlus = 0;
    boolean wasNight = false;
    public QuestManager questManager = new QuestManager(this);
    public FishingManager fishing = new FishingManager(this);

    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int inside = 51;
    public final int dungeon = 52;

    public gamePanel() {
        this.setPreferredSize(new java.awt.Dimension(screenWidth, screenHeight));
        this.setBackground(java.awt.Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public void setupFullScreen(JFrame window) {
        this.window = window;
        this.windowedSize = window.getSize();
        this.windowedLocation = window.getLocation();
    }

    public void toggleFullScreen() {
        if (window == null) return;
        
        if (isFullScreen) {
            exitFullScreen();
        } else {
            enterFullScreen();
        }
    }

    private void enterFullScreen() {
        if (!isFullScreen && device.isFullScreenSupported()) {
            try {
                // Save current window state
                windowedSize = window.getSize();
                windowedLocation = window.getLocation();
                
                // Remove window decorations and enter fullscreen
                window.setVisible(false);
                window.dispose();
                window.setUndecorated(true);
                window.setResizable(false);
                
                // Enter full screen exclusive mode
                device.setFullScreenWindow(window);
                window.setVisible(true);
                
                // Force repaint and focus
                window.validate();
                this.requestFocusInWindow();
                
                isFullScreen = true;
                
                // Recreate lighting filter for new screen size
                eManager.lighting.setLightSource();
                
                this.repaint();
                
                System.out.println("Entered full screen mode");
            } catch (Exception e) {
                System.err.println("Failed to enter fullscreen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void exitFullScreen() {
        if (isFullScreen) {
            try {
                // Exit full screen exclusive mode
                device.setFullScreenWindow(null);
                
                // Restore window
                window.setVisible(false);
                window.dispose();
                window.setUndecorated(false);
                window.setResizable(false);
                window.setSize(windowedSize);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                
                // Force repaint and focus
                window.validate();
                this.requestFocusInWindow();
                
                isFullScreen = false;
                
                // Recreate lighting filter for original screen size
                eManager.lighting.setLightSource();
                
                this.repaint();
                
                System.out.println("Exited full screen mode");
            } catch (Exception e) {
                System.err.println("Failed to exit fullscreen: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setupGame() {
        // Fresh run: no bosses beaten yet (a loaded save sets these again)
        data.Progress.maliBrocDefeated = false;
        data.Progress.baronRotjawDefeated = false;
        data.Progress.peaKingDefeated = false;
        ngPlus = 0;
        questManager.reset();
        aSetter.setObject();     
        aSetter.setNPC();   
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        eManager.setup(); 
        playerStatus = new PlayerStatus(this);
        gameState = titleState; 
        currentArea = outside;
    }

    public void resetGame(boolean restart) {
        currentArea = outside;
        removeTempEntity();
        bossBattleOn = false;
        player.setDefaultPositions();
        player.restoreLifeandSeed();
        player.resetCounters();
        aSetter.setNPC();
        aSetter.setMonster();

        // ✅ Refresh attack/defense based on current stats + equipment
        player.attack = player.getAttack();
        player.defense = player.getDefense();

        if (restart) {
            player.setDefaultValues();
            aSetter.setObject();
            aSetter.setInteractiveTile();
            eManager.lighting.resetDay();
            eManager.weather.reset();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }

            // Sleep until the next frame is due so the loop doesn't pin a CPU core at 100%
            long sleepNanos = (long) ((1 - delta) * drawInterval);
            if (sleepNanos > 0) {
                try {
                    Thread.sleep(sleepNanos / 1000000, (int) (sleepNanos % 1000000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            frameCount++;
            
            player.update();
            playerStatus.update();

            // Update NPCs
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }

            // Night falls / day breaks messages (outside only)
            boolean night = isNight();
            if (night != wasNight) {
                ui.addMessage(night ? "Night falls... the veggies grow stronger. (Double coins!)" : "The sun rises. The veggies calm down.");
                wasNight = night;
            }

            // Update monsters
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    applyScaling(monster[currentMap][i], night);
                    if (monster[currentMap][i].alive && !monster[currentMap][i].dying) {
                        monster[currentMap][i].update();
                    }
                    if (!monster[currentMap][i].alive) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            // === Carrot respawn logic ===
            if (aSetter.allCarrotsDead(currentMap)) {
                if (!waitingToRespawnCarrots) {
                    waitingToRespawnCarrots = true;
                    carrotRespawnCounter = 0;
                } else {
                    carrotRespawnCounter++;
                    if (carrotRespawnCounter >= 300) { // 5 seconds at 60 FPS
                        aSetter.respawnCarrots(currentMap);
                        waitingToRespawnCarrots = false;
                        carrotRespawnCounter = 0;
                    }
                }
            } else {
                waitingToRespawnCarrots = false;
                carrotRespawnCounter = 0;
            }

             // === Peas respawn logic ===
             if (aSetter.allPeasDead(currentMap)) {
                if (!waitingToRespawnPeas) {
                    waitingToRespawnPeas = true;
                    peasRespawnCounter = 0;
                } else {
                    peasRespawnCounter++;
                    if (peasRespawnCounter >= 300) { // 5 seconds at 60 FPS
                        aSetter.respawnPeas(currentMap);
                        waitingToRespawnPeas = false;
                        peasRespawnCounter = 0;
                    }
                }
            } else {
                waitingToRespawnPeas = false;
                peasRespawnCounter = 0;
            }

            // === Broccoli respawn logic ===
            if (aSetter.allBroccoliDead(currentMap)) {
                if (!waitingToRespawnBroccoli) {
                    waitingToRespawnBroccoli = true;
                    broccoliRespawnCounter = 0;
                } else {
                    broccoliRespawnCounter++;
                    if (broccoliRespawnCounter >= 300) {
                    aSetter.respawnBroccoli(currentMap);
                    waitingToRespawnBroccoli = false;
                    broccoliRespawnCounter = 0;
                    }
                }
            } else {
                   waitingToRespawnBroccoli = false;
                   broccoliRespawnCounter = 0;
            }

            // === Onion and potato respawn ===
            if (aSetter.allOnionsDead(currentMap)) {
                if (++onionRespawnCounter >= NEW_ENEMY_RESPAWN_FRAMES) {
                    aSetter.spawnOnions(currentMap);
                    onionRespawnCounter = 0;
                }
            } else {
                onionRespawnCounter = 0;
            }
            if (aSetter.allMarshDead(currentMap)) {
                if (++marshRespawnCounter >= NEW_ENEMY_RESPAWN_FRAMES) {
                    aSetter.spawnMarsh(currentMap);
                    marshRespawnCounter = 0;
                }
            } else {
                marshRespawnCounter = 0;
            }
            if (aSetter.allPotatoesDead(currentMap)) {
                if (++potatoRespawnCounter >= NEW_ENEMY_RESPAWN_FRAMES) {
                    aSetter.spawnPotatoes(currentMap);
                    potatoRespawnCounter = 0;
                }
            } else {
                potatoRespawnCounter = 0;
            }
            
        }

        if (gameState == fishingState) {
            fishing.update();
        }

        // Update projectiles
        for (int i = 0; i < projectile[currentMap].length; i++) {
            if (projectile[currentMap][i] != null) {
                if (projectile[currentMap][i].alive) {
                    projectile[currentMap][i].update();
                }
                if (!projectile[currentMap][i].alive) {
                    projectile[currentMap][i] = null; 
                }
            }
        }

        // Update particles
        for (int i = 0; i < particleList.size(); i++) {
            if (particleList.get(i) != null) {
                if (particleList.get(i).alive) {
                    particleList.get(i).update();
                }
                if (!particleList.get(i).alive) {
                    particleList.remove(i);
                    i--; // stay on this index so the next particle isn't skipped
                }
            }
        }

        // Update interactive tiles
        for (int i = 0; i < iTile[1].length; i++) {
            if (iTile[currentMap][i] != null) {
                iTile[currentMap][i].update();
            }
        }
        eManager.update();

        if (gameState == pauseState) {
            // Handle pause state updates if needed
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        // Store the original transform and calculate scale info
        java.awt.geom.AffineTransform originalTransform = g2.getTransform();
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        double scaleX = (double) panelWidth / screenWidth;
        double scaleY = (double) panelHeight / screenHeight;
        double scale = Math.min(scaleX, scaleY);
        int offsetX = (int) ((panelWidth - (screenWidth * scale)) / 2);
        int offsetY = (int) ((panelHeight - (screenHeight * scale)) / 2);
        
        // If in fullscreen, fill entire screen with black first (for letterboxing)
        if (isFullScreen) {
            g2.setColor(java.awt.Color.BLACK);
            g2.fillRect(0, 0, panelWidth, panelHeight);
            
            // Apply scaling transformation
            g2.translate(offsetX, offsetY);
            g2.scale(scale, scale);
        }
        
        if (gameState == titleState) {
            ui.draw(g2); 
        } 
        else {
            tileM.draw(g2);

            for(int i = 0; i < iTile[1].length; i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
                }
            }

            entityList.add(player);

            for(int i = 0; i< npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]); 
                }
            }
            
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]); 
                }
            }
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]); 
                }
            }
            for (int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    entityList.add(particleList.get(i)); 
                }
            }

            Collections.sort(entityList, new Comparator<entity>() {
                @Override
                public int compare(entity e1, entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }  
            entityList.clear();

            // Rain and fog sit over the world but under the night darkness
            eManager.drawWeather(g2);

            // Draw environment lighting in scaled context for windowed mode
            if (!isFullScreen) {
                eManager.draw(g2);
            }

            drawRageTint(g2);

            // Draw cutscenes in scaled context (they'll cover the full game area)
            csManager.draw(g2);

            // Draw UI in windowed mode or if not in transition/cutscene
            if (!isFullScreen) {
                ui.draw(g2);
            }
        }
        
        // Draw overlays in fullscreen (outside scaled context)
        if (isFullScreen && gameState != titleState) {
            // Reset transform
            g2.setTransform(originalTransform);
            
            // Draw lighting overlay over entire screen
            eManager.draw(g2);
            
            // Re-apply scaling for UI
            g2.translate(offsetX, offsetY);
            g2.scale(scale, scale);
            
            // Draw UI elements on top of lighting
            ui.draw(g2);
        }
        
        g2.dispose();
    }

    /** In phase 2, Malibroc's lair glows a pulsing red. */
    void drawRageTint(Graphics2D g2) {
        if (currentMap != 3 || !bossBattleOn) return;
        for (entity m : monster[3]) {
            if (m instanceof monster.MON_BossBroccoli && m.inRage && !m.dying) {
                float a = 0.10f + 0.06f * (float) Math.sin(frameCount / 10.0);
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, a));
                g2.setColor(new java.awt.Color(255, 40, 20));
                g2.fillRect(0, 0, screenWidth, screenHeight);
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));
                return;
            }
        }
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }    

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

    /** Starts or stops the rain loop. Storms play it a notch louder. */
    public void setRainSound(boolean on, boolean storm) {
        int volume = Math.max(0, Math.min(5, se.volumeScale - (storm ? 0 : 1)));
        if (on && (!rainPlaying || volume != rainVolume)) {
            if (rainPlaying) weatherSound.stop();
            weatherSound.volumeScale = volume;
            weatherSound.setFile(27);
            weatherSound.play();
            weatherSound.loop();
            rainPlaying = true;
            rainVolume = volume;
        } else if (!on && rainPlaying) {
            weatherSound.stop();
            rainPlaying = false;
        }
    }

    public void drawPlayerPositionOverlay(Graphics2D g2) {
        int radius = 5;

        // Player screen position is always fixed in the center
        int screenX = player.screenX;
        int screenY = player.screenY;

        // Draw a red dot at the player's screen center
        g2.setColor(java.awt.Color.RED);
        g2.fillOval(screenX - radius, screenY - radius, radius * 2, radius * 2);

        // Draw world coordinates
        g2.setColor(java.awt.Color.WHITE);
        g2.drawString("X: " + player.worldX/64 + "  Y: " + player.worldY/64, screenX + 10, screenY - 10);
    }

    /** Starts the music for whichever area the player is in. */
    public void playAreaMusic() {
        stopMusic();
        if (currentArea == inside) {
            playMusic(25);
        } else if (currentArea == dungeon) {
            playMusic(18);
        } else {
            playMusic(0);
        }
    }

    public void changeArea() {
        if(nextArea != currentArea) {
           
            stopMusic();
            if (nextArea == outside) {
                playMusic(0); 
            } else if (nextArea == inside) {
                playMusic(25);
            } else if (nextArea == dungeon) {
                playMusic(18);
            }
        }

        currentArea = nextArea;
        aSetter.setMonster();
    }

    /**
     * New Game+: start the story over but keep level, stats, coins, upgrades,
     * gear and quest progress. Keys and the Helm go back, and every enemy gets
     * tougher each cycle (see applyScaling).
     */
    public void startNewGamePlus() {
        ngPlus++;

        String[] storyItems = {"Gold Key", "Bronze Key", "Silver Key", "Citrusian Helm"};
        for (String item : storyItems) {
            int index;
            while ((index = player.searchItemInInventory(item)) != 999) {
                player.inventory.remove(index);
            }
        }

        data.Progress.maliBrocDefeated = false;
        data.Progress.baronRotjawDefeated = false;
        data.Progress.peaKingDefeated = false;

        for (int m = 0; m < maxMap; m++) {
            java.util.Arrays.fill(obj[m], null);
            java.util.Arrays.fill(npc[m], null);
            java.util.Arrays.fill(monster[m], null);
            java.util.Arrays.fill(iTile[m], null);
            java.util.Arrays.fill(projectile[m], null);
        }
        particleList.clear();
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        questManager.restoreQuestItems();

        player.setDefaultPositions();
        player.restoreLifeandSeed();
        player.resetCounters();
        playerStatus.clearEffects();
        currentArea = outside;
        nextArea = outside;
        bossBattleOn = false;
        eManager.lighting.resetDay();
        eManager.weather.reset();

        csManager.sceneNum = csManager.NA;
        csManager.scenePhase = 0;
        gameState = playState;
        playAreaMusic();
        ui.addMessage("New Game+ " + ngPlus + ": the veggies are tougher now!");
    }

    /** True when it's dark outside. Dungeons and houses don't count. */
    public boolean isNight() {
        if (currentArea != outside || eManager.lighting == null) return false;
        environment.Lighting l = eManager.lighting;
        return l.dayState == l.night || (l.dayState == l.dusk && l.filterAlpha > 0.6f);
    }

    /**
     * Makes monsters tougher for New Game+ (every cycle) and at night (regular
     * outdoor enemies only, not bosses). Called every frame; each buff is applied once.
     */
    void applyScaling(entity m, boolean night) {
        if (m.dying) return;

        // New Game+: +50% health, +2 attack, +1 defense per cycle, and more exp
        if (ngPlus > 0 && !m.ngScaled) {
            m.maxLife = m.maxLife * (2 + ngPlus) / 2;
            m.life = m.maxLife;
            m.attack += 2 * ngPlus;
            m.defense += ngPlus;
            m.exp = m.exp * (2 + ngPlus) / 2;
            m.ngScaled = true;
        }

        // Night: +50% health and +2 attack while it lasts
        boolean shouldBuff = night && !m.boss;
        if (shouldBuff && !m.nightBuffed) {
            m.nightBonusLife = m.maxLife / 2;
            m.maxLife += m.nightBonusLife;
            m.life += m.nightBonusLife;
            m.attack += 2;
            m.nightBuffed = true;
        } else if (!shouldBuff && m.nightBuffed) {
            m.maxLife -= m.nightBonusLife;
            m.life = Math.max(1, Math.min(m.life, m.maxLife));
            m.attack -= 2;
            m.nightBuffed = false;
        }
    }

    public void removeTempEntity() {
        for(int mapNum = 0; mapNum < maxMap; mapNum++) {
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
                    obj[mapNum][i] = null;
                }
            }
        }
    }
}