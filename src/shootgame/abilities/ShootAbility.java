package shootgame.abilities;

import shootgame.Bullet;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;

import java.awt.event.MouseEvent;

import static shootgame.Game.shooter;

public class ShootAbility extends Ability {
    public MouseEvent lastMouseEvent = null;

    public ShootAbility() {
        super(0.25);
    }

    public void onUse() {
        Vector2 mouseClickGlobalPosition = Engine.getMouseClickGlobalPosition(lastMouseEvent);
        Vector2 shooterPosition = shooter.getGlobalPosition();

        Vector2 direction = mouseClickGlobalPosition.subtract(shooterPosition).unit();
        Bullet bullet = new Bullet(shooter.getGlobalPosition(), direction.multiply(700));
    }
}
