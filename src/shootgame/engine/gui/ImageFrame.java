package shootgame.engine.gui;

import basicgraphics.BasicFrame;
import basicgraphics.images.Picture;
import shootgame.engine.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ImageFrame extends GuiFrame {
    private Picture currentPic;
    private String imageName;
    public ImageFrame(Vector2 pos, Vector2 sizeScale, Vector2 sizeOffset, String imageName) {
        super(pos, sizeScale, sizeOffset);
        this.imageName = imageName;
        update(true);
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
            setDrawingPriority(1001);
            return;
        }

        if (imageName == null) {
            imageName = "light-cat-white.png";
        }
        if (currentPic == null) {
            currentPic = new Picture(imageName);
        }

        // Modified code of picture
        Image img = currentPic.getImage();
        Image scaledImage = BasicFrame.createImage((int)pixelSize.X, (int)pixelSize.Y);
        Graphics2D g2 = (Graphics2D) scaledImage.getGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setToScale(pixelSize.X / img.getWidth(currentPic), pixelSize.Y / img.getHeight(currentPic));
        g2.drawImage(img, xform, currentPic);
        Picture newPic = new Picture(scaledImage);
        setPicture(newPic);
        setDrawingPriority(1000 + zIndex);
    }
}
