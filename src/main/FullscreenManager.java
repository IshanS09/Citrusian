package main;

import java.awt.*;
import javax.swing.*;

public class FullscreenManager {
    
    private JFrame window;
    private GraphicsDevice device;
    private boolean isFullScreen = false;
    private Dimension windowedSize;
    private Point windowedLocation;
    
    public FullscreenManager(JFrame window) {
        this.window = window;
        this.device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // Store original windowed size
        this.windowedSize = window.getSize();
        this.windowedLocation = window.getLocation();
    }
    
    public void toggleFullScreen() {
        if (isFullScreen) {
            exitFullScreen();
        } else {
            enterFullScreen();
        }
    }
    
    public void enterFullScreen() {
        if (!isFullScreen && device.isFullScreenSupported()) {
            // Save current window state
            windowedSize = window.getSize();
            windowedLocation = window.getLocation();
            
            // Remove window decorations
            window.dispose();
            window.setUndecorated(true);
            window.setVisible(true);
            
            // Enter full screen
            device.setFullScreenWindow(window);
            isFullScreen = true;
            
            System.out.println("Entered full screen mode");
        }
    }
    
    public void exitFullScreen() {
        if (isFullScreen) {
            // Exit full screen
            device.setFullScreenWindow(null);
            
            // Restore window decorations
            window.dispose();
            window.setUndecorated(false);
            window.setSize(windowedSize);
            window.setLocation(windowedLocation);
            window.setVisible(true);
            
            isFullScreen = false;
            
            System.out.println("Exited full screen mode");
        }
    }
    
    public boolean isFullScreen() {
        return isFullScreen;
    }
}