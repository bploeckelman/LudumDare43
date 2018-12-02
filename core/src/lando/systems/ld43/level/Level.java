package lando.systems.ld43.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import lando.systems.ld43.entities.enemies.*;
import lando.systems.ld43.screens.GameScreen;

import java.util.ArrayList;

public class Level {
    public float timer;
    public ArrayList<EnemyDef> enemies;
    GameScreen gameScreen;

    public Level(GameScreen gameScreen, int levelIndex){
        FileHandle levelFile = Gdx.files.internal("levels/levelWTF.json");
        switch (levelIndex){
            case 1:
                levelFile = Gdx.files.internal("levels/level1.json");
        }
        this.gameScreen = gameScreen;
        this.timer = 0;
//        enemies = new ArrayList<EnemyDef>();
//        enemies.add(new EnemyDef(1f, 800, 200, EnemyDef.EnemyType.Drone));
//        enemies.add(new EnemyDef(1f, 800, 400, EnemyDef.EnemyType.Drone));
//        enemies.add(new EnemyDef(5.5f, 800, 100, EnemyDef.EnemyType.Vertical));
//        enemies.add(new EnemyDef(5.7f, 800, 200, EnemyDef.EnemyType.Vertical));
//        enemies.add(new EnemyDef(5.9f, 800, 300, EnemyDef.EnemyType.Vertical));
//        enemies.add(new EnemyDef(20f, 800, 300, EnemyDef.EnemyType.Drone));

        Json json = new Json();
        json.addClassTag("Enemy", EnemyDef.class);
//        System.out.println(json.prettyPrint(enemies));
        enemies = json.fromJson(ArrayList.class, levelFile);
        //enemies.add(new EnemyDef(10f, 900, 300, EnemyDef.EnemyType.MiniBoss1));
    }


    public void update(float dt){
        timer += dt;
        for (int i = enemies.size()-1; i >= 0; i--){
            EnemyDef enemy = enemies.get(i);
            if (enemy.time < timer){
                switch (enemy.type) {
                    case Drone:
                        gameScreen.enemies.add(new DroneEnemy(gameScreen, enemy.x, enemy.y));
                        break;
                    case Vertical:
                        gameScreen.enemies.add(new VerticalEnemy(gameScreen, enemy.x, enemy.y));
                        break;
                    case Beeline:
                        gameScreen.enemies.add(new BeelineEnemy(gameScreen, enemy.x, enemy.y));
                        break;
                    case VerticalTrailing:
                        gameScreen.enemies.add(new VerticalTrailingEnemy(gameScreen, enemy.x, enemy.y));
                        break;
                    case MiniBoss1:
                        this.gameScreen.dialogUI.reset(this.gameScreen, "boss1-encounter.json").show();
                        gameScreen.enemies.add(new MiniBoss1(gameScreen, enemy.x, enemy.y));
                }
                enemies.remove(i);
            }
        }
    }
}
