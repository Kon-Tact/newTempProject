package com.libgdx.roguelike;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Joystick {


    Circle circle0, circle1;
    boolean positionFixe = false;


    public Joystick(float x, float y, float radius ){
        circle0 = new Circle(x,y,radius);
        circle1 = new Circle(x,y,radius/5.0f);
    }

    public void update(float x, float y){
        if(circle0.contains(x,y)){
            circle1.setPosition(x,y);
        }
    }

    public void render(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.circle(circle0.x, circle0.y, circle0.radius);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(circle1.x, circle1.y, circle1.radius);
        renderer.end();
    }
    public void setPosition(float x, float y){
        circle0.setX(x);
        circle0.setY(y);
        circle1.setX(x);
        circle1.setY(y);
    }

    public Circle getCircle0() {
        return circle0;
    }

    public void setCircle0(Circle circle0) {
        this.circle0 = circle0;
    }

    public Circle getCircle1() {
        return circle1;
    }

    public void setCircle1(Circle circle1) {
        this.circle1 = circle1;
    }

    public boolean isPositionFixe() {
        return positionFixe;
    }

    public void setPositionFixe(boolean positionFixe) {
        this.positionFixe = positionFixe;
    }
}
