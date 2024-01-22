import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private final int GAME_WIDTH = 700;
    private final int GAME_HEIGHT = 700;
    private final int SPACE_SIZE = 50;
    private final int BODY_PARTS = 3;
    private final Color SNAKE_COLOR = Color.GREEN;
    private final Color FOOD_COLOR = Color.RED;
    private final Color BACKGROUND_COLOR = Color.BLACK;

    private int score = 0;
    private String direction = "down";
    private int delay = 0;  // Default delay

    private ArrayList<Point> snake;
    private Point food;

    private Timer timer;

    public SnakeGame(int delay) {
        setTitle("Snake Game");
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(this);

        this.delay = delay;  // Set delay based on difficulty

        snake = new ArrayList<>();
        for (int i = 0; i < BODY_PARTS; i++) {
            snake.add(new Point(0, i * SPACE_SIZE));
        }

        spawnFood();

        timer = new Timer(delay, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        move();
        checkCollisions();
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (!direction.equals("right")) {
                    direction = "left";
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!direction.equals("left")) {
                    direction = "right";
                }
                break;
            case KeyEvent.VK_UP:
                if (!direction.equals("down")) {
                    direction = "up";
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!direction.equals("up")) {
                    direction = "down";
                }
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    private void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case "left":
                head.x -= SPACE_SIZE;
                break;
            case "right":
                head.x += SPACE_SIZE;
                break;
            case "up":
                head.y -= SPACE_SIZE;
                break;
            case "down":
                head.y += SPACE_SIZE;
                break;
        }

        snake.add(0, head);

        if (head.equals(food)) {
            score++;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(GAME_WIDTH / SPACE_SIZE) * SPACE_SIZE;
        int y = random.nextInt(GAME_HEIGHT / SPACE_SIZE) * SPACE_SIZE;
        food = new Point(x, y);
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= GAME_WIDTH || head.y < 0 || head.y >= GAME_HEIGHT) {
            gameover();
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameover();
            }
        }
    }

    private void gameover() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score is " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public void paint(Graphics g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(FOOD_COLOR);
        g.fillRect(food.x, food.y, SPACE_SIZE, SPACE_SIZE);

        g.setColor(SNAKE_COLOR);
        for (Point point : snake) {
            g.fillRect(point.x, point.y, SPACE_SIZE, SPACE_SIZE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int delay = 1000;  // Default delay (Medium)
            
            // Ask user to choose difficulty mode
            String[] options = {"Easy", "Medium", "Hard"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose Difficulty",
                    "Snake Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            // Set delay based on the user's choice
            switch (choice) {
                case 0:
                    delay = 2000;  // Easy
                    break;
                case 2:
                    delay = 500;  // Hard
                    break;
            }

            new SnakeGame(delay).setVisible(true);
        });
    }
}
