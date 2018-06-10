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
    private static Integer score;


    static Label scoreLabel;

    public Hud(SpriteBatch batch){
        score=0;
        viewport= new FitViewport(CarGame.WIDTH,CarGame.HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,batch);

        Table table =new Table();
        table.top();
        table.right();
        table.setFillParent(true);

        scoreLabel=new Label(String.format("%d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        table.add(scoreLabel).padRight(10);
        stage.addActor(table);
    }

    public void update(float delta){

    }

    public static void addScore(){
        score++;
        scoreLabel.setText(String.format("%d",score));
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
