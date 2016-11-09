package com.luckenbaughgdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ObjectMap;
import com.luckenbaughgdx.game.Assets;
import com.luckenbaughgdx.game.WorldController;
import com.luckenbaughgdx.game.objects.AbstractGameObject;
import com.luckenbaughgdx.game.objects.Bee;
import com.luckenbaughgdx.game.objects.Bone;
import com.luckenbaughgdx.game.objects.Pile;
import com.luckenbaughgdx.game.objects.Pooch;
import com.luckenbaughgdx.game.objects.Rock;
import com.luckenbaughgdx.game.objects.Treat;

public class CollisionHandler implements ContactListener
{
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;

    private WorldController world;

    public CollisionHandler(WorldController w)
    {
        world = w;
        listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }

    public void addListener(short categoryA, short categoryB, ContactListener listener)
    {
        addListenerInternal(categoryA, categoryB, listener);
        addListenerInternal(categoryB, categoryA, listener);
    }

    @Override
    public void beginContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Gdx.app.log("CollisionHandler-begin A", "begin");
        processContact(contact);
        // processContact(contact);

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.beginContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.preSolve(contact, oldManifold);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.postSolve(contact, impulse);
        }
    }

    @Override
    public void endContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Gdx.app.log("CollisionHandler-end A", fixtureA.getBody().getLinearVelocity().x+" : "+fixtureA.getBody().getLinearVelocity().y);
        processContactEnd(contact);

        ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
        if (listener != null)
        {
            listener.endContact(contact);
        }
    }

    private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            listenerCollection = new ObjectMap<Short, ContactListener>();
            listeners.put(categoryA, listenerCollection);
        }
        listenerCollection.put(categoryB, listener);
    }

    private ContactListener getListener(short categoryA, short categoryB)
    {
        ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
        if (listenerCollection == null)
        {
            return null;
        }
        return listenerCollection.get(categoryB);
    }

    private void processContact(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject) fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject) fixtureB.getBody().getUserData();

        if (objA instanceof Pooch)
        {
            processPlayerContact(fixtureA, fixtureB);
        }
        else if (objB instanceof Pooch)
        {
            processPlayerContact(fixtureB, fixtureA);
        }
    }

    private void processContactEnd(Contact contact)
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        AbstractGameObject objA = (AbstractGameObject) fixtureA.getBody().getUserData();
        AbstractGameObject objB = (AbstractGameObject) fixtureB.getBody().getUserData();

        if (objA instanceof Pooch)
        {
            processPlayerContactEnd(fixtureA, fixtureB);
        }
        else if (objB instanceof Pooch)
        {
            processPlayerContactEnd(fixtureB, fixtureA);
        }
    }
    private void processPlayerContact(Fixture playerFixture, Fixture objFixture)
    {
        if (objFixture.getBody().getUserData() instanceof Rock)
        {
            Pooch player = (Pooch) playerFixture.getBody().getUserData();
            //playerFixture.getBody().setLinearVelocity(player.velocity);
            player.dustParticles.start();
            world.spacePressed = false;

        }
        else if (objFixture.getBody().getUserData() instanceof Treat)
        {
            Treat treat = (Treat) objFixture.getBody().getUserData();
            treat.collected = true;
            AudioManager.instance.play(Assets.instance.sounds.pickupTreat);
            world.score += treat.getScore();
            world.flagForRemoval(treat);
        }
        else if (objFixture.getBody().getUserData() instanceof Bee)
        {
            Bee bee = (Bee) objFixture.getBody().getUserData();
            bee.collected = true;
            AudioManager.instance.play(Assets.instance.sounds.hitBee);
            world.score += bee.getScore();
            world.flagForRemoval(bee);
        }
        else if (objFixture.getBody().getUserData() instanceof Pile)
        {
            Pile pile = (Pile) objFixture.getBody().getUserData();
            pile.collected = true;
            AudioManager.instance.play(Assets.instance.sounds.hitPile);
            world.score += pile.getScore();
            world.level.pooch.setPilePowerdown(true);
            world.flagForRemoval(pile);
        }
        else if (objFixture.getBody().getUserData() instanceof Bone)
        {
            world.goalReached = true;
            world.timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
            Vector2 centerPosBunnyHead = new Vector2(world.level.pooch.position);
            centerPosBunnyHead.x += world.level.pooch.bounds.width;
            world.spawnCarrots(centerPosBunnyHead, Constants.CARROTS_SPAWN_MAX, Constants.CARROTS_SPAWN_RADIUS);
        }
    }
    private void processPlayerContactEnd(Fixture playerFixture, Fixture objFixture)
    {
        if (objFixture.getBody().getUserData() instanceof Rock)
        {
            Pooch player = (Pooch) playerFixture.getBody().getUserData();
            //playerFixture.getBody().setLinearVelocity(player.velocity);
            if(player.hasPilePowerdown)
                AudioManager.instance.play(Assets.instance.sounds.jumpWithPile);
            else
                AudioManager.instance.play(Assets.instance.sounds.jump);
            player.dustParticles.allowCompletion();
           // world.spacePressed = true;


        }

    }

}