package shootgame.engine.particles;

import basicgraphics.BasicFrame;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Range;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.Vector;

public class ParticleSystem extends Entity {
    private Range velocityRangeX;
    private Range velocityRangeY;
    private Range accelerationRangeX;
    private Range accelerationRangeY;
    private Range lifespanRange;
    private Range sizeRange = new Range(5, 10);
    private Vector2 position;
    private Vector2 upVector = new Vector2(0, 1);
    private double rate = 10;
    private boolean enabled = false;
    private double progress = 0;
    private int particlesToEmit = 0;
    private double startTick = Engine.getTick();
    private Color particleColor = Color.BLUE;

    public ParticleSystem(Vector2 position, Range velocityRangeX, Range velocityRangeY, Range accelerationRangeX, Range accelerationRangeY, Range lifespanRange, double rate) {
        super(1, 1, position, Color.white, -1);

        this.position = position;
        this.rate = rate;

        this.velocityRangeX = velocityRangeX;
        this.velocityRangeY = velocityRangeY;
        this.accelerationRangeX = accelerationRangeX;
        this.accelerationRangeY = accelerationRangeY;
        this.lifespanRange = lifespanRange;
    }

    public Vector2 getUpVector() {
        return upVector;
    }

    public void setUpVector(Vector2 upVector) {
        this.upVector = upVector.unit();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void emitParticle() {
        Vector2 pos = position;
        Vector2 vel = new Vector2(velocityRangeX.getDouble(), velocityRangeY.getDouble());
        Vector2 accel = new Vector2(accelerationRangeX.getDouble(), accelerationRangeY.getDouble());
        Vector2 size = new Vector2(sizeRange.getDouble(), sizeRange.getDouble());
        double lifespan = lifespanRange.getDouble();

        Image im1 = BasicFrame.createImage((int) size.X, (int) size.Y);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(particleColor);
        imgr.fillOval(0, 0, (int) size.X, (int) size.Y);
        Picture pic = new Picture(im1).transparentBright();

        Particle particle = new Particle(pos, vel, accel, size, upVector, lifespan, pic);
    }

    private void emitParticle(Vector2 positionOverride) {
        Vector2 pos = positionOverride;
        Vector2 vel = new Vector2(velocityRangeX.getDouble(), velocityRangeY.getDouble());
        Vector2 accel = new Vector2(accelerationRangeX.getDouble(), accelerationRangeY.getDouble());
        Vector2 size = new Vector2(sizeRange.getDouble(), sizeRange.getDouble());
        double lifespan = lifespanRange.getDouble();

        Image im1 = BasicFrame.createImage((int) size.X, (int) size.Y);
        Graphics imgr = im1.getGraphics();
        imgr.setColor(particleColor);
        imgr.fillOval(0, 0, (int) size.X, (int) size.Y);
        Picture pic = new Picture(im1).transparentBright();

        Particle particle = new Particle(pos, vel, accel, size, upVector, lifespan, pic);
    }

    public void emit(int amount) {
        for (int i = 0; i < amount; i++) {
            emitParticle();
        }
    }

    public void emit(int amount, Vector2 positionOverride) {
        for (int i = 0; i < amount; i++) {
            emitParticle(positionOverride);
        }
    }

    public void setParticlesToEmit(int particlesToEmit) {
        this.particlesToEmit = particlesToEmit;
    }

    public int getParticlesToEmit() {
        return particlesToEmit;
    }

    @Override
    public void update(double deltaTime) {
        if (enabled) {
            progress += (rate * deltaTime);
        } else {
            progress = 0;
        }

        double timePassed = Engine.getTick() - startTick;
        Vector2 newUpVector = new Vector2(Math.cos(timePassed * 3), Math.sin(timePassed * 3));
        setUpVector(newUpVector);

        if (progress >= 1) {
            particlesToEmit += Math.floor(progress);
            progress -= Math.floor(progress);
        }
    }

    public void setColor(Color color) {
        this.particleColor = color;
    }

    public void setSizeRange(Range sizeRange) {
        this.sizeRange = sizeRange;
    }
}
