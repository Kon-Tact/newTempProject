package com.libgdx.roguelike;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.entitygestion.Player;

import java.util.Iterator;


public class Game extends ApplicationAdapter implements InputProcessor {

    boolean display=false;


    int refreshValue = 0;
    int refreshValueTrigger = 5;//20;

    Map map;
//    TiledMap tiledMap;
//    TiledMapRenderer tiledMapRenderer;

    OrthographicCamera camera;
    Viewport viewport;
    int SCREEN_WIDTH = 0;
    int SCREEN_HEIGHT = 0;
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Sprite myPlayerSprite;
    Player myPlayer;
    TextureRegion textureRegion;
    int speed = 80;
    int calculatedWidth = 0;
    int calculatedHeight = 0;

    FirebaseInterface _FBIC;
    Cursor cursor;

    Joystick joystick;

    ShapeRenderer shapeRenderer;
   // Vector3 mouse;



    public static boolean lockOnListReadFromDB = false;

    public Game(FirebaseInterface FBIC) {
        _FBIC = FBIC;
    }

    public Game() {
    }





    @Override
    public void resize(int width, int height) {
        // viewport.update(width, height);
    }

    @Override
    public void create() {



        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);




        joystick = new Joystick(100,100,200);
        shapeRenderer=new ShapeRenderer();




        TiledMap tempTiledMap = new TmxMapLoader().load("sampleMap.tmx");
        map = new Map(tempTiledMap,new OrthogonalTiledMapRenderer(tempTiledMap));

        map.tiledMapRenderer.render();

        batch = new SpriteBatch();
        initializeCharacter();
        _FBIC.init(myPlayer.uniqueID);
        cursor = new Cursor(myPlayer.spriteTint);

        Iterator<String> it = map.getTiledMap().getProperties().getKeys();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        int widthMap = Integer.parseInt(map.getTiledMap().getProperties().get("width") + "");
        int heightMap = Integer.parseInt(map.getTiledMap().getProperties().get("height") + "");
        int tilewidth = Integer.parseInt(map.getTiledMap().getProperties().get("tilewidth") + "");
        int tileheight = Integer.parseInt(map.getTiledMap().getProperties().get("tileheight") + "");

        calculatedWidth = widthMap * tilewidth;
        calculatedHeight = heightMap * tileheight;

