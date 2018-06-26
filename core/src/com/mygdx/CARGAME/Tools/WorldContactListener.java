package com.mygdx.CARGAME.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.CARGAME.Sprite.CircleObject;
import com.mygdx.CARGAME.Sprite.RunningObject;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA=contact.getFixtureA();
        Fixture fixB=contact.getFixtureB();

        if (fixA.getUserData()=="car"||fixB.getUserData()=="car"){
         //   System.out.println("Collision Car!!!");
            Fixture head=fixA.getUserData()=="car"?fixA:fixB;
            Fixture object=head==fixA?fixB:fixA;
            if (object.getUserData()!=null)
      //      System.out.println("Contact !!!!"+object.getUserData().getClass());


            if (object.getUserData()!=null&& RunningObject.class.isAssignableFrom(object.getUserData().getClass())){
            //    System.out.println("Collision\n");
                ((RunningObject) object.getUserData()).onHeadHit();
            }
            else{
                System.out.println("wall hit");
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
