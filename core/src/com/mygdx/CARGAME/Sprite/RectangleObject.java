package com.mygdx.CARGAME.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.CARGAME.CarGame;
import com.mygdx.CARGAME.Screens.PlayScreen;

import java.util.Random;

public class RectangleObject extends RunningObject {
    protected PolygonShape shape;
    private Sound sound1;
    private Sound sound2;


    public RectangleObject(PlayScreen screen, World world, int left, String name) {
        super(screen, world, left, name);
        shape = new PolygonShape();
        shape.setAsBox(CarGame.OBJECT_SIZE / 4 / CarGame.PPM, CarGame.OBJECT_SIZE / 4 / CarGame.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = CarGame.RECTANGLE_BIT;
        fdef.filter.maskBits = CarGame.CAR_BIT;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        if (!CarGame.ENABLE_3D) {
            if (left < 2)
                this.texture = new TextureRegion(getTexture(), 35, 0, CarGame.OBJECT_SIZE, CarGame.OBJECT_SIZE);
            else
                this.texture = new TextureRegion(getTexture(), 67, 32, CarGame.OBJECT_SIZE, CarGame.OBJECT_SIZE);
            setBounds(0, 0, CarGame.OBJECT_SIZE * 1.1f / CarGame.PPM, CarGame.OBJECT_SIZE * 1.1f / CarGame.PPM);

            setRegion(this.texture);
        }

        Color color;
        if (left < 2) color = Color.valueOf("#42c5f4");
        else color = Color.valueOf("#fc4e4e");
        if (CarGame.ENABLE_3D) {
            model = screen.modelBuilder.createBox(CarGame.OBJECT_SIZE / CarGame.PPM, CarGame.OBJECT_SIZE / CarGame.PPM, CarGame.OBJECT_SIZE / CarGame.PPM,
                    new Material(ColorAttribute.createDiffuse(color)),
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            instance = new ModelInstance(model);
            instance.transform.setToTranslation(this.body.getPosition().x, this.body.getPosition().y, 0f);
            //       instance.transform.trn(this.body.getLinearVelocity().x/CarGame.PPM,this.body.getLinearVelocity().y/CarGame.PPM,0f);
        }
    }


    public void onHeadHit() {
        playDieMusic();
        //   System.out.println("HIT HIT HIT");
        // setFree();
        if (CarGame.ENABLE_3D)
            instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.valueOf("#84ff3d")));
        screen.setGameOver(true);
    }

    @Override
    public void reset(int left) {
        //body.setActive(true);
        switch (left) {
            case 0: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);

                body.setTransform(CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }
            break;
            case 1: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(3 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(3 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }
            break;
            case 2: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(5 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(5 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }
            break;
            case 3: {
                if (CarGame.ENABLE_3D)
                    instance.transform.setToTranslation(7 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, 0f);
                body.setTransform(7 * CarGame.WIDTH / 8 / CarGame.PPM, (CarGame.HEIGHT + 10) / CarGame.PPM, body.getAngle());
            }
            break;
        }
        if (!CarGame.ENABLE_3D) {
            if (left < 2)
                this.texture = new TextureRegion(getTexture(), 35, 0, CarGame.OBJECT_SIZE, CarGame.OBJECT_SIZE);
            else
                this.texture = new TextureRegion(getTexture(), 67, 32, CarGame.OBJECT_SIZE, CarGame.OBJECT_SIZE);
            setRegion(this.texture);
        }

        body.setLinearVelocity(0, -CarGame.OBJECT_VELOCITY);

        filter.categoryBits = CarGame.RECTANGLE_BIT;
        filter.maskBits = CarGame.CAR_BIT;
        fixture.setFilterData(filter);


        if (CarGame.ENABLE_3D) {
            Color color;
            if (left < 2) color = Color.valueOf("#42c5f4");
            else color = Color.valueOf("#fc4e4e");

            instance.materials.get(0).set(ColorAttribute.createDiffuse(color));

            //   instance.transform.trn(this.body.getLinearVelocity().x/CarGame.PPM,this.body.getLinearVelocity().y/CarGame.PPM,0f);
        }
    }

    public void playDieMusic() {
        int i = new Random().nextInt(2);
        if (i == 0) {
            sound1 = CarGame.dieOneMusic;
            long id = sound1.play();
        } else {
            sound2 = CarGame.dieTwoMusic;
            long id = sound2.play();
        }
    }
}
