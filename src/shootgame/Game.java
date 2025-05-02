package shootgame;

import basicgraphics.*;
import shootgame.abilities.DashAbility;
import shootgame.abilities.HealAbility;
import shootgame.abilities.ShootAbility;
import shootgame.enemies.BossEnemy;
import shootgame.enemies.Enemy;
import shootgame.enemies.RegularEnemy;
import shootgame.enemies.ShotgunEnemy;
import shootgame.engine.Engine;
import shootgame.engine.Range;
import shootgame.engine.Vector2;
import shootgame.engine.gui.GuiFrame;
import shootgame.engine.particles.ParticleSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Game {
    public static Shooter shooter;
    public static BossEnemy boss;
    static Random random = new Random();
    static String[][] layout = {
     //       {"Top"},
            {"Middle"},
    //        {"Bottom"}
    };

    public static HashMap<Integer, Boolean> keysHeld = new HashMap<Integer, Boolean>();
    static {
        keysHeld.put(KeyEvent.VK_W, false);
        keysHeld.put(KeyEvent.VK_A, false);
        keysHeld.put(KeyEvent.VK_S, false);
        keysHeld.put(KeyEvent.VK_D, false);
    }

    public static LinkedList<EnemyBulletBill> enemyBulletQueue = new LinkedList<>();

    public static void main(String[] args) {
        BasicFrame mainFrame = new BasicFrame("Shooter Game");
        mainFrame.setStringLayout(layout);

        Engine.init();

        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();

        mainFrame.add("Middle", gameSpriteComponent);
        mainFrame.show();

        // gaming
        shooter = new Shooter();
        System.out.println("shooter created");
        Engine.setCameraTarget(shooter);
        System.out.println("camera target set to shooter");
        Enemy regular = new RegularEnemy();
        regular.setGlobalPosition(new Vector2(0, 0));

        Enemy enemy2 = new ShotgunEnemy();
        enemy2.setGlobalPosition(new Vector2(50, 50));

        GameUI.init();

        GuiFrame shootAbilityGuiFrame = GameUI.addAbilityGuiFrame("gun.jpg");
        ShootAbility shootAbility = new ShootAbility();
        shootAbility.connectToAbilityGuiFrame(shootAbilityGuiFrame);

        GuiFrame dashAbilityGuiFrame = GameUI.addAbilityGuiFrame("evasion scarf.png");
        DashAbility dashAbility = new DashAbility();
        dashAbility.connectToAbilityGuiFrame(dashAbilityGuiFrame);

        GuiFrame healAbilityGuiFrame = GameUI.addAbilityGuiFrame("health pot.jpg");
        HealAbility healAbility = new HealAbility();
        healAbility.connectToAbilityGuiFrame(healAbilityGuiFrame);

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

            @Override
            public void keyPressed(KeyEvent ke) {
                int keycode = ke.getKeyCode();
                if (moveCodes.contains(keycode)) {
                    keysHeld.put(keycode, true);
                    shooter.updateShooterVelocity();
                }

                if (keycode == KeyEvent.VK_F) {
                    dashAbility.startHold();
                }

                if (keycode == KeyEvent.VK_H) {
                    healAbility.startHold();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                int keycode = ke.getKeyCode();
                if (moveCodes.contains(keycode)) {
                    keysHeld.put(keycode, false);
                    shooter.updateShooterVelocity();
                }

                if (keycode == KeyEvent.VK_F) {
                    dashAbility.releaseHold();
                }

                if (keycode == KeyEvent.VK_H) {
                    healAbility.releaseHold();
                }
            }
        };
        mainFrame.addKeyListener(key);

        Vector2 offset = new Vector2(0, 0);
        Engine.setCameraOffset(offset);

        SummoningCircle bg = new SummoningCircle();

        // mouse
        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                /*
                Vector2 mouseClickGlobalPosition = Engine.getMouseClickGlobalPosition(e);
                Vector2 shooterPosition = shooter.getGlobalPosition();

                Vector2 direction = mouseClickGlobalPosition.subtract(shooterPosition).unit();
                Bullet bullet = new Bullet(shooter.getGlobalPosition(), direction.multiply(700));
                 */
                shootAbility.startHold();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                shootAbility.releaseHold();
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
        gameSpriteComponent.addSpriteSpriteCollisionListener(Enemy.class, shootgame.Bullet.class, new SpriteSpriteCollisionListener<Enemy, shootgame.Bullet>() {
            @Override
            public void collision(Enemy enemy, shootgame.Bullet bullet) {
                bullet.destroy();
                enemy.takeDamage(25);
            }
        });

        gameSpriteComponent.addSpriteSpriteCollisionListener(shootgame.Shooter.class, shootgame.EnemyBullet.class, new SpriteSpriteCollisionListener<shootgame.Shooter, shootgame.EnemyBullet>() {
            @Override
            public void collision(shootgame.Shooter shooter, shootgame.EnemyBullet enemyBullet) {
                enemyBullet.destroy();
                shooter.takeDamage(5);
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

        particles.setEnabled(false);
    //    particles.emit(10);

        GuiFrame guiFrame = new GuiFrame(new Vector2(0.5, 0.5), new Vector2(1, 1), new Vector2(0, 0), -10000);
        guiFrame.setBackgroundColor(Color.black);
        // frame
        /*
        TextFrame rotationTextFrame = new TextFrame(new Vector2(0.5, 0.5), new Vector2(1, 1), new Vector2(0, 0), "1");
        ImageFrame imageFrame = new ImageFrame(new Vector2(0, 0.5), new Vector2(0.5, 3), new Vector2(0, 0), "light-cat-white.png");
        TextFrame textFrame = new TextFrame(new Vector2(0.5, 0.5), new Vector2(1, 1), new Vector2(0, 0), "Epic text");
        GuiFrame guiFrame2 = new GuiFrame(new Vector2(1, 0.5), new Vector2(0.25, 2), new Vector2(0, 0));
        GuiFrame guiFrame = new GuiFrame(new Vector2(0.5, 0.5), new Vector2(0.25, 0.25), new Vector2(0, 0));
        guiFrame.setBackgroundColor(Color.blue);
        guiFrame2.setParent(guiFrame);
        textFrame.setParent(guiFrame2);
        imageFrame.setParent(guiFrame);
        rotationTextFrame.setParent(imageFrame);
        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                double currentTick = Engine.getTick();
                double x = 0.5 + (Math.sin(currentTick) * 0.5);
                double y = 0.5 + (Math.cos(currentTick) * 0.5);
            //    guiFrame.setPos(new Vector2(x, y));

                double xSize = 0.5 + (Math.sin(currentTick * 0.423) * 0.5);
                guiFrame.setSizeScale(new Vector2(xSize, guiFrame.getSizeScale().Y));

                double x2 = 0.5 + (Math.sin(currentTick * 3.3) * 0.5);
                double y2 = 0.5 + (Math.cos(currentTick * 3.3) * 0.5);
                guiFrame2.setPos(new Vector2(x2, y2));

                float hue = (1 + (float)Math.sin(currentTick * 2)) * 0.5f;
                Color newColor = Color.getHSBColor(hue, 1, 1);
                guiFrame2.setBackgroundColor(newColor);

                double rotation = (currentTick * 100) % 360;
                imageFrame.setRotation(rotation);
                rotationTextFrame.setFrameText(String.format("%.2f", (rotation * Math.PI) / 180));
            }
        });
         */
    }

    public static void queueEnemyBullet(Vector2 origin, Vector2 velocity, int size, double damage) {
        enemyBulletQueue.add(new EnemyBulletBill(origin, velocity, size, damage));
    }
}
