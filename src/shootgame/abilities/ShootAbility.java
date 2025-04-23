package shootgame.abilities;

import shootgame.Bullet;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;

import java.awt.*;
import java.awt.event.MouseEvent;

import static shootgame.Game.shooter;

public class ShootAbility extends Ability {
    public ShootAbility() {
        super(0.1);
    }

    public void onUse() {
        Point mouseLocation = Engine.getGameSpriteComponent().getMousePosition();
        Vector2 mouseGlobalPosition = Engine.convertScreenPositionToGlobalPosition(new Vector2(mouseLocation.x, mouseLocation.y));
        Vector2 shooterPosition = shooter.getGlobalPosition();

        Vector2 direction = mouseGlobalPosition.subtract(shooterPosition).unit();
        Bullet bullet = new Bullet(shooter.getGlobalPosition(), direction.multiply(700));
    }
}
