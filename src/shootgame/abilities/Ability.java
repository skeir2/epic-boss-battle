package shootgame.abilities;

import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;
import shootgame.engine.gui.GuiFrame;

import static shootgame.Game.died;
import static shootgame.Game.shooter;

public abstract class Ability {
    double lastUsedTime = 0;
    double cooldown = 0;
    private boolean holding = false;

    public Ability(double cooldown) {
        this.cooldown = cooldown;
        this.lastUsedTime = Engine.getTick();

        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                if (holding) {
                    if (canUse()) {
                        use();
                    }
                }
            }
        });
    }

    public abstract void onUse();

    public boolean canUse() {
        double currentTick = Engine.getTick();
        double timePassed = (currentTick - lastUsedTime);

        if (died) {
            return false;
        }

        return (timePassed >= cooldown);
    }

    public boolean use() {
        boolean used = false;

        if (canUse()) {
            lastUsedTime = Engine.getTick();
            used = true;
            onUse();
        }

        return used;
    }

    public void startHold() {
        holding = true;
    }

    public void releaseHold() {
        holding = false;
    }

    public void connectToAbilityGuiFrame(GuiFrame abilityGuiFrame) {
        GuiFrame cooldownOverlay = null;

        for (GuiFrame frame : abilityGuiFrame.getChildren()) {
            if (frame.getName().equals("CooldownOverlay")) {
                cooldownOverlay = frame;
                break;
            }
        }

        if (cooldownOverlay == null) {
            return;
        }

        final GuiFrame overlayChosen = cooldownOverlay;

        ClockWorker.addTask(new Task() {
            @Override
            public void run() {
                double currentTick = Engine.getTick();
                double timePassed = (currentTick - lastUsedTime);
                double progress = Math.clamp(timePassed / cooldown, 0, 1);
                overlayChosen.setSizeScale(new Vector2(1, 1 - progress));
            }
        });
    }
}
