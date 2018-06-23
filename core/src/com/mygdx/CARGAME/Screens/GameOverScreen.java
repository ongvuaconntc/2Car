package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.scenes.Hud;
import com.mygdx.entity.ScreenshotFactory;

public class GameOverScreen implements Screen {
    private Hud hud;
    private Viewport viewport;
    private Texture capturedLastFrame;
    private Texture replayTexture;
    private ImageButton replayButton;

    private CarGame game;
    private OrthographicCamera game_cam;

    public GameOverScreen(CarGame game, Texture lastFrame){
        this.game = game;
        this.capturedLastFrame = lastFrame;
        game_cam=new OrthographicCamera();
        viewport=new StretchViewport((game.WIDTH/game.PPM),(game.HEIGHT/game.PPM),game_cam);
        game_cam.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

        hud=new Hud(game.batch);
        Gdx.input.setInputProcessor(hud.stage);

        BitmapFont f = new BitmapFont();
        f.getData().setScale(2f);
        Label.LabelStyle font = new Label.LabelStyle(f, Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Tap to Play Again", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        replayTexture = new Texture("replay.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(replayTexture));
        replayButton = new ImageButton(drawable);
        replayButton.setSize(game.WIDTH/3, game.HEIGHT/2 - game.WIDTH/3);
        replayButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                btnReplayClick();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down");
                return true;
            }
        });
        replayButton.setPosition(hud.stage.getWidth() / 2 - replayButton.getWidth() / 2 , hud.stage.getHeight() / 2 - replayButton.getHeight() / 2);
        hud.stage.addActor(replayButton);
//        hud.stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game_cam.combined);
        game.batch.begin();

        Color c = game.batch.getColor();
        game.batch.setColor(c.r, c.g, c.b, 0.6f);
        game.batch.draw(capturedLastFrame,0,0,viewport.getWorldWidth(),viewport.getWorldHeight());
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void btnReplayClick() {
        game.setScreen(new PlayScreen((CarGame) game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
