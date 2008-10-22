package com.baseoneonline.java.jme.metaballs;

import com.jme.math.Vector3f;

interface ScalarField {
     public float calculate(Vector3f point);
     public Vector3f normal(Vector3f point);
}