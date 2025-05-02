package shootgame.engine;

import basicgraphics.ClockWorker;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import shootgame.*;
import shootgame.enemies.BossEnemy;
import shootgame.enemies.Enemy;
import shootgame.enemies.RegularEnemy;
import shootgame.enemies.ShotgunEnemy;
import shootgame.engine.particles.ParticleSystem;

import java.awt.event.MouseEvent;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    static Random random = new Random();

    private static SpriteComponent gameSpriteComponent;
    private static ArrayList<Entity> allEntities = new ArrayList<Entity>();

    private static Entity cameraTarget;
    private static Vector2 cameraOffset = new Vector2(0, 0);

    private static Clock clock = Clock.systemDefaultZone();
    private static long lastTickTime = clock.millis();
    private static double gameStartTick;

    private static double spawnerTimer = 0;
    private static double bossSpawnTime = 1;
    private static boolean bossSpawned = false;

    public static void update(double deltaTime) {
        boolean spawnBoss = false;
        if (!bossSpawned) {
            spawnerTimer += deltaTime;

            if (spawnerTimer > 3) {
                spawnerTimer -= 3;

                Enemy enemy = new RegularEnemy();

                Vector2 randomPosition = new Vector2(random.nextDouble(-100, 100), random.nextDouble(-100, 100));
                enemy.setGlobalPosition(randomPosition);

                ShotgunEnemy shotgunEnemy = new ShotgunEnemy();
                Vector2 randomPosition2 = new Vector2(random.nextDouble(-100, 100), random.nextDouble(-100, 100));
                shotgunEnemy.setGlobalPosition(randomPosition2);
            }

            if (getTick() - gameStartTick > bossSpawnTime) {
                spawnBoss = true;
            }
        }

        ArrayList<Entity> entitiesMarkedForDestruction = new ArrayList<Entity>();
        ArrayList<ParticleSystem> particleSystemsNeedingToEmit = new ArrayList<ParticleSystem>();

        for (Entity entity : allEntities) {
            Vector2 newPosition = entity.getGlobalPosition().add(entity.getVelocity().multiply(deltaTime));
            entity.setGlobalPosition(newPosition);

            if (entity == cameraTarget) {
                int width = gameSpriteComponent.getWidth();
                int height = gameSpriteComponent.getHeight();
                entity.setCenterX((width / (double) 2) - cameraOffset.X);
                entity.setCenterY((height / (double) 2) + cameraOffset.Y);
            } else {
                int width = gameSpriteComponent.getWidth();
                int height = gameSpriteComponent.getHeight();

                Vector2 globalPosition = entity.getGlobalPosition();
                Vector2 cameraPosition = cameraTarget.getGlobalPosition();

                Vector2 offsetFromCenter = new Vector2(globalPosition.X - cameraPosition.X, globalPosition.Y - cameraPosition.Y);

                entity.setCenterX((width / (double) 2) + offsetFromCenter.X - cameraOffset.X);
                entity.setCenterY((height / (double) 2) - offsetFromCenter.Y + cameraOffset.Y);
            }

            entity.update(deltaTime);

            if (entity.getMarkedForDestruction()) {
                entitiesMarkedForDestruction.add(entity);
            }

            if (entity instanceof ParticleSystem) {
                ParticleSystem particles = (ParticleSystem) entity;
                if (particles.getParticlesToEmit() > 0) {
                    particleSystemsNeedingToEmit.add(particles);
                }
            }
        }

        for (ParticleSystem particles : particleSystemsNeedingToEmit) {
            particles.emit(particles.getParticlesToEmit());
            particles.setParticlesToEmit(0);
        }

        for (Entity entityMarkedForDestruction : entitiesMarkedForDestruction) {
            entityMarkedForDestruction.destroy();
        }

        for (EnemyBulletBill enemyBulletBill : Game.enemyBulletQueue) {
            EnemyBullet bullet = new EnemyBullet(enemyBulletBill.origin, enemyBulletBill.velocity, enemyBulletBill.size, enemyBulletBill.damage);
        }
        Game.enemyBulletQueue.clear();

        if (spawnBoss) {
            bossSpawned = true;

            BossEnemy boss = new BossEnemy();
            Game.boss = boss;

            GameUI.initializeBossHealthBarFrame();
        }
    }

    public static void init() {
        gameSpriteComponent = new SpriteComponent();

        long lastTickTime = clock.millis();
        gameStartTick = getTick();

        ClockWorker.addTask(new updateTask());

        ClockWorker.addTask(gameSpriteComponent.moveSprites());
        ClockWorker.initialize(16);
    }

    public static SpriteComponent getGameSpriteComponent() {
        return gameSpriteComponent;
    }

    public static void setCameraTarget(Entity entity) {
        cameraTarget = entity;
    }

    public static Entity getCameraTarget() {
        return cameraTarget;
    }

    public static void setCameraOffset(Vector2 offset) {
        cameraOffset = offset;
    }

    public static Vector2 getCameraOffset() {
        return cameraOffset;
    }

    public static void entityCreated(Entity entity) {
        allEntities.add(entity);
    }

    public static void entityDestroyed(Entity entity) {
        allEntities.remove(entity);
    }

    private static class updateTask extends Task {
        @Override
        public void run() {
            double deltaTime = (clock.millis() - lastTickTime) / (double) 1000;
            update(deltaTime);
            lastTickTime = clock.millis();
        }
    }

    public static Vector2 convertScreenPositionToGlobalPosition(Vector2 screenPos) {
        Vector2 cameraTargetPos = cameraTarget.getGlobalPosition();
        Vector2 cameraPos = cameraTargetPos.add(cameraOffset);
        double width = gameSpriteComponent.getWidth();
        double height = gameSpriteComponent.getHeight();
        Vector2 offsetFromCenterOfScreen = new Vector2((-width / 2.0) + screenPos.X, (-height / 2.0) + (height - screenPos.Y));
        double x = cameraPos.X + offsetFromCenterOfScreen.X;
        double y = cameraPos.Y + offsetFromCenterOfScreen.Y;

        return new Vector2(x, y);
    }

    public static Vector2 getMouseClickGlobalPosition(MouseEvent e) {
        Vector2 screenPos = new Vector2(e.getX(), e.getY());
        return convertScreenPositionToGlobalPosition(screenPos);
    }

    public static double getTick() {
        return clock.millis() / (double) 1000;
    }
}
