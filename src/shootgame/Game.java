package shootgame;

import basicgraphics.*;
import basicgraphics.images.BackgroundPainter;
import basicgraphics.images.Picture;
import basicgraphics.sounds.ReusableClip;
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
    public static boolean died = false;
    public static ParticleSystem bulletParticles;
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

    public static ReusableClip healSound;
    public static ReusableClip dashSound;
    public static ReusableClip enemyDieSound;
    public static ReusableClip playerDamagedSound;
    public static ReusableClip bossTheme;

    public static void main(String[] args) {
        BasicFrame mainFrame = new BasicFrame("Simple Bullet Hell Battle");

        Engine.init();

        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();

        // CARDS
        // game card
        Card gameCard = mainFrame.getCard();
        gameCard.setStringLayout(layout);
        gameCard.add("Middle", gameSpriteComponent);
        gameCard.setPainter(new BackgroundPainter(new Picture("grey background.png")));

        // info screen
        Card infoCard = mainFrame.getCard();

        infoCard.setPainter(new BackgroundPainter(new Picture("info card.png")));
        String[][] infoLayout = {
                {"Title"},
                {"PlayButton"}
        };
        infoCard.setStringLayout(infoLayout);
        JLabel blankTitle = new JLabel("");
        blankTitle.setForeground(Color.white);
        infoCard.add("Title", blankTitle);
        JButton beginButton = new JButton("Begin Trial");
        beginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                healSound = new ReusableClip("Potion.wav");
                dashSound = new ReusableClip("Dash.wav");
                enemyDieSound = new ReusableClip("GlassBreak.wav");
                playerDamagedSound = new ReusableClip("TerrariaHurt.wav");
                bossTheme = new ReusableClip("TeamGrimoireDantalion.wav");

                Engine.gameStartTick = Engine.getTick();
                Engine.lastTickTime = Engine.clock.millis();
                gameCard.showCard();
                // The BasicContainer bc2 must request the focus
                // otherwise, it can't get keyboard events.
                gameCard.requestFocus();

                // Start the timer
                ClockWorker.initialize(16);
            }
        });
        infoCard.add("PlayButton",beginButton);

        // title
        Card titleCard = mainFrame.getCard();

        titleCard.setPainter(new BackgroundPainter(new Picture("title card.png")));
        String[][] titleLayout = {
                {"Title"},
                {"PlayButton"}
        };
        titleCard.setStringLayout(titleLayout);
        JLabel title = new JLabel("Welcome!");
        title.setForeground(Color.white);
        titleCard.add("Title", title);
        JButton jstart = new JButton("Start");
        jstart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoCard.showCard();
                infoCard.requestFocus();
            }
        });
        titleCard.add("PlayButton",jstart);
        titleCard.showCard();

    //    gameCard.showCard();
        mainFrame.show();

        // gaming
        shooter = new Shooter();
        System.out.println("shooter created");
        Engine.setCameraTarget(shooter);
        System.out.println("camera target set to shooter");
        shooter.setGlobalPosition(new Vector2(-300, 0));
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
        gameCard.addKeyListener(key);

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

        // particle
        ParticleSystem bulletParticles = new ParticleSystem(
                new Vector2(0, 0),
                new Range(-250, 250),
                new Range(-250, 250),
                new Range(-250, 250),
                new Range(-250, 250),
                new Range(0.1, 0.5),
                250
        );
        bulletParticles.setColor(Color.yellow);
        bulletParticles.setSizeRange(new Range(3, 6));
        Game.bulletParticles = bulletParticles;

        ParticleSystem bulletDebrisParticles = new ParticleSystem(
                new Vector2(0, 0),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(0.1, 0.5),
                250
        );
        bulletDebrisParticles.setColor(Color.yellow);
        bulletDebrisParticles.setSizeRange(new Range(3, 6));

        ParticleSystem redBulletDebrisParticles = new ParticleSystem(
                new Vector2(0, 0),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(-50, 50),
                new Range(0.1, 0.5),
                250
        );
        redBulletDebrisParticles.setColor(Color.red);
        redBulletDebrisParticles.setSizeRange(new Range(3, 6));

        // collisions
        gameSpriteComponent.addSpriteSpriteCollisionListener(Enemy.class, shootgame.Bullet.class, new SpriteSpriteCollisionListener<Enemy, shootgame.Bullet>() {
            @Override
            public void collision(Enemy enemy, shootgame.Bullet bullet) {
                bulletDebrisParticles.emit(3, bullet.getGlobalPosition());
                bullet.destroy();
                enemy.takeDamage(25);
            }
        });

        gameSpriteComponent.addSpriteSpriteCollisionListener(shootgame.Shooter.class, shootgame.EnemyBullet.class, new SpriteSpriteCollisionListener<shootgame.Shooter, shootgame.EnemyBullet>() {
            @Override
            public void collision(shootgame.Shooter shooter, shootgame.EnemyBullet enemyBullet) {
                if (!died) {
                    redBulletDebrisParticles.emit(3, enemyBullet.getGlobalPosition());
                    playerDamagedSound.playOverlapping();
                    enemyBullet.destroy();
                    shooter.takeDamage(enemyBullet.damage);
                }
            }
        });

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

    public static void queueEnemyBullet(Vector2 origin, Vector2 velocity, int size, double damage, double lifespan, int bulletType) {
        enemyBulletQueue.add(new EnemyBulletBill(origin, velocity, size, damage, lifespan, bulletType));
    }

    public static void onDied() {
        if (!died) {
            died = true;
            BasicDialog.getOK("You died!");
            shooter.setCostume("dead apple.png");
            System.exit(0);
            shooter.setVelocity(new Vector2(0, 0));
        }
    }
}
