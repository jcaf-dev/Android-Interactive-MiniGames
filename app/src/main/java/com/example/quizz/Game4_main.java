package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Game4_main extends AppCompatActivity {

    private  int usu;
    private long backPressedTime;
    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView Nave;
    private ImageView Moneda_dos;
    private ImageView Moneda;
    private ImageView Meteorito;

    private Button boton_terminar;
    private int frameHeight;
    private int Nave_medidas;
    private int Ancho_pantalla;
    private int Alto_pantalla;
    private int Nave_Y;
    private int Moneda_dos_X;
    private int Moneda_dos_Y;
    private int Moneda_X;
    private int Moneda_Y;
    private int Meteorito_X;
    private int Meteorito_Y;
    private int Nave_vel;
    private int Moneda_dos_vel;
    private int moneda_vel;
    private int meteorito;
    private int puntuacion = 0;
    private Handler handler = new Handler();
    private Timer tiempo = new Timer();
    private boolean action_flg = false;
    private boolean start_flg = false;
    private int niveles_superados;


    // "back" para salir, 2 veces
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"¿Seguro que quieres salir?",Toast.LENGTH_SHORT).show();
        }
        backPressedTime =  System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game4_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        boton_terminar = (Button) findViewById(R.id.button_final);
        boton_terminar.setVisibility(View.GONE);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        Nave = (ImageView) findViewById(R.id.box);
        Moneda_dos = (ImageView) findViewById(R.id.orange);
        Moneda = (ImageView) findViewById(R.id.pink);
        Meteorito = (ImageView) findViewById(R.id.black);


        //Botener en tamano de la pantalla.
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        Ancho_pantalla = size.x;
        Alto_pantalla = size.y;

        Nave_vel = Math.round(Alto_pantalla / 60);
        Moneda_dos_vel = Math.round(Ancho_pantalla / 60);
        moneda_vel = Math.round(Ancho_pantalla / 36);
        meteorito = Math.round(Ancho_pantalla / 45);

        //Mover fuera de la pantalla.
        Moneda_dos.setX(-80);
        Moneda_dos.setY(-80);
        Moneda.setX(-80);
        Moneda.setY(-80);
        Meteorito.setX(-80);
        Meteorito.setY(-80);

        scoreLabel.setText("Score : 0");
    }

    public void CambiarPos() {

        comprobarhit();

        // moneda dos
        Moneda_dos_X -= Moneda_dos_vel;
        if (Moneda_dos_X < 0) {
            Moneda_dos_X = Ancho_pantalla + 20;
            Moneda_dos_Y = (int) Math.floor(Math.random() * (frameHeight - Moneda_dos.getHeight()));
        }
        Moneda_dos.setX(Moneda_dos_X);
        Moneda_dos.setY(Moneda_dos_Y);


        // meteorito
        Meteorito_X -= meteorito;
        if (Meteorito_X < 0) {
            Meteorito_X = Ancho_pantalla + 10;
            Meteorito_Y = (int) Math.floor(Math.random() * (frameHeight - Meteorito.getHeight()));
        }
        Meteorito.setX(Meteorito_X);
        Meteorito.setY(Meteorito_Y);


        // moneda
        Moneda_X -= moneda_vel;
        if (Moneda_X < 0) {
            Moneda_X = Ancho_pantalla + 5000;
            Moneda_Y = (int) Math.floor(Math.random() * (frameHeight - Moneda.getHeight()));
        }
        Moneda.setX(Moneda_X);
        Moneda.setY(Moneda_Y);


        // Mover nave
        if (action_flg == true) {
            // Touching
            Nave_Y -= Nave_vel;

        } else {
            // Liberando
            Nave_Y += Nave_vel;
        }

        // Comprobar nave posicion
        if (Nave_Y < 0) Nave_Y = 0;

        if (Nave_Y > frameHeight - Nave_medidas) Nave_Y = frameHeight - Nave_medidas;

        Nave.setY(Nave_Y);

        scoreLabel.setText("Score : " + puntuacion);

    }


    public void comprobarhit() {

        int MonedaDosX = Moneda_dos_X + Moneda_dos.getWidth() / 2;
        int MonedaDosY = Moneda_dos_Y + Moneda_dos.getHeight() / 2;

        if (0 <= MonedaDosX && MonedaDosX <= Nave_medidas &&
                Nave_Y <= MonedaDosY && MonedaDosY <= Nave_Y + Nave_medidas) {

            puntuacion += 10;
            Moneda_dos_X = -10;
        }

        int MonedaX = Moneda_X + Moneda.getWidth() / 2;
        int MonedaY = Moneda_Y + Moneda.getHeight() / 2;

        if (0 <= MonedaX && MonedaX <= Nave_medidas &&
                Nave_Y <= MonedaY && MonedaY <= Nave_Y + Nave_medidas) {

            puntuacion += 30;
            Moneda_X = -10;
        }

        int MeteoritoX = Meteorito_X + Meteorito.getWidth() / 2;
        int MeteoritoY = Meteorito_Y + Meteorito.getHeight() / 2;

        if (0 <= MeteoritoX && MeteoritoX <= Nave_medidas && Nave_Y <= MeteoritoY && MeteoritoY <= Nave_Y + Nave_medidas) {
            boton_terminar = (Button) findViewById(R.id.button_final);
            tiempo.cancel();
            boton_terminar.setVisibility(View.VISIBLE);
            tiempo = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            Nave_Y = (int) Nave.getY();

            Nave_medidas = Nave.getHeight();

            startLabel.setVisibility(View.GONE);

            tiempo.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CambiarPos();
                        }
                    });
                }
            }, 0, 70);

        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }

    public void Terminar(View view){
        Intent intent = new Intent(getApplicationContext(), Actividad2.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Niveles", niveles_superados);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

}