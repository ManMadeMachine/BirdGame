/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.birdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.birdgame.util.Constants;

/**
 *
 * @author ManMadeMachine
 */
public class Enemy {
    public static final String TAG = Enemy.class.getName();
    private Texture texture;
    private Vector2 position;
    private Vector2 size;
    private Rectangle bounds;
    
    private float speed = 0.5f;
    
    private float yDelta = 0.5f;
    private float deltaCounter = 0.01f;
    
    public Enemy(){
        //Initialize
        this.texture = new Texture(createTempPixmap());
        
        //Size
        this.size = new Vector2(0.25f, 0.25f);
        
        //position (moving to right atm)
        this.position = new Vector2(-this.size.x, 2.5f);
        
        //bounding rectangle
        this.bounds = new Rectangle(0, 0, 32, 32);
    }
    
    private Pixmap createTempPixmap(){
        int w = 32;
        int h = 32;
        
        Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        
        return pixmap;
    }
    
    public void update(float deltaTime){
        //Update movement, move to the right
        this.position.x += speed * deltaTime;
        
        //Update delta value
        yDelta += deltaCounter;
        
        //Clamp y coordinate delta value
        if (yDelta < -0.5f || yDelta > 0.5f){
            deltaCounter *= -1;
        }
        
        //up-down movement
        this.position.y += yDelta * deltaTime;
        
        //Clamp y-coordinate delta
        //yDelta = MathUtils.clamp(yDelta, -0.5f, 0.5f);
        
    }
    
    public boolean isOutOfBounds(){
        return (this.position.x > Constants.WORLD_WIDTH);
    }
    
    public void render(SpriteBatch batch){
        batch.draw(this.texture, this.position.x, this.position.y, this.size.x, this.size.y);
    }
    
    public void dispose(){
        this.texture.dispose();
    }
}
