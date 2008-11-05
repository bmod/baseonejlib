package com.baseoneonline.java.jme.applet;

import jmetest.renderer.TestBoxColor;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.awt.applet.SimpleJMEApplet;

public class AppletTestBoxColor extends SimpleJMEApplet {
    private TriMesh t;
    private Quaternion rotQuat;
    private float angle = 0;
    private Vector3f axis;

    @Override
	public void simpleAppletSetup() {
        getLightState().setEnabled(false);
        rotQuat = new Quaternion();
        axis = new Vector3f(1, 1, 0.5f).normalizeLocal();

        final Vector3f max = new Vector3f(5, 5, 5);
        final Vector3f min = new Vector3f(-5, -5, -5);

        t = new Box("Box", min, max);
        t.setModelBound(new BoundingBox());
        t.updateModelBound();
        t.setLocalTranslation(new Vector3f(0, 0, -15));
        getRootNode().attachChild(t);

        t.setRandomColors();

        final TextureState ts = getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(TextureManager.loadTexture(
                TestBoxColor.class.getClassLoader().getResource(
                        "jmetest/data/images/Monkey.png"), MinificationFilter.NearestNeighborLinearMipMap,
                MagnificationFilter.Bilinear));

        getRootNode().setRenderState(ts);
    }

    @Override
	public void simpleAppletUpdate() {
        final float tpf = getTimePerFrame();
        if (tpf < 1) {
            angle = angle + (tpf * 25);
            if (angle > 360) {
                angle -= 360;
            }
        }

        rotQuat.fromAngleNormalAxis(angle * FastMath.DEG_TO_RAD, axis);
        t.setLocalRotation(rotQuat);
    }
}