package shootgame.enemies;

import shootgame.Game;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.Random;

public abstract class Enemy extends Entity {
    static Random random = new Random();
    double speed = random.nextDouble(40, 200);
    public double health = 100;
    private double lastShootTick = Engine.getTick();
    private double shootCooldown = 2;

    public Enemy(int size, Color color) {
        super(size, size, new Vector2(0, 0), color, 10);
    }

    public void takeDamage(double damage) {
        health -= damage;

        if (health <= 0) {
            Game.enemyDieSound.playOverlapping();
            onDeath();
            markForDestruction();
        }

        damageTaken();
    }

    public void damageTaken() {

    }

    public void onDeath() {

    }

    @Override
    public abstract void update(double deltaTime);
}
