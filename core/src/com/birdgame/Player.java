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

import com.birdgame.util.Constants;

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
    private float jumpVelocity;
    
    public Player(){
        //initialize stuff..
        this.width = 64;
        this.height = 64;
        
        //Set starting position. Y coordinate will be the ground level
        this.position = new Vector2(0.5f, Constants.GROUND_LEVEL);
        this.scale = 1.0f;
        
        //Create and initialize sprite
        this.sprite = new Sprite(createTexture(this.width, this.height));
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOrigin(this.position.x /2, this.position.x / 2);
        this.sprite.setScale(this.scale);
        this.sprite.setSize(0.5f, 0.5f);
        
        //Set player jump velocity
        this.jumpVelocity = Constants.PLAYER_JMP_VELOCITY;
        
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
        //float x = this.position.x;
        
        //Check input and move/rotate player left or right
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            //rotate the sprite
            this.sprite.rotate(-rotationAmount);
            
            //Apply deltaX to x coordinate
            this.position.x += deltaX;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            this.sprite.rotate(rotationAmount);
            
            //Apply -deltaX to x coordinate
            this.position.x -= deltaX;
        }
        //Jump
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            //Jump only if the player isn't already jumping
            if (!this.jumping){
                Gdx.app.debug(TAG, "Jump!");
                this.jumping = true;
            }
        }
        
        //Update the x coordinate
        this.sprite.setX(this.position.x);
        
        //Update the jump motion if the player is jumpin'
        if (this.jumping)
            this.jump(deltaTime);

        //Check the "pulsating" effect limits
        if (this.scale > 1.25f || this.scale < 0.75f){
            this.scaleFactor *= -1.0f;
        }
        //Update scale by the pulsation
        this.scale += scaleFactor;
        
        //update the pulsation
        this.sprite.setScale(this.scale);
    }
    
    //Method for jump movement. Updates the player
    private void jump(float deltaTime){
        //Update the jump velocity with gravity
        this.jumpVelocity = this.jumpVelocity - Constants.GRAVITY;

        //Update the y coordinate with the current jump velocity
        this.position.y += (this.jumpVelocity * deltaTime); 
        this.sprite.setY(this.position.y);
        Gdx.app.debug(TAG, "Doing jump!!");
        
        //Check if the player is still above ground
        if (this.position.y <= Constants.GROUND_LEVEL){
            //Ground the player and set jumping to false
            this.position.y = Constants.GROUND_LEVEL;
            this.jumping = false;
            
            //Also reset the jump velocity to the starting value; otherwise
            //the player can jump only once...
            this.jumpVelocity = Constants.PLAYER_JMP_VELOCITY;
        }
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
