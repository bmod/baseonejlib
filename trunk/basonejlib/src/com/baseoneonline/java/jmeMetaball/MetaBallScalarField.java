package com.baseoneonline.java.jmeMetaball;

import com.jme.math.Vector3f;
 
class MetaBallScalarField implements ScalarField {
 
    private final MetaBall[] balls;

    
    public MetaBallScalarField(MetaBall... balls) {
        this.balls = balls;
    }
 
    public float calculate(Vector3f point) {
        float sum = 0;
        for (MetaBall ball : balls) {
             sum += ball.getWeight() / (ball.getPosition().distanceSquared(point) + 0.001f);
        }
        return sum;
    }
 
    public Vector3f normal(Vector3f point) {
        Vector3f normal = new Vector3f();
        for (MetaBall ball : balls) {
            Vector3f direction = point.subtract(ball.getPosition());
            float lengthSquared = direction.lengthSquared() + 0.001f;
            normal.addLocal(direction.divideLocal(lengthSquared*lengthSquared));
        }        
        return normal.normalizeLocal();
    }
}