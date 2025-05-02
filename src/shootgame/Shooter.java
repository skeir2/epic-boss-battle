package shootgame;

import basicgraphics.*;
import basicgraphics.images.Picture;
import shootgame.engine.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Clock;

import static shootgame.Game.keysHeld;

public class Shooter extends Entity {
    public boolean controllable = true;
    public double health = 100;

    public Shooter() {
        super(40, 40, new Vector2(0, 0), "apple.png", 15);
    }

    @Override
    public void update(double deltaTime) {
        Vector2 velocity = getVelocity();
        Vector2 goalOffset = velocity.unit().multiply(10);
        Vector2 offset = Vector2.lerp(Engine.getCameraOffset(), goalOffset, deltaTime * 10);
        Engine.setCameraOffset(offset);
    }

    public Vector2 getCurrentDirection() {
        int xDir = 0;
        int yDir = 0;

        if (keysHeld.get(KeyEvent.VK_W)) {
            yDir = 1;
        }
        if (keysHeld.get(KeyEvent.VK_S)) {
            yDir = -1;
        }
        if (keysHeld.get(KeyEvent.VK_W) && keysHeld.get(KeyEvent.VK_S)) {
            yDir = 0;
        }
        if (keysHeld.get(KeyEvent.VK_D)) {
            xDir = 1;
        }
        if (keysHeld.get(KeyEvent.VK_A)) {
            xDir = -1;
        }
        if (keysHeld.get(KeyEvent.VK_A) && keysHeld.get(KeyEvent.VK_D)) {
            xDir = 0;
        }

        return (new Vector2(xDir, yDir)).unit();
    }

    public void updateShooterVelocity() {
        Vector2 dir = getCurrentDirection();
        int speed = 250;

        if (!this.controllable) {
            dir = new Vector2(0, 0);
        }

        Vector2 velocity = dir.multiply(speed);
        this.setVelocity(velocity);
    }

    public void takeDamage(double damage) {
        this.health = Math.clamp(this.health - damage, 0, 100);
        GameUI.updateHealthBar();
    }
}
