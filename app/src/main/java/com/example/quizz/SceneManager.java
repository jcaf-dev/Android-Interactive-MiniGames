package com.example.quizz;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;
    public static boolean terminar;
    public SceneManager(){
        ACTIVE_SCENE = 0;
        scenes.add(new GameplayScene());
        terminar = false;
    }
    public  void recieveTouch(MotionEvent motionEvent){
        scenes.get(ACTIVE_SCENE).recieveTouch(motionEvent);
    }
    public void update(){
        scenes.get(ACTIVE_SCENE).update();
    }
    public void draw(Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
    public int getActiveScene(){
        return ACTIVE_SCENE;
    }
    public boolean getTerminar(){
        return terminar;
    }

}
