import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SimpleGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 50;
    private static final int ENEMY_SIZE = 30;

    private int playerX = WIDTH / 2;
    private int playerY = HEIGHT / 2;
    private ArrayList<Point> enemies;
    private Timer timer;

    public SimpleGame() {
        enemies = new ArrayList<>();
        spawnEnemies(5); // Spawn 5 enemies
        timer = new Timer(30, this); // Update every 30ms
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    private void spawnEnemies(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(WIDTH - ENEMY_SIZE);
            int y = rand.nextInt(HEIGHT - ENEMY_SIZE);
            enemies.add(new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        // Draw player
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        // Draw enemies
        g.setColor(Color.RED);
        for (Point enemy : enemies) {
            g.fillOval(enemy.x, enemy.y, ENEMY_SIZE, ENEMY_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update enemy positions randomly
        for (Point enemy : enemies) {
            Random rand = new Random();
            enemy.x += rand.nextInt(5) - 2; // Move left or right
            enemy.y += rand.nextInt(5) - 2; // Move up or down

            // Keep enemies within bounds
            enemy.x = Math.max(0, Math.min(WIDTH - ENEMY_SIZE, enemy.x));
            enemy.y = Math.max(0, Math.min(HEIGHT - ENEMY_SIZE, enemy.y));
        }
        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);
        for (Point enemy : enemies) {
            Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, ENEMY_SIZE, ENEMY_SIZE);
            if (playerRect.intersects(enemyRect)) {
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0); // End game on collision
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                playerX -= 10; // Move left
                break;
            case KeyEvent.VK_RIGHT:
                playerX += 10; // Move right
                break;
            case KeyEvent.VK_UP:
                playerY -= 10; // Move up
                break;
            case KeyEvent.VK_DOWN:
                playerY += 10; // Move down
                break;
        }
        // Keep player within bounds
        playerX = Math.max(0, Math.min(WIDTH - PLAYER_SIZE, playerX));
        playerY = Math.max(0, Math.min(HEIGHT - PLAYER_SIZE, playerY));
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Game");
        SimpleGame gamePanel = new SimpleGame();
        frame.add(gamePanel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
/**Overview of Game Mechanics
Game Window: The game is displayed in a window created by the JFrame class. 
The game panel where all graphics are drawn is a subclass of JPanel.

Player and Enemies:

The player is represented as a blue square that the user can control with the arrow keys.
Enemies are represented as red circles that spawn randomly on the screen and move in a random manner.
Game Loop:

The game uses a Timer to create a game loop that updates the game state and repaints the screen at regular intervals (every 30 milliseconds).
During each update, the positions of the enemies are altered randomly, and collisions are checked.
Collision Detection: If the player collides with any enemy, a "Game Over!" message is displayed, and the game exits.
*/
