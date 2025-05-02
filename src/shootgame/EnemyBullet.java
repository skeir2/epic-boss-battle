package shootgame;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

public class EnemyBullet extends Entity {
    private double creationTick = Engine.getTick();
    private double lifespan = 1;
    private double damage = 0;

    public EnemyBullet(Vector2 origin, Vector2 velocity, int size, double damage) {
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
    }

    @Override
    public void update(double deltaTime) {
        double timePassed = Engine.getTick() - creationTick;

        if (timePassed >= lifespan) {
            markForDestruction();
        }
    }
}
