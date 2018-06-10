package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Screens.PlayScreen;


public class Car extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion carGoStraight;
    public enum State{TURNLEFT,TURNRIGHT,STRAIGHT};

    public State currentState;
    public State previousState;
    private Animation<TextureRegion> carTurnLeft;
    private Animation<TextureRegion> carTurnRight;

    private float stateTimer;

    public Car(World world, PlayScreen playScreen,boolean blue,String name){
        super(playScreen.getAtlas().findRegion(name));
        this.world=world;
        currentState=State.STRAIGHT;
        previousState=State.STRAIGHT;

        stateTimer=0;
        Array<TextureRegion> frames=new Array();
        for (int i=1;i<4;i++){
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));

        }
        carTurnRight=new Animation<TextureRegion>(0.1f,frames);

        frames.clear();

        for (int i=4;i<7;i++){
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        }

        carTurnLeft=new Animation<TextureRegion>(0.1f,frames);


        defineCar(blue);
        carGoStraight=new TextureRegion(getTexture(),0,0,16,16);
        setRegion(carGoStraight);


    }

    public void defineCar(boolean blue){
        BodyDef bdef=new BodyDef();
        if (blue)
        bdef.position.set(26,26);
        else bdef.position.set(182,26);

        bdef.type=BodyDef.BodyType.DynamicBody;

        b2body=world.createBody(bdef);
        b2body.applyTorque(0,true);

      //  b2body.setLinearVelocity(new Vector2(0,0));
        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(5);
        fdef.filter.categoryBits= CarGame.CAR_BIT;
        fdef.filter.maskBits=CarGame.CIRCLE_BIT|CarGame.RECTANGLE_BIT|CarGame.GROUND_BIT;
        fdef.shape=shape;


        b2body.createFixture(fdef);
        System.out.println("car width"+getWidth()+" car height"+getHeight());
        fdef=new FixtureDef();
        shape=new CircleShape();
        shape.setRadius(7);
        fdef.shape=shape;
        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData("car");

        //barriers
        bdef=new BodyDef();
        if (blue)
        bdef.position.set(24,26);
        else bdef.position.set(184,26);
        bdef.type=BodyDef.BodyType.StaticBody;
        Body body=world.createBody(bdef);

        fdef=new FixtureDef();
        Rectangle rect=new Rectangle();
        if (blue)
        rect.set(16,20,10,10);
        else
            rect.set(182,20,10,10);
        PolygonShape polygonShape=new PolygonShape();
        polygonShape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);
        fdef.filter.categoryBits= CarGame.GROUND_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fdef.shape=polygonShape;

        body.createFixture(fdef);

        bdef=new BodyDef();
        if (blue)
        bdef.position.set(80,26);
        else bdef.position.set(128,26);
        bdef.type=BodyDef.BodyType.StaticBody;
        body=world.createBody(bdef);

        //  b2body.setLinearVelocity(new Vector2(0,0));
        fdef=new FixtureDef();
        rect=new Rectangle();

        if (blue)
        rect.set(80,20,10,10);
        else
            rect.set(130,20,10,10);

        polygonShape=new PolygonShape();
        polygonShape.setAsBox(rect.getWidth()/2,rect.getHeight()/2);

        fdef.filter.categoryBits= CarGame.GROUND_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fdef.shape=polygonShape;

        body.createFixture(fdef);
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(delta));
    }
    private State getState(){
        if (b2body.getLinearVelocity().x>0) return State.TURNRIGHT;
        if (b2body.getLinearVelocity().x<0) return State.TURNLEFT;
        return State.STRAIGHT;
    }

    public TextureRegion getFrame(float delta){
        currentState=getState();
        TextureRegion region;

        switch (currentState){
            case TURNLEFT:
                region=carTurnLeft.getKeyFrame(stateTimer);
                break;
            case TURNRIGHT:
                region=carTurnRight.getKeyFrame(stateTimer);
                break;

            case STRAIGHT:
            default:
                region=carGoStraight;
            break;

        }

        stateTimer=currentState==previousState?stateTimer+delta:0;
        previousState=currentState;
        return region;
    }
}
