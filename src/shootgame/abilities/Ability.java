package shootgame.abilities;

import basicgraphics.ClockWorker;
import basicgraphics.Task;
import shootgame.engine.Engine;
import shootgame.engine.Vector2;
import shootgame.engine.gui.GuiFrame;

public abstract class Ability {
    double lastUsedTime = 0;
    double cooldown = 0;

    public Ability(double cooldown) {
        this.cooldown = cooldown;
        this.lastUsedTime = Engine.getTick();
    }

    public abstract void onUse();

    public boolean canUse() {
        double currentTick = Engine.getTick();
        double timePassed = (currentTick - lastUsedTime);

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
