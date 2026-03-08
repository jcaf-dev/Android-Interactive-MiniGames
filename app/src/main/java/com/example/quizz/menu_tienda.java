package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class menu_tienda extends AppCompatActivity {

    private int pistas;
    private  TextView pistaText;
    private  int usu;
    private int niveles_superados;
    private int puntuacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tienda);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        puntuacion = extras.getInt("Respuestas_Correctas", 0);

        try{
            SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
            String info = preferencias.getString("tienda","");
            pistas = Integer.parseInt(info);

            pistaText = (TextView) findViewById(R.id.contador_pistas);
            pistaText.setText(pistas+"");

        }catch (Exception e){
            pistas = 0;
        }
    }

    //Guarda los datos de la compra cuando pulsas "back"
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String nombre = "tienda";

        SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString(nombre,pistas+"");
        obj_editor.commit();

        Intent intent = new Intent(getApplicationContext(), menu_mapa.class);
        Bundle extras = new Bundle();
        extras.putInt("Usu", usu);
        intent.putExtras(extras);
        startActivity(intent);

        Toast.makeText(getBaseContext(),"Compra guardada", Toast.LENGTH_SHORT).show();
    }

    public void boton1(View view){
        pistas++;
        pistaText.setText(pistas+"");
        Toast.makeText(getBaseContext(),"Ha comprado 1 pista", Toast.LENGTH_SHORT).show();
    }

    public void boton2(View view){
        pistas = pistas + 2;
        pistaText.setText(pistas+"");
        Toast.makeText(getBaseContext(),"Ha comprado 2 pistas", Toast.LENGTH_SHORT).show();
    }

    public void boton3(View view){
        pistas = pistas + 3;
        pistaText.setText(pistas+"");
        Toast.makeText(getBaseContext(),"Ha comprado 3 pistas", Toast.LENGTH_SHORT).show();
    }

    public  void menu_mapa_metodo(View view){

        Intent intent = new Intent(getApplicationContext(), Recycled.class);
        Bundle extras = new Bundle();
        extras.putInt("Respuestas_Correctas", puntuacion);
        extras.putInt("Usu", usu);
        extras.putInt("Niveles", niveles_superados);

        intent.putExtras(extras);
        startActivity(intent);
    }

}
