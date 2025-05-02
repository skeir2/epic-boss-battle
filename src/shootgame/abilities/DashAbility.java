package shootgame.abilities;

import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.Bullet;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;

import static shootgame.Game.shooter;

public class DashAbility extends Ability {
    Vector2 dashDirection = new Vector2(0, 0);
    double dashDuration = 0.15;
    double dashSpeed = 1000;
    double dashTick;
    boolean dashing = false;

    public DashAbility() {
        super(1.5);

        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                if (dashing) {
                    shooter.setVelocity(dashDirection.multiply(dashSpeed));
                    if (Engine.getTick() - dashTick > dashDuration) {
                        dashing = false;
                        shooter.controllable = true;
                        shooter.updateShooterVelocity();
                    }
                }
            }
        });
    }

    public void onUse() {
        Vector2 dashDirection = shooter.getCurrentDirection();
        this.dashDirection = dashDirection;
        dashing = true;
        dashTick = Engine.getTick();
        shooter.controllable = false;
    }
}
