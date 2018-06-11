package com.mygdx.CARGAME;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.CARGAME.Screens.PlayScreen;

public class CarGame extends Game {
	public SpriteBatch batch;
	public static final int WIDTH=360;
    public static final int HEIGHT=640;
    public static final int OBJECT_SIZE=32;
	public static final int CAR_SIZE=64;

	public static final int CAR_VELOCITY=600;
	public static final int OBJECT_VELOCITY=300;


	public static final short CAR_BIT=1;
    public static final short CIRCLE_BIT=2;
    public static final short RECTANGLE_BIT=4;
    public static final short GROUND_BIT=8;
	public static final short DESTROYED_BIT=16;





	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}
