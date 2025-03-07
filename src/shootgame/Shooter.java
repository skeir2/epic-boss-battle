package shootgame;

import basicgraphics.*;
import basicgraphics.images.Picture;
import shootgame.engine.*;

import java.awt.*;
import java.time.Clock;

public class Shooter extends Entity {
    public Shooter() {
        super(40, 40, new Vector2(0, 0), Color.blue);
        setDrawingPriority(100);
    }

    @Override
    public void update(double deltaTime) {
        Vector2 velocity = getVelocity();
        Vector2 goalOffset = velocity.unit().multiply(10);
        Vector2 offset = Vector2.lerp(Engine.getCameraOffset(), goalOffset, deltaTime * 10);
        Engine.setCameraOffset(offset);
    }
}
