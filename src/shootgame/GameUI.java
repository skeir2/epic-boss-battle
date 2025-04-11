package shootgame;

import shootgame.engine.Vector2;
import shootgame.engine.gui.GuiFrame;

import java.awt.*;
import java.util.Vector;

public class GameUI {
    public static void init() {
        GuiFrame abilitiesHolder = new GuiFrame(new Vector2(0.5, 0.9), new Vector2(0.3, 0.14), new Vector2(0, 0));
        abilitiesHolder.setBackgroundColor(Color.gray);


    }
}
