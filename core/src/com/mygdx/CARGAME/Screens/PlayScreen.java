package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.mygdx.entity.ScreenshotFactory;

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
    private Array<RunningObject> listFreeObjects;

    private Array<Body> deadBodies;
    private float deltaTimer;
    private float generateTimer;
    private float generateTimer2;
    private ReentrantLock lock;
    private boolean gameOver=false;
    private Texture capturedLastFrame;
    private static boolean touchOne;
    private static boolean touchTwo;
    private boolean[] touch;

    private float renderTime=0;

    private Box2DDebugRenderer  box2DDebugRenderer;

    private Car blueCar;
    private Car redCar;

    public PlayScreen(CarGame carGame){
        touch=new boolean[20];
        atlas=new TextureAtlas("Car.pack");

        atlasObjects=new TextureAtlas("Object.pack");
        listObjects=new Array<RunningObject>();
        deadBodies=new Array<Body>();
        lock=new ReentrantLock();
        deltaTimer = 0.6f;
        generateTimer=0;
        generateTimer2 = 0;


        this.game=carGame;

        game_cam=new OrthographicCamera();
        game_port=new StretchViewport((game.WIDTH/game.PPM),(game.HEIGHT/game.PPM),game_cam);
        game_cam.position.set(game_port.getWorldWidth()/2,game_port.getWorldHeight()/2,0);


        hud=new Hud(game.batch);
        background=new Texture("background.jpg");
      //  System.out.println("WIDTH:"+background.getWidth()+ " HEIGHT:"+background.getHeight());

        world=new World(new Vector2(0,0),true);
        box2DDebugRenderer=new Box2DDebugRenderer();

        blueCar=new Car(world,this,true,"Car_Blue");
        redCar=new Car(world,this,false,"Car_Red");



        world.setContactListener(new WorldContactListener());


//        Pixmap pixmap = new Pixmap(Gdx.files.internal("background.jpg"));
//        Pixmap pixmap1 = new Pixmap((int) game_port.getWorldWidth(), (int) game_port.getWorldHeight(), pixmap.getFormat());
//        pixmap1.drawPixmap(pixmap,
//                0, 0, pixmap.getWidth(), pixmap.getHeight(),
//                0, 0, pixmap1.getWidth(), pixmap1.getHeight()
//        );
//        System.out.println("world height "+game_port.getWorldHeight()+" world width"+game_port.getWorldWidth()+"" +
//                "pix Width"+pixmap.getWidth()+" pix Height"+pixmap.getHeight()+" pix1 Width"+pixmap1.getWidth()+" pix1 Height"+pixmap1.getHeight());
//        background = new Texture(pixmap1);
//        pixmap.dispose();
//        pixmap1.dispose();

        initTouchStatus();
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

    public void initTouchStatus() {
        touchOne = true; // init true (car one on left lane)
        touchTwo = false; // init false (car two on the right lane)
        touch=new boolean[20];
    }

    void handleInput(){
//        for (int i=0;i<20;i++) {
//            if (Gdx.input.isTouched(i)) {
//                if (!touch[i]) {
//                    touch[i] = true;
//                    float x = Gdx.input.getX(i);
//                    float y = Gdx.input.getY(i);
//                    Vector3 mousePos = new Vector3(x, y, 0);
//                    game_cam.unproject(mousePos);
//                    x = mousePos.x;
//                    y = mousePos.y;
//                    if (x < CarGame.WIDTH / 2 / CarGame.PPM) {
//                        if (x > blueCar.b2body.getPosition().x) {
//                            blueCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
//                        } else if (x < blueCar.b2body.getPosition().x) {
//                            blueCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
//                        }
//                    } else {
//
//                        if (x > redCar.b2body.getPosition().x) {
//                            redCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
//                        } else if (x < redCar.b2body.getPosition().x) {
//                            redCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
//                        }
//                    }
//                }
//
//            } else {
//                touch[i] = false;
//            }
//        }

//        if (Gdx.input.isTouched(1)){
//
//            if (!touchTwo) {
//                touchTwo=false;
//                float x = Gdx.input.getX(1);
//                float y = Gdx.input.getY(1);
//                Vector3 mousePos = new Vector3(x, y, 0);
//                game_cam.unproject(mousePos);
//                x = mousePos.x;
//                y = mousePos.y;
//                if (x < CarGame.WIDTH / 2 / CarGame.PPM) {
//                    //       System.out.println("blue Car x Position "+blueCar.b2body.getPosition().x);
//                    if (blueCar.b2body.getLinearVelocity().x < 0) {
//                        blueCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
//                    } else if (blueCar.b2body.getLinearVelocity().x > 0) {
//                        blueCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
//                    }
//                } else {
//
//                    if (redCar.b2body.getLinearVelocity().x < 0) {
//                        redCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
//                    } else if (redCar.b2body.getLinearVelocity().x > 0) {
//                        redCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
//                    }
//                }
//            }
//
//        }
//        else{
//            touchTwo=false;
//        }
        for (int i=0;i<20;i++) {
            if (Gdx.input.isTouched(i)) {
                if (!touch[i]) {
                    touch[i] = true;
                    float x = Gdx.input.getX(i);
                    float y = Gdx.input.getY(i);
                    Vector3 mousePos = new Vector3(x, y, 0);
                    game_cam.unproject(mousePos);
                    x = mousePos.x;
                    y = mousePos.y;
                    if (x < CarGame.WIDTH / 2 / CarGame.PPM) {
                        if (touchOne) {
                            touchOne = false;
                            blueCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
                        } else {
                            blueCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
                            touchOne = true;
                        }
                    } else {
                        if (touchTwo) {
                            touchTwo = false;
                            redCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
                        } else {
                            touchTwo = true;
                            redCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
                        }
                    }
                }

            } else {
                touch[i] = false;
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
    boolean findObject(Class t,int left){
        for (RunningObject ob:listObjects)
            if (ob.getClass().isAssignableFrom(t)&&ob.fixture.getFilterData().categoryBits==CarGame.DESTROYED_BIT){
                ob.reset(left);
                return true;
            }
        return false;
    }
    void generateObjects(float delta){
        if (generateTimer>0.7f){
            generateTimer=0;
        }
        if (generateTimer2>0.7f + deltaTimer) {
            generateTimer2 = deltaTimer;
        }

        if (generateTimer==0f) {

            Random random = new Random();
            boolean circle=random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2);
                boolean good=findObject(CircleObject.class,left);
                if (!good) {
                    CircleObject co = new CircleObject(this, world, left, "circle");
                    listObjects.add(co);
                }
            }
            else{
                int left = random.nextInt(2);
                boolean good=findObject(RectangleObject.class,left);
                if (!good) {
                    RectangleObject co = new RectangleObject(this, world, left, "rectangle");
                    listObjects.add(co);
                 }
            }
        }

        if (generateTimer2>=deltaTimer - 0.01 && generateTimer2 < deltaTimer + 0.01) {

            Random random = new Random();
            boolean circle=random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2)+2;
                boolean good=findObject(CircleObject.class,left);
                if (!good) {
                    CircleObject co = new CircleObject(this, world, left, "circle_red");
                    listObjects.add(co);
                }

            }
            else{
                int left = random.nextInt(2)+2;
                boolean good=findObject(RectangleObject.class,left);
                if (!good) {
                    RectangleObject co = new RectangleObject(this, world, left, "rectangle_red");
                    listObjects.add(co);
                }
            }



        }
        generateTimer+=delta;
        generateTimer2+=delta;

        // generateTimer += 0.015;
      //  generateTimer2 += 0.015;
    }

    void update(float delta){

        handleInput();
        generateObjects(delta);
        world.step(1/60f,6,2);



        blueCar.update(delta);
        redCar.update(delta);

       // lock.lock();

        for (RunningObject co : listObjects)
        if (co.filter.categoryBits!=CarGame.DESTROYED_BIT){
            co.update(delta);
            if (co.body.getPosition().y < 0) {
                if (CircleObject.class.isAssignableFrom(co.getClass())){
                    setGameOver(true);
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.batch.setProjectionMatrix(game_cam.combined);
        game.batch.begin();

        game.batch.draw(background,0,0,game_port.getWorldWidth(),game_port.getWorldHeight());
        blueCar.draw(game.batch);
        redCar.draw(game.batch);

        for (RunningObject co : listObjects)
            co.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
//        box2DDebugRenderer.render(world,game_cam.combined);

        if (gameOver){
            initTouchStatus();//reset touch status
            getLastFrame();
            game.setScreen(new GameOverScreen(game, capturedLastFrame));
            dispose();
        }
    }

    public void getLastFrame() {
        ScreenshotFactory sf = new ScreenshotFactory();
        Pixmap pixmap = sf.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Pixmap pixmap1 = new Pixmap((int) game_port.getScreenWidth(), (int) game_port.getScreenHeight(), pixmap.getFormat());
        pixmap1.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, pixmap1.getWidth(), pixmap1.getHeight()
        );
        capturedLastFrame = new Texture(pixmap1);
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
