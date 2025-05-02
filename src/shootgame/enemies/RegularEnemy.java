package shootgame.enemies;

import shootgame.Game;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.Random;

public class RegularEnemy extends Enemy {
    static Random random = new Random();
    double speed = random.nextDouble(40, 200);
    private double lastShootTick = Engine.getTick();
    private double shootCooldown = 2;

    public RegularEnemy() {
        super(40, Color.yellow);
    }

    @Override
    public void update(double deltaTime) {
        Entity target = Engine.getCameraTarget();

        Vector2 targetPosition = target.getGlobalPosition();
        Vector2 entityPosition = getGlobalPosition();
        Vector2 direction = targetPosition.subtract(entityPosition).unit();
        Vector2 velocity = direction.multiply(speed);

        setVelocity(velocity);

        if (Engine.getTick() - lastShootTick > shootCooldown) {
            // shoot
            Vector2 bulletVelocity = direction.multiply(500);
            Game.queueEnemyBullet(this.getGlobalPosition(), bulletVelocity.rotate(Math.toRadians(15)), 12, 10);
            lastShootTick = Engine.getTick();
        }
    }
}
