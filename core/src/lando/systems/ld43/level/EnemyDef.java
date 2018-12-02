package lando.systems.ld43.level;

public class EnemyDef {
    public enum EnemyType {Drone, Vertical, MiniBoss1}
    public float time;
    public float x;
    public float y;
    public EnemyType type;


    public EnemyDef(){

    }

    public EnemyDef(float time, float x, float y, EnemyType type){
        this.time = time;
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
