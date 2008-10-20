package com.baseoneonline.java.blips;



import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.AbstractGame;
import com.jme.input.InputSystem;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.Timer;

/**
 * <code>FixedFramerateGame</code> attempts to run the game at a fixed frame
 * rate. The main loop makes every effort to render at the specified rate,
 * however it is not guaranteed that the frame rate will not dip below the
 * desired value. Game logic is updated at the same rate as the rendering. For
 * example, if the rendering is running at 60 frames per second, the logic will
 * also be updated 60 times per second.
 * 
 * Note that <code>setFrameRate(int)</code> cannot be called prior to calling
 * <code>start()</code> or a <code>NullPointerException</code> will be
 * thrown. If no frame rate is specified, the game will run at 60 frames per
 * second.

 */
public abstract class BasicFixedRateGame extends AbstractGame {
    private static final Logger logger = Logger
            .getLogger(BasicFixedRateGame.class.getName());

    //Frame-rate managing stuff
    private Timer timer;

    private int frames = 0;

    private long startTime;

    private long preferredTicksPerFrame;

    private long frameStartTick;

    private long frameDurationTicks;
    
    /**
     * Alpha bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int alphaBits = 0;

    /**
     * Depth bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int depthBits = 8;

    /**
     * Stencil bits to use for the renderer. Any changes must be made prior to call of start().
     */
    protected int stencilBits = 0;

    /**
     * Number of samples to use for the multisample buffer. Any changes must be made prior to call of start().
     */
    protected int samples = 0;
    
    protected Camera cam;
    protected DisplaySystem display;
    protected Renderer renderer;
    protected KeyBindingManager key;

    /**
     * Set preferred frame rate. The main loop will make every attempt to
     * maintain the given frame rate. This should not be called prior to the
     * application being <code>start()</code> -ed.
     * 
     * @param fps
     *            the desired frame rate in frames per second
     */
    public void setFrameRate(int fps) {
        if (fps <= 0) {
                throw new IllegalArgumentException(
                        "Frames per second cannot be less than one.");
        }

        logger.info("Attempting to run at " + fps + " fps.");
        preferredTicksPerFrame = timer.getResolution() / fps;
    }

    /**
     * Gets the current frame rate.
     * 
     * @return the current number of frames rendering per second
     */
    public float getFramesPerSecond() {
        float time = (timer.getTime() - startTime)
                / (float) timer.getResolution();
        float fps = frames / time;

        startTime = timer.getTime();
        frames = 0;

        return fps;
    }

    /**
     * <code>startFrame</code> begin monitoring the current frame. This method
     * should be called every frame before update and drawing code.
     */
    private void startFrame() {
        frameStartTick = timer.getTime();
    }

