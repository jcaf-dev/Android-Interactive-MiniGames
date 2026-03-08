package com.example.quizz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.animation.ObjectAnimator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Actividad1 extends AppCompatActivity {

    //---------------- Variables ----------------//

    private String RespuestaCorrecta;
    private int RespuestaCorrectaContador = 0;
    private int QuizzContador = 1;
    static final private int RondasDelQuizz = 2;
    private int niveles_superados;


    ArrayList<ArrayList<String>> QuizzArray = new ArrayList<>();

    //---------------- Botones y textos ----------------//
    String Texto_de_Boton="Sin seleccion";
    private Button RespuestaBoton1;
    private Button RespuestaBoton2;
    private Button RespuestaBoton3;
    private Button RespuestaBoton4;
    private TextView ContadorTexto;
    private TextView PreguntasTexto;
    Dialog dialogo;
    private  int usu;
    private Typeface fuente1;


    SoundPool sp;
    int sonido_acierto,sonido_error;

    //------ Pista ------//
    private int pistas;
    private int Numero_del_boton_correcto;
    private ImageButton boton_pista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);

        //-----Pista-----//
        boton_pista = (ImageButton) findViewById(R.id.boton_pista);

        //----- MUSICA -----//
        Button Boton_de_sonido;
        Boton_de_sonido = (Button)findViewById(R.id.boton_sonido);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, Sonido_BG_Service.class);

        //----- Carga el numero de Pistas -----//
        try{
            SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
            String info = preferencias.getString("tienda","");
            pistas = Integer.parseInt(info);
        }catch (Exception e){
            pistas = 0;
        }


        if(Globales.musica) {
            startService(music);
            Boton_de_sonido.setBackgroundResource(R.drawable.pausa);
        }else{
            Boton_de_sonido.setBackgroundResource(R.drawable.reproducir);
        }
        //---------------- Respuesta de la segunda actividad ----------------//

        QuizzContador = getIntent().getIntExtra("Jugar_Otra_vez",1);

        ContadorTexto = (TextView)findViewById(R.id.ContadorTexto);
        PreguntasTexto = (TextView)findViewById(R.id.PreguntasTexto);
        RespuestaBoton1 = (Button)findViewById(R.id.RespuestaBoton1);
        RespuestaBoton2 = (Button)findViewById(R.id.RespuestaBoton2);
        RespuestaBoton3 = (Button)findViewById(R.id.RespuestaBoton3);
        RespuestaBoton4 = (Button)findViewById(R.id.RespuestaBoton4);


        //---------------- Creacion de un ArrayList temporal ----------------//
        System.out.println("HOLAAA ANTES");
        CogerPreguntas();
        System.out.println("HOLAAA DESPUES");

        //---------------- Sonido de botones ------------------//
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sonido_acierto = sp.load(this,R.raw.acierto,1);
        sonido_error = sp.load(this,R.raw.fallo,2);

        //Activa el Dialogo
        dialogo=new Dialog(this);
        dialogo.setCanceledOnTouchOutside(false);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        usu = extras.getInt("Usu");
        //Cargar niveles
        try {
            niveles_superados = extras.getInt("Niveles");

        }catch (Exception e){
            niveles_superados = 0;
        }

        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        textoResultado.setText("Seleccionado: " + Texto_de_Boton);



        // PREFERENCES
        String fuenteS1 = new String();
        int aux2;
        char aux3;
        SharedPreferences preferencias = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String info = preferencias.getString("ajustes1","");
        if(info.isEmpty()){
            aux2= 1;
            aux3= 'B';
        }else {
            aux2 = Integer.parseInt("" + info.charAt(0));
            aux3 = info.charAt(1);
        }
        ConstraintLayout Layout01 =  findViewById(R.id.Layout01);


        switch (aux2)
        {
            case 1:
                fuenteS1 = "fuentes/Agatha.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 2:
                fuenteS1 = "fuentes/Athenic.ttf";
                this.fuente1 = Typeface.createFromAsset(getAssets(), fuenteS1);
                break;
            case 3:
                this.fuente1 = Typeface.SANS_SERIF;
                break;
            default:
        }

        ContadorTexto.setTypeface(fuente1);
        PreguntasTexto.setTypeface(fuente1);
        switch (aux3)
        {
            case 'B':
                Layout01.setBackgroundColor(Color.WHITE);
                break;
            case 'R':
                Layout01.setBackgroundColor(Color.RED);
                break;
            case 'A':
                Layout01.setBackgroundColor(Color.parseColor("#FF00DDFF"));
                break;
            default:
        }

        MostrarNextQuizz(); //-- Funcion que muestra las pantallas del Quizz --//
    }

    //----------------------------------------------------------------------------------//

    public void  SeleccionarRespuesta(View vista){
        limpiar();
        TextView textoResultado =(TextView)findViewById(R.id.seleccion);

        Button  Respuesta_de_Boton = (Button) findViewById(vista.getId());

        //---------------- Cuando se pulsa la opcion, guardamos el resultado en esta variable ----------------//
        Texto_de_Boton = Respuesta_de_Boton.getText().toString();
        textoResultado.setText("Seleccionado: " +Texto_de_Boton);
        Respuesta_de_Boton.setTextColor(Color.BLACK);

    }
    public void seleccion(View vista){
        Switch sw= (Switch) findViewById(R.id.Switchseleccion);
        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        if(sw.isChecked())
        {
            textoResultado.setVisibility(View.VISIBLE);
        }else{
            textoResultado.setVisibility(View.GONE);

        }
    }


    public void limpiar(){
        Button  Boton1= (Button) findViewById(R.id.RespuestaBoton1);
        Button  Boton2= (Button) findViewById(R.id.RespuestaBoton2);
        Button  Boton3= (Button) findViewById(R.id.RespuestaBoton3);
        Button  Boton4= (Button) findViewById(R.id.RespuestaBoton4);
        Boton1.setTextColor(Color.parseColor("#FF616161"));
        Boton2.setTextColor(Color.parseColor("#FF616161"));
        Boton3.setTextColor(Color.parseColor("#FF616161"));
        Boton4.setTextColor(Color.parseColor("#FF616161"));
    }

    public void ComprobarRespuesta(View vista){

        //---------------- Cuadro de alerta ----------------//
        limpiar();
        String AlertaTitulo;
        boolean acierto;

        if(Texto_de_Boton.equals(RespuestaCorrecta)){
            //----- correcto ----//
            sp.play(sonido_acierto,1,1,1,0,0);
            acierto=true;
            RespuestaCorrectaContador = RespuestaCorrectaContador + 3;
        }
        else{
            //----- Incorrecto -----//
            sp.play(sonido_error,1,1,1,0,0);
            if(RespuestaCorrectaContador < 3 && RespuestaCorrectaContador >= 0)
                RespuestaCorrectaContador = 0;
            else{
                RespuestaCorrectaContador = RespuestaCorrectaContador - 2;
            }
            acierto=false;
        }

        TextView textoResultado =(TextView)findViewById(R.id.seleccion);
        dialogo.setContentView(R.layout.popup);
        dialogo.setCanceledOnTouchOutside(false);
        Button bot= (Button) dialogo.findViewById(R.id.button2);
        Button reiniciar= (Button) dialogo.findViewById(R.id.button3);
        reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                ContadorTexto = (TextView)findViewById(R.id.ContadorTexto);
                PreguntasTexto = (TextView)findViewById(R.id.PreguntasTexto);
                RespuestaBoton1 = (Button)findViewById(R.id.RespuestaBoton1);
                RespuestaBoton2 = (Button)findViewById(R.id.RespuestaBoton2);
                RespuestaBoton3 = (Button)findViewById(R.id.RespuestaBoton3);
                RespuestaBoton4 = (Button)findViewById(R.id.RespuestaBoton4);

                CogerPreguntas();
                //---------------- Creacion de un ArrayList temporal ----------------//

                /*for(int i = 0; i<QuizzDatos.length; i++){
                    ArrayList<String> ArrayTemporal = new ArrayList<>();

                    ArrayTemporal.add(QuizzDatos[i][0]);    //-- Pregunta musica --//
                    ArrayTemporal.add(QuizzDatos[i][1]);    //-- Opcion1 --//
                    ArrayTemporal.add(QuizzDatos[i][2]);    //-- Opcion2 --//
                    ArrayTemporal.add(QuizzDatos[i][3]);    //-- Opcion3 --//
                    ArrayTemporal.add(QuizzDatos[i][4]);    //-- Opcion4 --//

                    QuizzArray.add(ArrayTemporal);
                }*/
                RespuestaCorrectaContador = 0;
                QuizzContador = 1;
                MostrarNextQuizz();

            }
        });

        bot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
                if(QuizzContador == RondasDelQuizz){
                    //-------- Guardamos el numero de pistas que quedan -------//

                    String nombre = "tienda";
                    SharedPreferences preferencias = getSharedPreferences("tienda_datos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor obj_editor = preferencias.edit();
                    obj_editor.putString(nombre,pistas+"");
                    obj_editor.commit();

                    //-------- Iniciamos la segunda actividad --------//

                    Intent intent = new Intent(getApplicationContext(),fotos2.class);
                    Bundle extras = new Bundle();
                    extras.putInt("Respuestas_Correctas",RespuestaCorrectaContador);
                    extras.putInt("Usu", usu);
                    extras.putInt("Niveles", niveles_superados);
                    intent.putExtras(extras);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(0,0);
                }
                else{
                    //-------- Si no se ha llegado al numero de preguntas límite se suma 1 --------//
                    QuizzContador++;
                    MostrarNextQuizz();
                }


            }
        });

        TextView tx=(TextView) dialogo.findViewById(R.id.resultadoPalabra);
        if(acierto) {
            tx.setText("GANASTE\nla respuesta era " + RespuestaCorrecta+ "\nLleva una puntuación de "+RespuestaCorrectaContador);
        }else{
            tx.setText("PERDISTE\nLa respuesta era " + RespuestaCorrecta+ "\nLleva una puntuación de "+RespuestaCorrectaContador);
        }
        Texto_de_Boton="Sin seleccion";
        textoResultado.setText("Seleccionado: " +Texto_de_Boton);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.show();
    }

    public void Boton_de_sonido(View v){
        if (mServ != null && Globales.musica==false) {
            v.setBackgroundResource(R.drawable.pausa);
            mServ.resumeMusic();
            Globales.musica=true;
        }
        else if(mServ != null && Globales.musica){
           v.setBackgroundResource(R.drawable.reproducir);
            mServ.pauseMusic();
            Globales.musica=false;
        }
    }

    public void CogerPreguntas(){

        ArrayList<String> aux= new ArrayList<>();
        ArrayList<String> aux2= new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bbdd = admin.getWritableDatabase();
       int max= bbdd.rawQuery("SELECT numero FROM preguntas",null).getCount();
        System.out.println("MAX: " + max);
       int numero1= (int) Math.floor(Math.random()*(max-1) +1);
        System.out.println("HOLAAA-------> numero1: "+ numero1);
       int numero2= (int) Math.floor(Math.random()*(max-1) +1);
       while(numero2==numero1){
           numero2= (int) Math.floor(Math.random()*(max-1) +1);
       }
        System.out.println("HOLAAA-------> numero2: "+ numero2);
        Cursor fila = bbdd.rawQuery("select opcion1,opcion2,opcion3,opcion4,opcion5 from preguntas where numero ="+numero1, null);
        if(fila.moveToFirst()) {
            aux.add(fila.getString(0));
            aux.add(fila.getString(1));
            aux.add(fila.getString(2));
            aux.add(fila.getString(3));
            aux.add(fila.getString(4));
            QuizzArray.add(aux);
        }

        Cursor fila2 = bbdd.rawQuery("select opcion1,opcion2,opcion3,opcion4,opcion5 from preguntas where numero =" + numero2, null);
        if(fila2.moveToFirst()) {
            aux2.add(fila2.getString(0));
            aux2.add(fila2.getString(1));
            aux2.add(fila2.getString(2));
            aux2.add(fila2.getString(3));
            aux2.add(fila2.getString(4));
            QuizzArray.add(aux2);

        }
    }

    public void ProducirPista(View view){

        if(pistas>0) {

            ObjectAnimator rotate;
            if (Numero_del_boton_correcto == 1) {
                rotate = ObjectAnimator.ofFloat(RespuestaBoton1, "rotation", 0f, 5f, 0f, -5f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
            } else if (Numero_del_boton_correcto == 2) {
                rotate = ObjectAnimator.ofFloat(RespuestaBoton2, "rotation", 0f, 5f, 0f, -5f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
            } else if (Numero_del_boton_correcto == 3) {
                rotate = ObjectAnimator.ofFloat(RespuestaBoton3, "rotation", 0f, 5f, 0f, -5f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
            } else {
                rotate = ObjectAnimator.ofFloat(RespuestaBoton4, "rotation", 0f, 5f, 0f, -5f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
            }

            pistas--;
            rotate.setRepeatCount(3); // repeat the loop 20 times
            rotate.setDuration(500); // animation play time 100 ms
            rotate.start();
            Toast.makeText(this, "Te quedan : "+pistas+" pistas", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "No tienes pistas suficientes", Toast.LENGTH_SHORT).show();
        }

    }


    public void MostrarNextQuizz(){

        ContadorTexto.setText("Pregunta "+ QuizzContador);

        //----- Numero Random para elegir la pregunta -----//
        Random random = new Random();
        int NumeroAleatorio = random.nextInt(QuizzArray.size());

        //----- pregunta elegida -----//
        ArrayList<String> Quizz = QuizzArray.get(NumeroAleatorio);

        //----- Pregunta por pantalla y se da valor a la respuesta correcta -----//
        PreguntasTexto.setText(" Que canción es del grupo "+ Quizz.get(0));
        RespuestaCorrecta = Quizz.get(1);

        ArrayList<String> QuizzAux = new ArrayList<String>(Quizz);
        QuizzAux.remove(0);
        Collections.shuffle(QuizzAux);

        //----- Pista -----//
        Numero_del_boton_correcto = 0;
        for(int i = 0; i<4;i++){                            //Debido a que shuffle aleatoriza todas las respuestas, no se sabe
             if(QuizzAux.get(i).equals(RespuestaCorrecta)){     // con certeza cual es si no se comprar tocando el boton y usando la
                 Numero_del_boton_correcto = i + 1;         // funcion ComprobarRespuesta
                 i = 4;
             }
        }

        //----- Posibles respuestas que aparecen por pantalla -----//

        RespuestaBoton1.setText(QuizzAux.get(0));
        RespuestaBoton2.setText(QuizzAux.get(1));
        RespuestaBoton3.setText(QuizzAux.get(2));
        RespuestaBoton4.setText(QuizzAux.get(3));

        //----- Eliminar el quiz usado -----//
        QuizzArray.remove(NumeroAleatorio);
    }


    Sonido_BG_Service mServ;
    boolean mIsBound = false;

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((Sonido_BG_Service.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,Sonido_BG_Service.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && Globales.musica) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (mServ != null) {
            mServ.pauseMusic();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,Sonido_BG_Service.class);
        stopService(music);

    }







}
