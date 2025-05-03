package shootgame;

import shootgame.engine.Vector2;

public class EnemyBulletBill {
    public Vector2 origin;
    public Vector2 velocity;
    public int size;
    public double damage;
    public double lifespan;
    public int bulletType = 1;

    public EnemyBulletBill(Vector2 origin, Vector2 velocity, int size, double damage, double lifespan, int bulletType) {
        this.origin = origin;
        this.velocity = velocity;
        this.size = size;
        this.damage = damage;
        this.lifespan = lifespan;
        this.bulletType = bulletType;
    }

}
