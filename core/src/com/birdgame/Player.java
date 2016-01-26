/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.birdgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author ManMadeMachine
 */
public class Player {
    public static final String TAG = Player.class.getName();
    
    //Player sprite, position and origin
    private Sprite sprite;
    private Vector2 position;
    
    //Player rotation in degrees and scale
    private float rotationSpeed = 270; //90 degrees per second
    private float scale;
    private float scaleFactor = 0.01f;
    
    //Player width and height in pixels
    private int width;
    private int height;
    
    //boolean for player jumping
    private boolean jumping;
    
    //player jump "impulse" velocity
    private float jumpVelocity = 1.0f;
    
    //TODO: Move to the Constants class
    private float gravity = 0.1f;
    
    //DEBUG: how long does the jump last (milliseconds)?
    private long jumpTime = 2000;
    
    //DEBUG: when did the last jump start?
    private long jumpStartTime = 0;
    
    public Player(){
        //initialize stuff..
        this.width = 64;
        this.height = 64;
        
        //Set starting position
        this.position = new Vector2(0.5f, 0.5f);
        this.scale = 1.0f;
        
        //Create and initialize sprite
        this.sprite = new Sprite(createTexture(this.width, this.height));
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOrigin(this.position.x /2, this.position.x / 2);
        this.sprite.setScale(this.scale);
        this.sprite.setSize(0.5f, 0.5f);
        
        //Player starts grounded
        this.jumping = false;
        
        Gdx.app.debug(TAG, "Created player!");
    }
    
    //Create a ball-shaped texture
    private Texture createTexture(int width, int height){
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        
        //Set color to light red
        pixmap.setColor(1.0f, 0.5f, 0.5f, 1.0f);
        
        //Draw a circle
        pixmap.fillCircle(width / 2, height/2, (width/2)-1);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.drawLine(width/2, 1, width/2, height/2);
        
        return new Texture(pixmap);
    }
    
    public void update(float deltaTime){
        //Calculate the amount of rotation that can be applied
        float rotationAmount = rotationSpeed * deltaTime;
        
        //Calculate the delta x to be applied this update call
        float deltaX = (rotationAmount / 360.0f) * 2 * MathUtils.PI * (sprite.getWidth() / 2.0f);
        
        //Last x coordinate
        float x = this.sprite.getX();
        
        //Check input and move/rotate player left or right
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            //rotate the sprite
            this.sprite.rotate(-rotationAmount);
            
            //Apply deltaX to x coordinate
            x += deltaX;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            this.sprite.rotate(rotationAmount);
            
            //Apply -deltaX to x coordinate
            x -= deltaX;
        }
        //Jump
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            if (!this.jumping){
                Gdx.app.debug(TAG, "Jump!");
                this.jumpStartTime = TimeUtils.millis();
                this.jumping = true;
            }
            else{
                Gdx.app.debug(TAG, "Already jumping!");
            }
        }
        
        //Update the x coordinate
        this.sprite.setX(x);
        
        //DEBUG: update the jumping boolean, if the jumping has ended
        if (TimeUtils.millis() - this.jumpStartTime > jumpTime){
            this.jumping = false;
            this.jumpStartTime = 0;
        }
        
        //Check the "pulsating" effect limits
        if (this.scale > 1.25f || this.scale < 0.75f){
            this.scaleFactor *= -1.0f;
        }
        
        this.scale += scaleFactor;
        
        //update the pulsation
        this.sprite.setScale(this.scale);
    }
    
    //Render the sprite
    public void render(SpriteBatch batch){
        this.sprite.draw(batch);
    }
    
    //Dispose any disposable stuff
    public void dispose(){
        this.sprite.getTexture().dispose();
    }
}
