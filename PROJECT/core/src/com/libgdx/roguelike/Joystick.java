package com.libgdx.roguelike;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Joystick {

    Circle circle, circle2;
    public Joystick(float x, float y, float radius ){
        circle = new Circle(x,y,radius);
        circle2 = new Circle(x,y,radius/5.0f);
    }

    public void update(float x, float y){


        if(circle.contains(x,y)){
            circle2.setPosition(x,y);

        }

    }

    public void render(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.circle(circle.x, circle.y, circle.radius);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(circle2.x,circle2.y,circle2.radius);
        renderer.end();
    }


}
