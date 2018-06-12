package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Screens.PlayScreen;

public class CircleObject extends RunningObject {
    protected CircleShape shape;


    public CircleObject (PlayScreen screen,World world, int left,String name){
        super(screen,world,left,name);
        shape=new CircleShape();
        shape.setRadius(CarGame.OBJECT_SIZE/10/CarGame.PPM);
        FixtureDef fdef =new FixtureDef();
        fdef.shape=shape;
        fdef.filter.categoryBits= CarGame.CIRCLE_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fixture=body.createFixture(fdef);
        body.createFixture(fdef).setUserData(this);

        if (left<2)
        this.texture=new TextureRegion(getTexture(),35,32,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        else
            this.texture=new TextureRegion(getTexture(),1,31,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        setBounds(0,0,CarGame.OBJECT_SIZE/CarGame.PPM,CarGame.OBJECT_SIZE/CarGame.PPM);
        setRegion(this.texture);
    }

    public void onHeadHit(){
//        System.out.println("HIT HIT HIT");
        screen.getHud().addScore();
        screen.addBody(body);
        screen.removeObject(this);
    }


}
