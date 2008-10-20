package com.baseoneonline.java.blips;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Shapes {

	public static Quad getImageQuad(Image im, float size) {
		Quad q = new Quad("imgQuad", size, size);

		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		Texture t = TextureManager.loadTexture(im,
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t);
		
		q.setLightCombineMode(LightCombineMode.Off);
		q.setRenderState(ts);
		q.setModelBound(new BoundingBox());
		q.updateModelBound();
		return q;
	}

	public static Image getPlayerArc() {
		int size = 256;
		BufferedImage image = new BufferedImage(size, size,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		float strokeWidth = 30;
		float hs = strokeWidth/2;
		float arcLength = 200;
		
		g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_BEVEL));
		Shape s = new Arc2D.Float(hs, hs, size-strokeWidth, size-strokeWidth,
				-arcLength/2, arcLength, Arc2D.OPEN);
		g.draw(s);
		return image;
	}

	public static Image getRing() {
		int size = 256;
		BufferedImage image = new BufferedImage(size, size,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fill(new Rectangle(size, size));

		Shape s;
		float strokeWidth = 10;

		float hs = strokeWidth / 2;
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(strokeWidth));
		s = new Ellipse2D.Float(hs, hs, size - strokeWidth * 2, size
				- strokeWidth);
		g.draw(s);

		return image;
	}

}
