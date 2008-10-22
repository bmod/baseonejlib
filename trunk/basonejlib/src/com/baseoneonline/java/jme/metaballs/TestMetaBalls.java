package com.baseoneonline.java.jme.metaballs;

import com.jme.app.SimpleGame;
 
public class TestMetaBalls extends SimpleGame {
 
    public static void main(String[] args) {
        TestMetaBalls app = new TestMetaBalls();
        app.start();
    }
    
    
    MetaBallSystem mballsys;
 
    @Override
    protected void simpleInitGame() {
    	mballsys = new MetaBallSystem();
    	rootNode.attachChild(mballsys);
    }
 
    @Override
    protected void simpleUpdate() {
    	mballsys.update(tpf);
    }
 

}