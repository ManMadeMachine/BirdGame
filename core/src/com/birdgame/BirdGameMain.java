package com.birdgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.birdgame.util.Constants;

public class BirdGameMain extends ApplicationAdapter {
    public static final String TAG = BirdGameMain.class.getName();
    
    SpriteBatch batch;
    OrthographicCamera camera;

    public Player player;
    
    public Array<Enemy> enemies;
    
    //time variables for enemy spawn
    public long lastEnemySpawn = 0;
    public long enemySpawnTime = 10000;  //Spawn enemy every two seconds

    @Override
    public void create () {

        //Set debug level!
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();

        //Create a camera with a viewport size of 5x5 metres in game world units
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT * (h / w));
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        //Create the player
        player = new Player();
        
        enemies = new Array<Enemy>();
    }

    @Override
    public void resize(int width, int height){
        camera.viewportWidth = Constants.WORLD_WIDTH;
        camera.viewportHeight = Constants.WORLD_HEIGHT * ((float)height / (float)width);
        camera.update();
    }

    @Override
    public void render () {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.5f, 0.65f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //UPDATE
        player.update(Gdx.graphics.getDeltaTime());

        //Check if a new enemy needs to be spawned
        if (TimeUtils.millis() - lastEnemySpawn >= enemySpawnTime){
            //Spawn a new enemy
            enemies.add(new Enemy());
            Gdx.app.debug(TAG, "Added enemy!");
            
            //Update the last spawn time
            lastEnemySpawn = TimeUtils.millis();
        }
        
        //update enemies
        for (Enemy e : enemies){
            e.update(Gdx.graphics.getDeltaTime());
            
            //Check if the enemy flew out and remove it from the list
            if (e.isOutOfBounds()){
                enemies.removeValue(e, true);
                Gdx.app.debug(TAG, "Enemy removed! Array size: " + enemies.size);
            }
        }
        
        batch.begin();
        player.render(batch);
        
        //Render the enemies
        for (Enemy e : enemies){
            e.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose(){
        player.dispose();
        
        for (Enemy e : enemies){
            e.dispose();
        }
    }
}
