package com.baseoneonline.java.jmeMetaball;

import static com.jme.math.FastMath.nextRandomFloat;

import com.jme.math.Vector3f;
 
public class MetaBall {
      
    private Vector3f position;
    private Vector3f speed;
    private float weight;
      
    public MetaBall(Vector3f position, float weight, Vector3f speed) {
        this.position = position;
        this.weight = weight;
        this.speed = speed;
    }
    
    public static MetaBall getRandomBall(Vector3f boxSize, float maxWeight, float maxSpeed) {
        Vector3f position = new Vector3f(
                (nextRandomFloat() * boxSize.x * 2) - boxSize.x,
                (nextRandomFloat() * boxSize.y * 2) - boxSize.y,
                (nextRandomFloat() * boxSize.z * 2) - boxSize.z);
        Vector3f speed = new Vector3f(
                nextRandomFloat() * maxSpeed,
                nextRandomFloat() * maxSpeed,
                nextRandomFloat() * maxSpeed);
        float weight = maxWeight * nextRandomFloat() + 1f;
        return new MetaBall(position, weight, speed);        
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public void setPosition(Vector3f position) {
        this.position = position;
    }
 
    public float getWeight() {
        return weight;
    }
 
    public void setWeight(float weight) {
        this.weight = weight;
    }
 
    public Vector3f getSpeed() {
        return speed;
    }
 
    public void setSpeed(Vector3f speed) {
        this.speed = speed;
    }
}