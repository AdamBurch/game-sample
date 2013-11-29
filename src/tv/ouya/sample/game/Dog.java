package tv.ouya.sample.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;

public abstract class Dog extends RenderObject {
    private final Ramper forward;
    private final Ramper stop;

    private boolean isVisible = false;
    private boolean isDead = false;

    static final private float c_playerRadius = 0.5f;

    public Dog() {
        super(c_playerRadius, true);
        this.forward = getForward(); //new Ramper(0.2f, 3000);
        this.stop = getStop(); //new Ramper(0, 700);
        setCollisionListener(new CollisionListener() {
            @Override
            public void onCollide(PointF prev, RenderObject me, RenderObject other) {
                if (other instanceof Wall) {
                    Wall wall = (Wall) other;

                    translation = wall.slideAgainst(prev, translation, getRadius());
                }
            }
            
        });

        isVisible = true;

        // Pick a random starting location
        // TODO: Move this to constructor
        translation.x = (float) (Math.random() * (GameRenderer.BOARD_WIDTH - 1.0f) + 1.0f);
        translation.y = (float) (Math.random() * (GameRenderer.BOARD_HEIGHT - 1.0f) + 1.0f);
        rotation = (float) (Math.random() * 360.0f);
    }

    protected abstract Ramper getStop();
    protected abstract Ramper getForward();

    public boolean isValid() {
        return isVisible;
    }

    public void die() {
        isDead = true;
    }

    @Override
    protected void initModel() {
        final short[] _indicesArray = {0, 1, 2, 1, 3, 2};

        // float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(_indicesArray.length * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        // short has 2 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(_indicesArray.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();

        final float[] coords = getVertices();

        vertexBuffer.put(coords);
        indexBuffer.put(_indicesArray);

        vertexBuffer.position(0);
        indexBuffer.position(0);
    }

    /**
     * @return an array where every sequence of 3
     * is an (x,y,z) coord. 
     * 
     * EX:
     * {
     *         -0.5f,  0.5f, 0.0f, // 0
     *          0.0f, -0.2f, 0.0f, // 1
     *          0.0f,  0.1f, 0.0f, // 2
     *          0.5f,  0.5f, 0.0f, // 3
     *  };
     */
    protected abstract float[] getVertices();

    @Override
    protected void update() {
        if (!isValid()) {
            return;
        }

        super.update();
        doUpdate();

        float forwardAmount = 0.0f;
        if(shouldGoForward()) {
            forwardAmount = forward.rampMe();
            stop.ready(forwardAmount);
        } else {
            forwardAmount = stop.rampMe();
            forward.ready(forwardAmount);
        }

        if (!isDead && forwardAmount != 0.0f) {
            goForward(forwardAmount);
        }
    }

    protected abstract boolean shouldGoForward();

    /*
    PointF playerPos = player.getPosition();
    float xFromPlayer = playerPos.x - this.translation.x;
    float yFromPlayer = playerPos.y - this.translation.y;
    float desiredDir = (float) Math.toDegrees( Math.atan2(-xFromPlayer, yFromPlayer));
    setRotate(desiredDir);
     */
    protected abstract void doUpdate();

    @Override
    protected void doRender(GL10 gl) {
        if (!isValid()) {
            return;
        }

        setColor(gl, getColor());
        super.doRender(gl);
    }

    /**
     * @return an int from android.graphics.Color;
     */
    protected abstract int getColor();

    @Override
    public boolean doesCollide(RenderObject other) {
        if (other instanceof Player) {
            return false;
        }
        return super.doesCollide(other);
    }
    
    /**
     * yeah yeah, points aren't vectors. Bite me.
     * @param obj the target.
     * @return a vector that tells you how far you are from the target.
     */
    protected PointF getVectorToTarget(RenderObject obj) {
        PointF pos = obj.getPosition();
        float xDist = pos.x - this.translation.x;
        float yDist = pos.y - this.translation.y;
        return new PointF(xDist, yDist);
    }
}
