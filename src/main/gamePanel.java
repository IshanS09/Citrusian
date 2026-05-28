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
    public final int maxWorldCol = 100; // 100 tiles across in the world
    public final int maxWorldRow = 100; // 100 tiles down in the world
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
    EnvironmentManager eManager = new EnvironmentManager(this); // Initialize EnvironmentManager with gamePanel instance
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

    public int frameCount = 0;

    // Player instance
    Sound music = new Sound(); // Initialize Sound instance
    Sound se = new Sound(); // Initialize Sound instance
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

    public boolean bossBattleOn = false;

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
        long timer = 0;
        long drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
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

            // Update monsters
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
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

            // Draw environment lighting in scaled context for windowed mode
            if (!isFullScreen) {
                eManager.draw(g2);
            }

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