package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class tienda extends AppCompatActivity {

    private int pistas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        try{
            SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
            String info = preferencias.getString("tienda","");
            pistas = Integer.parseInt(info);
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

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);

        Toast.makeText(getBaseContext(),"Compra guardada", Toast.LENGTH_SHORT).show();
    }

    public void boton1(View view){
        pistas++;
        Toast.makeText(getBaseContext(),"Ha comprado 1 pista", Toast.LENGTH_SHORT).show();
    }

    public void boton2(View view){
        pistas = pistas + 2;
        Toast.makeText(getBaseContext(),"Ha comprado 2 pistas", Toast.LENGTH_SHORT).show();
    }

    public void boton3(View view){
        pistas = pistas + 3;
        Toast.makeText(getBaseContext(),"Ha comprado 3 pistas", Toast.LENGTH_SHORT).show();
    }


}
