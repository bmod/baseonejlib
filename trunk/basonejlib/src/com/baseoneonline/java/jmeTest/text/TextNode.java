package com.baseoneonline.java.jmeTest.text;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

public class TextNode extends Node {

	private final TTFont ttFont;
	private String text;
	private FontQuad[] quads;

	public TextNode(TTFont ttFont) {
		this.ttFont = ttFont;

		// Enable alpha blend
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		bs.setSourceFunction(SourceFunction.SourceAlpha);
		bs.setDestinationFunction(DestinationFunction.One);
		//bs.setTestEnabled(true);
		//bs.setTestFunction(TestFunction.GreaterThan);
		//bs.setReference(.5f);
		setRenderState(bs);
		
		// Disable light (set pure color)
		setLightCombineMode(LightCombineMode.Off);
		updateRenderState();
	}

	public void setColor(ColorRGBA col) {
		
	}

	public void setText(String text) {
		this.text = text;
		updateTextQuads();
	}

	private void updateTextQuads() {
		detachAllChildren();
		char[] chars = text.toCharArray();
		float x = 0;
		float y = 0;
		float z = 0;
		quads = new FontQuad[chars.length];
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			FontQuad q = ttFont.getFontQuad(c);
			q.setModelBound(new BoundingBox());
			q.updateModelBound();
			q.setLocalTranslation(x, y, z);
			quads[i] = q;
			x += q.getWidth();
			attachChild(q);
		}
		

	}

}
