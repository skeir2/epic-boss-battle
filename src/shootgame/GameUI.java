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
        GuiFrame abilityGuiFrame = new GuiFrame(new Vector2(0.5, 0.5), new Vector2(0.2, 1), new Vector2(0, 0), 1001);
        abilityGuiFrame.setParent(abilitiesHolder);
        abilityGuiFrame.setBackgroundColor(Color.blue);

        ImageFrame imageFrame = new ImageFrame(new Vector2(0.5, 0.5), new Vector2(1, 1), new Vector2(0, 0), "gun.jpg", 1005);
        imageFrame.setParent(abilityGuiFrame);

        GuiFrame cooldownOverlayFrame = new GuiFrame(new Vector2(0.5, 1), new Vector2(1, 1), new Vector2(0, 0), 1010);
        cooldownOverlayFrame.setAnchorPoint(new Vector2(0.5, 1));
        cooldownOverlayFrame.setParent(abilityGuiFrame);
        cooldownOverlayFrame.setName("CooldownOverlay");
        cooldownOverlayFrame.setBackgroundColor(Color.green);
        cooldownOverlayFrame.setBackgroundTransparency(0.5);

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
        abilitiesHolder = new GuiFrame(new Vector2(0.5, 0.9), new Vector2(0.3, 0.14), new Vector2(0, 0), 1000);
        abilitiesHolder.setBackgroundColor(Color.gray);
    }
}
