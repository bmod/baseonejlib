package com.baseoneonline.java.jmeTest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

import com.baseoneonline.java.jmeTest.text.FontQuad;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.BillboardNode;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.TestFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class TextLabel2D {
	private final String text;
	private float blurIntensity = 0.1f;
	private int kernelSize = 5;
	private ConvolveOp blur;
	private Color foreground = new Color(1f, 1f, 1f);
	private Color background = new Color(0f, 0f, 0f);
	private float fontResolution = 40f;
	private int shadowOffsetX = 2;
	private int shadowOffsetY = 2;
	private Font font;
	private FontQuad quad;

	private TextureState ts;

	public TextLabel2D(final String text) {
		this.text = text;
		updateKernel();
		setFont(Font.decode("Sans PLAIN 40"));
	}

	public void setFont(final Font font) {
		this.font = font;
	}

	public void setShadowOffsetX(final int offsetPixelX) {
		shadowOffsetX = offsetPixelX;
	}

	public void setShadowOffsetY(final int offsetPixelY) {
		shadowOffsetY = offsetPixelY;
	}

	public void setBlurSize(final int kernelSize) {
		this.kernelSize = kernelSize;
		updateKernel();
	}

	public void setBlurStrength(final float strength) {
		blurIntensity = strength;
		updateKernel();
	}

	public void setFontResolution(final float fontResolution) {
		this.fontResolution = fontResolution;
	}

	private void updateKernel() {
		final float[] kernel = new float[kernelSize * kernelSize];
		Arrays.fill(kernel, blurIntensity);
		blur = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernel));
	}

	/**
	 *
	 * @param scaleFactors
	 *            is set to the factors needed to adjust texture coords to the
	 *            next-power-of-two- sized resulting image
	 */
	private BufferedImage getImage(final Vector2f scaleFactors) {
		BufferedImage tmp0 = new BufferedImage(10, 10,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) tmp0.getGraphics();
		final Font drawFont = font.deriveFont(fontResolution);
		g2d.setFont(drawFont);
		final Rectangle2D b = g2d.getFontMetrics().getStringBounds(text, g2d);

		final int actualX = (int) b.getWidth() + kernelSize + 1 + shadowOffsetX;
		final int actualY = (int) b.getHeight() + kernelSize + 1 + shadowOffsetY;

		final int desiredX = FastMath.nearestPowerOfTwo(actualX);
		final int desiredY = FastMath.nearestPowerOfTwo(actualY);

		if (scaleFactors != null) {
			scaleFactors.x = (float) actualX / desiredX;
			scaleFactors.y = (float) actualY / desiredY;
		}

		tmp0 = new BufferedImage(desiredX, desiredY,
				BufferedImage.TYPE_INT_ARGB);

		g2d = (Graphics2D) tmp0.getGraphics();
		g2d.setFont(drawFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		final int textX = kernelSize / 2;
		final int textY = g2d.getFontMetrics().getMaxAscent() - kernelSize / 2;

		g2d.setColor(background);
		g2d.drawString(text, textX + shadowOffsetX, textY + shadowOffsetY);

		final BufferedImage ret = blur.filter(tmp0, null);

		g2d = (Graphics2D) ret.getGraphics();
		g2d.setFont(drawFont);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setColor(foreground);
		g2d.drawString(text, textX, textY);

		return ret;
	}

	public FontQuad getQuad() {
		if (null == quad) {
			return null;
		}
		return quad;
	}

	public void updateTexture() {

	}

	public FontQuad getQuad(final float height) {
		if (null != quad) {
			return quad;
		}
		final Vector2f scales = new Vector2f();
		final BufferedImage img = getImage(scales);
		final float w = img.getWidth() * scales.x;
		final float h = img.getHeight() * scales.y;
		final float factor = height / h;
		quad = new FontQuad("textLabel2d", w * factor, h * factor);
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		final Texture tex = TextureManager.loadTexture(img,
				MinificationFilter.Trilinear,
				MagnificationFilter.Bilinear, true);

		/*
		 * TexCoords texCo = ret.getTextureCoords(0); texCo.coords =
		 * BufferUtils.createFloatBuffer(16); texCo.coords.rewind(); for(int
		 * i=0; i < texCo.coords.limit(); i+=2){ float u = texCo.coords.get();
		 * float v = texCo.coords.get(); texCo.coords.put(u*scales.x);
		 * texCo.coords.put(v*scales.y); } ret.setTextureCoords(texCo);
		 * ret.updateGeometricState(0, true);
		 */
		tex.setScale(new Vector3f(scales.x, scales.y, 1));
		ts.setTexture(tex);
		ts.setEnabled(true);
		quad.setRenderState(ts);

		quad.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

		final BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as.setBlendEnabled(true);
		as.setTestEnabled(true);
		as.setTestFunction(TestFunction.GreaterThan);
		as.setEnabled(true);
		quad.setRenderState(as);

		quad.setLightCombineMode(LightCombineMode.Off);
		quad.updateRenderState();
		return quad;
	}

	public BillboardNode getBillboard(final float height) {
		final BillboardNode bb = new BillboardNode("bb");
		final FontQuad q = getQuad(height);
		bb.attachChild(q);
		return bb;
	}

	public void setForeground(final Color foreground) {
		this.foreground = foreground;
	}

	public void setBackground(final Color background) {
		this.background = background;
	}
}