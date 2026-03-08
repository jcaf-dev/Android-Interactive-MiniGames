package com.example.quizz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private  int usu;
    private SceneManager manager;
    private int niveles_superados;
    private boolean acabar = false;


    public GamePanel(Context context, int usu, int nivel) {
        super(context);
        System.out.println("ESTOY DENTRO ARRIBA");
        getHolder().addCallback(this);
        this.usu=usu;
        this.niveles_superados=nivel;
        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();
        System.out.println("ESTOY DENTRO ABAJO");
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        System.out.println("LLEGUE AL FINAL DEL TODO\n\n\n");
        Context context = getContext();
        ((Game_5)context).finish();
        /*while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }*/

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        manager.recieveTouch(event);

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        manager.update();
        if(manager.getTerminar()){acabar=true;}

    }
    public boolean getAcabar(){
        return acabar;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        System.out.println("HE LLEGADO");
       // canvas.drawColor(Color.WHITE);
        manager.draw(canvas);
    }
}