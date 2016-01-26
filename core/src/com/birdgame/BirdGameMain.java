package com.birdgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.birdgame.util.Constants;

public class BirdGameMain extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;

    public Player player;

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

        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose(){
        player.dispose();
    }
}