        Gdx.input.setInputProcessor(this);


    }

    private void initializeCharacter() {

        myPlayer = new Player();
        myPlayer.initializeSprite();
//        batch = player.getBatch();
        myPlayerSprite = myPlayer.getSprite();
        textureAtlas = myPlayer.getTextureAtlas();
        textureRegion = myPlayer.getTextureRegion();
        myPlayerSprite.setPosition(50, 50);
    }



    @Override
    public void render() {




        displayJoystick();
     //   if(joystick.getDirectionInput()!=-1){
//        System.out.print("refreshValue   " + refreshValue);
//        System.out.println("    refreshValueTrigger   " + refreshValueTrigger);
        if(Gdx.input.isTouched(0) ) {
            refreshValue++;
            if(refreshValue==refreshValueTrigger){
//                System.out.println("mdddddddddqsljdpoqjdqp");
//                System.out.println("mdddddddddqsljdpoqjdqp");
//                System.out.println("mdddddddddqsljdpoqjdqp");
//                System.out.println("mdddddddddqsljdpoqjdqp");
                refreshValue=0;
//                movePlayer(joystick.getDirectionInput());
            }

        }
      //  }


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        map.getTiledMapRenderer().setView(camera);
        map.getTiledMapRenderer().render();

        batch.begin();

        myPlayerSprite.draw(batch);
        cursor.draw(batch);

        batch.end();

        if(display){
           joystick.render(shapeRenderer);
        }



    }

    private void displayJoystick() {
        if(Gdx.input.isTouched(0)){
            if(!display){
                if(!joystick.isPositionFixe()){
                    joystick.setPosition(Gdx.input.getX(),SCREEN_HEIGHT-Gdx.input.getY());
                }
            }
            display = true;
        }else{
            joystick.setDirectionInput(-1);
            display = false;
        }
        update();
    }

    @Override
    public boolean keyUp(int keycode) {
//        display =false;
//        System.out.println("KEYUP    " );
//        System.out.println("KEYUP    " );
//        System.out.println("KEYUP    " );
//        shapeRenderer.flush();
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        display =true;
        movePlayer(keycode);

        return false;
    }

    private void movePlayer(int keycode) {
        if (keycode == Input.Keys.LEFT) {
           myPlayer.checkSprite("LEFT");
           System.out.println("LEFT");
                myPlayer.setX(myPlayer.getX() - speed);
            _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());
            System.out.println("POSITION ====================== " + myPlayer.getX());
            if (myPlayer.getX() < SCREEN_WIDTH * 1.0 / 4.0) {
                if (camera.position.x < SCREEN_WIDTH * 1.0 / 4.0) {
                    if (myPlayer.getX() > 0) {
                        myPlayer.setX(myPlayer.getX() + speed);
                    }
                } else {
                    myPlayer.setX(myPlayer.getX() + speed);

                    camera.position.x -= speed;

                }
            }
        }
        if (keycode == Input.Keys.RIGHT) {
            myPlayer.checkSprite("RIGHT");
            System.out.println("RIGHT");
            myPlayer.setX(myPlayer.getX() + speed);
            _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());
            System.out.println("POSITION ====================== " + myPlayer.getX());
            if (myPlayer.getX() > SCREEN_WIDTH * 3.0 / 4.0) {
                if (camera.position.x > calculatedWidth - SCREEN_WIDTH * 1.0 / 4.0) {
                    if (myPlayer.getX() < SCREEN_WIDTH) {
                        myPlayer.setX(myPlayer.getX() - speed);
                    }
                } else {
                    myPlayer.setX(myPlayer.getX() - speed);
                    camera.position.x += speed;
                }
            }
        }
        if (keycode == Input.Keys.UP) {
            myPlayer.checkSprite("UP");
            System.out.println("UP");
            myPlayer.setY(myPlayer.getY() + speed);
            _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());
            System.out.println("POSITION ====================== " + myPlayer.getY());
            if (myPlayer.getY() > SCREEN_HEIGHT * 3.0 / 4.0) {
                if (camera.position.y > calculatedHeight - SCREEN_HEIGHT * 1.0 / 4.0) {
                    if (myPlayer.getY() < SCREEN_HEIGHT) {
                        myPlayer.setY(myPlayer.getY() - speed);
                    }
                } else {
                    myPlayer.setY(myPlayer.getY() - speed);
                    camera.position.y += speed;
                }
            }
        }
        if (keycode == Input.Keys.DOWN) {
            myPlayer.checkSprite("DOWN");
            System.out.println("DOWN");
            myPlayer.setY(myPlayer.getY() - speed);
            if (myPlayer.getY() < SCREEN_HEIGHT * 1.0 / 4.0) {
                if (camera.position.y < SCREEN_HEIGHT * 1.0 / 4.0) {
                    if (myPlayer.getY() > 0) {
                        myPlayer.setY(myPlayer.getY() + speed);
                        _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());
                        System.out.println("POSITION ====================== " + myPlayer.getY());
                    }
                } else {
                    myPlayer.setY(myPlayer.getY() + speed);
                    camera.position.y -= speed;
                }
            }

        }
        System.out.println();
        batch.begin();
        myPlayerSprite = myPlayer.getSprite();
        myPlayerSprite.draw(batch);
        batch.end();
      //
         camera.update();


 //         Vector3 tmp =  new Vector3();

//        camera.projection.setToOrtho(camera.zoom * -camera.viewportWidth / 2, camera.zoom * (camera.viewportWidth / 2), camera.zoom * -(camera.viewportHeight / 2),
//                camera.zoom * camera.viewportHeight / 2, camera.near, camera.far);
   //     camera.view.setToLookAt(camera.position, tmp.set(camera.position).add(camera.direction), camera.up);
//        camera.combined.set(camera.projection);
//        Matrix4.mul(camera.combined.val, camera.view.val);
//
// //       if (updateFrustum) {
//            camera.invProjectionView.set(camera.combined);
//            Matrix4.inv(camera.invProjectionView.val);
//        camera.frustum.update(camera.invProjectionView);
  //     }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        System.out.println("mouseMoved ");
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("scrolled ");
        return false;
    }

    int speedTouch = 20;


    @Override
    public void dispose(){
        batch.dispose();
        shapeRenderer.dispose();
    }


    public void update(){
        Vector3 vector = new Vector3();
        if(Gdx.input.isTouched(0)) {
            camera.unproject(vector.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            joystick.update(vector.x, vector.y);
        }
    }
}