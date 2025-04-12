package shootgame;

import shootgame.engine.Vector2;
import shootgame.engine.gui.GuiFrame;
import shootgame.engine.gui.ImageFrame;

import java.awt.*;
import java.util.Vector;
import java.util.ArrayList;

public class GameUI {
    public static GuiFrame abilitiesHolder = null;

    public static GuiFrame addAbilityGuiFrame() {
        GuiFrame abilityGuiFrame = new GuiFrame(new Vector2(0.5, 0.5), new Vector2(0.2, 1), new Vector2(0, 0));
        abilityGuiFrame.setParent(abilitiesHolder);
        abilityGuiFrame.setBackgroundColor(Color.blue);

        ImageFrame imageFrame = new ImageFrame(new Vector2(0.5, 0.5), new Vector2(1, 1), new Vector2(0, 0), "gun.jpg");
        imageFrame.setParent(abilityGuiFrame);
        imageFrame.setZIndex(-10000); // I have no idea how the draw order works

        GuiFrame cooldownOverlayFrame = new GuiFrame(new Vector2(0.5, 0), new Vector2(1, 1), new Vector2(0, 0));
        cooldownOverlayFrame.setAnchorPoint(new Vector2(0.5, 0));
        cooldownOverlayFrame.setParent(abilityGuiFrame);
        cooldownOverlayFrame.setZIndex(10000);
        cooldownOverlayFrame.setName("CooldownOverlay");
        cooldownOverlayFrame.setBackgroundColor(Color.green);

        // reposition abiility frames
        ArrayList<GuiFrame> children = abilitiesHolder.getChildren();
        for (int i = 0; i < children.size(); i++) {
            GuiFrame frame = children.get(i);
            double totalSize = children.size() * 0.25;
            double xPos = (0.5 - (totalSize / 2)) + (((i + 1) / (double)children.size()) * totalSize);
            frame.setPos(new Vector2(xPos, 0.5));
        }

        return abilityGuiFrame;
    }

    public static void init() {
        abilitiesHolder = new GuiFrame(new Vector2(0.5, 0.9), new Vector2(0.3, 0.14), new Vector2(0, 0));
        abilitiesHolder.setBackgroundColor(Color.gray);
        abilitiesHolder.setZIndex(-10000);
    }
}
