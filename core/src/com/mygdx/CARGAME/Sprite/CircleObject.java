package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
        shape.setRadius(CarGame.OBJECT_SIZE/3/CarGame.PPM);
        FixtureDef fdef =new FixtureDef();
        fdef.shape=shape;
        fdef.filter.categoryBits= CarGame.CIRCLE_BIT;
        fdef.filter.maskBits=CarGame.CAR_BIT;
        this.filter=fdef.filter;
        fixture=body.createFixture(fdef);
        body.createFixture(fdef).setUserData(this);

        if (left<2)
        this.texture=new TextureRegion(getTexture(),35,32,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        else
            this.texture=new TextureRegion(getTexture(),1,31,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        setBounds(0,0,CarGame.OBJECT_SIZE*1.1f/CarGame.PPM,CarGame.OBJECT_SIZE*1.1f/CarGame.PPM);
        setRegion(this.texture);

        Color color;
        if (left<2) color=Color.BLUE;
        else color=Color.RED;
        if (CarGame.ENABLE_3D) {
            model = screen.modelBuilder.createSphere(CarGame.OBJECT_SIZE/CarGame.PPM, CarGame.OBJECT_SIZE/CarGame.PPM, CarGame.OBJECT_SIZE/CarGame.PPM, 20, 20,
                    new Material(ColorAttribute.createDiffuse(color)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
            instance = new ModelInstance(model);
            instance.transform.setToTranslation(this.body.getPosition().x,this.body.getPosition().y,0f);
      //      instance.transform.trn(this.body.getLinearVelocity().x/CarGame.PPM,this.body.getLinearVelocity().y/CarGame.PPM,0f);
        }
    }

    public void onHeadHit(){
//        System.out.println("HIT HIT HIT");
       // screen.addBody(body);
        screen.getHud().addScore();
        setFree();
    }

    @Override
    public void reset(int left) {
      //  body.resetMassData();
       // body.setActive(true);
        switch (left){
            case 0: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);

                body.setTransform(CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }break;
            case 1: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(3 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(3 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }   break;
            case 2: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(5 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(5 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }   break;
            case 3: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(7 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(7 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }   break;
        }

        body.setLinearVelocity(0,-CarGame.OBJECT_VELOCITY);

        if (left<2)
            this.texture=new TextureRegion(getTexture(),35,32,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        else
            this.texture=new TextureRegion(getTexture(),1,31,CarGame.OBJECT_SIZE,CarGame.OBJECT_SIZE);
        filter.categoryBits= CarGame.CIRCLE_BIT;
        filter.maskBits=CarGame.CAR_BIT;
        fixture.setFilterData(filter);
        setRegion(this.texture);
        if (CarGame.ENABLE_3D){
            Color color;
            if (left<2) color=Color.BLUE;
            else color=Color.RED;

            instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
       //        instance.transform.trn(this.body.getLinearVelocity().x/CarGame.PPM,this.body.getLinearVelocity().y/CarGame.PPM,0f);
        }
    }
}
