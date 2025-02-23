package shootgame.engine;

import basicgraphics.ClockWorker;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.Task;
import shootgame.Enemy;

import java.time.Clock;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;

public class Engine {
    static Random random = new Random();

    private static SpriteComponent gameSpriteComponent;
    private static Entity entityBeingControlled;
    private static ArrayList<Entity> allEntities = new ArrayList<Entity>();

    private static Clock clock = Clock.systemDefaultZone();
    private static long lastTickTime = clock.millis();

    private static double spawnerTimer = 0;

    public static void update(double deltaTime) {
        spawnerTimer += deltaTime;

        if (spawnerTimer > 3) {
            spawnerTimer -= 3;

            Enemy enemy = new Enemy();
            enemy.getEntity().setPosition(random.nextDouble(-100, 100), random.nextDouble(-100, 100));
        }

        for (Entity entity : allEntities) {
            double newPositionX = (entity.getPositionX() + entity.getVelocityX() * deltaTime);
            double newPositionY = (entity.getPositionY() + entity.getVelocityY() * deltaTime);

            entity.setPosition(newPositionX, newPositionY);

            if (entity == entityBeingControlled) {
                int width = gameSpriteComponent.getWidth();
                int height = gameSpriteComponent.getHeight();
                entity.setCenterX(width / 2);
                entity.setCenterY(height / 2);
            } else {
                int width = gameSpriteComponent.getWidth();
                int height = gameSpriteComponent.getHeight();

                double globalX = entity.getPositionX();
                double globalY = entity.getPositionY();
                double cameraX = entityBeingControlled.getPositionX();
                double cameraY = entityBeingControlled.getPositionY();

                double offsetFromCenterX = globalX - cameraX;
                double offsetFromCenterY = globalY - cameraY;

                entity.setCenterX((width / 2) + offsetFromCenterX);
                entity.setCenterY((height / 2) - offsetFromCenterY);
            }
        }
    }

    public static void init() {
        gameSpriteComponent = new SpriteComponent();

        long lastTickTime = clock.millis();

        ClockWorker.addTask(new updateTask());

        ClockWorker.addTask(gameSpriteComponent.moveSprites());
        ClockWorker.initialize(16);
    }

    public static SpriteComponent getGameSpriteComponent() {
        return gameSpriteComponent;
    }

    public static void setEntityBeingControlled(Entity entity) {
        entityBeingControlled = entity;
    }

    public static Entity getEntityBeingControlled() {
        return entityBeingControlled;
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
}
