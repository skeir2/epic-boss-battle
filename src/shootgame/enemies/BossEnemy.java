package shootgame.enemies;

import basicgraphics.BasicDialog;
import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.Game;
import shootgame.GameUI;
import shootgame.engine.Engine;
import shootgame.engine.Entity;
import shootgame.engine.Vector2;

import java.awt.*;
import java.util.Random;

import static shootgame.Game.*;

public class BossEnemy extends Enemy {
    static Random random = new Random();
    double speed = 50;
    public double maxHealth = 22000;
    private double lastShootTick = Engine.getTick();
    private double shootCooldown = 1.6;
    private double lastRingTick = Engine.getTick();
    private double ringCooldown = 2;
    private double lastSpinnyTick = Engine.getTick();
    private double spinnyCooldown = 0.06;
    private int spinnyIndex = 0;
    private double lastBigHomingBulletsTick = Engine.getTick();
    private double bigHomingBulletsCooldown = 0.5;
    private double teleportRadius = 1000;

    private double lastPatternSpinnyTick = Engine.getTick();
    private double patternSpinnyCooldown = 0.3;
    private int patternSpinnyIndex = 0;

    public BossEnemy() {
        super(100, Color.orange);
        this.health = maxHealth;
    }

    @Override
    public void damageTaken() {
        GameUI.updateBossHealthBar();
    }

    @Override
    public void onDeath() {
        bossTheme.stop();
        BasicDialog.getOK("You won! thanks for playing!");
        System.exit(0);
    }

    @Override
    public void update(double deltaTime) {
        double healthPercentage = health / maxHealth;
        double currentTick = Engine.getTick();
        Entity target = Engine.getCameraTarget();

        Vector2 targetPosition = target.getGlobalPosition();
        Vector2 entityPosition = getGlobalPosition();
        Vector2 direction = targetPosition.subtract(entityPosition).unit();

        double distanceToTarget = (targetPosition.subtract(entityPosition)).magnitude();
        if (distanceToTarget > teleportRadius) {
            Vector2 offsetVec = (new Vector2(0, 200)).rotate(Math.toRadians(random.nextDouble(0, 360)));
            Vector2 newPos = targetPosition.add(offsetVec);
            this.setGlobalPosition(newPos);
        }

        Vector2 velocity = direction.multiply(speed);

        setVelocity(velocity);

        boolean finale = (healthPercentage < 0.15);
        if (finale && currentTick - lastPatternSpinnyTick > patternSpinnyCooldown) {
            teleportRadius = 700;
            double speed = 300;
            patternSpinnyIndex += 1;

            int forks = 20;

            if (healthPercentage < 0.05) {
                speed = 320;
                forks = 40;
                this.speed = 100;
                spinnyCooldown = 0.01;
            }

            for (int i = 0; i < forks; i++) {
                double angleIncrement = (360 / (double)forks);
                double deg = i * angleIncrement;
                deg += (angleIncrement / (double)2) * (patternSpinnyIndex % 2);
                Vector2 smallBulletVelocity = (new Vector2(0, 1)).rotate(Math.toRadians(deg)).multiply(speed);
                Game.queueEnemyBullet(this.getGlobalPosition(), smallBulletVelocity, 20, 5, 5, 1);
            }

            lastPatternSpinnyTick = Engine.getTick();
        }
        if (finale) {
            return;
        }

        boolean canBigHomingBullets = (healthPercentage < 0.25);
        if (canBigHomingBullets && currentTick - lastBigHomingBulletsTick > bigHomingBulletsCooldown) {
            // big ol red ones
            Vector2 bulletVelocity = direction.multiply(400);
            Game.queueEnemyBullet(this.getGlobalPosition(), bulletVelocity, 100, 10, 2, 2);
            lastBigHomingBulletsTick = Engine.getTick();
        }
        if (canBigHomingBullets) {
            return;
        }

        boolean canSpinny = (healthPercentage < 0.7 && healthPercentage > 0.35);
        if (canSpinny && currentTick - lastSpinnyTick > spinnyCooldown) {
            // spinny
            spinnyIndex += 1;

            int forks = 4;

            if (healthPercentage < 0.6) {
                forks = 10;
            }
            if (healthPercentage < 0.5) {
                forks = 5;
            }

            for (int i = 0; i < forks; i++) {
                double deg = i * (360 / (double)forks);
                deg += spinnyIndex * 3;
                Vector2 smallBulletVelocity = (new Vector2(0, 1)).rotate(Math.toRadians(deg)).multiply(1000);
                Game.queueEnemyBullet(this.getGlobalPosition(), smallBulletVelocity, 20, 5, 5, 1);
            }

            lastSpinnyTick = Engine.getTick();
        };
        if (canSpinny && healthPercentage > 0.5) {
            return;
        }

        if (currentTick - lastShootTick > shootCooldown) {
            // shoot
            Vector2 bulletVelocity = direction.multiply(700);
            if (healthPercentage > 0.5) {
                Game.queueEnemyBullet(this.getGlobalPosition(), bulletVelocity, 50, 10, 5, 1);
            } else {
                Game.queueEnemyBullet(this.getGlobalPosition(), bulletVelocity, 30, 10, 2, 2);
            }
            if (healthPercentage < 0.35) {
                shootCooldown = 0.5;
            }
            lastShootTick = Engine.getTick();
        }

        if (healthPercentage < 0.5 && healthPercentage > 0.4) {
            return;
        }

        if (healthPercentage < 0.9 && currentTick - lastRingTick > ringCooldown) {
            // rings
            int forks = 10;
            if (healthPercentage < 0.8) {
                forks = 25;
                ringCooldown = 1.2;
            }
            for (int i = 0; i < forks; i++) {
                double deg = i * (360 / (double)forks);
                Vector2 smallBulletVelocity = direction.rotate(Math.toRadians(deg)).multiply(400);
                Game.queueEnemyBullet(this.getGlobalPosition(), smallBulletVelocity, 20, 5, 5, 1);
            }
            lastRingTick = Engine.getTick();
        }
    }
}
