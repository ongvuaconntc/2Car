package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Sprite.Car;
import com.mygdx.CARGAME.Sprite.CircleObject;
import com.mygdx.CARGAME.Sprite.RectangleObject;
import com.mygdx.CARGAME.Sprite.RunningObject;
import com.mygdx.CARGAME.Tools.SmartFontGenerator;
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
    private Texture background3d;
    private Array<RunningObject> listObjects;
    private Array<RunningObject> listFreeObjects;
    private static Label scoreLabel;
    private static int score;
    private ImageButton pauseButton;

    private Array<Body> deadBodies;
    private float deltaTimer;
    private float generateTimer;
    private float generateTimer2;
    private ReentrantLock lock;
    private boolean gameOver = false;
    private Texture capturedFrame;
    private static boolean touchOne;
    private static boolean touchTwo;
    private boolean[] touch;
    public static boolean runningState = true;
    private int lastScore=0;
    private int increaseBase=1;
    public enum State {
        START,
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

    private boolean renderStatus=true;

    private State state = State.START;

    private float renderTime = 0;

    private Box2DDebugRenderer box2DDebugRenderer;

    private Car blueCar;
    private Car redCar;

    //3d variables
    public PerspectiveCamera cam_3d;
    public ModelBuilder modelBuilder;
    public Model model;
    public Model modelred;
    public Model wall;

    public ModelInstance instance_blue;
    public ModelInstance instance_red;
    public ModelInstance instance_wall;
    private AnimationController controller;
    private AnimationController controllerred;
    private AnimationController controller_rolling;
    private AnimationController controller_rolling_red;
    boolean turning_blue=false;
    boolean turning_red=false;




    public PlayScreen(CarGame carGame) {
        touch = new boolean[20];
        atlas = new TextureAtlas("Car.pack");
        atlasObjects = new TextureAtlas("Object.pack");
        listObjects = new Array<RunningObject>();
        deadBodies = new Array<Body>();
        lock = new ReentrantLock();
        deltaTimer = 0.6f;
        generateTimer = 0;
        generateTimer2 = 0;


        this.game = carGame;

        game_cam = new OrthographicCamera();
        game_port = new StretchViewport((game.WIDTH / game.PPM), (game.HEIGHT / game.PPM), game_cam);
        game_cam.position.set(game_port.getWorldWidth() / 2, game_port.getWorldHeight() / 2, 0);


        hud = new Hud(game.batch);
        background = new Texture("background.jpg");
        background3d = new Texture("3d_background.jpg");
        //  System.out.println("WIDTH:"+background.getWidth()+ " HEIGHT:"+background.getHeight());

        world = new World(new Vector2(0, 0), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        blueCar = new Car(world, this, true, "Car_Blue");
        redCar = new Car(world, this, false, "Car_Red");


        world.setContactListener(new WorldContactListener());
        initTouchStatus();

        long tStart = System.currentTimeMillis();
        if (CarGame.ENABLE_3D) init3D();
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("elapsed second time: " + elapsedSeconds);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                float x = Gdx.input.getX(pointer);
                float y = Gdx.input.getY(pointer);
                Vector3 mousePos = new Vector3(x, y, 0);
                game_cam.unproject(mousePos);
                x = mousePos.x;
                y = mousePos.y;
                float btnX = pauseButton.getX() / game.PPM;
                float btnWidth = pauseButton.getWidth() / game.PPM;
                float btnY = pauseButton.getY() / game.PPM;
                float btnHeight = pauseButton.getHeight() / game.PPM;
                if (x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight) {
                    btnPauseClick();
                    return true;
                }
                if (!runningState) return true;
                if (x < CarGame.WIDTH / 2 / CarGame.PPM) {
                    if (touchOne) {
                        touchOne = false;
                        blueCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
                        if (runningState&&CarGame.ENABLE_3D) {
                            controller.current = null;
                            turning_blue=true;
                            controller.setAnimation("Scene", 0.f, 1, 1, 1.7f, new AnimationController.AnimationListener() {
                                @Override
                                public void onEnd(AnimationController.AnimationDesc animation) {
                                    turning_blue=false;
                                }

                                @Override
                                public void onLoop(AnimationController.AnimationDesc animation) {

                                }
                            });
                        }
                    } else {
                        blueCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
                        if (runningState&&CarGame.ENABLE_3D) {
                            controller.current = null;
                            turning_blue=true;
                            controller.setAnimation("Scene", 2f, 1, 1, 1.7f,  new AnimationController.AnimationListener() {
                                @Override
                                public void onEnd(AnimationController.AnimationDesc animation) {
                                    turning_blue=false;
                                }

                                @Override
                                public void onLoop(AnimationController.AnimationDesc animation) {

                                }
                            });
                        }
                        touchOne = true;
                    }
                } else
                if (x > CarGame.WIDTH / 2 / CarGame.PPM){
                    if (touchTwo) {
                        touchTwo = false;
                        redCar.b2body.setLinearVelocity(CarGame.CAR_VELOCITY, 0);
                        if (runningState&&CarGame.ENABLE_3D) {
                            controllerred.current = null;
                            turning_red=true;
                            controllerred.setAnimation("Scene", 0.f, 1, 1, 1.7f,  new AnimationController.AnimationListener() {
                                @Override
                                public void onEnd(AnimationController.AnimationDesc animation) {
                                    turning_red=false;
                                }

                                @Override
                                public void onLoop(AnimationController.AnimationDesc animation) {

                                }
                            });
                        }
                    } else {
                        touchTwo = true;
                        redCar.b2body.setLinearVelocity(-CarGame.CAR_VELOCITY, 0);
                        if (runningState&&CarGame.ENABLE_3D) {
                            controllerred.current = null;
                            turning_red=true;
                            controllerred.setAnimation("Scene", 2f, 1, 1, 1.7f,  new AnimationController.AnimationListener() {
                                @Override
                                public void onEnd(AnimationController.AnimationDesc animation) {
                                    turning_red=false;
                                }

                                @Override
                                public void onLoop(AnimationController.AnimationDesc animation) {

                                }
                            });
                        }
                    }
                }

                return true;
            }
        });
        initDrawScore(hud.stage);
        initDrawPauseBtn(hud.stage);
        runningState=true;


    }
    public void reset(){
        deltaTimer = 0.6f;
        generateTimer = 0;
        generateTimer2 = 0;
        lastScore=0;
        runningState=true;
        game.OBJECT_VELOCITY=6;
        increaseBase=1;
    }

    public void init3D(){
        System.out.println("init 3d");
        cam_3d = new PerspectiveCamera(100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam_3d.position.set(CarGame.WIDTH/CarGame.PPM/2f, -2f, 4f);
        cam_3d.lookAt(CarGame.WIDTH/CarGame.PPM/2f,10f,0.5f);
        cam_3d.near = 0.5f;
        cam_3d.far = 1000f;
        cam_3d.update();

        modelBuilder = new ModelBuilder();
//        model = modelBuilder.createBox(0.5f, 0.5f, 2f,
//                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//        instance_blue = new ModelInstance(model);
//        instance_blue.transform.setToTranslation(blueCar.b2body.getPosition().x,blueCar.b2body.getPosition().y,0f);

        // Model loader needs a binary json reader to decode
        UBJsonReader jsonReader = new UBJsonReader();
        // Create a model loader passing in our json reader
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        model = modelLoader.loadModel(Gdx.files.getFileHandle("object3d/carsample.g3db", Files.FileType.Internal));
        model.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLUE));
        model.materials.get(2).set(ColorAttribute.createDiffuse(Color.valueOf("#4c5159")));
        model.materials.get(1).set(ColorAttribute.createDiffuse(Color.valueOf("#7585ff")));
        model.materials.get(3).set(ColorAttribute.createDiffuse(Color.valueOf("#4c5159")));


        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        instance_blue = new ModelInstance(model);
        instance_blue.transform.setToTranslation(blueCar.b2body.getPosition().x,blueCar.b2body.getPosition().y,0f);
        for (Animation animation:instance_blue.animations){
            System.out.println("ID:"+animation.id);
        }

