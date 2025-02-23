package shootgame;

import basicgraphics.*;
import basicgraphics.images.Picture;
import shootgame.engine.*;

import java.awt.*;
import java.time.Clock;

public class Shooter {
    Entity entity;

    public Shooter() {
        entity = new Entity(40, 40, 40, 40, Color.blue);
        entity.setDrawingPriority(100);
    }

    public Entity getEntity() {
        return entity;
    }
}
