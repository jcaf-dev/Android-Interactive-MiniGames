package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class menu_mapa extends AppCompatActivity {

    private ImageView primer_nivel;
    private ImageView segundo_nivel;
    private ImageView tercer_nivel;
    private ImageView cuarto_nivel;
    private ImageView quinto_nivel;

    private ImageView fondo_menu_mapa;
    private TextView contador_pistas;
    private int pistas;
    private TextView pistaText;

    private int niveles_superados;
    private Animation animacion_primer_nivel;

    private  int usu;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_mapa);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        usu = extras.getInt("Usu");
        //Cargar niveles
       try {
            niveles_superados = extras.getInt("Niveles");

        } catch (Exception e) {
            niveles_superados = 0;
        }

        System.out.println(niveles_superados);
        fondo_menu_mapa = (ImageView) findViewById(R.id.fondo_menu_mapa);


        //------ Fondos de niveles superados ------//
        //------ Gif ------//
        if (niveles_superados == 0) {
            primer_nivel = (ImageView) findViewById(R.id.boton_robot);
            Glide.with(this).load(R.drawable.robo).into(primer_nivel);
            primer_nivel.setVisibility(View.VISIBLE);
            segundo_nivel = (ImageView) findViewById(R.id.boton_mago2);
            Glide.with(this).load(R.drawable.magobn).into(segundo_nivel);
            segundo_nivel.setVisibility(View.VISIBLE);
            tercer_nivel = (ImageView) findViewById(R.id.boton_ayla2);
            Glide.with(this).load(R.drawable.dragonbn).into(tercer_nivel);
            tercer_nivel.setVisibility(View.VISIBLE);
            cuarto_nivel = (ImageView) findViewById(R.id.cangrejo2);
            Glide.with(this).load(R.drawable.cangrejobn).into(cuarto_nivel);
            cuarto_nivel.setVisibility(View.VISIBLE);
            quinto_nivel = (ImageView) findViewById(R.id.boton_dragon2);
            Glide.with(this).load(R.drawable.aylabn).into(quinto_nivel);
            quinto_nivel.setVisibility(View.VISIBLE);
        } else if (niveles_superados == 1) {
            fondo_menu_mapa.setImageResource(R.drawable.primer_nivel_superado);
            primer_nivel = (ImageView) findViewById(R.id.boton_robot2);
            Glide.with(this).load(R.drawable.mtbn).into(primer_nivel);
            primer_nivel.setVisibility(View.VISIBLE);
            segundo_nivel = (ImageView) findViewById(R.id.boton_mago);
            Glide.with(this).load(R.drawable.magus).into(segundo_nivel);
            segundo_nivel.setVisibility(View.VISIBLE);
            tercer_nivel = (ImageView) findViewById(R.id.boton_ayla2);
            Glide.with(this).load(R.drawable.dragonbn).into(tercer_nivel);
            tercer_nivel.setVisibility(View.VISIBLE);
            cuarto_nivel = (ImageView) findViewById(R.id.cangrejo2);
            Glide.with(this).load(R.drawable.cangrejobn).into(cuarto_nivel);
            cuarto_nivel.setVisibility(View.VISIBLE);
            quinto_nivel = (ImageView) findViewById(R.id.boton_dragon2);
            Glide.with(this).load(R.drawable.aylabn).into(quinto_nivel);
            quinto_nivel.setVisibility(View.VISIBLE);
        } else if (niveles_superados == 2) {
            fondo_menu_mapa.setImageResource(R.drawable.segundo_nivel_superado);

            primer_nivel = (ImageView) findViewById(R.id.boton_robot2);
            Glide.with(this).load(R.drawable.mtbn).into(primer_nivel);
            primer_nivel.setVisibility(View.VISIBLE);
            segundo_nivel = (ImageView) findViewById(R.id.boton_mago2);
            Glide.with(this).load(R.drawable.magobn).into(segundo_nivel);
            segundo_nivel.setVisibility(View.VISIBLE);
            tercer_nivel = (ImageView) findViewById(R.id.boton_ayla);
            Glide.with(this).load(R.drawable.dragon).into(tercer_nivel);
            tercer_nivel.setVisibility(View.VISIBLE);
            cuarto_nivel = (ImageView) findViewById(R.id.cangrejo2);
            Glide.with(this).load(R.drawable.cangrejobn).into(cuarto_nivel);
            cuarto_nivel.setVisibility(View.VISIBLE);
            quinto_nivel = (ImageView) findViewById(R.id.boton_dragon2);
            Glide.with(this).load(R.drawable.aylabn).into(quinto_nivel);
            quinto_nivel.setVisibility(View.VISIBLE);
        } else if (niveles_superados == 3) {
            fondo_menu_mapa.setImageResource(R.drawable.tercer_nivel_superado);
            primer_nivel = (ImageView) findViewById(R.id.boton_robot2);
            Glide.with(this).load(R.drawable.mtbn).into(primer_nivel);
            primer_nivel.setVisibility(View.VISIBLE);
            segundo_nivel = (ImageView) findViewById(R.id.boton_mago2);
            Glide.with(this).load(R.drawable.magobn).into(segundo_nivel);
            segundo_nivel.setVisibility(View.VISIBLE);
            tercer_nivel = (ImageView) findViewById(R.id.boton_ayla2);
            Glide.with(this).load(R.drawable.dragonbn).into(tercer_nivel);
            tercer_nivel.setVisibility(View.VISIBLE);
            cuarto_nivel = (ImageView) findViewById(R.id.cangrejo);
            Glide.with(this).load(R.drawable.cangrejo).into(cuarto_nivel);
            cuarto_nivel.setVisibility(View.VISIBLE);
            quinto_nivel = (ImageView) findViewById(R.id.boton_dragon2);
            Glide.with(this).load(R.drawable.aylabn).into(quinto_nivel);
            quinto_nivel.setVisibility(View.VISIBLE);

        } else if (niveles_superados == 4) {
            primer_nivel = (ImageView) findViewById(R.id.boton_robot2);
            Glide.with(this).load(R.drawable.mtbn).into(primer_nivel);
            segundo_nivel = (ImageView) findViewById(R.id.boton_mago2);
            segundo_nivel.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.magobn).into(segundo_nivel);
            primer_nivel.setVisibility(View.VISIBLE);
            tercer_nivel = (ImageView) findViewById(R.id.boton_ayla2);
            Glide.with(this).load(R.drawable.dragonbn).into(tercer_nivel);
            tercer_nivel.setVisibility(View.VISIBLE);
            cuarto_nivel = (ImageView) findViewById(R.id.cangrejo2);
            Glide.with(this).load(R.drawable.cangrejobn).into(cuarto_nivel);
            cuarto_nivel.setVisibility(View.VISIBLE);
            quinto_nivel = (ImageView) findViewById(R.id.boton_dragon);
            Glide.with(this).load(R.drawable.ayla).into(quinto_nivel);
            fondo_menu_mapa.setImageResource(R.drawable.cuarto_nivel_superado);
            quinto_nivel.setVisibility(View.VISIBLE);
        }else if(niveles_superados==5){
            fondo_menu_mapa.setVisibility(View.GONE);
            Button foto = findViewById(R.id.button12);
            foto.setVisibility(View.VISIBLE);
            Button reinicio = findViewById(R.id.button11);
            reinicio.setVisibility(View.VISIBLE);
            TextView t1= findViewById(R.id.textView14);
            t1.setVisibility(View.VISIBLE);
            TextView t2= findViewById(R.id.textView15);
            t2.setVisibility(View.VISIBLE);
            TextView t3= findViewById(R.id.textView16);
            t3.setVisibility(View.VISIBLE);

        }

        //Pistas
        try{
            SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
            String info = preferencias.getString("tienda","");
            pistas = Integer.parseInt(info);
        }catch (Exception e){
            pistas = 0;
        }
        pistaText = (TextView) findViewById(R.id.contador_pistas);
        pistaText.setText(pistas+"");

    }
    // "back" para salir, 2 veces
    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"¿Seguro que quieres salir?",Toast.LENGTH_SHORT).show();
        }

        backPressedTime =  System.currentTimeMillis();
    }


    //-----------------------------------------------------------------------------//

    public void primer_nivel_metodo(View view){

            Intent intent = new Intent(getApplicationContext(), Actividad1.class );
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);

        }

    public void segundo_nivel_metodo(View view){

        if(niveles_superados > 0) {
            Intent intent = new Intent(getApplicationContext(), Game_2.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Te faltan superar purebas",Toast.LENGTH_SHORT).show();
        }
    }

    public void tercer_nivel_metodo(View view){

        if(niveles_superados > 0) {
            Intent intent = new Intent(getApplicationContext(), game3.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Te faltan superar purebas",Toast.LENGTH_SHORT).show();
        }
    }

    public void cuarto_nivel_metodo(View view){

        if(niveles_superados > 0) {
            Intent intent = new Intent(getApplicationContext(), Game4_main.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Te faltan superar purebas",Toast.LENGTH_SHORT).show();
        }
    }

    public void quinto_nivel_metodo(View view){

        if(niveles_superados > 0) {
            Intent intent = new Intent(getApplicationContext(), Game_5.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Te faltan superar purebas",Toast.LENGTH_SHORT).show();
        }
    }

        public void tienda_metodo(View view){
            Intent intent = new Intent(getApplicationContext(), menu_tienda.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }

        public void foto (View v){
            Intent intent = new Intent(getApplicationContext(), RAFOTO.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);
            intent.putExtras(extras);
            startActivity(intent);
        }
        public void Volver(View v){

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, 1);

        }
        public void tabla (View v){
            Intent intent = new Intent(getApplicationContext(), RecycledMapa.class);
            Bundle extras = new Bundle();
            extras.putInt("Usu", usu);
            extras.putInt("Niveles", niveles_superados);

            intent.putExtras(extras);
            startActivity(intent);

        }
}

