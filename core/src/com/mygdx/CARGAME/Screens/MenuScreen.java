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

public class MenuScreen implements Screen {
    private Hud hud;
    private Viewport viewport;
    private Texture capturedFrame;

    private CarGame game;
    private OrthographicCamera game_cam;

    public MenuScreen(CarGame game, Texture frame) {
        this.game = game;
        this.capturedFrame = (frame == null) ? new Texture("menu-bg.jpg") : frame;
        game_cam = new OrthographicCamera();
        viewport = new StretchViewport((game.WIDTH / game.PPM), (game.HEIGHT / game.PPM), game_cam);
        game_cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        hud = new Hud(game.batch);
        Gdx.input.setInputProcessor(hud.stage);

        Texture playTexture = new Texture("2d.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(playTexture));
        ImageButton playButton = new ImageButton(drawable);
        playButton.setSize(game.WIDTH / 3, game.WIDTH / 3);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnStartClick();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down");
                return true;
            }
        });
        playButton.setPosition(hud.stage.getWidth() / 2 - playButton.getWidth() / 2, hud.stage.getHeight() / 2 - 4 * playButton.getHeight() / 5);

        Texture playTexture2 = new Texture("3d.png");
        Drawable drawable2 = new TextureRegionDrawable(new TextureRegion(playTexture2));
        ImageButton playButton2 = new ImageButton(drawable2);
        playButton2.setSize(game.WIDTH / 3, game.WIDTH / 3);
        playButton2.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnStartClick2();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down");
                return true;
            }
        });
        playButton2.setPosition(hud.stage.getWidth() / 2 - playButton2.getWidth() / 2, hud.stage.getHeight() / 1.5f - 4 * playButton2.getHeight() / 5);

        hud.stage.addActor(playButton);
        hud.stage.addActor(playButton2);

        SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("LiberationMono-Regular.ttf");
        BitmapFont fontSmall = fontGen.createFont(exoFile, "exo-small", 30);
        BitmapFont fontMedium = fontGen.createFont(exoFile, "exo-medium", 48);
        BitmapFont fontLarge = fontGen.createFont(exoFile, "exo-large", 72);


        Label.LabelStyle smallStyle = new Label.LabelStyle();
        smallStyle.font = fontSmall;
        Label.LabelStyle mediumStyle = new Label.LabelStyle();
        mediumStyle.font = fontMedium;
        Label.LabelStyle largeStyle = new Label.LabelStyle();
        largeStyle.font = fontLarge;

        Label lbGame = new Label("2CARS", largeStyle);
        Table table = new Table();
        table.setFillParent(true);
        table.add(lbGame).colspan(2).spaceBottom(20).row();
//        tablez.align(Align.center);
        table.defaults().size(hud.stage.getWidth(), hud.stage.getHeight() / 6);
        table.setPosition(0, hud.stage.getHeight() / 5);
        hud.stage.addActor(table);
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
        game.batch.draw(capturedFrame, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.batch.end();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    private void btnStartClick() {
        CarGame.ENABLE_3D = false;
        PlayScreen p = new PlayScreen(game);

        p.setState(PlayScreen.State.RUN);
        p.reset();
        game.setScreen(p);
        dispose();
    }

    private void btnStartClick2() {
        CarGame.ENABLE_3D = true;
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
