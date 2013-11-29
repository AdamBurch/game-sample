package tv.ouya.sample.game;

import android.graphics.PointF;

public class Rex extends Dog {

    private static final float[] VERTICES = {
        -0.5f,  0.5f, 0.0f, // 0
        0.0f, -0.2f, 0.0f, // 1
        0.0f,  0.1f, 0.0f, // 2
        0.5f,  0.5f, 0.0f, // 3
    };
    private final Player adam;

    public Rex(Player player) {
        this.adam = player;
    }

    @Override
    protected Ramper getStop() {
        return new Ramper(0, 700);
    }

    @Override
    protected Ramper getForward() {
        return new Ramper(0.2f, 3000);
    }

    @Override
    protected float[] getVertices() {
        return Rex.VERTICES;
    }

    @Override
    protected boolean shouldGoForward() {
        PointF from = getVectorToTarget(adam);
        return (from.x * from.x + from.y * from.y) > 36;
    }

    @Override
    protected void doUpdate() {
        PointF from = getVectorToTarget(adam);
        float desiredDir = (float) Math.toDegrees( Math.atan2(-from.x, from.y));
        setRotate(desiredDir);
    }

    @Override
    protected int getColor() {
        return android.graphics.Color.WHITE;
    }

}
