package com.mygdx.CARGAME;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.mygdx.CARGAME.Screens.MenuScreen;
import com.mygdx.CARGAME.Screens.PlayScreen;

public class CarGame extends Game {
	public SpriteBatch batch;
	public static final float PPM=45;
	public static final int WIDTH=360;
    public static final int HEIGHT=640;
    public static final int OBJECT_SIZE=32;
	public static final int CAR_SIZE=64;

	public static final int CAR_VELOCITY=7;
	public static int OBJECT_VELOCITY=7;


	public static final short CAR_BIT=1;
    public static final short CIRCLE_BIT=2;
    public static final short RECTANGLE_BIT=4;
    public static final short GROUND_BIT=8;
	public static final short DESTROYED_BIT=16;
	public static boolean isLoopingMusic = true;
	public static float volumnInitBackground = 0.5f;
	public static float volumnInitScore = 0.5f;
	public static float volumnInitDie = 0.5f;
	public static Music backgroundMusic;
	public static Sound scoreMusic;
	public static Sound dieOneMusic;
	public static Sound dieTwoMusic;
	public static Model modelBlue;
    public static Model modelRed;
    public static Model wall;

	public static boolean ENABLE_3D=true;

	//3d variables
	public ModelBatch modelBatch;
	public ModelCache cache;
	public Environment environment;


	@Override
	public void create () {
		batch = new SpriteBatch();
		//if (ENABLE_3D) {
			modelBatch = new ModelBatch();
			cache = new ModelCache();
			environment = new Environment();
			environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 0.5f));
			environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, 0f, -1f));
		//}
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background-music.mp3"));
        scoreMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/score.mp3"));
        dieOneMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/die1.mp3"));
        dieTwoMusic  = Gdx.audio.newSound(Gdx.files.internal("sounds/die2.mp3"));
		setScreen(new MenuScreen(this, null));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		if (ENABLE_3D)
		modelBatch.dispose();
	}
}
