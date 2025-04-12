package shootgame.engine.gui;

import basicgraphics.BasicFrame;
import basicgraphics.Sprite;
import basicgraphics.SpriteComponent;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.ArrayList;

// Literally copying frames from roblox
public class GuiFrame extends Sprite {
    Vector2 pos;
    Vector2 sizeScale;
    Vector2 sizeOffset;
    Vector2 anchorPoint = new Vector2(0.5, 0.5);
    Color backgroundColor = Color.black;
    double rotation = 0;
    int zIndex = 0;
    String name = "GuiFrame";
    GuiFrame parent = null;
    ArrayList<GuiFrame> children = new ArrayList<>();

    public GuiFrame(Vector2 pos, Vector2 sizeScale, Vector2 sizeOffset) {
        super(Engine.getGameSpriteComponent().getScene());

        Image img = BasicFrame.createImage(10, 10);
        Graphics graphics = img.getGraphics();
        graphics.setColor(backgroundColor);
        Picture pic = new Picture(img).transparentBright();
        setPicture(pic);
        setDrawingPriority(1000 + zIndex);

        this.pos = pos;
        this.sizeScale = sizeScale;
        this.sizeOffset = sizeOffset;
        update(true);

        Vector2 pixelSize = getPixelSize();
    }

    public Vector2 getPixelSize() {
        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();
        int screenWidth = gameSpriteComponent.getWidth();
        int screenHeight = gameSpriteComponent.getHeight();
        Vector2 parentSize = new Vector2(screenWidth, screenHeight);
        if (parent != null) {
            parentSize = parent.getPixelSize();
        }
        Vector2 scaleSize = new Vector2((int)parentSize.X * sizeScale.X, (int)parentSize.Y * sizeScale.Y);
        Vector2 pixelSize = scaleSize.add(sizeOffset);

        return pixelSize;
    }

    public Vector2 getPixelPosition() {
        SpriteComponent gameSpriteComponent = Engine.getGameSpriteComponent();
        int screenWidth = gameSpriteComponent.getWidth();
        int screenHeight = gameSpriteComponent.getHeight();
        Vector2 parentSize = new Vector2(screenWidth, screenHeight);
        Vector2 parentPosition = new Vector2(0, 0);
        if (parent != null) {
            parentSize = parent.getPixelSize();
            parentPosition = parent.getPixelPosition();
        }
        Vector2 pixelSize = getPixelSize();

        Vector2 pixelPosition = new Vector2(((int) parentSize.X * pos.X) + (int)parentPosition.X, ((int) parentSize.Y * pos.Y) + (int)parentPosition.Y);
        Vector2 anchorPointOffset = new Vector2(-pixelSize.X * anchorPoint.X, -pixelSize.Y * anchorPoint.Y);
    //    anchorPointOffset = new Vector2(0, 0);
        pixelPosition = pixelPosition.add(anchorPointOffset);

        return pixelPosition;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
        update();
    }

    public void setSizeScale(Vector2 sizeScale) {
        this.sizeScale = sizeScale;
        update(true);
    }

    public void setSizeOffset(Vector2 sizeOffset) {
        this.sizeOffset = sizeOffset;
        update(true);
    }

    public void update() {
        posChanged();

        for (GuiFrame childGuiFrame : children) {
            childGuiFrame.update();
        }

    //    System.out.println("Pos: " + pos + " | " + getPixelPosition());
    //    System.out.println("Size: " + sizeScale + " | " + getPixelSize());
    }

    public void update(boolean updateSize) {
        if (updateSize) {
            sizeChanged();
        }

        posChanged();

        for (GuiFrame childFrame : children) {
            childFrame.update(updateSize);
        }

    //    System.out.println("Pos: " + pos + " | " + getPixelPosition());
    //    System.out.println("Size: " + sizeScale + " | " + getPixelSize());
    }

    public void posChanged() {
        Vector2 pixelPos = getPixelPosition();
        setX((int) pixelPos.X);
        setY((int) pixelPos.Y);
    }

    public void sizeChanged() {
        /*
        Picture pic = getPicture();
        Graphics graphics = pic.getImage().getGraphics();

        Dimension currentSize = pic.getSize();
        Vector2 pixelSize = getPixelSize();
        graphics.clearRect(0, 0, currentSize.width, currentSize.height);
        graphics.fillRect(0, 0, (int) pixelSize.X, (int) pixelSize.Y);
        setPicture(pic);
        */
        Vector2 pixelSize = getPixelSize();

        if ((int) pixelSize.X <= 0 || (int) pixelSize.Y <= 0) {
            Image newImg = BasicFrame.createImage(5, 5);
        //    newImg.getScaledInstance();
            Graphics newGraphics = newImg.getGraphics();
            Picture newPic = new Picture(newImg);
            setPicture(newPic);
            setDrawingPriority(1000 + zIndex);
            return;
        }

        Image newImg = BasicFrame.createImage((int) pixelSize.X, (int) pixelSize.Y);
        Graphics newGraphics = newImg.getGraphics();
        newGraphics.setColor(backgroundColor);
        newGraphics.fillRect(0, 0, (int) pixelSize.X, (int) pixelSize.Y);
        Picture newPic = new Picture(newImg);
        setPicture(newPic);
        setDrawingPriority(1000 + zIndex);
    }

    public Vector2 getSizeScale() {
        return sizeScale;
    }

    public void setParent(GuiFrame parent) {
        this.parent = parent;
        parent.children.add(this);
        update(true);
    }

    @Override
    public void destroy() {
        if (parent != null) {
            parent.children.remove(this);
        }
        super.destroy();
    }

    public void setBackgroundColor(Color newColor) {
        this.backgroundColor = newColor;
        update(true);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        double rads = (rotation * Math.PI) / 180;
   //     this.setVel(Math.sin(rads), Math.cos(rads));
        this.setHeadingOffset(rads);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAnchorPoint(Vector2 anchorPoint) {
        this.anchorPoint = anchorPoint;
        update();
    }

    public Vector2 getAnchorPoint() {
        return anchorPoint;
    }

    public ArrayList<GuiFrame> getChildren() {
        return children;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
        setDrawingPriority(1000 + zIndex);
        update();
    }
}
