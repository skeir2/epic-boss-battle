package shootgame.engine;

import basicgraphics.*;
import basicgraphics.images.Picture;

import java.awt.*;

public class Entity extends Sprite {
    static int ballSize = 40;
    private Vector2 globalPosition = new Vector2(0, 0);
    private Vector2 velocity = new Vector2(0, 0);
    private boolean markedForDestruction = false;

    public Entity(int sizeX, int sizeY, Vector2 globalPosition, Color color) {
        super(Engine.getGameSpriteComponent().getScene());

        Image im1 = BasicFrame.createImage(sizeX, sizeY);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(color);
        imgr.fillOval(0, 0, sizeX, sizeY);
        Picture p = new Picture(im1).transparentBright();

        setPicture(p);

        setGlobalPosition(globalPosition);

        Engine.entityCreated(this);
    }

    public Vector2 getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(Vector2 globalPosition) {
        this.globalPosition = globalPosition;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    @Override
    public void destroy() {
        Engine.entityDestroyed(this);
        super.destroy();
    }

    public boolean getMarkedForDestruction() {
        return markedForDestruction;
    }

    public void markForDestruction() {
        markedForDestruction = true;
    }

    public void update(double deltaTime) {

    }
}
