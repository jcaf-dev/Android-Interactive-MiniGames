package com.example.quizz;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw (Canvas canvas);
    public void recieveTouch(MotionEvent motionEvent);
    public void terminate();
}
