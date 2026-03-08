package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Game_5 extends Activity {
    private int usu;
    private int niveles_superados;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("ASDFADSF");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        context= this.getApplicationContext();
        usu = extras.getInt("Usu");
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT=dm.heightPixels;
        setContentView(new GamePanel(this, usu, niveles_superados));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), Actividad2.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        extras.putInt("Respuestas_Correctas", Globales.puntuacionPrueba5);
        extras.putInt("Niveles", niveles_superados);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
