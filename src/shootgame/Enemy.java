package shootgame;

import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.Random;

public class Enemy extends Entity {
    static Random random = new Random();
    double speed = random.nextDouble(40, 200);
    private double health = 100;

    public Enemy() {
        super(40, 40, new Vector2(0, 0), Color.orange);
        setDrawingPriority(60);
    }

    public void takeDamage(double damage) {
        health -= damage;

        if (health <= 0) {
            markForDestruction();
        }
    }

    @Override
    public void update(double deltaTime) {
        Entity target = Engine.getCameraTarget();

        Vector2 targetPosition = target.getGlobalPosition();
        Vector2 entityPosition = getGlobalPosition();
        Vector2 direction = targetPosition.subtract(entityPosition).unit();
        Vector2 velocity = direction.multiply(speed);

        setVelocity(velocity);
    }
}
