package tv.ouya.sample.game;

import android.graphics.PointF;

public class Bambino extends Dog {
    private static final float[] VERTICES = {
        -0.5f,  0.5f, 0.0f, // 0
        0.0f, -0.2f, 0.0f, // 1
        0.0f,  0.1f, 0.0f, // 2
        0.5f,  0.5f, 0.0f, // 3
    };
    
    private Player target;
    private Dog rex;
    
    public Bambino(Dog rex, Player target) {
        this.rex = rex;
        this.target = target;
    }

    @Override
    protected Ramper getStop() {
        return new Ramper(0, 700);
    }

    @Override
    protected Ramper getForward() {
        return new Ramper(0.05f, 3000);
    }

    @Override
    protected float[] getVertices() {
        return Bambino.VERTICES;
    }



    @Override
    protected boolean shouldGoForward() {
        return true;
    }

    @Override
    protected void doUpdate() {
          PointF toTarget = getVectorToTarget(target);
          if(rex.isValid()) {
              PointF awayFromRex = getVectorToTarget(rex);
              toTarget.x = toTarget.x - awayFromRex.x;
              toTarget.y = toTarget.y - awayFromRex.y;
          }
          float desiredDir = (float) Math.toDegrees( Math.atan2(-toTarget.x, toTarget.y));
          desiredDir =  desiredDir - rotation;
          rotate(desiredDir/2);
    }

    @Override
    protected int getColor() {
        return android.graphics.Color.BLACK;
    }

}
