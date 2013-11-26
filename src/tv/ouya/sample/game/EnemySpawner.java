package tv.ouya.sample.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

public class EnemySpawner extends RenderObject {

    private Dog target;
    public EnemySpawner(Dog target) {
        super(0.0f, false);
        this.target = target;
        lastSpawnTime = SystemClock.elapsedRealtime();
    }

    private static final float SPAWN_INTERVAL = 2000;
    
    private float lastSpawnTime;
    public void update() {
        if(shouldSpawn()) {
            spawn();
        }
    }

    private void spawn() {
        new Enemy(target);
        lastSpawnTime = SystemClock.elapsedRealtime();
    }

    private boolean shouldSpawn() {
        return SystemClock.elapsedRealtime() - lastSpawnTime  > SPAWN_INTERVAL;
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
                0.0f,  0.0f, 0.0f, // 0
                0.0f,  0.0f, 0.0f, // 1
                0.0f,  0.0f, 0.0f, // 2
        };

        vertexBuffer.put(coords);
        indexBuffer.put(_indicesArray);

        vertexBuffer.position(0);
        indexBuffer.position(0);
    }

    @Override
    protected void doRender(GL10 gl) {}
}
