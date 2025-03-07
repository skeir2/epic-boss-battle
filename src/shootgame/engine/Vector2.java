package shootgame.engine;

// Epic vector math time
public class Vector2 {
    public final double X;
    public final double Y;

    public Vector2(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    public double magnitude() {
        return Math.sqrt(X*X + Y*Y);
    }

    public Vector2 unit() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vector2(0, 0);
        }
        return new Vector2(X / mag, Y / mag);
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(X + vec.X,  Y + vec.Y);
    }

    public Vector2 subtract(Vector2 vec) {
        return new Vector2(X - vec.X,  Y - vec.Y);
    }

    public Vector2 multiply(double scaleFactor) {
        return new Vector2(X * scaleFactor, Y * scaleFactor);
    }

    public Vector2 divide(double scaleFactor) {
        return new Vector2(X / scaleFactor, Y / scaleFactor);
    }

    public static Vector2 lerp(Vector2 a, Vector2 b, double t) {
        Vector2 difference = b.subtract(a);
        return a.add(difference.multiply(t));
    }

    @Override
    public String toString() {
        return String.format("(%g, %g)", X, Y);
    }
}
