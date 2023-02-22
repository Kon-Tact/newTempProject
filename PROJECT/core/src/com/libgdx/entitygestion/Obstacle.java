package com.libgdx.entitygestion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle implements Entity {

    private static TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    public Sprite sprite;
    public String uniqueID;
    public Color spriteTint;
    public Rectangle rect;


    public Obstacle() {
    }

    public Obstacle(float x, float y, float width, float height) {

    }



    @Override
    public void initializeSprite() {

    }

    @Override
    public void checkSprite(String string) {

    }
}
