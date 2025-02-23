package shootgame;

import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.engine.Engine;
import shootgame.engine.Entity;

import java.awt.*;
import java.util.Random;

public class Enemy {
    static Random random = new Random();
    double speed = random.nextDouble(40, 200);

    Entity entity;

    public Enemy() {
        entity = new Entity(40, 40, 0, 0, Color.orange);
        entity.setDrawingPriority(60);

        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                Entity target = Engine.getEntityBeingControlled();
                double xDifference = (target.getPositionX() - entity.getPositionX());
                double yDifference = (target.getPositionY() - entity.getPositionY());

                entity.setVelocity(Math.signum(xDifference) * speed, Math.signum(yDifference) * speed);
            }
        });
    }

    public Entity getEntity() {
        return entity;
    }
}
