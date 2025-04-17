package shootgame;

import basicgraphics.BasicFrame;
import basicgraphics.ClockWorker;
import basicgraphics.Task;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

public class Bullet extends Entity {
    private double creationTick = Engine.getTick();
    private double lifespan = 1;

    public Bullet(Vector2 origin, Vector2 velocity) {
        super(15, 15, new Vector2(0, 0), Color.yellow, 20);

        Image im1 = BasicFrame.createImage(15, 15);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(Color.black);
        imgr.fillOval(0, 0, 15, 15);
        imgr.setColor(Color.yellow);
        imgr.fillOval(1, 1, 13, 13);
        Picture p = new Picture(im1).transparentBright();
        setPicture(p);

        setGlobalPosition(origin);
        setVelocity(velocity);
    }

    @Override
    public void update(double deltaTime) {
        double timePassed = Engine.getTick() - creationTick;

        if (timePassed >= lifespan) {
            markForDestruction();
        }
    }
}
