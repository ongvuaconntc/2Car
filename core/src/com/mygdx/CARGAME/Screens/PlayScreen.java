package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
    private static Label scoreLabel;
    private Table tableScore;

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
        initTouchStatus();

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                   // touch[pointer] = true;
                    float x = Gdx.input.getX(pointer);
                    float y = Gdx.input.getY(pointer);
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
                return true;
            }
        });
        initDrawScore(hud.stage);
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
//        generateTimer+=delta;
//        generateTimer2+=delta;

         generateTimer += 0.015;
         generateTimer2 += 0.015;
    }

    void update(float delta){
        generateObjects(delta);
        updateScore();
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
//        box2DDebugRenderer.render(world,game_cam.combined);

        if (gameOver){
            initTouchStatus();//reset touch status
            getLastFrame();
            game.setScreen(new GameOverScreen(game, capturedLastFrame));
            dispose();
        }
        hud.stage.draw();
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

    public void initDrawScore(Stage stage){
        tableScore =new Table();
        tableScore.top();
        tableScore.right();
        tableScore.setFillParent(true);
        scoreLabel=new Label(String.format("%d",hud.getScore()),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        tableScore.add(scoreLabel).padRight(10);
        stage.addActor(tableScore);
    }


    public void updateScore() {
        scoreLabel.setText(String.format("%d",hud.getScore()));
    }

    public void removeActorTableScore() {

    }
}
