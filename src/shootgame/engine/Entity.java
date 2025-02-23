package shootgame.engine;

import basicgraphics.*;
import basicgraphics.images.Picture;

import java.awt.*;

public class Entity extends Sprite {
    static int ballSize = 40;
    private double positionX = 0;
    private double positionY = 0;
    private double velocityX = 0;
    private double velocityY = 0;

    public Entity(int sizeX, int sizeY, double positionX, double positionY, Color color) {
        super(Engine.getGameSpriteComponent().getScene());

        Image im1 = BasicFrame.createImage(sizeX, sizeY);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(color);
        imgr.fillOval(0, 0, sizeX, sizeY);
        Picture p = new Picture(im1).transparentBright();

        setPicture(p);

        setPosition(positionX, positionY);

        Engine.entityCreated(this);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }
}