    /**
     * <code>endFrame</code> ends the current frame. Pads any excess time in
     * the frame by sleep()-ing the thread in order to maintain the desired
     * frame rate. No attempt is made to rectify frames which have taken too
     * much time.
     */
    private void endFrame() {
        frames++;

        frameDurationTicks = timer.getTime() - frameStartTick;

        while (frameDurationTicks < preferredTicksPerFrame) {
            long sleepTime = ((preferredTicksPerFrame - frameDurationTicks) * 1000)
                    / timer.getResolution();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.warning("Error sleeping during main loop.");
            }

            frameDurationTicks = timer.getTime() - frameStartTick;
        }
    }

    /**
     * Render and update logic at a specified fixed rate.
     */
    @Override
	public final void start() {
        logger.info("Application started.");
        try {
            getAttributes();

            initSystem();

            assertDisplayCreated();

            timer = Timer.getTimer();
            setFrameRate(60); //default to 60 fps

            initGame();

            //main loop
            while (!finished && !display.isClosing()) {
                startFrame();

                //handle input events prior to updating the scene
                // - some applications may want to put this into update of the game state
                InputSystem.update();

                //update game state, do not use interpolation parameter
                update(-1.0f);

                //render, do not use interpolation parameter
                render(-1.0f);

                //swap buffers
                display.getRenderer().displayBackBuffer();

                endFrame();

                Thread.yield();
            }

        } catch (Throwable t) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "start()", "Exception in game loop", t);
        } finally {
            cleanup();
        }
        logger.info("Application ending.");

        display.reset();
        quit();
    }
    protected void cameraPerspective() {
        cam.setFrustumPerspective( 45.0f, (float) display.getWidth()
                / (float) display.getHeight(), 1, 1000 );
        cam.setParallelProjection( false );
        cam.update();
    }
    /**
     * Quits the program abruptly using <code>System.exit</code>.
     * 
     * @see AbstractGame#quit()
     */
    @Override
	protected void quit() {
        if (display != null) {
            display.close();
        }
        System.exit(0);
    }

    /**
     * @param interpolation
     *            unused in this implementation
     * @see AbstractGame#update(float interpolation)
     */
    @Override
	protected void update(float interpolation) {
    	
    	if (key.isValidCommand("exit")) {
    		
    	}
    	
    	update();
    }
    
    protected abstract void update();
    

    /**
     * @param interpolation
     *            unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    @Override
	protected void render(float interpolation) {
        renderer = display.getRenderer();
        /** Clears the previously rendered information. */
        renderer.clearBuffers();
        
        // Execute renderQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();
    }

    /**
     * @see AbstractGame#initSystem()
     */
    @Override
	protected void initSystem() {
        logger.info(getVersion());
        try {
            /**
             * Get a DisplaySystem acording to the renderer selected in the
             * startup box.
             */
            display = DisplaySystem.getDisplaySystem(settings.getRenderer() );
            
            display.setMinDepthBits( depthBits );
            display.setMinStencilBits( stencilBits );
            display.setMinAlphaBits( alphaBits );
            display.setMinSamples( samples );

            /** Create a window with the startup box's information. */
            display.createWindow(settings.getWidth(), settings.getHeight(),
                    settings.getDepth(), settings.getFrequency(),
                    settings.isFullscreen() );
            logger.info("Running on: " + display.getAdapter()
                    + "\nDriver version: " + display.getDriverVersion() + "\n"
                    + display.getDisplayVendor() + " - "
                    + display.getDisplayRenderer() + " - "
                    + display.getDisplayAPIVersion());
            
            
            /**
             * Create a camera specific to the DisplaySystem that works with the
             * display's width and height
             */
            cam = display.getRenderer().createCamera( display.getWidth(),
                    display.getHeight() );

        } catch ( JmeException e ) {
            /**
             * If the displaysystem can't be initialized correctly, exit
             * instantly.
             */
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
            System.exit( 1 );
        }

        /** Set a black background. */
        display.getRenderer().setBackgroundColor( ColorRGBA.black.clone() );

        /** Set up how our camera sees. */
        cameraPerspective();
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 25.0f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        /** Move our camera to a correct place and orientation. */
        cam.setFrame( loc, left, up, dir );
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        /** Assign the camera to this renderer. */
        display.getRenderer().setCamera( cam );


        /** Get a high resolution timer for FPS updates. */
        timer = Timer.getTimer();

        /** Sets the title of our display. */
        String className = getClass().getName();
        if ( className.lastIndexOf( '.' ) > 0 ) className = className.substring( className.lastIndexOf( '.' )+1 );
        display.setTitle( className );



        KeyBindingManager.getKeyBindingManager().set( "exit",
                KeyInput.KEY_ESCAPE );
    
    }


    /**
     * @see AbstractGame#initGame()
     */
    @Override
	protected abstract void initGame();

    @Override
    protected void reinit() {
    	
    }

    /**
     * @see AbstractGame#cleanup()
     */
    @Override
	protected void cleanup() {
        logger.info( "Cleaning up resources." );

        TextureManager.doTextureCleanup();
        if (display != null && display.getRenderer() != null)
            display.getRenderer().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
    }
}