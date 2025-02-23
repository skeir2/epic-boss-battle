package shootgame;

import basicgraphics.images.Picture;
import shootgame.engine.Entity;

import java.awt.*;

public class Background {
    Entity entity;

    public Background() {
        entity = new Entity(40, 40, 40, 40, Color.blue);

        Picture pic = new Picture("shooterGameBackground.png");
        pic.setSize(400, 400);
        entity.setDrawingPriority(-100);
        entity.setPicture(pic);
    }

    public Entity getEntity() {
        return entity;
    }
}
