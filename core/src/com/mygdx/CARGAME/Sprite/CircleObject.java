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


    public CircleObject (PlayScreen screen,World world, int left){
        super(screen,world,left,"circle");
        shape=new CircleShape();
        shape.setRadius(16);
        FixtureDef fdef =new FixtureDef();
        fdef.shape=shape;
        fdef.filter.categoryBits= CarGame.CIRCLE_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fixture=body.createFixture(fdef);
        body.createFixture(fdef).setUserData(this);

        this.texture=new TextureRegion(getTexture(),0,0,32,32);
        setRegion(this.texture);
    }

    public void onHeadHit(){
        System.out.println("HIT HIT HIT");
        screen.getHud().addScore();
        screen.addBody(body);
        screen.removeObject(this);
    }


}
