package shootgame;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

import static shootgame.Game.shooter;

public class EnemyBullet extends Entity {
    private double creationTick = Engine.getTick();
    private double lifespan = 1;
    public double damage = 0;
    private int bulletType = 1;
    private double speed = 0;

    public EnemyBullet(Vector2 origin, Vector2 velocity, int size, double damage, double lifespan, int bulletType) {
        super(size, size, new Vector2(0, 0), Color.pink, 20);

        Image im1 = BasicFrame.createImage(size, size);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(Color.pink);
        imgr.fillOval(0, 0, size, size);
        imgr.setColor(Color.red);
        imgr.fillOval(1, 1, size - 2, size - 2);
        Picture p = new Picture(im1).transparentBright();
        setPicture(p);

        setGlobalPosition(origin);
        setVelocity(velocity);

        this.damage = damage;
        this.lifespan = lifespan;
        this.bulletType = bulletType;
        this.speed = velocity.magnitude();
    }

    @Override
    public void update(double deltaTime) {
        double timePassed = Engine.getTick() - creationTick;

        if (bulletType == 2) {
            Vector2 targetPosition = shooter.getGlobalPosition();
            Vector2 targetLookAt = (targetPosition.subtract(this.getGlobalPosition())).unit();
            Vector2 newDirection = Vector2.lerp(this.getVelocity().unit(), targetLookAt, 0.05);
            this.setVelocity(newDirection.multiply(speed));
        }

        if (timePassed >= lifespan) {
            markForDestruction();
        }
    }
}
