package tv.ouya.sample.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.SystemClock;

public class Enemy extends RenderObject {

    static final private float c_enemyRadius = 0.5f;

    private Dog target;
    private float spawnTime;
    private Ramper attackSpeed;
    private static final float ATTACK_CHARGE_TIME = 2000;

    public Enemy(Dog target) {
        super(c_enemyRadius, false);

        attackSpeed = new Ramper(0.3f, 1000);
        attackSpeed.ready(0.0f);
        // Pick a random starting location
        translation.x = (float) (Math.random() * (GameRenderer.BOARD_WIDTH - 1.0f) + 1.0f);
        translation.y = (float) (Math.random() * (GameRenderer.BOARD_HEIGHT - 1.0f) + 1.0f);
        rotation = (float) (Math.random() * 360.0f);

        this.target = target;
        this.spawnTime = SystemClock.elapsedRealtime();
    }

    @Override
    protected void update() {
        super.update();

        if(SystemClock.elapsedRealtime() - spawnTime < ATTACK_CHARGE_TIME) {
            PointF playerPos = target.getPosition();
            float xFromPlayer = playerPos.x - this.translation.x;
            float yFromPlayer = playerPos.y - this.translation.y;
            float desiredDir = (float) Math.toDegrees( Math.atan2(-xFromPlayer, yFromPlayer));
            setRotate(desiredDir);
        } else {
            goForward(attackSpeed.rampMe());
        }
    }

    @Override
    protected void initModel() {
        final short[] _indicesArray = {0, 1, 2};

        // float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(_indicesArray.length * 3 * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

        // short has 2 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(_indicesArray.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();

        final float[] coords = {
                -0.5f, -0.5f, 0.0f, // 0
                0.5f, -0.5f, 0.0f, // 1
                0.0f,  0.5f, 0.0f, // 2
        };

        vertexBuffer.put(coords);
        indexBuffer.put(_indicesArray);

        vertexBuffer.position(0);
        indexBuffer.position(0);
    }

    @Override
    protected void doRender(GL10 gl) {
        int color = Color.YELLOW;
        setColor(gl, color);
        super.doRender(gl);
    }

}
