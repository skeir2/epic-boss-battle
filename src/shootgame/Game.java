package shootgame;

import basicgraphics.*;
import basicgraphics.examples.BasicGraphics;
import shootgame.engine.Engine;
import shootgame.engine.Range;
import shootgame.engine.Vector2;
import shootgame.engine.particles.ParticleSystem;

import javax.swing.*;
import java.awt.event.*;
import java.time.Clock;
import java.util.*;

public class Game {
    static Random random = new Random();
    static String[][] layout = {
            {"Top"},
            {"Middle"},
            {"Bottom"}
    };

    public static void main(String[] args) {
        BasicFrame mainFrame = new BasicFrame("Shooter Game");
        mainFrame.setStringLayout(layout);

        final JButton topButton = new JButton("Top button");
        topButton.setFocusable(false);
        topButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(topButton, "hi");
            }
        });
        mainFrame.add("Top", topButton);

        final JButton bottomButton = new JButton("Spawn enemy at random location");
        bottomButton.setFocusable(false);
        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Enemy enemy = new Enemy();
                enemy.setGlobalPosition(new Vector2(random.nextDouble(-100, 100), random.nextDouble(-100, 100)));
            }
        });
        mainFrame.add("Bottom", bottomButton);

        Engine.init();

        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();

        mainFrame.add("Middle", gameSpriteComponent);
        mainFrame.show();

        // gaming
        Shooter shooter = new Shooter();
        Engine.setCameraTarget(shooter);

        Enemy enemy = new Enemy();
        enemy.setGlobalPosition(new Vector2(0, 0));

        Enemy enemy2 = new Enemy();
        enemy2.setGlobalPosition(new Vector2(50, 50));

        KeyAdapter key = new KeyAdapter() {
            final List<Integer> shootCodes = Arrays.asList(
                    KeyEvent.VK_UP,
                    KeyEvent.VK_DOWN,
                    KeyEvent.VK_LEFT,
                    KeyEvent.VK_RIGHT
            );
            final List<Integer> moveCodes = Arrays.asList(
                    KeyEvent.VK_W,
                    KeyEvent.VK_A,
                    KeyEvent.VK_S,
                    KeyEvent.VK_D
            );
            HashMap<Integer, Boolean> keysHeld = new HashMap<Integer, Boolean>();
            {
                keysHeld.put(KeyEvent.VK_W, false);
                keysHeld.put(KeyEvent.VK_A, false);
                keysHeld.put(KeyEvent.VK_S, false);
                keysHeld.put(KeyEvent.VK_D, false);
            }

            private void updateShooterVelocity() {
                int xDir = 0;
                int yDir = 0;
                int speed = 250;

                if (keysHeld.get(KeyEvent.VK_W)) {
                    yDir = 1;
                }
                if (keysHeld.get(KeyEvent.VK_S)) {
                    yDir = -1;
                }
                if (keysHeld.get(KeyEvent.VK_W) && keysHeld.get(KeyEvent.VK_S)) {
                    yDir = 0;
                }
                if (keysHeld.get(KeyEvent.VK_D)) {
                    xDir = 1;
                }
                if (keysHeld.get(KeyEvent.VK_A)) {
                    xDir = -1;
                }
                if (keysHeld.get(KeyEvent.VK_A) && keysHeld.get(KeyEvent.VK_D)) {
                    xDir = 0;
                }

                Vector2 velocity = (new Vector2(xDir, yDir)).unit().multiply(speed);
                shooter.setVelocity(velocity);
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int keycode = ke.getKeyCode();
                if (moveCodes.contains(keycode)) {
                    keysHeld.put(keycode, true);
                    updateShooterVelocity();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                int keycode = ke.getKeyCode();
                if (moveCodes.contains(keycode)) {
                    keysHeld.put(keycode, false);
                    updateShooterVelocity();
                }
            }
        };
        mainFrame.addKeyListener(key);

        Vector2 offset = new Vector2(0, 0);
        Engine.setCameraOffset(offset);

        Background bg = new Background();

        // mouse
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                Vector2 mouseClickGlobalPosition = Engine.getMouseClickGlobalPosition(e);
                Vector2 shooterPosition = shooter.getGlobalPosition();

                Vector2 direction = mouseClickGlobalPosition.subtract(shooterPosition).unit();
                Bullet bullet = new Bullet(shooter.getGlobalPosition(), direction.multiply(700));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        gameSpriteComponent.addMouseListener(ml);

        // collisions
        gameSpriteComponent.addSpriteSpriteCollisionListener(shootgame.Enemy.class, shootgame.Bullet.class, new SpriteSpriteCollisionListener<shootgame.Enemy, shootgame.Bullet>() {
            @Override
            public void collision(shootgame.Enemy enemy, shootgame.Bullet bullet) {
                bullet.destroy();
                enemy.takeDamage(25);
            }
        });

        // particle
        ParticleSystem particles = new ParticleSystem(
                new Vector2(0, 0),
                new Range(-25, 25),
                new Range(150, 700),
                new Range(-3, 3),
                new Range(-100, 3),
                new Range(1, 2),
                250
        );

        particles.setEnabled(true);
    }
}
