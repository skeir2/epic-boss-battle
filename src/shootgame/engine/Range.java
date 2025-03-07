package shootgame.engine;

import java.util.Random;

public class Range {
    private static Random rng = new Random();
    public double min;
    public double max;

    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getDouble() {
        return rng.nextDouble(min, max);
    }
}
