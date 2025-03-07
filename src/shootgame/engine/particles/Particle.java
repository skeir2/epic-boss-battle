package shootgame.engine.particles;

import basicgraphics.BasicFrame;
import basicgraphics.images.Picture;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;

public class Particle extends Entity {
    private double startTick = Engine.getTick();
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;
    private Vector2 size;
    private Vector2 upVector;
    private double lifespan;

    public Particle(Vector2 position, Vector2 velocity, Vector2 acceleration, Vector2 size, Vector2 upVector, double lifespan, Picture pic) {
        super();
        setPicture(pic);
        setGlobalPosition(position);
        setX(-100);
        setY(-100);
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.size = size;
        this.lifespan = lifespan;

        this.upVector = upVector;
        this.upVector = this.upVector.unit();
    }

    @Override
    public void update(double deltaTime) {
        Vector2 currentPos = this.getGlobalPosition();
        Vector2 upVector = this.upVector;
        Vector2 rightVector = new Vector2(upVector.Y, -upVector.X);

        Vector2 newVelocity = velocity.add(acceleration.multiply(deltaTime));
        this.velocity = newVelocity;

        Vector2 upVectorDisplacement = upVector.multiply(newVelocity.Y).multiply(deltaTime);
        Vector2 rightVectorDisplacement = rightVector.multiply(newVelocity.X).multiply(deltaTime);
        Vector2 displacement = upVectorDisplacement.add(rightVectorDisplacement);
        Vector2 newPos = currentPos.add(displacement);
        setGlobalPosition(newPos);

        if (Engine.getTick() - startTick >= lifespan) {
            markForDestruction();
        }
    }
}
