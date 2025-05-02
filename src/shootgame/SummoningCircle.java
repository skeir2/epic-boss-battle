package shootgame;

import basicgraphics.images.Picture;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

public class SummoningCircle extends Entity {
    public SummoningCircle() {
        super(40, 40, new Vector2(0, 0), Color.blue, 0);

        Picture pic = new Picture("summoning circle.jpg");
        pic.setSize(400, 400);
        setPicture(pic);
    }
}
