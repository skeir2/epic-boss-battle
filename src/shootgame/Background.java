package shootgame;

import basicgraphics.images.Picture;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

public class Background extends Entity {
    public Background() {
        super(40, 40, new Vector2(0, 0), Color.blue, 0);

        Picture pic = new Picture("shooterGameBackground.png");
        pic.setSize(400, 400);
        setPicture(pic);
    }
}
