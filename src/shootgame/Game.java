package shootgame;

import basicgraphics.*;
import shootgame.engine.Engine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
                enemy.getEntity().setPosition(random.nextDouble(-100, 100), random.nextDouble(-100, 100));
            }
        });
        mainFrame.add("Bottom", bottomButton);

        Engine.init();

        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();

        mainFrame.add("Middle", gameSpriteComponent);
        mainFrame.show();

        // gaming
        Shooter shooter = new Shooter();
        Engine.setEntityBeingControlled(shooter.getEntity());

        Enemy enemy = new Enemy();
        enemy.getEntity().setPosition(0, 0);

        Enemy enemy2 = new Enemy();
        enemy2.getEntity().setPosition(50, 50);

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
                int xVel = 0;
                int yVel = 0;
                int speed = 250;

                if (keysHeld.get(KeyEvent.VK_W)) {
                    yVel = speed;
                }
                if (keysHeld.get(KeyEvent.VK_S)) {
                    yVel = -speed;
                }
                if (keysHeld.get(KeyEvent.VK_W) && keysHeld.get(KeyEvent.VK_S)) {
                    yVel = 0;
                }
                if (keysHeld.get(KeyEvent.VK_D)) {
                    xVel = speed;
                }
                if (keysHeld.get(KeyEvent.VK_A)) {
                    xVel = -speed;
                }
                if (keysHeld.get(KeyEvent.VK_A) && keysHeld.get(KeyEvent.VK_D)) {
                    xVel = 0;
                }

                shooter.getEntity().setVelocity(xVel, yVel);
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

        Background bg = new Background();
    }
}
