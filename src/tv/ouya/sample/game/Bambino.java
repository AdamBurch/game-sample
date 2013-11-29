package tv.ouya.sample.game;

public class Bambino extends Dog {
    private static final float[] VERTICES = {
        -0.5f,  0.5f, 0.0f, // 0
        0.0f, -0.2f, 0.0f, // 1
        0.0f,  0.1f, 0.0f, // 2
        0.5f,  0.5f, 0.0f, // 3
    };
    public Bambino(Dog rex) {
        // TODO Auto-generated constructor stub
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
        return Bambino.VERTICES;
    }



    @Override
    protected boolean shouldGoForward() {
      //  PointF from = getVectorToTarget(adam);
     //   return (from.x * from.x + from.y * from.y) > 36;
        return false;
    }

    @Override
    protected void doUpdate() {
      //  PointF from = getVectorToTarget(adam);
      //  float desiredDir = (float) Math.toDegrees( Math.atan2(-from.x, from.y));
      //  setRotate(desiredDir);
    }

    @Override
    protected int getColor() {
        return android.graphics.Color.BLACK;
    }

}
