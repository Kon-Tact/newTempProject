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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.entitygestion.Player;

import java.util.Iterator;


public class Game extends ApplicationAdapter implements InputProcessor {

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
        map.setTiledMap( new TmxMapLoader().load("sampleMap.tmx"));
        map.setTiledMapRenderer(new OrthogonalTiledMapRenderer(map.getTiledMap()));
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
        _FBIC.readDocumentsFromDB();

        detectInput();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.getTiledMapRenderer().setView(camera);
        map.getTiledMapRenderer().render();

        batch.begin();

        if (!lockOnListReadFromDB) {
            lockOnListReadFromDB = true;
            for (Player pl : CoreInterfaceClass.allPlayers) {
//                System.out.println("============ " + pl.uniqueID + " ■ " + pl.getX() + "/" + pl.getY());
                if (!pl.uniqueID.equals(myPlayer.uniqueID)) {
                    pl.sprite.draw(batch);
                }
            }
            lockOnListReadFromDB = false;
        }
        myPlayerSprite.draw(batch);
        cursor.draw(batch);

        batch.end();
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            myPlayer.checkSprite("LEFT");
            System.out.println("LEFT");
            myPlayer.setX(myPlayer.getX() - speed);
            _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());
            System.out.println("POSITION ====================== " + myPlayer.getX());
            if (myPlayer.getX() < SCREEN_WIDTH * 1 / 4) {
                if (camera.position.x < SCREEN_WIDTH * 1 / 4) {
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
            if (myPlayer.getX() > SCREEN_WIDTH * 3 / 4) {
                if (camera.position.x > calculatedWidth - SCREEN_WIDTH * 1 / 4) {
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
            if (myPlayer.getY() > SCREEN_HEIGHT * 3 / 4) {
                if (camera.position.y > calculatedHeight - SCREEN_HEIGHT * 1 / 4) {
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
            if (myPlayer.getY() < SCREEN_HEIGHT * 1 / 4) {
                if (camera.position.y < SCREEN_HEIGHT * 1 / 4) {
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
        camera.update();
        return false;
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

    private void detectInput() {
        //touch & mouse
        if (Gdx.input.isTouched()) {

            // get screen position
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            cursor.setPosition((int) xTouchPixels, SCREEN_HEIGHT - (int) yTouchPixels);

            //convert to world position
            Vector2 touchPoint = new Vector2(xTouchPixels, SCREEN_HEIGHT - yTouchPixels);
            //touchPoint = viewport.unproject(touchPoint);

            //calculate x + y differences
            float touchDistance = touchPoint.dst(new Vector2(myPlayerSprite.getX(), myPlayerSprite.getY()));

//            System.out.println(touchDistance + " ■ detectInput ■ " + xTouchPixels + " / " + yTouchPixels);

            if (touchDistance > 20f) {

                float xTouchDifference = touchPoint.x - myPlayerSprite.getX();
                float yTouchDifference = touchPoint.y - myPlayerSprite.getY();

                // scale to max speed
                float xMove = xTouchDifference / touchDistance * speedTouch; //* deltaTime;
                float yMove = yTouchDifference / touchDistance * speedTouch; // * deltaTime;

//                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
//                else xMove = Math.max(xMove, leftLimit);
//
//                if (yMove > 0) yMove = Math.min(yMove, upLimit);
//                else yMove = Math.max(yMove, downLimit);

                myPlayerSprite.translate(xMove, yMove);
                myPlayer.setX(myPlayerSprite.getX());
                myPlayer.setY(myPlayerSprite.getY());
                _FBIC.sendToDB(myPlayer.getX(), myPlayer.getY());

            }
        }

    }
}