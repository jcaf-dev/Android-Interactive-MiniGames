package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class game3 extends AppCompatActivity {
    private  int usu;
    private long backPressedTime;
    private int niveles_superados;


    //------ Pista ------//
    private int pistas;
    private ImageButton boton_pista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        //Cargar niveles
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }
    }

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

    public void terminar(View view){
        int puntuacion = 2;
        Intent intent = new Intent(getApplicationContext(), resolverdiferencias.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Niveles", niveles_superados);
        intent.putExtras(extras);
        startActivity(intent);
    }


}
