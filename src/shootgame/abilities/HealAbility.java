package shootgame.abilities;

import shootgame.Bullet;
import shootgame.Game;
import shootgame.GameUI;
import shootgame.engine.Engine;
import shootgame.engine.Range;
import shootgame.engine.Vector2;
import shootgame.engine.particles.Particle;
import shootgame.engine.particles.ParticleSystem;

import java.awt.*;

import static shootgame.Game.shooter;

public class HealAbility extends Ability {
    ParticleSystem healParticles;

    public HealAbility() {
        super(20);

        ParticleSystem particles = new ParticleSystem(
                new Vector2(0, 0),
                new Range(-250, 250),
                new Range(-250, 250),
                new Range(-3, 3),
                new Range(-100, 3),
                new Range(0.1, 1),
                250
        );
        particles.setColor(Color.green);
        particles.setUpVector(new Vector2(0, 1));
        this.healParticles = particles;
    }

    public void onUse() {
        shooter.health = Math.clamp(shooter.health + 70, 0, 100);
        healParticles.emit(10, shooter.getGlobalPosition());
        GameUI.updateHealthBar();
        Game.healSound.playOverlapping();
    }
}
