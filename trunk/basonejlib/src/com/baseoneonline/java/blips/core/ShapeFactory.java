package com.baseoneonline.java.blips.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class ShapeFactory {


	public static Quad getImageQuad(final ImagePaintable im,
			final int resolution, final float size) {
		final BufferedImage image = new BufferedImage(resolution, resolution,
				BufferedImage.TYPE_INT_ARGB_PRE);
		final Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, resolution, resolution);
		im.paint(g, resolution, resolution);

		final Quad q = new Quad("imgQuad", size, size);
		final TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		final Texture t = TextureManager.loadTexture(image,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t);
		q.setRenderState(ts);
		
		final BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.One);
		q.setRenderState(bs);

		q.setLightCombineMode(LightCombineMode.Off);
		q.updateRenderState();
		q.setModelBound(new BoundingBox());
		q.updateModelBound();

		return q;
	}

}