//        modelred = modelBuilder.createBox(0.5f, 0.5f, 0.5f,
//                new Material(ColorAttribute.createDiffuse(Color.RED)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//        instance_red = new ModelInstance(modelred);
//        instance_red.transform.setToTranslation(redCar.b2body.getPosition().x,redCar.b2body.getPosition().y,0f);

        // Model loader needs a binary json reader to decode
      //  UBJsonReader jsonReader2 = new UBJsonReader();
        // Create a model loader passing in our json reader
       // G3dModelLoader modelLoader2 = new G3dModelLoader(jsonReader2);
        // Now load the model by name
        // Note, the model (g3db file ) and textures need to be added to the assets folder of the Android proj
        modelred = modelLoader.loadModel(Gdx.files.getFileHandle("object3d/carsample.g3db", Files.FileType.Internal));
        modelred.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
        modelred.materials.get(2).set(ColorAttribute.createDiffuse(Color.valueOf("#4c5159")));
        modelred.materials.get(1).set(ColorAttribute.createDiffuse(Color.valueOf("#ffba75")));
        modelred.materials.get(3).set(ColorAttribute.createDiffuse(Color.valueOf("#4c5159")));


        // Now create an instance.  Instance holds the positioning data, etc of an instance of your model
        instance_red = new ModelInstance(modelred);
        instance_red.transform.setToTranslation(redCar.b2body.getPosition().x,redCar.b2body.getPosition().y,0f);

        for (Animation animation:instance_blue.animations){
            System.out.println("ID red:"+animation.id);
        }
        //background
        Texture texture = new Texture("background1.jpg");
        Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        wall = modelBuilder.createBox(CarGame.WIDTH/CarGame.PPM, 20*CarGame.HEIGHT/CarGame.PPM, 0.1f, material, attributes);
        instance_wall=new ModelInstance(wall);
        instance_wall.transform.setToTranslation(CarGame.WIDTH/CarGame.PPM/2f,7f,-1f);


        //load animation
        controller = new AnimationController(instance_blue);
        controllerred= new AnimationController(instance_red);
        controller_rolling= new AnimationController(instance_blue);
        controller_rolling_red= new AnimationController(instance_red);

        controllerred.current = null;
        controllerred.setAnimation("Scene", 2f, 1, 1, 100, null);
        controller.current = null;
        controller.setAnimation("Scene", 0f, 1, 1, 100, null);

        controller_rolling.current = null;
        controller_rolling.setAnimation("Scene", 4.1f, 0.85f, -1, 2, null);
        controller_rolling_red.current = null;
        controller_rolling_red.setAnimation("Scene", 4.1f, 0.85f, -1, 2, null);




