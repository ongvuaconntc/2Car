package com.mygdx.CARGAME.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Tools.SmartFontGenerator;
import com.mygdx.CARGAME.scenes.Hud;

public class GameOverScreen implements Screen {
    private Hud hud;
    private Viewport viewport;
    private Texture capturedLastFrame;

    private CarGame game;
    private OrthographicCamera game_cam;

    public GameOverScreen(CarGame game, Texture lastFrame, int score) {
        this.game = game;
        this.capturedLastFrame = lastFrame;
        game_cam = new OrthographicCamera();
        viewport = new StretchViewport((game.WIDTH / game.PPM), (game.HEIGHT / game.PPM), game_cam);
        game_cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        hud = new Hud(game.batch);
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

        Texture replayTexture = new Texture("replay.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(replayTexture));
        ImageButton replayButton = new ImageButton(drawable);
        replayButton.setSize(game.WIDTH / 3, game.WIDTH / 3);
        replayButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnReplayClick();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down");
                return true;
            }
        });
        replayButton.setPosition(hud.stage.getWidth() / 2 - replayButton.getWidth() / 2, hud.stage.getHeight() / 2 - 4 * replayButton.getHeight() / 3);
        hud.stage.addActor(replayButton);

        SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        BitmapFont fontSmall = fontGen.createFont(exoFile, "exo-small", 30);
        BitmapFont fontMedium = fontGen.createFont(exoFile, "exo-medium", 48);
        BitmapFont fontLarge = fontGen.createFont(exoFile, "exo-large", 64);


        Label.LabelStyle smallStyle = new Label.LabelStyle();
        smallStyle.font = fontSmall;
        Label.LabelStyle mediumStyle = new Label.LabelStyle();
        mediumStyle.font = fontMedium;
        Label.LabelStyle largeStyle = new Label.LabelStyle();
        largeStyle.font = fontLarge;

        Label lbScore = new Label("SCORE", smallStyle);
        Label lbScoreValue = new Label("" + score, smallStyle);
        Label lbBest = new Label("BEST", smallStyle);
        Label lbBestValue = new Label("5", smallStyle);
        Label lbGameover = new Label("GAME OVER", mediumStyle);

        Label large = new Label("Large Font", largeStyle);

        Table tablez = new Table();
        tablez.setFillParent(true);
        tablez.add(lbGameover).colspan(2).spaceBottom(20).row();
        tablez.add(lbScore).spaceBottom(10);
        tablez.add(lbScoreValue).spaceBottom(10);
        tablez.row();
        tablez.add(lbBest);
        tablez.add(lbBestValue);
        tablez.row();
//        table.align(Align.center);

        tablez.defaults().size(hud.stage.getWidth(), hud.stage.getHeight() / 6);
        tablez.setPosition(0, hud.stage.getHeight() / 6);

//        tablez.add(large).row();

        hud.stage.addActor(tablez);
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
        game.batch.setColor(c.r, c.g, c.b, 0.4f);
        game.batch.draw(capturedLastFrame, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private void btnReplayClick() {
        PlayScreen p = new PlayScreen(game);
        p.setState(PlayScreen.State.RUN);
        p.reset();
        game.setScreen(p);
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
