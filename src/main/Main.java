package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {

    public static JFrame window;
    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Citrusian");
        new Main().setIcon();
       
        gamePanel gamePanel = new gamePanel();
        window.add(gamePanel);
        
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupFullScreen(window);

        gamePanel.setupGame();

        gamePanel.startGameThread();
        gamePanel.requestFocusInWindow(); 
        
    }

    public void setIcon() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("res/Objects/CitrusianHelmet.png"));
        window.setIconImage(icon.getImage());

    }
}
