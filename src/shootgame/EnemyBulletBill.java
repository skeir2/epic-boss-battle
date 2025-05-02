package shootgame;

import shootgame.engine.Vector2;

public class EnemyBulletBill {
    public Vector2 origin;
    public Vector2 velocity;
    public int size;
    public double damage;

    public EnemyBulletBill(Vector2 origin, Vector2 velocity, int size, double damage) {
        this.origin = origin;
        this.velocity = velocity;
        this.size = size;
        this.damage = damage;
    }

}
