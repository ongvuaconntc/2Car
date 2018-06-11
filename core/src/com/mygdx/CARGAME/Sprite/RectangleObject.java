package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Screens.PlayScreen;

public class RectangleObject extends RunningObject {
    protected PolygonShape shape;


    public RectangleObject (PlayScreen screen, World world, int left,String name){
        super(screen,world,left,name);
        shape=new PolygonShape();
        shape.setAsBox(CarGame.OBJECT_SIZE/2,CarGame.OBJECT_SIZE/2);
        FixtureDef fdef =new FixtureDef();
        fdef.shape=shape;
        fdef.filter.categoryBits= CarGame.RECTANGLE_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fixture=body.createFixture(fdef);
        body.createFixture(fdef).setUserData(this);

        if (left<2)
        this.texture=new TextureRegion(getTexture(),35,0,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        else
            this.texture=new TextureRegion(getTexture(),67,32,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);

        setRegion(this.texture);
    }


    public void onHeadHit(){
     //   System.out.println("HIT HIT HIT");
        screen.addBody(body);
        screen.setGameOver(true);
}
}
