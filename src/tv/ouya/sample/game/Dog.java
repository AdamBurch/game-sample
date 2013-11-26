package tv.ouya.sample.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaPlayer;

public class Dog extends RenderObject {
    private final Player player;
    private boolean isVisible = false;
    private boolean isDead = false;
    private float forwardAmount;

    static final private int[] c_playerColors = {
        Color.WHITE,
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN
    };
    static final private int c_deadColor = Color.DKGRAY;
    static final private float c_playerRadius = 0.5f;

    static MediaPlayer s_sfx[] = null;

    public Dog(Player player) {
        super(c_playerRadius, true);
        this.player = player;
        this.up = new Ramper(0.2f, 3000);
        this.down = new Ramper(0, 700);
        setCollisionListener(new CollisionListener() {
            @Override
            public void onCollide(PointF prev, RenderObject me, RenderObject other) {
                if (other instanceof Wall) {
                    Wall wall = (Wall) other;

                    translation = wall.slideAgainst(prev, translation, getRadius());
                }
            }
        });
    }

    public void init() {
        isVisible = true;

        // Pick a random starting location
        translation.x = (float) (Math.random() * (GameRenderer.BOARD_WIDTH - 1.0f) + 1.0f);
        translation.y = (float) (Math.random() * (GameRenderer.BOARD_HEIGHT - 1.0f) + 1.0f);
        rotation = (float) (Math.random() * 360.0f);
    }

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

        final float[] coords = {
               -0.5f,  0.5f, 0.0f, // 0
                0.0f, -0.2f, 0.0f, // 1
                0.0f,  0.1f, 0.0f, // 2
                0.5f,  0.5f, 0.0f, // 3
        };

        vertexBuffer.put(coords);
        indexBuffer.put(_indicesArray);

        vertexBuffer.position(0);
        indexBuffer.position(0);
    }

    final float c_forwardSpeed = 0.1f;
    private Ramper up;
    private Ramper down;

    @Override
    protected void update() {
        if (!isValid()) {
            return;
        }

        super.update();

        PointF playerPos = player.getPosition();
        float xFromPlayer = playerPos.x - this.translation.x;
        float yFromPlayer = playerPos.y - this.translation.y;
        float desiredDir = (float) Math.toDegrees( Math.atan2(-xFromPlayer, yFromPlayer));
        setRotate(desiredDir);

        double dist = (xFromPlayer * xFromPlayer) + (yFromPlayer * yFromPlayer);
        if(dist > 25) {
            forwardAmount = up.rampMe();
            down.ready(forwardAmount);
        } else {
            forwardAmount = down.rampMe();
            up.ready(forwardAmount);
        }

        if (!isDead && forwardAmount != 0.0f) {
            goForward(forwardAmount);
        }
    }

    @Override
    protected void doRender(GL10 gl) {
        if (!isValid()) {
            return;
        }

        int color = isDead ? c_deadColor : c_playerColors[3];
        setColor(gl, color);
        super.doRender(gl);
    }

    @Override
    public boolean doesCollide(RenderObject other) {
        if (other instanceof Player) {
            return false;
        }
        return super.doesCollide(other);
    }
}
