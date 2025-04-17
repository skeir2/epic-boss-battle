package shootgame.engine.gui;

import basicgraphics.BasicFrame;
import basicgraphics.images.Picture;
import shootgame.engine.Vector2;

import java.awt.*;

public class TextFrame extends GuiFrame {
    public String frameText = "PLACEHOLDER";
    public TextFrame(Vector2 pos, Vector2 sizeScale, Vector2 sizeOffset, String frameText, int drawingPriority) {
        super(pos, sizeScale, sizeOffset, drawingPriority);
        this.frameText = frameText;
        update(true);
    }

    public void setFrameText(String newText) {
        frameText = newText;
        update();
    }

    @Override
    public void sizeChanged() {
        Vector2 pixelSize = getPixelSize();

        if ((int) pixelSize.X <= 0 || (int) pixelSize.Y <= 0) {
            Image newImg = BasicFrame.createImage(5, 5);
            //    newImg.getScaledInstance();
            Graphics newGraphics = newImg.getGraphics();
            Picture newPic = new Picture(newImg);
            setPicture(newPic);
            return;
        }

        Image newImg = BasicFrame.createImage((int) pixelSize.X * 10, (int) pixelSize.Y * 10);
        Graphics newGraphics = newImg.getGraphics();

        String text = frameText;
        if (text == null) {
            text = "placeholder";
        }

        Font newFont = new Font(Font.SANS_SERIF, Font.PLAIN, 100);
        newGraphics.setColor(Color.black);
        newGraphics.setFont(newFont);
        newGraphics.drawString(text, 0, (int) pixelSize.Y);
        Picture newPic = new Picture(newImg);
        setPicture(newPic);
    }
}