//        controller.setAnimation("Scene", 1, new AnimationController.AnimationListener() {
//            @Override
//            public void onEnd(AnimationController.AnimationDesc animation) {
//            }
//
//            @Override
//            public void onLoop(AnimationController.AnimationDesc animation) {
//                Gdx.app.log("INFO","Animation Ended");
//            }
//        });

    }

    public void render3D(){
        instance_blue.transform.setToTranslation(blueCar.b2body.getPosition().x,blueCar.b2body.getPosition().y,0f);
        instance_red.transform.setToTranslation(redCar.b2body.getPosition().x,redCar.b2body.getPosition().y,0f);

        for (RunningObject ob:listObjects)
            if (ob.fixture.getFilterData().categoryBits != CarGame.DESTROYED_BIT)
            ob.instance.transform.setToTranslation(ob.body.getPosition().x,ob.body.getPosition().y,0f);

        controller.update(Gdx.graphics.getDeltaTime());
        controllerred.update(Gdx.graphics.getDeltaTime());

        if (!turning_blue)
        controller_rolling.update(Gdx.graphics.getDeltaTime());
        if (!turning_red)
        controller_rolling_red.update(Gdx.graphics.getDeltaTime());


        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        game.batch.setProjectionMatrix(game_cam.combined);
        game.batch.begin();
        game.batch.draw(background3d, 0, 0, game_port.getWorldWidth(), game_port.getWorldHeight());
        game.batch.end();

        game.modelBatch.begin(cam_3d);
        game.modelBatch.render(instance_wall,game.environment);
        game.modelBatch.render(instance_blue,game.environment);
        game.modelBatch.render(instance_red,game.environment);
        for (RunningObject ob : listObjects)
            if (ob.fixture.getFilterData().categoryBits != CarGame.DESTROYED_BIT)
            game.modelBatch.render(ob.instance,game.environment);

        game.modelBatch.end();
    }

    public void dispose3D(){
        for (RunningObject ob:listObjects)
            ob.model.dispose();
        model.dispose();
        modelred.dispose();
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

    public void addBody(Body body) {
        deadBodies.add(body);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private void initTouchStatus() {
        touchOne = true; // init true (car one on left lane)
        touchTwo = false; // init false (car two on the right lane)
        touch = new boolean[20];
    }

    public Hud getHud() {
        return hud;
    }

    public void removeObject(RunningObject co) {
        lock.lock();
        System.out.println("try remove object object");
        try {
            listObjects.removeValue(co, false);
        } finally {
            lock.unlock();
        }


    }

    private boolean findObject(Class t, int left) {
        for (RunningObject ob : listObjects)
            if (ob.getClass().isAssignableFrom(t) && ob.fixture.getFilterData().categoryBits == CarGame.DESTROYED_BIT) {
                ob.reset(left);
                return true;
            }
        return false;
    }

    private void updateVelocity(){
        for (RunningObject ob:listObjects){
            if (ob.fixture.getFilterData().categoryBits != CarGame.DESTROYED_BIT) {
                ob.body.setLinearVelocity(0, -CarGame.OBJECT_VELOCITY);
          //      if (CarGame.ENABLE_3D)
          //      ob.instance.transform.trn(ob.body.getLinearVelocity().x,ob.body.getLinearVelocity().y,0f);
            }
        }
    }

    private void generateObjects(float delta) {
        if (!runningState) return;
        if (generateTimer > 0.7f) {
            generateTimer = 0;
        }
        if (generateTimer2 > 0.7f + deltaTimer) {
            generateTimer2 = deltaTimer;
        }

        if (generateTimer == 0f) {

            Random random = new Random();
            boolean circle = random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2);
                boolean good = findObject(CircleObject.class, left);
                if (!good) {
                    CircleObject co = new CircleObject(this, world, left, "circle");
                    listObjects.add(co);
                }
            } else {
                int left = random.nextInt(2);
                boolean good = findObject(RectangleObject.class, left);
                if (!good) {
                    RectangleObject co = new RectangleObject(this, world, left, "rectangle");
                    listObjects.add(co);
                }
            }
        }

        if (generateTimer2 >= deltaTimer - 0.01 && generateTimer2 < deltaTimer + 0.01) {

            Random random = new Random();
            boolean circle = random.nextBoolean();
            if (circle) {
                int left = random.nextInt(2) + 2;
                boolean good = findObject(CircleObject.class, left);
                if (!good) {
                    CircleObject co = new CircleObject(this, world, left, "circle_red");
                    listObjects.add(co);
                }

            } else {
                int left = random.nextInt(2) + 2;
                boolean good = findObject(RectangleObject.class, left);
                if (!good) {
                    RectangleObject co = new RectangleObject(this, world, left, "rectangle_red");
                    listObjects.add(co);
                }
            }


        }
//        generateTimer+=delta;
//        generateTimer2+=delta;
        generateTimer += 0.02;
        generateTimer2 += 0.02;
    }

    private void update(float delta) {
        if (runningState)
        world.step(1 / 60f, 6, 2);
        else {
            world.step(1 / 100000f, 6, 2);
            delta=delta/100000f;
        }
        generateObjects(delta);
        updateScore();



        blueCar.update(delta);
        redCar.update(delta);

        // lock.lock();

        for (RunningObject co : listObjects)
            if (co.filter.categoryBits != CarGame.DESTROYED_BIT) {
                co.update(delta);
                if (co.body.getPosition().y < 70/CarGame.PPM) {
                    if (CircleObject.class.isAssignableFrom(co.getClass())) {
                        if (CarGame.ENABLE_3D) co.instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#84ff3d")));
                        setGameOver(true);
                    }
                }
            }
    }

    @Override
    public void render(float delta) {

        if (lastScore==Hud.getScore()-increaseBase){
            CarGame.OBJECT_VELOCITY+=1;
            lastScore=Hud.getScore();
            if (increaseBase<30)
            increaseBase=increaseBase*2;
            updateVelocity();
        }
        switch (state) {
            case START:
                update(delta);
                if (!CarGame.ENABLE_3D) {
                    Gdx.gl.glClearColor(0, 0, 0, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


                    game.batch.setProjectionMatrix(game_cam.combined);
                    game.batch.begin();

                    game.batch.draw(background, 0, 0, game_port.getWorldWidth(), game_port.getWorldHeight());
                    blueCar.draw(game.batch);
                    redCar.draw(game.batch);
                    game.batch.end();
                }
                else render3D();


                game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

                if (!CarGame.ENABLE_3D)box2DDebugRenderer.render(world,game_cam.combined);
                hud.stage.draw();
                getFrame();
                game.setScreen(new StartScreen(game, capturedFrame));
                dispose();
                break;
            case RUN:
                update(delta);
                if (!CarGame.ENABLE_3D) {
                    Gdx.gl.glClearColor(0, 0, 0, 1);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                    game.batch.setProjectionMatrix(game_cam.combined);
                    game.batch.begin();

                    game.batch.draw(background, 0, 0, game_port.getWorldWidth(), game_port.getWorldHeight());
                    blueCar.draw(game.batch);
                    redCar.draw(game.batch);

                    for (RunningObject co : listObjects)
                        co.draw(game.batch);

                    game.batch.end();
                }
                else render3D();

                game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
                if (!CarGame.ENABLE_3D)   box2DDebugRenderer.render(world,game_cam.combined);
                hud.stage.draw();
                if (gameOver) {
                    initTouchStatus();//reset touch status
                    getFrame();
                    game.setScreen(new GameOverScreen(game, capturedFrame, score));
                    dispose();
                }
                break;
            case PAUSE:
                break;
            case RESUME:
                break;
            default:
                break;
        }



    }

    private void getFrame() {
        ScreenshotFactory sf = new ScreenshotFactory();
        Pixmap pixmap = sf.getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Pixmap pixmap1 = new Pixmap(game_port.getScreenWidth(), game_port.getScreenHeight(), pixmap.getFormat());
        pixmap1.drawPixmap(pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, pixmap1.getWidth(), pixmap1.getHeight()
        );
        capturedFrame = new Texture(pixmap1);
    }

    @Override
    public void dispose() {
        hud.dispose();
        background.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        if (CarGame.ENABLE_3D)
        dispose3D();
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        this.state = State.PAUSE;

    }

    @Override
    public void resume() {
        this.state = State.RESUME;

    }

    @Override
    public void resize(int width, int height) {
        game_port.update(width, height);
    }

    private void initDrawScore(Stage stage) {
        Table tableScore = new Table();
        tableScore.top();
        tableScore.right();
        tableScore.setFillParent(true);
        SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        BitmapFont fontSmall = fontGen.createFont(exoFile, "exo-medium", 36);
        Label.LabelStyle smallStyle = new Label.LabelStyle();
        smallStyle.font = fontSmall;

        scoreLabel = new Label(String.format("%d", hud.score), new Label.LabelStyle(fontSmall, Color.WHITE));
        tableScore.add(scoreLabel).padRight(10).padTop(5);
        stage.addActor(tableScore);
    }

    private void initDrawPauseBtn(Stage stage) {
        Texture pauseTexture = new Texture("pauseBtn.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(pauseTexture));
        pauseButton = new ImageButton(drawable);
        pauseButton.setSize(game.WIDTH / 10, game.WIDTH / 10);
        pauseButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnPauseClick();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down");
                return true;
            }
        });
        pauseButton.setPosition(10, hud.stage.getHeight() - pauseButton.getHeight() - 10);
        stage.addActor(pauseButton);
    }

    private void btnPauseClick() {
        System.out.println("btnPauseClick");
        if (runningState) {

            Gdx.graphics.setContinuousRendering(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gdx.graphics.requestRendering();
                    System.out.println("set Continuous Rendering false in thread");
                }
            }).start();
            System.out.println("set Continuous Rendering false");
        } else {

            Gdx.graphics.setContinuousRendering(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Gdx.graphics.requestRendering();
                    System.out.println("set Continuous Rendering true in thread");
                }
            }).start();
            System.out.println("set Continuous Rendering true");
        }
        runningState = !runningState;

    }


    private void updateScore() {
        score = hud.score;
        scoreLabel.setText(String.format("%d", score));
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        PlayScreen.score = score;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
