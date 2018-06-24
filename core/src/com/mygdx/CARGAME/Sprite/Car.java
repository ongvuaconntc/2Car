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
        int indexX=0;
        if (!blue) indexX=512;

        stateTimer=0;
        Array<TextureRegion> frames=new Array();
        for (int i=1;i<4;i++){
            frames.add(new TextureRegion(getTexture(),indexX+i*CarGame.CAR_SIZE,0,CarGame.CAR_SIZE,CarGame.CAR_SIZE));

        }
        carTurnRight=new Animation<TextureRegion>(0.1f,frames);

        frames.clear();

        for (int i=4;i<7;i++){
            frames.add(new TextureRegion(getTexture(),indexX+i*CarGame.CAR_SIZE,0,CarGame.CAR_SIZE,CarGame.CAR_SIZE));
        }

        carTurnLeft=new Animation<TextureRegion>(0.1f,frames);


        defineCar(blue);
        carGoStraight=new TextureRegion(getTexture(),indexX,0,CarGame.CAR_SIZE,CarGame.CAR_SIZE);
        setBounds(0,0,CarGame.CAR_SIZE*1.1f/CarGame.PPM,CarGame.CAR_SIZE*1.1f/CarGame.PPM);
        setRegion(carGoStraight);


    }

    public void defineCar(boolean blue){
        BodyDef bdef=new BodyDef();
        if (blue)
        bdef.position.set(CarGame.WIDTH/8/CarGame.PPM,100/CarGame.PPM);
        else bdef.position.set(7*CarGame.WIDTH/8/CarGame.PPM,100/CarGame.PPM);

        bdef.type=BodyDef.BodyType.DynamicBody;

        b2body=world.createBody(bdef);
        b2body.applyTorque(0,true);

      //  b2body.setLinearVelocity(new Vector2(0,11));
        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(2/CarGame.PPM);
        fdef.filter.categoryBits= CarGame.CAR_BIT;
        fdef.filter.maskBits=CarGame.CIRCLE_BIT|CarGame.RECTANGLE_BIT|CarGame.GROUND_BIT;
        fdef.shape=shape;


        b2body.createFixture(fdef);
        System.out.println("car width"+getWidth()+" car height"+getHeight());
        fdef=new FixtureDef();
        shape=new CircleShape();
        shape.setRadius(30/CarGame.PPM);
        fdef.shape=shape;
        fdef.isSensor=true;
        b2body.createFixture(fdef).setUserData("car");

        //barriers
        bdef=new BodyDef();
        if (blue)
        bdef.position.set((CarGame.WIDTH/8-11)/CarGame.PPM,100/CarGame.PPM);
        else bdef.position.set((7*CarGame.WIDTH/8+11)/CarGame.PPM,100/CarGame.PPM);
        bdef.type=BodyDef.BodyType.StaticBody;
        Body body=world.createBody(bdef);

        fdef=new FixtureDef();
        Rectangle rect=new Rectangle();
        if (blue)
        rect.set((CarGame.WIDTH/8-11)/CarGame.PPM,94/CarGame.PPM,720/CarGame.PPM,720/CarGame.PPM);
        else
            rect.set((7*CarGame.WIDTH/8+11)/CarGame.PPM,94/CarGame.PPM,720/CarGame.PPM,720/CarGame.PPM);
        PolygonShape polygonShape=new PolygonShape();
        polygonShape.setAsBox(rect.getWidth()/2/CarGame.PPM,rect.getHeight()/2/CarGame.PPM);
        fdef.filter.categoryBits= CarGame.GROUND_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        fdef.shape=polygonShape;

        body.createFixture(fdef);

        bdef=new BodyDef();
        if (blue)
        bdef.position.set((3*CarGame.WIDTH/8+11)/CarGame.PPM,100/CarGame.PPM);
        else bdef.position.set((5*CarGame.WIDTH/8-11)/CarGame.PPM,100/CarGame.PPM);
        bdef.type=BodyDef.BodyType.StaticBody;
        body=world.createBody(bdef);

        //  b2body.setLinearVelocity(new Vector2(0,0));
        fdef=new FixtureDef();
        rect=new Rectangle();

        if (blue)
        rect.set((3*CarGame.WIDTH/8+11)/CarGame.PPM,94/CarGame.PPM,720/CarGame.PPM,720/CarGame.PPM);
        else
            rect.set((5*CarGame.WIDTH/8-11)/CarGame.PPM,94  /CarGame.PPM,720/CarGame.PPM,720/CarGame.PPM);

        polygonShape=new PolygonShape();
        polygonShape.setAsBox(rect.getWidth()/2/CarGame.PPM,rect.getHeight()/2/CarGame.PPM);

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
