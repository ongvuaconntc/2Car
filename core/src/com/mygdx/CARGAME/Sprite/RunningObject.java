package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Screens.PlayScreen;

public abstract class RunningObject extends Sprite {
    public TextureRegion texture;
    protected World world;
    public Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public RunningObject(PlayScreen screen, World world,int left,String name){
        super(screen.getAtlasObjects().findRegion(name));
        this.world=world;
        this.screen=screen;
        BodyDef bdef=new BodyDef();
        switch (left){
            case 0: bdef.position.set(CarGame.WIDTH/8,410);
                break;
            case 1: bdef.position.set(3*CarGame.WIDTH/8,410);
                break;
            case 2: bdef.position.set(5*CarGame.WIDTH/8,410);
                break;
            case 3: bdef.position.set(7*CarGame.WIDTH/8,410);
                break;
        }
        bdef.type=BodyDef.BodyType.DynamicBody;

        body=world.createBody(bdef);
        body.applyTorque(0,true);
        body.setLinearVelocity(0,-100);



    }

    public void update(float delta){
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
    }
    public abstract void onHeadHit();
}
