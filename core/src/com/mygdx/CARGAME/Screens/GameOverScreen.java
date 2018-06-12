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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.scenes.Hud;
import com.mygdx.entity.ScreenshotFactory;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Hud hud;
    private Texture capturedLastFrame;

    private CarGame game;
    private OrthographicCamera game_cam;

    public GameOverScreen(CarGame game, Texture lastFrame){
        this.game = game;
        this.capturedLastFrame = lastFrame;
        game_cam=new OrthographicCamera();
        viewport=new StretchViewport((game.WIDTH/game.PPM),(game.HEIGHT/game.PPM),game_cam);
        game_cam.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

        hud=new Hud(game.batch);


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

        hud.stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((CarGame) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game_cam.combined);
        game.batch.begin();
        game.batch.draw(capturedLastFrame,0,0);
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
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
        hud.dispose();
    }
}
