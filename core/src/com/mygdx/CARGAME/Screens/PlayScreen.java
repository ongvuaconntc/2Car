package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Sprite.Car;
import com.mygdx.CARGAME.Sprite.CircleObject;
import com.mygdx.CARGAME.Sprite.RectangleObject;
import com.mygdx.CARGAME.Sprite.RunningObject;
import com.mygdx.CARGAME.Tools.WorldContactListener;
import com.mygdx.CARGAME.scenes.Hud;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class PlayScreen implements Screen {
    private CarGame game;
    private OrthographicCamera game_cam;
    private Viewport game_port;
    private Hud hud;
    private World world;
    private TextureAtlas atlas;
    private TextureAtlas atlas_RED;
    private TextureAtlas atlasObjects;
    private Texture background;
    private Array<RunningObject> listObjects;
    private Array<Body> deadBodies;
    private float deltaTimer;
    private float generateTimer;
    private float generateTimer2;
    private ReentrantLock lock;
    private boolean gameOver=false;

    private Box2DDebugRenderer  box2DDebugRenderer;

    private Car blueCar;
    private Car redCar;

    public PlayScreen(CarGame carGame){
        atlas=new TextureAtlas("Car.pack");
        atlas_RED=new TextureAtlas("Car_RED.pack");

        atlasObjects=new TextureAtlas("Object.pack");
        listObjects=new Array<RunningObject>();
        deadBodies=new Array<Body>();
        lock=new ReentrantLock();
        deltaTimer = 0.6f;
        generateTimer=0;
        generateTimer2 = 0;


        this.game=carGame;

        game_cam=new OrthographicCamera();
        game_port=new StretchViewport(game.WIDTH,game.HEIGHT,game_cam);
        game_cam.position.set(game_port.getWorldWidth()/2,game_port.getWorldHeight()/2,0);


        hud=new Hud(game.batch);
        background=new Texture("background.jpg");
        System.out.println("WIDTH:"+background.getWidth()+ " HEIGHT:"+background.getHeight());

        world=new World(new Vector2(0,0),true);
        box2DDebugRenderer=new Box2DDebugRenderer();

        blueCar=new Car(world,this,true,"car_blue");
        redCar=new Car(world,this,false,"car_blue");



        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas getAtlasObjects() {
        return atlasObjects;
    }

    public TextureAtlas getAtlas_RED() {
        return atlas_RED;
    }

    @Override
    public void show() {

    }

    public void addBody(Body body){
        deadBodies.add(body);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    void handleInput(){

        if (Gdx.input.isTouched(0)){
            float x=Gdx.input.getX(0);
            float y=Gdx.input.getY(0);
            Vector3 mousePos=new Vector3(x,y,0);
            game_cam.unproject(mousePos);
            x=mousePos.x;
            y=mousePos.y;
            if (x<CarGame.WIDTH/2) {
                //       System.out.println("blue Car x Position "+blueCar.b2body.getPosition().x);
                if (x > blueCar.b2body.getPosition().x) {
                    blueCar.b2body.setLinearVelocity(300, 0);
                } else if (x < blueCar.b2body.getPosition().x) {
                    blueCar.b2body.setLinearVelocity(-300, 0);
                }
            }
            else{

                if (x > redCar.b2body.getPosition().x) {
                    redCar.b2body.setLinearVelocity(300, 0);
                } else if (x < redCar.b2body.getPosition().x) {
                    redCar.b2body.setLinearVelocity(-300, 0);
                }
            }

        }

        if (Gdx.input.isTouched(1)){
            float x=Gdx.input.getX(1);
            float y=Gdx.input.getY(1);
            Vector3 mousePos=new Vector3(x,y,0);
            game_cam.unproject(mousePos);
            x=mousePos.x;
            y=mousePos.y;
            if (x<CarGame.WIDTH/2) {
                //       System.out.println("blue Car x Position "+blueCar.b2body.getPosition().x);
                if (blueCar.b2body.getLinearVelocity().x<0) {
                    blueCar.b2body.setLinearVelocity(300, 0);
                } else if (blueCar.b2body.getLinearVelocity().x>0) {
                    blueCar.b2body.setLinearVelocity(-300, 0);
                }
            }
            else{

                if (redCar.b2body.getLinearVelocity().x<0) {
                    redCar.b2body.setLinearVelocity(300, 0);
                } else if (redCar.b2body.getLinearVelocity().x>0) {
                    redCar.b2body.setLinearVelocity(-300, 0);
                }
            }

        }


    }

    public Hud getHud() {
        return hud;
    }
    public void removeObject( RunningObject co){
        lock.lock();
        System.out.println("try remove object object");
        try{
            listObjects.removeValue(co,false);
        }
        finally {
            lock.unlock();
        }


    }
    void generateObjects(float delta){
        if (generateTimer>2f){
            generateTimer=0;
        }
        if (generateTimer2>2f + deltaTimer) {
            generateTimer2 = deltaTimer;
        }

        if (generateTimer==0f) {

            Random random = new Random();
            boolean circle=random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2);

                CircleObject co = new CircleObject(this, world, left,"circle");
                lock.lock();
                try {
                    listObjects.add(co);
                } finally {
                    lock.unlock();
                }
            }
            else{
                int left = random.nextInt(2);
                RectangleObject co = new RectangleObject(this, world, left,"rectangle");
                lock.lock();
                try {
                    listObjects.add(co);
                } finally {
                    lock.unlock();
                }
            }
        }

        if (generateTimer2>=deltaTimer - 0.01 && generateTimer2 < deltaTimer + 0.01) {

            Random random = new Random();
            boolean circle=random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2)+2;
                CircleObject co = new CircleObject(this, world, left,"circle_red");
                lock.lock();
                try {
                    listObjects.add(co);
                } finally {
                    lock.unlock();
                }
            }
            else{
                int left = random.nextInt(2)+2;
                RectangleObject co = new RectangleObject(this, world, left,"rectangle_red");
                lock.lock();
                try {
                    listObjects.add(co);
                } finally {
                    lock.unlock();
                }
            }



        }
//        generateTimer+=delta;
        generateTimer += 0.015;
        generateTimer2 += 0.015;
    }

    void update(float delta){
        handleInput();
        generateObjects(delta);
        world.step(1/60f,1,1);
        while (deadBodies.size>0){
            Body body=deadBodies.get(0);
            deadBodies.removeIndex(0);
            world.destroyBody(body);
            body.setUserData(null);
        }


        blueCar.update(delta);
        redCar.update(delta);

        lock.lock();
        try {
            for (RunningObject co : listObjects) {
                co.update(delta);
                if (co.body.getPosition().y < 0) {
                    listObjects.removeValue(co, false);
                    if (CircleObject.class.isAssignableFrom(co.getClass())){
                        setGameOver(true);
                    }
                }
            }
        }
        finally {
            lock.unlock();
        }
       System.out.println("list Circle size"+listObjects.size);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.batch.setProjectionMatrix(game_cam.combined);
        game.batch.begin();
        game.batch.draw(background,0,0);
        blueCar.draw(game.batch);
        redCar.draw(game.batch);
        lock.lock();
        try {
            for (RunningObject co : listObjects) {
                co.draw(game.batch);
            }
        }
        finally {
            lock.unlock();
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        box2DDebugRenderer.render(world,game_cam.combined);

        if (gameOver){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose() {
        hud.dispose();
        background.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();


    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        game_port.update(width,height);
    }
}
