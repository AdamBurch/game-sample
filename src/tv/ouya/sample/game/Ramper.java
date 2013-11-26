package tv.ouya.sample.game;

import android.os.SystemClock;

public class Ramper {

    private float start;
    private final float end;
    private final float timeLength;
    private float readyTime;
    
    public Ramper(float end, float timeLength) {
        this.end = end;
        this.timeLength = timeLength;
        this.readyTime = SystemClock.elapsedRealtime();
    }
    
    public void ready(float start) {
        this.start = start;
        this.readyTime = SystemClock.elapsedRealtime();
    }
    
    public float rampMe() {
        float elapsed = SystemClock.elapsedRealtime() -  this.readyTime;
        if(elapsed > timeLength) {
            return end;
        } else {
            return start + (end - start) * elapsed / timeLength;
        }
    }
}
