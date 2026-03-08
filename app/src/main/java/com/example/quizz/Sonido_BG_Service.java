package com.example.quizz;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

public class Sonido_BG_Service extends Service implements MediaPlayer.OnErrorListener{

    MediaPlayer mp;
    private int length=0;
    private final IBinder mBinder = new ServiceBinder();

    public Sonido_BG_Service() {
    }
public class ServiceBinder extends Binder{
    Sonido_BG_Service getService(){
            return Sonido_BG_Service.this;
        }
}

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();

        mp = MediaPlayer.create(this, R.raw.fenix);
        mp.setOnErrorListener(this);

        if (mp != null) {
            mp.setLooping(true);
            mp.setVolume(50, 50);
        }

        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mp, what, extra);
                return true;
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(mp != null){

            mp.start();
           // Toast.makeText(this,"Play",Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }



    public void resumeMusic() {
        if (mp != null) {
            if (!mp.isPlaying()) {

                mp.seekTo(length);
                mp.start();
            }
        }
    }

    public void startMusic() {
        mp = MediaPlayer.create(this, R.raw.fenix);
        mp.setOnErrorListener(this);

        if (mp != null) {
            mp.setLooping(true);
            mp.setVolume(50, 50);
            mp.start();
        }

    }

    public void stopMusic() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } finally {
                mp = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } finally {
                mp = null;
            }
        }
        return false;
    }
    public void pauseMusic() {
        if (mp != null) {
            if (mp.isPlaying()) {

                mp.pause();
                length = mp.getCurrentPosition();
            }
        }
    }

}
