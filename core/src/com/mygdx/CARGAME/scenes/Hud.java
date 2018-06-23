package com.mygdx.CARGAME.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.CARGAME.CarGame;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    public static Integer score;

    public Hud(SpriteBatch batch){
        score=0;
        viewport= new FitViewport(CarGame.WIDTH,CarGame.HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,batch);
    }

    public void update(float delta){

    }

    public static void addScore(){
        score++;
    }
    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static Integer getScore() {
        return score;
    }

    public static void setScore(Integer score) {
        Hud.score = score;
    }
}
